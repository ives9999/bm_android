package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.Gson
import com.sportpassword.bm.Adapters.MemberSectionAdapter
import com.sportpassword.bm.Data.MemberRow
import com.sportpassword.bm.Data.MemberSection
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_vc.*
import kotlinx.android.synthetic.main.activity_member_vc.list_container
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast

var memberSections: ArrayList<MemberSection> = arrayListOf()
lateinit var memberSectionAdapter: MemberSectionAdapter

class MemberVC : MyTableVC() {

    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "member"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_vc)

        setBottomTabFocus()
        setMyTitle("會員")

        dataService = MemberService

        memberSections = initSectionRow()
        init()
    }

    override fun init() {
        super.init()

        recyclerView = list_container
        refreshLayout = page_refresh
        maskView = mask

        val loginBtn = findViewById<LinearLayout>(R.id.loginBtn)
        loginBtn.setOnClickListener { loginBtnPressed() }
        val reginBtn = findViewById<LinearLayout>(R.id.registerBtn)
        reginBtn.setOnClickListener { registerBtnPressed() }
        val forgetPasswordBtn = findViewById<LinearLayout>(R.id.forgetPasswordBtn)
        forgetPasswordBtn.setOnClickListener { forgetpasswordBtnPressed() }

        setRecyclerViewRefreshListener()

        memberSectionAdapter = MemberSectionAdapter(this, R.layout.cell_section, this)
        memberSections = initSectionRow()
        memberSectionAdapter.setMyTableSection(memberSections)
        recyclerView.adapter = memberSectionAdapter
        loginout()
    }

    override fun refresh() {
        if (member.isLoggedIn) {
            Loading.show(mask)
            dataService.getOne(this, hashMapOf("token" to member.token!!)) { success ->
                runOnUiThread {
                    Loading.hide(mask)
                }
                if (success) {
                    val table = jsonToModel<MemberTable>(MemberService.jsonString)
                    table?.toSession(this, true)
                    memberSections = initSectionRow()
                    memberSectionAdapter.setMyTableSection(memberSections)
                    runOnUiThread {
                        memberSectionAdapter.notifyDataSetChanged()
                        loginout()
                    }
                } else {
                    warning("無法從伺服器取得會員資料，請稍後再試或聯絡管理員")
                }
            }
        } else {
            _logoutBlock()
        }
    }

    private fun login() {
        toLogin()
    }

    private fun logout() {
        member.isLoggedIn = false
        member.reset()
        loginout()
    }

    fun loginout() {
        //println(member.isLoggedIn)
        if (member.isLoggedIn) {
            _loginBlock()
        } else {
            _logoutBlock()
        }
    }
    
    private fun _loginBlock() {
        //_loginAdapter()
        memberSections = initSectionRow()
        memberSectionAdapter.setMyTableSection(memberSections)
        memberSectionAdapter.notifyDataSetChanged()

        nameLbl.text = member.nickname
        if (member.avatar!!.isNotEmpty()) {
            member.avatar!!.image(this, avatarView)
        }

        loginTV.text = "登出"
        registerBtn.visibility = View.INVISIBLE
        forgetPasswordBtn.visibility = View.INVISIBLE
        list_container.visibility = View.VISIBLE
        //menu_team_container.visibility = View.VISIBLE
        refreshLayout = page_refresh
//        initMemberFunction()
    }
    
    private fun _logoutBlock() {
        nameLbl.text = "未登入"
        loginTV.text = "登入"
        registerBtn.visibility = View.VISIBLE
        forgetPasswordBtn.visibility = View.VISIBLE
        list_container.visibility = View.INVISIBLE
        avatarView.setImageResource(R.drawable.menuprofileicon)
        //menu_team_container.visibility = View.INVISIBLE
    }

    override fun cellClick(sectionIdx: Int, rowIdx: Int) {
//        println(sectionIdx)
//        println(rowIdx)
        val memberSection = memberSections[sectionIdx]
        val row = memberSection.items[rowIdx]
        val segue = row.segue
        when(segue) {
            TO_PROFILE -> this.toRegister()
            TO_PASSWORD -> toUpdatePassword()
            "email" -> this.toValidate("email")
            "mobile" -> this.toValidate("mobile")
//            "blacklist" -> goBlackList()
            "calendar_course_signup" -> toCalendarCourseSignup()
            "refresh" -> refresh()
            TO_MEMBER_ORDER_LIST -> this.toMemberOrderList()
            TO_MEMBER_CART_LIST -> this.toMemberCartList()
            TO_MEMBER_SIGNUP_LIST -> this.toMemberSignupList(row.able_type)
            "manager_team" -> this.toManager("team")
            "toRequestManagerTeam" -> this.toRequestManagerTeam()
            "manager_course" -> this.toManager("course")
            TO_LIKE -> {
                val able_type: String = row.able_type
                when(able_type) {
                    "team" -> this.toTeam(null, true, true, true)
                    "arena" -> this.toArena(true, true, true)
                    "teach" -> this.toTeach(true)
                    "coach" -> this.toCoach(true)
                    "course" -> this.toCourse(true, true, true)
                    "product" -> this.toProduct(true)
                    "store" -> this.toStore(true)
                }
            }
            "delete" -> delete()
        }
    }

    private fun initSectionRow(): ArrayList<MemberSection> {
        val sections: ArrayList<MemberSection> = arrayListOf()

        sections.add(makeSection0Row1())
        sections.add(makeSection1Row())
        sections.add(makeSection2Row(false))
        sections.add(makeSection3Row())
        sections.add(makeSection4Row())
        sections.add(makeSection5Row())

        return sections
    }

//    private fun updateSectionRow1(): ArrayList<MemberSection> {
//        val sections: ArrayList<MemberSection> = arrayListOf()
//        for ((idx, memberSection) in memberSections.withIndex()) {
//            val isExpanded: Boolean = memberSection.isExpanded
//            if (idx == 0) {
//                sections.add(makeSection0Row1(isExpanded))
//            } else if (idx == 1) {
//                sections.add(makeSection1Row(isExpanded))
//            } else if (idx == 2) {
//                sections.add(makeSection2Row(isExpanded))
//            } else if (idx == 3) {
//                sections.add(makeSection3Row(isExpanded))
//            }
//        }
//        return sections
//    }

    fun makeSection0Row1(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        //if (isExpanded) {
        val fixedRows = makeSection0FixRow()
        rows.addAll(fixedRows)

        val validateRows = makeSection0ValidateRow()
        rows.addAll(validateRows)

        val refreshRows = makeSection0RefreshRow()
        rows.addAll(refreshRows)
        //}

        val s: MemberSection = MemberSection("會員資料", isExpanded)
        s.items.addAll(rows)

        return s
    }

    private fun makeSection0FixRow(): ArrayList<MemberRow> {
        val rows: ArrayList<MemberRow> = arrayListOf()
        val r1: MemberRow = MemberRow("帳戶資料", "account", "", TO_PROFILE)
        rows.add(r1)
        val r2: MemberRow = MemberRow("更改密碼", "password", "", TO_PASSWORD)
        rows.add(r2)
        return rows
    }

    private fun makeSection0RefreshRow(): ArrayList<MemberRow> {
        val rows: ArrayList<MemberRow> = arrayListOf()
        val r1: MemberRow = MemberRow("重新整理", "refresh", "", "refresh")
        rows.add(r1)
        return rows
    }

    private fun makeSection0ValidateRow(): ArrayList<MemberRow> {
        val rows: ArrayList<MemberRow> = arrayListOf()
        val validate: Int = member.validate
        if (validate and EMAIL_VALIDATE <= 0) {
            val r: MemberRow = MemberRow("email認證", "email", "", "email")
            rows.add(r)
        }
        if (validate and MOBILE_VALIDATE <= 0) {
            val r: MemberRow = MemberRow("手機認證", "mobile", "", "mobile")
            rows.add(r)
        }
        return rows
    }

    private fun makeSection1Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        //if (isExpanded) {

        val r1: MemberRow = MemberRow("購物車", "cart", "", TO_MEMBER_CART_LIST)
        rows.add(r1)
        val r2: MemberRow = MemberRow("訂單查詢", "order", "", TO_MEMBER_ORDER_LIST)
        rows.add(r2)
        //}

        val s: MemberSection = MemberSection("訂單查詢", isExpanded)
        s.items.addAll(rows)

        return s
    }

    private fun makeSection2Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        //if (isExpanded) {
        val r1: MemberRow = MemberRow("球隊", "team", "", TO_LIKE, "team")
        rows.add(r1)
        val r2: MemberRow = MemberRow("球館", "arena", "", TO_LIKE, "arena")
        rows.add(r2)
        val r3: MemberRow = MemberRow("教學", "teach", "", TO_LIKE, "teach")
        rows.add(r3)
        val r4: MemberRow = MemberRow("教練", "coach", "", TO_LIKE, "coach")
        rows.add(r4)
        val r5: MemberRow = MemberRow("課程", "course", "", TO_LIKE, "course")
        rows.add(r5)
        val r6: MemberRow = MemberRow("商品", "product", "", TO_LIKE, "product")
        rows.add(r6)
        val r7: MemberRow = MemberRow("體育用品店", "store", "", TO_LIKE, "store")
        rows.add(r7)
        //}

        val s: MemberSection = MemberSection("喜歡", isExpanded)
        s.items.addAll(rows)

        return s
    }
    private fun makeSection3Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        val r1: MemberRow = MemberRow("球隊", "team", "", TO_MEMBER_SIGNUP_LIST, "team")
        rows.add(r1)
        val r2: MemberRow = MemberRow("課程", "course", "", TO_MEMBER_SIGNUP_LIST, "course")
        rows.add(r2)

        val s: MemberSection = MemberSection("報名", isExpanded)
        s.items.addAll(rows)

        return s
    }

    private fun makeSection4Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        val r1: MemberRow = MemberRow("球隊", "team", "", "manager_team", "team")
        rows.add(r1)
        val r2: MemberRow = MemberRow("球隊申請管理權", "team", "", "toRequestManagerTeam", "team")
        rows.add(r2)
        val r3: MemberRow = MemberRow("課程", "course", "", "manager_course", "course")
        rows.add(r3)

        val s: MemberSection = MemberSection("管理", isExpanded)
        s.items.addAll(rows)

        return s
    }

    private fun makeSection5Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        val r1: MemberRow = MemberRow("刪除會員", "delete", "", "delete", "member")
        rows.add(r1)

        val s: MemberSection = MemberSection("刪除", isExpanded)
        s.items.addAll(rows)

        return s
    }

    private fun loginBtnPressed() {
        if (member.isLoggedIn) {
            logout()
            //MemberService.logout(this)
            //refresh()
        } else {
            login()
        }
    }

    private fun registerBtnPressed(){
        this.toRegister(this)
//        val registerIntent: Intent = Intent(activity, RegisterActivity::class.java)
//        startActivityForResult(registerIntent, this.REGISTER_REQUEST_CODE)
    }
    private fun forgetpasswordBtnPressed() {
        this.toForgetPassword()
//        val forgetPasswordIntent = Intent(activity, ForgetPasswordActivity::class.java)
//        startActivity(forgetPasswordIntent)
    }

    private fun toCalendarCourseSignup() {
        val intent = Intent(activity, CourseCalendarVC::class.java)
        startActivity(intent)
    }

    override fun handleMemberSectionExpanded(idx: Int) {
        //println(idx)
        val memberSection = memberSections[idx]
        var isExpanded: Boolean = memberSection.isExpanded
        isExpanded = !isExpanded
        memberSections[idx].isExpanded = isExpanded
        memberSectionAdapter.setMyTableSection(memberSections)
        memberSectionAdapter.notifyDataSetChanged()
    }

    fun delete() {
        warning("是否確定要刪除自己的會員資料？", true, "刪除") {
            Loading.show(mask)
            dataService.delete(this, "member", member.token!!, "trash") { success ->
                runOnUiThread {
                    Loading.hide(mask)
                }

                if (success) {
                    try {
                        val successTable: SuccessTable = Gson().fromJson(dataService.jsonString, SuccessTable::class.java)
                        if (!successTable.success) {
                            runOnUiThread {
                                warning(successTable.msg)
                            }
                        } else {
                            runOnUiThread {
                                deleteEnd()
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        runOnUiThread {
                            warning("解析JSON字串時，得到空值，請洽管理員")
                        }
                    }
                } else {
                    runOnUiThread {
                        warning("刪除失敗，請洽管理員")
                    }
                }
            }
        }
    }

    private fun deleteEnd() {
        info("您的帳號已經被刪除，羽球密碼感謝您的支持", "", "關閉") {
            logout()
        }
    }
}
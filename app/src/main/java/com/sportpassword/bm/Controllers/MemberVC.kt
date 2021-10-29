package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Data.MemberRow
import com.sportpassword.bm.Data.MemberSection
import com.sportpassword.bm.Models.MemberTable
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
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.backgroundColor

var memberSections: ArrayList<MemberSection> = arrayListOf()
lateinit var memberSectionAdapter: MemberSectionAdapter

class MemberVC : MyTableVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_vc)

        memberTabLine.backgroundColor = myColorGreen
        setMyTitle("會員")

        dataService = MemberService

        memberSections = initSectionRow()
        init()
    }

    fun init() {
        recyclerView = list_container
        refreshLayout = page_refresh
        maskView = mask

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener { loginBtnPressed() }
        val reginBtn = findViewById<Button>(R.id.registerBtn)
        reginBtn.setOnClickListener { registerBtnPressed() }
        val forgetPasswordBtn = findViewById<Button>(R.id.forgetPasswordBtn)
        forgetPasswordBtn.setOnClickListener { forgetpasswordBtnPressed() }

        setRecyclerViewRefreshListener()

        memberSectionAdapter = MemberSectionAdapter(this, R.layout.cell_section, this)
        memberSectionAdapter.setMyTableSection(memberSections)
        recyclerView.adapter = memberSectionAdapter
        loginout()
    }

    override fun refresh() {
        if (member.isLoggedIn) {
            Loading.show(mask)
            dataService.getOne(this, hashMapOf("token" to member.token!!)) { success ->
                Loading.hide(mask)
                if (success) {
                    val table = jsonToModel<MemberTable>(MemberService.jsonString)
                    table?.toSession(this, true)
                    memberSections = updateSectionRow1()
                    memberSectionAdapter.setMyTableSection(memberSections)
                    memberSectionAdapter.notifyDataSetChanged()
                    loginout()
                } else {
                    warning("無法從伺服器取得會員資料，請稍後再試或聯絡管理員")
                }
            }
        } else {
            _logoutBlock()
        }
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
        nicknameLbl.text = member.nickname
        if (member.avatar!!.isNotEmpty()) {
            member.avatar!!.image(this, avatarView)
        }
        loginBtn.text = "登出"
        registerBtn.visibility = View.INVISIBLE
        forgetPasswordBtn.visibility = View.INVISIBLE
        list_container.visibility = View.VISIBLE
        //menu_team_container.visibility = View.VISIBLE
        refreshLayout = page_refresh
//        initMemberFunction()
    }
    
    private fun _logoutBlock() {
        nicknameLbl.text = "未登入"
        loginBtn.text = "登入"
        registerBtn.visibility = View.VISIBLE
        forgetPasswordBtn.visibility = View.VISIBLE
        list_container.visibility = View.INVISIBLE
        avatarView.setImageResource(R.drawable.menuprofileicon)
        //menu_team_container.visibility = View.INVISIBLE
    }

    fun cellClick1(sectionIdx: Int, rowIdx: Int) {
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
            "manager_course" -> this.toManager("course")
            TO_LIKE -> {
                val able_type: String = row.able_type
                when(able_type) {
                    "team" -> this.toTeam(null, true)
                    "arena" -> this.toArena(true)
                    "teach" -> this.toTeach(true)
                    "coach" -> this.toCoach(true)
                    "course" -> this.toCourse(true)
                    "product" -> this.toProduct(true)
                    "store" -> this.toStore(true)
                }
            }
        }
    }

    private fun initSectionRow(): ArrayList<MemberSection> {
        val sections: ArrayList<MemberSection> = arrayListOf()

        sections.add(makeSection0Row1())
        sections.add(makeSection1Row())
        sections.add(makeSection2Row(false))
        sections.add(makeSection3Row())

        return sections
    }

    private fun updateSectionRow1(): ArrayList<MemberSection> {
        val sections: ArrayList<MemberSection> = arrayListOf()
        for ((idx, memberSection) in memberSections.withIndex()) {
            val isExpanded: Boolean = memberSection.isExpanded
            if (idx == 0) {
                sections.add(makeSection0Row1(isExpanded))
            } else if (idx == 1) {
                sections.add(makeSection1Row(isExpanded))
            } else if (idx == 2) {
                sections.add(makeSection2Row(isExpanded))
            } else if (idx == 3) {
                sections.add(makeSection3Row(isExpanded))
            }
        }
        return sections
    }

    private fun makeSection0Row1(isExpanded: Boolean=true): MemberSection {
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
        val r1: MemberRow = MemberRow("帳戶資料", "account", TO_PROFILE)
        rows.add(r1)
        val r2: MemberRow = MemberRow("更改密碼", "password", TO_PASSWORD)
        rows.add(r2)
        return rows
    }

    private fun makeSection0RefreshRow(): ArrayList<MemberRow> {
        val rows: ArrayList<MemberRow> = arrayListOf()
        val r1: MemberRow = MemberRow("重新整理", "refresh", "refresh")
        rows.add(r1)
        return rows
    }

    private fun makeSection0ValidateRow(): ArrayList<MemberRow> {
        val rows: ArrayList<MemberRow> = arrayListOf()
        val validate: Int = member.validate
        if (validate and EMAIL_VALIDATE <= 0) {
            val r: MemberRow = MemberRow("email認證", "email", "email")
            rows.add(r)
        }
        if (validate and MOBILE_VALIDATE <= 0) {
            val r: MemberRow = MemberRow("手機認證", "mobile", "mobile")
            rows.add(r)
        }
        return rows
    }

    private fun makeSection1Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        //if (isExpanded) {

        val r1: MemberRow = MemberRow("購物車", "cart", TO_MEMBER_CART_LIST)
        rows.add(r1)
        val r2: MemberRow = MemberRow("訂單查詢", "order", TO_MEMBER_ORDER_LIST)
        rows.add(r2)
        //}

        val s: MemberSection = MemberSection("訂單查詢", isExpanded)
        s.items.addAll(rows)

        return s
    }

    private fun makeSection2Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        //if (isExpanded) {
        val r1: MemberRow = MemberRow("球隊", "team", TO_LIKE, "team")
        rows.add(r1)
        val r2: MemberRow = MemberRow("球館", "arena", TO_LIKE, "arena")
        rows.add(r2)
        val r3: MemberRow = MemberRow("教學", "teach", TO_LIKE, "teach")
        rows.add(r3)
        val r4: MemberRow = MemberRow("教練", "coach", TO_LIKE, "coach")
        rows.add(r4)
        val r5: MemberRow = MemberRow("課程", "course", TO_LIKE, "course")
        rows.add(r5)
        val r6: MemberRow = MemberRow("商品", "product", TO_LIKE, "product")
        rows.add(r6)
        val r7: MemberRow = MemberRow("體育用品店", "store", TO_LIKE, "store")
        rows.add(r7)
        //}

        val s: MemberSection = MemberSection("喜歡", isExpanded)
        s.items.addAll(rows)

        return s
    }

    private fun makeSection3Row(isExpanded: Boolean=true): MemberSection {
        val rows: ArrayList<MemberRow> = arrayListOf()

        //if (isExpanded) {
        val r1: MemberRow = MemberRow("課程", "course", "manager_course")
        rows.add(r1)
        //}

        val s: MemberSection = MemberSection("管理", isExpanded)
        s.items.addAll(rows)

        return s
    }

    fun loginBtnPressed() {
        if (member.isLoggedIn) {
            member.isLoggedIn = false
            member.reset()
            loginout()
            //MemberService.logout(this)
            //refresh()
        } else {
            this.toLogin()
        }
    }

    fun registerBtnPressed(){
        this.toRegister()
//        val registerIntent: Intent = Intent(activity, RegisterActivity::class.java)
//        startActivityForResult(registerIntent, this.REGISTER_REQUEST_CODE)
    }
    fun forgetpasswordBtnPressed() {
        this.toForgetPassword()
//        val forgetPasswordIntent = Intent(activity, ForgetPasswordActivity::class.java)
//        startActivity(forgetPasswordIntent)
    }

    override fun toUpdatePassword() {
        this.toUpdatePassword()
//        val updatePasswordIntent = Intent(activity, UpdatePasswordActivity::class.java)
//        startActivity(updatePasswordIntent)
    }

    fun toCalendarCourseSignup() {
        val intent = Intent(activity, CourseCalendarVC::class.java)
        startActivity(intent)
    }

    override fun handleSectionExpanded(idx: Int) {
        //println(idx)
        val memberSection = memberSections[idx]
        var isExpanded: Boolean = memberSection.isExpanded
        isExpanded = !isExpanded
        memberSections[idx].isExpanded = isExpanded
        memberSectionAdapter.setMyTableSection(memberSections)
        memberSectionAdapter.notifyDataSetChanged()
    }
}

class MemberSectionAdapter(val context: Context, private val resource: Int, var delegate: MemberVC): RecyclerView.Adapter<MemberSectionViewHolder>() {

    private var memberSections: ArrayList<MemberSection> = arrayListOf()

    fun setMyTableSection(tableSections: ArrayList<MemberSection>) {
        this.memberSections = tableSections
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberSectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(resource, parent, false)

        return MemberSectionViewHolder(viewHolder)

    }

    override fun onBindViewHolder(holder: MemberSectionViewHolder, position: Int) {
        val section: MemberSection = memberSections[position]
        holder.titleLbl.text = section.title

        val tableSection: MemberSection = memberSections[position]
        var iconID: Int = 0
        if (tableSection.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)

        val adapter: MemberItemAdapter = MemberItemAdapter(context, position, memberSections[position], delegate)
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        holder.greater.setOnClickListener {
            delegate.handleSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return memberSections.size
    }
}

class MemberSectionViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView = viewHolder.findViewById(R.id.titleLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class MemberItemAdapter(val context: Context, private val sectionIdx: Int, private val memberSection: MemberSection, var delegate: MemberVC): RecyclerView.Adapter<MemberItemViewHolder>() {

    var memberRows: ArrayList<MemberRow> = memberSection.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.adapter_member_functions, parent, false)

        return MemberItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {
        val row: MemberRow = memberRows[position]
        holder.titleLbl.text = row.title

        val icon: String = row.icon
        val iconID = context.resources.getIdentifier(icon, "drawable", context.packageName)
        holder.iconView.setImageResource(iconID)

        holder.viewHolder.setOnClickListener {
            delegate.cellClick1(sectionIdx, position)
        }
    }

    override fun getItemCount(): Int {
        if (memberSection.isExpanded) {
            return memberRows.size
        } else {
            return 0
        }
    }

}

class MemberItemViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var iconView: ImageView = viewHolder.findViewById(R.id.icon)
    var titleLbl: TextView = viewHolder.findViewById(R.id.text)
}
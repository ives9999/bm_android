package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.MemberSectionAdapter
import com.sportpassword.bm.Data.MemberRow
import com.sportpassword.bm.Data.MemberSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.IconTextVertical2
import com.sportpassword.bm.Views.IconView2
import com.sportpassword.bm.Views.IconView2Delegate
import com.sportpassword.bm.databinding.ActivityMemberVcBinding
import com.sportpassword.bm.extensions.avatar
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import org.jetbrains.anko.runOnUiThread
import java.lang.Exception
import java.lang.reflect.Type

class MemberVC : BaseActivity(), IconView2Delegate {

   // var memberSections: ArrayList<MemberSection> = arrayListOf()
    //lateinit var memberSectionAdapter: MemberSectionAdapter

    private lateinit var binding: ActivityMemberVcBinding
    //private lateinit var view: ViewGroup
    var qrcodeIV2: IconView2? = null
    var logoutIV2: IconView2? = null
    var avatarIV: ImageView? = null
    var nameTV: TextView? = null
    var pointIconText: IconTextVertical2? = null
    var levelIconText: IconTextVertical2? = null

    private val tableType: Type = object : TypeToken<Tables2<MainMemberTable>>() {}.type
    lateinit var tableView: MyTable2VC<MainMemberViewHolder, MainMemberTable, MemberVC>

    //var rows: ArrayList<MainMemberTable> = arrayListOf()

    lateinit var qrEncoder: QRGEncoder

    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "member"

        super.onCreate(savedInstanceState)

        binding = ActivityMemberVcBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setBottomTabFocus()
        //setMyTitle("會員")

        dataService = MemberService

        findViewById<IconView2>(R.id.qrcodeIV2) ?. let {
            qrcodeIV2 = it
            it.delegate = this
        }

        findViewById<IconView2>(R.id.logoutIV2) ?. let {
            logoutIV2 = it
            it.delegate = this
        }

        findViewById<ImageView>(R.id.avatarIV) ?. let {
            avatarIV = it
        }

        findViewById<TextView>(R.id.nameTV) ?. let {
            nameTV = it
        }

        //level margin = 螢幕寬度 - container區塊padding - level區塊寬度 - 中間divide寬度
        val levelContainerPadding: Int = 20
        val levelWidth: Int = 87
        val dividerWidth: Int = 2
        val allPadding: Int = (levelContainerPadding*2+levelWidth*2+dividerWidth).dpToPx(context)
        val margin: Int = (screenWidth - allPadding) / 4

        findViewById<IconTextVertical2>(R.id.pointIconText) ?. let {
            pointIconText = it
            val lp: ViewGroup.MarginLayoutParams = it.layoutParams as MarginLayoutParams
            lp.marginStart = margin
            it.layoutParams = lp
        }

        findViewById<IconTextVertical2>(R.id.levelIconText) ?. let {
            levelIconText = it
            val lp: ViewGroup.MarginLayoutParams = it.layoutParams as MarginLayoutParams
            lp.marginEnd = margin
            it.layoutParams = lp
        }

        val recyclerView: RecyclerView = findViewById(R.id.list_container)
        tableView = MyTable2VC(recyclerView, null, R.layout.main_member_cell, ::MainMemberViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)

        for (mainMemberEnum in MainMemberEnum.values()) {
            tableView.rows.add(MainMemberTable(mainMemberEnum.chineseName, mainMemberEnum.getIcon()))
        }
//        tableView.rows.addAll(
//            arrayListOf(
//                MainMemberTable("會員資料", "ic_info_svg"),
//                MainMemberTable("訂單查詢", "ic_truck_svg"),
//                MainMemberTable("喜歡", "like_in_svg"),
//                MainMemberTable("參加", "ic_join_svg"),
//                MainMemberTable("管理", "ic_manager1_svg"),
//                MainMemberTable("銀行帳號", "ic_bank_account_svg"),
//                MainMemberTable("刪除帳號", "ic_account_delete_svg")
//            )
//        )
        tableView.setItems()
        //tableView.notifyDataSetChanged()

        refresh()

//        val loginBtn = findViewById<LinearLayout>(R.id.loginBtn)
//        loginBtn.setOnClickListener { loginBtnPressed() }
//        val registerBtn = findViewById<LinearLayout>(R.id.registerBtn)
//        registerBtn.setOnClickListener { registerBtnPressed() }
//        val forgetPasswordBtn = findViewById<LinearLayout>(R.id.forgetPasswordBtn)
//        forgetPasswordBtn.setOnClickListener { forgetpasswordBtnPressed() }

//        memberSections = initSectionRow()
//        init()
    }

    override fun init() {
        super.init()

        //recyclerView = binding.listContainer
        //refreshLayout = page_refresh


        //setRecyclerViewRefreshListener()

//        memberSectionAdapter = MemberSectionAdapter(this, R.layout.cell_section, this)
//        memberSections = initSectionRow()
//        memberSectionAdapter.setMyTableSection(memberSections)
//        recyclerView.adapter = memberSectionAdapter

        //println(member.avatar)
//        if (member.avatar!!.isNotEmpty()) {
//            Picasso.with(context)
//                .load(member.avatar)
//                .placeholder(R.drawable.loading_square_120)
//                .error(R.drawable.loading_square_120)
//                .into(binding.navDrawerHeaderInclude.avatarView)
//        }

//        loginout()
    }

    override fun refresh() {
        if (member.isLoggedIn) {
            loadingAnimation.start()
            dataService.getOne(this, hashMapOf("token" to member.token!!)) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                }
                if (success) {
                    //println(MemberService.jsonString)
                    val table = jsonToModel<MemberTable>(MemberService.jsonString)
                    table?.toSession(this, true)
                    runOnUiThread {
                        avatarIV?.avatar(member.avatar!!)
                        nameTV?.text = member.nickname
                        pointIconText?.text = "${member.coin} 點"
                        val goldEnum: MEMBER_SUBSCRIPTION_KIND = MEMBER_SUBSCRIPTION_KIND.stringToEnum(member.subscription!!)
                        levelIconText?.text = goldEnum.chineseName
                    }
//                    //session.dump()
//                    memberSections = initSectionRow()
//                    memberSectionAdapter.setMyTableSection(memberSections)
//                    runOnUiThread {
//                        memberSectionAdapter.notifyDataSetChanged()
//                        loginout()
//                    }
                } else {
                    runOnUiThread {
                        warning("無法從伺服器取得會員資料，請稍後再試或聯絡管理員")
                    }
                }
            }
        } else {
            //_logoutBlock()
        }
    }

//    private fun login() {
//        toLogin()
//    }

    fun logout() {
        member.isLoggedIn = false
        member.reset()
        finishAffinity()
        toLogin()
        //loginout()
    }

//    fun loginout() {
//        //println(member.isLoggedIn)
//        if (member.isLoggedIn) {
//            _loginBlock()
//        } else {
//            _logoutBlock()
//        }
//    }
    
//    private fun _loginBlock() {
        //_loginAdapter()
//        memberSections = initSectionRow()
//        memberSectionAdapter.setMyTableSection(memberSections)
//        memberSectionAdapter.notifyDataSetChanged()

//        binding.navDrawerHeaderInclude.nameLbl.text = member.nickname
//        if (member.avatar!!.isNotEmpty()) {
//            member.avatar!!.image(this, binding.navDrawerHeaderInclude.avatarView)
//        }

//        binding.loginOutInclude.loginTV.text = "登出"
//        binding.loginOutInclude.registerBtn.visibility = View.INVISIBLE
//        binding.loginOutInclude.forgetPasswordBtn.visibility = View.INVISIBLE
//        binding.listContainer.visibility = View.VISIBLE
        //menu_team_container.visibility = View.VISIBLE
//        refreshLayout = page_refresh
//        initMemberFunction()
//    }
    
//    private fun _logoutBlock() {
        //binding.navDrawerHeaderInclude.nameLbl.text = "未登入"
//        binding.loginOutInclude.loginTV.text = "登入"
//        binding.loginOutInclude.registerBtn.visibility = View.VISIBLE
//        binding.loginOutInclude.forgetPasswordBtn.visibility = View.VISIBLE
//        binding.listContainer.visibility = View.INVISIBLE
        //binding.navDrawerHeaderInclude.avatarView.setImageResource(R.drawable.menuprofileicon)
        //menu_team_container.visibility = View.INVISIBLE
//    }

    override fun cellClick(row: Table) {
        val _row: MainMemberTable? = row as? MainMemberTable
        _row.let {
            val mainMemberEnum: MainMemberEnum = MainMemberEnum.chineseGetEnum(it!!.title)
            if (mainMemberEnum == MainMemberEnum.bank) {
                toMemberBank()
            } else if (mainMemberEnum == MainMemberEnum.delete) {
                delete()
            } else {
                toMemberItem(it.title)
            }
        }
    }

    override fun cellClick(sectionIdx: Int, rowIdx: Int) {
//        println(sectionIdx)
//        println(rowIdx)
//        val memberSection = memberSections[sectionIdx]
//        val row = memberSection.items[rowIdx]
        //val segue = row.segue
        val segue = ""
        when(segue) {
            TO_PROFILE -> this.toRegister()
//            TO_PROFILE -> {
//                var isGrant: Boolean = true
//                //val b1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                val b2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                if (!b2) {
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(
//                            //Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            Manifest.permission.CAMERA
//                        ),
//                        500
//                    )
//                }
//            }
            TO_PASSWORD -> toUpdatePassword()
            "email" -> this.toValidate("email")
            "mobile" -> this.toValidate("mobile")
//            "blacklist" -> goBlackList()
            //"calendar_course_signup" -> toCalendarCourseSignup()
            "refresh" -> refresh()

            TO_MEMBER_ORDER_LIST -> this.toMemberOrderList()
            TO_MEMBER_CART_LIST -> this.toMemberCartList("member")
            //TO_MEMBER_SIGNUP_LIST -> this.toMemberSignupList(row.able_type)
            "manager_team" -> this.toManager("team")
            "toRequestManagerTeam" -> this.toRequestManagerTeam()
            "manager_course" -> this.toManager("course")
            TO_LIKE -> {
                //val able_type: String = row.able_type
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
            TO_MEMBER_BANK -> this.toMemberBank()
            "delete" -> delete()
            TO_MEMBER_COIN_LIST -> this.toMemberCoinList()
            TO_MEMBER_SURIPTION_KIND -> this.toMemberSuriptionKind()
            "qrcode" -> {

                val qrcodeIV: ImageView = makeQrcodeLayer()
                val qrcode: Bitmap? = generateQRCode(member.token!!)
                qrcodeIV.setImageBitmap(qrcode)
            }
            "toMemberTeamList" -> this.toMemberTeamList()
        }
    }

//    private fun initSectionRow(): ArrayList<MemberSection> {
//        val sections: ArrayList<MemberSection> = arrayListOf()
//
//        sections.add(makeSection4Row())
//        sections.add(makeSection0Row1())
//        sections.add(makeSection1Row())
//        sections.add(makeSection2Row(false))
//        sections.add(makeSection3Row())
//        sections.add(makeSectionBankRow())
//        sections.add(makeSectionXRow())
//
//        return sections
//    }

//    fun makeSection0Row1(isExpanded: Boolean=true): MemberSection {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//
//        //if (isExpanded) {
//        val fixedRows = makeSection0FixRow()
//        rows.addAll(fixedRows)
//
//        val validateRows = makeSection0ValidateRow()
//        rows.addAll(validateRows)
//
//        val refreshRows = makeSection0RefreshRow()
//        rows.addAll(refreshRows)
//        //}
//
//        val s: MemberSection = MemberSection("會員資料", true)
//        //val s: MemberSection = MemberSection("會員資料", isExpanded)
//        s.items.addAll(rows)
//
//        return s
//    }
//
//    private fun makeSection0FixRow(): ArrayList<MemberRow> {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//        var r: MemberRow = MemberRow("解碼點數", "coin", "", TO_MEMBER_COIN_LIST)
//        r.show = member.coin.formattedWithSeparator() + " 點"
//        rows.add(r)
//        r = MemberRow("訂閱會員", "member_level_up", "", TO_MEMBER_SURIPTION_KIND)
//        member.subscription?.let {
//            if (it.isNotEmpty()) {
//                val subscription: MEMBER_SUBSCRIPTION_KIND =
//                    MEMBER_SUBSCRIPTION_KIND.stringToEnum(it)
//                r.show = subscription.chineseName
//            }
//        }
//        rows.add(r)
//        r = MemberRow("帳戶資料", "account", "", TO_PROFILE)
//        rows.add(r)
//        r = MemberRow("QRCode", "qrcode", "", "qrcode")
//        rows.add(r)
//        r = MemberRow("更改密碼", "password", "", TO_PASSWORD)
//        rows.add(r)
//        return rows
//    }
//
//    private fun makeSection0RefreshRow(): ArrayList<MemberRow> {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//        val r1: MemberRow = MemberRow("重新整理", "refresh", "", "refresh")
//        rows.add(r1)
//        return rows
//    }
//
//    private fun makeSection0ValidateRow(): ArrayList<MemberRow> {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//        val validate: Int = member.validate
//        if (validate and EMAIL_VALIDATE <= 0) {
//            val r: MemberRow = MemberRow("email認證", "email", "", "email")
//            rows.add(r)
//        }
//        if (validate and MOBILE_VALIDATE <= 0) {
//            val r: MemberRow = MemberRow("手機認證", "mobile", "", "mobile")
//            rows.add(r)
//        }
//        return rows
//    }
//
//    private fun makeSection1Row(isExpanded: Boolean=true): MemberSection {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//
//        //if (isExpanded) {
//
//        val r1: MemberRow = MemberRow("購物車", "cart", "", TO_MEMBER_CART_LIST)
//        rows.add(r1)
//        val r2: MemberRow = MemberRow("訂單查詢", "order", "", TO_MEMBER_ORDER_LIST)
//        rows.add(r2)
//        //}
//
//        val s: MemberSection = MemberSection("訂單查詢", true)
//        //val s: MemberSection = MemberSection("訂單查詢", isExpanded)
//        s.items.addAll(rows)
//
//        return s
//    }
//
//    private fun makeSection2Row(isExpanded: Boolean=true): MemberSection {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//
//        //if (isExpanded) {
//        val r1: MemberRow = MemberRow("球隊", "team", "", TO_LIKE, "team")
//        rows.add(r1)
//        val r2: MemberRow = MemberRow("球館", "arena", "", TO_LIKE, "arena")
//        rows.add(r2)
//        val r3: MemberRow = MemberRow("教學", "teach", "", TO_LIKE, "teach")
//        rows.add(r3)
//        val r4: MemberRow = MemberRow("教練", "coach", "", TO_LIKE, "coach")
//        rows.add(r4)
//        val r5: MemberRow = MemberRow("課程", "course", "", TO_LIKE, "course")
//        rows.add(r5)
//        val r6: MemberRow = MemberRow("商品", "product", "", TO_LIKE, "product")
//        rows.add(r6)
//        val r7: MemberRow = MemberRow("體育用品店", "store", "", TO_LIKE, "store")
//        rows.add(r7)
//        //}
//
//        val s: MemberSection = MemberSection("喜歡", true)
//        //val s: MemberSection = MemberSection("喜歡", isExpanded)
//        s.items.addAll(rows)
//
//        return s
//    }
//    private fun makeSection3Row(isExpanded: Boolean=true): MemberSection {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//
//        val r1: MemberRow = MemberRow("球隊", "team", "", "toMemberTeamList", "team")
//        rows.add(r1)
//        val r2: MemberRow = MemberRow("臨打", "tempplay", "", TO_MEMBER_SIGNUP_LIST, "team")
//        rows.add(r2)
//        val r3: MemberRow = MemberRow("課程", "course", "", TO_MEMBER_SIGNUP_LIST, "course")
//        rows.add(r3)
//
//        val s: MemberSection = MemberSection("參加", true)
//        //val s: MemberSection = MemberSection("報名", isExpanded)
//        s.items.addAll(rows)
//
//        return s
//    }
//
//    private fun makeSection4Row(isExpanded: Boolean=true): MemberSection {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//
//        val r1: MemberRow = MemberRow("球隊", "team", "", "manager_team", "team")
//        rows.add(r1)
//        val r2: MemberRow = MemberRow("球隊申請管理權", "team", "", "toRequestManagerTeam", "team")
//        rows.add(r2)
//        val r3: MemberRow = MemberRow("課程", "course", "", "manager_course", "course")
//        rows.add(r3)
//
//        val s: MemberSection = MemberSection("管理", true)
//        //val s: MemberSection = MemberSection("管理", isExpanded)
//        s.items.addAll(rows)
//
//        return s
//    }
//
//    private fun makeSectionBankRow(isExpanded: Boolean=true): MemberSection {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//
//        val r1: MemberRow = MemberRow("銀行帳號", "bank", "", TO_MEMBER_BANK, "member")
//        rows.add(r1)
//
//        val s: MemberSection = MemberSection("銀行帳號", true)
//        s.items.addAll(rows)
//
//        return s
//    }
//
//    private fun makeSectionXRow(isExpanded: Boolean=true): MemberSection {
//        val rows: ArrayList<MemberRow> = arrayListOf()
//
//        val r1: MemberRow = MemberRow("刪除會員", "delete", "", "delete", "member")
//        rows.add(r1)
//
//        val s: MemberSection = MemberSection("刪除", true)
//        //val s: MemberSection = MemberSection("刪除", isExpanded)
//        s.items.addAll(rows)
//
//        return s
//    }
//
//    private fun loginBtnPressed() {
//        if (member.isLoggedIn) {
//            logout()
//            //MemberService.logout(this)
//            //refresh()
//        } else {
//            login()
//        }
//    }

//    private fun registerBtnPressed(){
//        this.toRegister(this)
//    }
//    private fun forgetpasswordBtnPressed() {
//        this.toForgetPassword()
//    }
//
//    private fun toCalendarCourseSignup() {
//        val intent = Intent(activity, CourseCalendarVC::class.java)
//        startActivity(intent)
//    }
//
//    override fun handleMemberSectionExpanded(idx: Int) {
//        //println(idx)
//        val memberSection = memberSections[idx]
//        var isExpanded: Boolean = memberSection.isExpanded
//        isExpanded = !isExpanded
//        memberSections[idx].isExpanded = isExpanded
//        memberSectionAdapter.setMyTableSection(memberSections)
//        memberSectionAdapter.notifyDataSetChanged()
//    }

    fun delete() {
        warning("是否確定要刪除自己的會員資料？", true, "刪除") {
            loadingAnimation.start()
            dataService.delete(this, "member", member.token!!, "trash") { success ->
                runOnUiThread {
                    loadingAnimation.stop()
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

    private fun getDataFromServer(page: Int) {}

    private fun deleteEnd() {
        info("您的帳號已經被刪除，羽球密碼感謝您的支持", "", "關閉") {
            logout()
        }
    }

    fun makeQrcodeLayer(): ImageView {
        val view = window.decorView.rootView as ViewGroup
        val maskView: LinearLayout = view.mask(this)

        val blackViewHeight: Int = 2000
        val blackViewPaddingLeft: Int = 20
        val blackView: RelativeLayout = maskView.blackView(this, blackViewPaddingLeft, (screenHeight - blackViewHeight)/2 + 100, screenWidth - 2*blackViewPaddingLeft, blackViewHeight)

        val qrcodeIV: ImageView = ImageView(this)
        val l: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        l.addRule(RelativeLayout.CENTER_HORIZONTAL)
        l.addRule(RelativeLayout.CENTER_VERTICAL)
        qrcodeIV.layoutParams = l
        blackView.addView(qrcodeIV)

        return qrcodeIV
    }

    fun generateQRCode(string: String): Bitmap? {
        var dimen = if (screenWidth < screenHeight) screenWidth else screenHeight
        dimen = dimen * 3 / 4
        qrEncoder = QRGEncoder(string, null, QRGContents.Type.TEXT, dimen)

        var bitmap: Bitmap? = null
        try {
            bitmap = qrEncoder.encodeAsBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }

    fun tableViewSetSelected(row: MainMemberTable): Boolean { return false }
    override fun iconPressed(icon: String) {
        if (icon == "ic_qrcode_svg") {
            val qrcodeIV: ImageView = makeQrcodeLayer()
            val qrcode: Bitmap? = generateQRCode(member.token!!)
            qrcodeIV.setImageBitmap(qrcode)
        } else if (icon == "ic_logout_svg") {
            warning("是否真的要登出？", true, "登出") {
                this.logout()
            }
        }
    }
}

enum class MainMemberEnum(val chineseName: String) {
    info("會員資料"),
    order("訂單查詢"),
    like("喜歡"),
    join("參加"),
    manager("管理"),
    bank("銀行帳號"),
    delete("刪除帳號");

    companion object {
        //val allValues: ArrayList<MainMemberEnum> = arrayListOf(info, order, like, join, manager, bank, delete)
        fun chineseGetEnum(text: String): MainMemberEnum {
            when (text) {
                "會員資料"-> return info
                "訂單查詢"-> return order
                "喜歡"-> return like
                "參加"-> return join
                "管理"-> return manager
                "銀行帳號"-> return bank
                "刪除帳號"-> return delete
                else-> return info
            }
        }
    }

    fun getIcon(): String {
        when (this) {
            info-> return "ic_info_svg"
            order-> return "ic_truck_svg"
            like-> return "ic_like_in_svg"
            join-> return "ic_join_svg"
            manager-> return "ic_manager1_svg"
            bank-> return "ic_bank_account_svg"
            delete-> return "ic_account_delete_svg"
        }
    }
}

class MainMemberViewHolder(
    context: Context,
    viewHolder: View,
    delegate: MemberVC
): MyViewHolder2<MainMemberTable, MemberVC>(context, viewHolder, delegate) {

    var iconIV: ImageView? = null
    var textTV: TextView? = null

    init {
        viewHolder.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }

        viewHolder.findViewById<TextView>(R.id.textTV) ?. let {
            textTV = it
        }
    }

    override fun bind(row: MainMemberTable, idx: Int) {
        super.bind(row, idx)

        iconIV?.setImage(row.icon)
        textTV?.text = row.title
    }
}

class MainMemberTable(title: String, icon: String): Table() {
    var icon: String = "nophoto"
    init {
        this.title = title
        this.icon = icon
    }
}
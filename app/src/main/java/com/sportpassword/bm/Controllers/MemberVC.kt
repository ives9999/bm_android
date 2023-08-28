package com.sportpassword.bm.Controllers

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.IconTextVertical2
import com.sportpassword.bm.Views.IconView2
import com.sportpassword.bm.Views.IconView2Delegate
import com.sportpassword.bm.databinding.ActivityMemberVcBinding
import com.sportpassword.bm.extensions.avatar
import com.sportpassword.bm.extensions.dpToPx
import com.sportpassword.bm.member
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
    var subscriptionIconText: IconTextVertical2? = null

    private val tableType: Type = object : TypeToken<Tables2<MainMemberTable>>() {}.type
    //lateinit var tableView: MyTable2VC<MainMemberViewHolder, MainMemberTable, MemberVC>

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MemberAdapter

    var items: ArrayList<MainMemberTable> = arrayListOf()

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

        findViewById<IconTextVertical2>(R.id.subscriptionIconText) ?. let {
            subscriptionIconText = it
            val lp: ViewGroup.MarginLayoutParams = it.layoutParams as MarginLayoutParams
            lp.marginEnd = margin
            it.layoutParams = lp
            it.setOnClickListener {
                toMemberSubscriptionKind()
            }
        }

        findViewById<LinearLayout>(R.id.levelRightContainer) ?. let {
            it.setOnClickListener {
                toMemberSubscriptionKind()
            }
        }

        initRecyclerView()

//        val recyclerView: RecyclerView = findViewById(R.id.list_container)
//        tableView = MyTable2VC(recyclerView, null, R.layout.main_member_cell, ::MainMemberViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)

//        binding.root.findViewById<ImageView>(R.id.bannerIV).apply {
//            this.setImage("banner_subscription")
//            this.setOnClickListener {
//                toMemberSubscriptionKind()
//            }
//        }


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
    }

    fun initRecyclerView() {
        recyclerView = binding.listContainer
        adapter = MemberAdapter(this, this)
        recyclerView.adapter = adapter

        for (mainMemberEnum in MainMemberEnum.values()) {
            items.add(MainMemberTable(mainMemberEnum.chineseName, mainMemberEnum.getIcon()))
        }
        adapter.items = items
        adapter.onBannerClick = {
            toMemberSubscriptionKind()
        }

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
                        pointIconText?.setText("${member.coin} 點")
                        val goldEnum: MEMBER_SUBSCRIPTION_KIND = MEMBER_SUBSCRIPTION_KIND.stringToEnum(member.subscription!!)
                        subscriptionIconText?.setText(goldEnum.chineseName)
                        subscriptionIconText?.setIcon("ic_subscription_${member.subscription}")
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

    override fun cellClick(row: Table) {
        val _row: MainMemberTable? = row as? MainMemberTable
        _row.let {
            val mainMemberEnum: MainMemberEnum = MainMemberEnum.chineseGetEnum(it!!.title)
            if (mainMemberEnum == MainMemberEnum.bank) {
                toMemberBank()
            } else if (mainMemberEnum == MainMemberEnum.delete) {
                delete()
            } else if (mainMemberEnum == MainMemberEnum.refresh) {
                refresh()
            } else {
                toMemberItem(it.title)
            }
        }
    }

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

    private fun makeQrcodeLayer(): ImageView {
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

    private fun generateQRCode(string: String): Bitmap? {
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
    delete("刪除帳號"),
    refresh("重新整理");

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
                "重新整理"-> return refresh
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
            refresh-> return "ic_refresh_g_svg"
        }
    }
}

class MemberAdapter(val context: Context, val delegate: MemberVC):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: ArrayList<MainMemberTable> = arrayListOf()
    var onBannerClick: (()-> Unit)? = null

    companion object {
        const val BANNER: Int = 1
        const val ITEM: Int = 2
    }

    override fun getItemViewType(position: Int): Int {
        //println("position: ${position}")
        val i = if (position == 0) MemberAdapter.BANNER else MemberAdapter.ITEM
        return i
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MemberAdapter.BANNER) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.member_banner_cell, parent, false)
            return BannerViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.main_member_cell, parent, false)
            return MainMemberViewHolder(context, view, delegate)
        }
    }

    override fun getItemCount(): Int {
        val n = items.size
        if (items.size > 0) {
            return items.size + 1
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is MainMemberViewHolder) {
            if (items.size >= position) {
                holder.bind(items[position-1], position)
            }
        }
    }

    inner class BannerViewHolder(view: View): ViewHolder(view) {
        init {
            view.findViewById<ImageView>(R.id.bannerIV) ?. let {
                it.setImage("banner_subscription")
            }

            view.setOnClickListener {
                onBannerClick?.invoke()
            }
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
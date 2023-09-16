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
import com.sportpassword.bm.Views.IconWithBGCircle
import com.sportpassword.bm.databinding.ActivityMemberVcBinding
import com.sportpassword.bm.databinding.IconWithBgCircleBinding
import com.sportpassword.bm.extensions.avatar
import com.sportpassword.bm.extensions.dpToPx
import com.sportpassword.bm.member
import java.lang.Exception
import java.lang.reflect.Type

class MemberVC : BaseActivity() {

    private lateinit var binding: ActivityMemberVcBinding

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MemberAdapter

    var rows: ArrayList<MainMemberItem> = arrayListOf()

    lateinit var qrEncoder: QRGEncoder

    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "member"

        super.onCreate(savedInstanceState)

        binding = ActivityMemberVcBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setBottomTabFocus()
        dataService = MemberService



        //level margin = 螢幕寬度 - container區塊padding - level區塊寬度 - 中間divide寬度
//        val levelContainerPadding: Int = 20
//        val levelWidth: Int = 87
//        val dividerWidth: Int = 2
//        val allPadding: Int = (levelContainerPadding*2+levelWidth*2+dividerWidth).dpToPx(context)
//        val margin: Int = (screenWidth - allPadding) / 4



        initRecyclerView()
        refresh()
    }

    fun initRecyclerView() {
        recyclerView = binding.listContainer
        adapter = MemberAdapter(this, this)
        recyclerView.adapter = adapter

        adapter.items = rows
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

                    rows.clear()
                    for (mainMemberEnum in MainMemberEnum.values()) {
                        if (mainMemberEnum == MainMemberEnum.validate_email && (member.validate and EMAIL_VALIDATE > 0)) {
                            continue
                        }

                        if (mainMemberEnum == MainMemberEnum.validate_mobile && (member.validate and MOBILE_VALIDATE > 0)) {
                            continue
                        }

                        rows.add(MainMemberItem(mainMemberEnum.chineseName, mainMemberEnum.getIcon()))
                    }
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
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
        warning("是否真的要登出？", true, "登出") {
            member.isLoggedIn = false
            member.reset()
            finishAffinity()
            toLogin()
        }
    }

    fun cellClick(row: MainMemberItem) {
        row.let {
            val mainMemberEnum: MainMemberEnum = MainMemberEnum.chineseGetEnum(it.title)
            if (mainMemberEnum == MainMemberEnum.validate_email) {
                toValidate("email")
            } else if (mainMemberEnum == MainMemberEnum.validate_mobile) {
                toValidate("mobile")
            } else if (mainMemberEnum == MainMemberEnum.bank) {
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

    private fun deleteEnd() {
        info("您的帳號已經被刪除，羽球密碼感謝您的支持", "", "關閉") {
            logout()
        }
    }

    public fun showQRCode(from: String) {
        val qrcodeIV = makeQrcodeLayer()
        val qrcode = generateQRCode(from)
        qrcodeIV.setImageBitmap(qrcode)
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

    fun tableViewSetSelected(row: MainMemberItem): Boolean { return false }
}

enum class MainMemberEnum(val chineseName: String) {
    validate_email("Email認證"),
    validate_mobile("手機認證"),
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
                "Email認證"-> return validate_email
                "手機認證"-> return validate_mobile
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
            validate_email-> return "ic_validate_svg"
            validate_mobile-> return "ic_validate_svg"
            info-> return "ic_info_svg"
            order-> return "ic_truck_svg"
            like-> return "ic_like_in_svg"
            join-> return "ic_join_svg"
            manager-> return "ic_manager1_svg"
            bank-> return "ic_bank_account_svg"
            delete-> return "ic_account_delete_svg"
            refresh-> return "ic_info_svg"
        }
    }
}

class MemberAdapter(val context: Context, val delegate: MemberVC):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: ArrayList<MainMemberItem> = arrayListOf()
    var onBannerClick: (()-> Unit)? = null

    companion object {
        const val AVATAR: Int = 1
        const val LEVEL: Int = 2
        const val BANNER: Int = 3
        const val ITEM: Int = 4
    }

    override fun getItemViewType(position: Int): Int {
        //println("position: ${position}")
        return when (position) {
            0 -> AVATAR
            1 -> LEVEL
            2 -> BANNER
            3 -> ITEM
            else -> ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == AVATAR) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.member_avatar_cell, parent, false)
            return AvatarViewHolder(view, delegate)
        } else if (viewType == LEVEL) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.member_level_cell, parent, false)
            return LevelViewHolder(view, delegate)
        } else if (viewType == MemberAdapter.BANNER) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.member_banner_cell, parent, false)
            return BannerViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.main_member_cell, parent, false)
            return MainMemberViewHolder(view, delegate)
        }
    }

    override fun getItemCount(): Int {
        //val n = items.size
        if (items.size > 0) {
            return items.size + 3
        } else {
            return 3
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is AvatarViewHolder) {
            holder.bind()
        } else if (holder is LevelViewHolder) {
            holder.bind()
        } else if (holder is MainMemberViewHolder) {
            if (items.size >= position - 3) {
                holder.bind(items[position-3], position)
            }
        }
    }

    inner class AvatarViewHolder(view: View, delegate: MemberVC): ViewHolder(view), IconView2Delegate {

        var delegate: MemberVC? = null
        var qrcodeIV2: IconView2? = null
        var logoutIV2: IconView2? = null
        var avatarIV: ImageView? = null
        var nameTV: TextView? = null

        init {
            this.delegate = delegate
            view.findViewById<IconView2>(R.id.qrcodeIV2) ?. let {
                qrcodeIV2 = it
                it.delegate = this
            }

            view.findViewById<IconView2>(R.id.logoutIV2) ?. let {
                logoutIV2 = it
                it.delegate = this
            }

            view.findViewById<ImageView>(R.id.avatarIV) ?. let {
                avatarIV = it
            }

            view.findViewById<TextView>(R.id.nameTV) ?. let {
                nameTV = it
            }
        }
        fun bind() {
            avatarIV?.avatar(member.avatar!!)
            nameTV?.text = member.nickname
        }

        override fun iconPressed(icon: String) {
            if (icon == "ic_qrcode_svg") {
                delegate?.showQRCode(member.token!!)
            } else if (icon == "ic_logout_svg") {
                delegate?.logout()
            }
        }
    }

    inner class LevelViewHolder(view: View, delegate: MemberVC): ViewHolder(view) {

        var pointIcon: IconWithBGCircle? = null
        var pointTextTV: TextView? = null
        var subscriptionIcon: IconWithBGCircle? = null
        var subscriptionTextTV: TextView? = null
        var delegate: MemberVC? = null

        init {
            this.delegate = delegate
            view.findViewById<IconWithBGCircle>(R.id.pointIcon) ?. let {
                pointIcon = it
            }

            view.findViewById<TextView>(R.id.pointTextTV) ?. let {
                pointTextTV = it
            }

            view.findViewById<IconWithBGCircle>(R.id.subscriptionIcon) ?. let {
                subscriptionIcon = it
                it.setOnClickListener {
                    delegate.toMemberSubscriptionKind()
                }
//            val lp: ViewGroup.MarginLayoutParams = it.layoutParams as MarginLayoutParams
//            lp.marginEnd = margin
//            it.layoutParams = lp
//            it.setOnClickListener {
//                toMemberSubscriptionKind()
//            }
            }

            view.findViewById<TextView>(R.id.subscriptionTextTV) ?. let {
                subscriptionTextTV = it
                it.setOnClickListener {
                    delegate.toMemberSubscriptionKind()
                }
            }

            view.findViewById<LinearLayout>(R.id.levelRightContainer) ?. let {
                it.setOnClickListener {
                    delegate.toMemberSubscriptionKind()
                }
            }
        }

        fun bind() {
            pointTextTV?.setText("${member.coin} 點")
            val goldEnum: MEMBER_SUBSCRIPTION_KIND = MEMBER_SUBSCRIPTION_KIND.stringToEnum(member.subscription!!)
            subscriptionTextTV?.setText("${goldEnum.chineseName}會員")
            subscriptionIcon?.setIcon("ic_subscription_${member.subscription}")
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

    inner class MainMemberViewHolder(val viewHolder: View, delegate: MemberVC): ViewHolder(viewHolder) {

        var delegate: MemberVC? = null
        var iconIV: ImageView? = null
        var textTV: TextView? = null

        init {
            this.delegate = delegate
            viewHolder.findViewById<ImageView>(R.id.iconIV) ?. let {
                iconIV = it
            }

            viewHolder.findViewById<TextView>(R.id.textTV) ?. let {
                textTV = it
            }
        }

        fun bind(row: MainMemberItem, idx: Int) {
            iconIV?.setImage(row.icon)
            textTV?.text = row.title

            viewHolder.setOnClickListener {
                delegate?.cellClick(row)
            }

        }
    }
}

class MainMemberItem(title: String, icon: String) {
    var title: String = ""
    var icon: String = "nophoto"
    init {
        this.title = title
        this.icon = icon
    }
}
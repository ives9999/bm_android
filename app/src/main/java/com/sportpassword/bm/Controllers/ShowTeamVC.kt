package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.SignupAdapter
import com.sportpassword.bm.Adapters.SignupViewHolder
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Data.SignupRow
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.Bottom
import com.sportpassword.bm.Views.EndlessRecyclerViewScrollListener
import com.sportpassword.bm.Views.TabSearch
import com.sportpassword.bm.Views.Top
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_show_team_vc.*
import kotlinx.android.synthetic.main.activity_show_team_vc.refresh
import kotlinx.android.synthetic.main.bottom_view_show.*
import java.util.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_search.view.*
import java.lang.reflect.Type
import kotlin.collections.ArrayList

class ShowTeamVC: ShowVC() {

    var top: Top? = null
    var showBottom: Bottom? = null
    var myTable: TeamTable? = null

    var isTempPlay: Boolean = true

    var topTags: ArrayList<HashMap<String, Any>> = arrayListOf (
        hashMapOf("key" to "introduce", "focus" to true, "tag" to 0, "icon" to "admin", "text" to "介紹", "class" to ""),
        hashMapOf("key" to "teammember", "focus" to false, "tag" to 1, "icon" to "team", "text" to "隊員", "class" to ""),
        hashMapOf("key" to "tempplay", "focus" to false, "tag" to 2, "icon" to "tempplay", "text" to "臨打", "class" to "")
    )
    var focusTabIdx: Int = 0
    var isTeamMemberLoaded: Boolean = false
    var teamMemberRows: ArrayList<SignupRow> = arrayListOf()

    var introduceContainerLL: LinearLayout? = null
    var teamMemberContainerLL: LinearLayout? = null
    var tempPlayContainerLL: LinearLayout? = null
    var teamMemberTableView: RecyclerView? = null
    var tempPlayTableView: RecyclerView? = null

    //team member
    lateinit var teamMemberAdapter: TeamMemberAdapter
    private lateinit var teamMemberLinearLayoutManager: LinearLayoutManager
    protected lateinit var teamMemberScrollListener: MemberTeamScrollListener
    var teamMemberPage: Int = 1
    var teamMemberPerpage: Int = PERPAGE

    //temp play
    lateinit var tempPlayAdapter: SignupAdapter
    private lateinit var tempPlayLinearLayoutManager: LinearLayoutManager
    protected lateinit var tempPlayScrollListener: TempPlayScrollListener
    var tempPlayPage: Int = 1
    var tempPlayPerpage: Int = PERPAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_show_team_vc)

        dataService = TeamService

        refreshLayout = refresh
        setRefreshListener()

        bottom_button_count = 3
//        initAdapter()
        super.onCreate(savedInstanceState)

//        tableRowKeys = mutableListOf("arena","interval_show","ball","leader","mobile_show","fb","youtube","website","email","pv","created_at_show")
//        tableRows = hashMapOf(
//            "arena" to hashMapOf("icon" to "arena","title" to "球館","content" to ""),
//            "interval_show" to hashMapOf("icon" to "clock","title" to "時段","content" to ""),
//            "ball" to hashMapOf("icon" to "ball","title" to "球種","content" to ""),
//            "leader" to hashMapOf("icon" to "member1","title" to "隊長","content" to ""),
//            "mobile_show" to hashMapOf("icon" to "mobile","title" to "行動電話","content" to ""),
//            "fb" to  hashMapOf("icon" to "fb","title" to "FB","content" to ""),
//            "youtube" to hashMapOf("icon" to "youtube","title" to "Youtube","content" to ""),
//            "website" to hashMapOf("icon" to "website","title" to "網站","content" to ""),
//            "email" to hashMapOf("icon" to "email1","title" to "EMail","content" to ""),
//            "pv" to hashMapOf("icon" to "pv","title" to "瀏覽數","content" to ""),
//            "created_at_show" to hashMapOf("icon" to "calendar","title" to "建立日期","content" to "")
//        )

        findViewById<Top>(R.id.top) ?. let {
            top = it
            it.showPrev(true)
        }

        findViewById<Bottom>(R.id.bottom) ?. let {
            showBottom = it
            it.showButton(false, true, false)
            it.setOnSubmitClickListener(signup)
            it.setOnLikeClickListener(like)
            it.setOnCancelClickListener(cancel)
        }

        findViewById<LinearLayout>(R.id.introduceContainerLL) ?. let {
            introduceContainerLL = it
        }

        findViewById<LinearLayout>(R.id.teamMemberContainerLL) ?. let {
            teamMemberContainerLL = it
        }

        findViewById<LinearLayout>(R.id.tempPlayContainerLL) ?. let {
            tempPlayContainerLL = it
        }

        teamMemberAdapter = TeamMemberAdapter(this, this)
        findViewById<RecyclerView>(R.id.teamMemberTableView) ?. let {
            teamMemberTableView = it
            it.adapter = teamMemberAdapter

            teamMemberLinearLayoutManager = LinearLayoutManager(this)
            it.layoutManager = teamMemberLinearLayoutManager
            teamMemberScrollListener = MemberTeamScrollListener(teamMemberLinearLayoutManager)
            it.addOnScrollListener(teamMemberScrollListener)
        }

        tempPlayAdapter = SignupAdapter(this, this)
        findViewById<RecyclerView>(R.id.tempPlayTableView) ?. let {
            tempPlayTableView = it
            it.adapter = tempPlayAdapter

            tempPlayLinearLayoutManager = LinearLayoutManager(this)
            it.layoutManager = tempPlayLinearLayoutManager
            tempPlayScrollListener = TempPlayScrollListener(tempPlayLinearLayoutManager)
            it.addOnScrollListener(tempPlayScrollListener)
        }

        init()
        refresh()
    }

    override fun init() {
        super.init()
        initTopTab()
    }

    private fun initTopTab() {

        val lp_tag = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp_tag.gravity = Gravity.CENTER
        lp_tag.weight = 1F
        val textSize: Float = 16F

        for ((i, topTag) in topTags.withIndex()) {
            val tab: TabSearch = TabSearch(this)
            tab.layoutParams = lp_tag
            tab.gravity = Gravity.CENTER

            var idx: Int = 1000
            if (topTag.containsKey("tag")) {
                idx = topTag["tag"] as Int
            }
            tab.tag = idx

            var icon: String = "like"
            if (topTag.containsKey("icon")) {
                icon = topTag["icon"] as String
            }
            tab.icon.setImage(icon)

            var text: String = "預設"
            if (topTag.containsKey("text")) {
                text = topTag["text"] as String
            }
            tab.text.text = text
            tab.text.textSize = textSize
            topTags[i]["class"] = tab

            findViewById<LinearLayout>(R.id.tag_container) ?. let {
                it.addView(tab)
            }

            tab.setOnClickListener {
                tabPressed(it)
            }
        }
        updateTabSelected(focusTabIdx)
    }

    override fun refresh() {

        if (token != null) {
//            signupRows.clear()
//            showRows.clear()
            initData()
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
            dataService.getOne(this, params) { success ->
                if (success) {
                    runOnUiThread {
                        genericTable()
                        if (table != null) {
                            table!!.filterRow()

                            if (table!!.name.isNotEmpty()) {
                                setMyTitle(table!!.name)
                            } else if (table!!.title.isNotEmpty()) {
                                setMyTitle(table!!.title)
                            }
                            setFeatured()
                            setData()
                            setContent()
                            showAdapter.rows = showRows
                            showAdapter.notifyDataSetChanged()

                            isLike = table!!.like
                            likeCount = table!!.like_count
                            setLike()

                            _tabPressed(focusTabIdx)
                        }
                    }
                }
                runOnUiThread {
                    closeRefresh()
                    Loading.hide(mask)
                }
            }
        }
    }

    //這是給內頁介紹詳細資料用的
    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<TeamTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            runOnUiThread {
                warning(e.localizedMessage!!)
            }
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as TeamTable
            myTable!!.filterRow()
            top?.setTitle(myTable!!.name)
            showBottom?.setLike(myTable!!.like, myTable!!.like_count)
        } else {
            runOnUiThread {
                warning("解析伺服器所傳的字串失敗，請洽管理員")
            }
        }
    }

    protected fun countTeamMemberTotalPage() {
        val _totalPage: Int = totalCount / teamMemberPerpage
        totalPage = if (totalCount % teamMemberPerpage > 0) _totalPage+1 else _totalPage

        if (totalPage <= 1) {
            teamMemberTableView?.removeOnScrollListener(teamMemberScrollListener)
        }
    }

    private fun getTeamMemberList(page: Int, perPage: Int) {
        Loading.show(mask)
        loading = true

        TeamService.teamMemberList(this, token!!, page, perPage) { success ->
            runOnUiThread {
                Loading.hide(mask)

                val tableType: Type = object : TypeToken<Tables2<TeamMemberTable>>() {}.type
                val tables2: Tables2<TeamMemberTable>? = jsonToModels2<Tables2<TeamMemberTable>, TeamMemberTable>(TeamService.jsonString, tableType)
                if (tables2 == null) {
                    msg = "無法從伺服器取得正確的json資料，請洽管理員"
                } else {
                    if (tables2.success) {
                        if (tables2.rows.size > 0) {

                            for ((idx, row) in tables2.rows.withIndex()) {
                                row.filterRow()
                                val baseIdx: Int = (page-1)*perPage

                                val nickname: String =
                                    ((row.memberTable != null) then { row.memberTable!!.nickname })
                                        ?: ""
                                val token: String = ((row.memberTable != null) then { row.memberTable!!.token }) ?: ""
                                val signupRow: SignupRow = SignupRow((baseIdx + idx + 1).toString(), nickname, token)
                                teamMemberRows.add(signupRow)
                            }

                            if (this.teamMemberPage == 1) {
                                this.teamMemberPage = tables2.page
                                this.teamMemberPerpage = tables2.perPage
                                totalCount = tables2.totalCount
                                val _totalPage: Int = totalCount / perPage
                                totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                                countTeamMemberTotalPage()
                            }

                            teamMemberDataLbl.visibility = View.VISIBLE
                            teamMemberDataLbl.text = "總人數${totalCount}位"
                            teamMemberAdapter.rows = teamMemberRows
                            teamMemberAdapter.notifyDataSetChanged()
                        } else {
                            val rootView: ViewGroup = getRootView()
                            rootView.setInfo(this, "目前暫無資料")
                        }
                    } else {
                        msg = "解析JSON字串時，沒有成功，系統傳回值錯誤，請洽管理員"
                    }
                }
            }
        }
    }

    override fun setBottomButtonPadding() {
        //當沒有報名時
        findViewById<Button>(R.id.signupButton) ?. let {
            if (it.visibility == View.GONE) {
                bottom_button_count -= 1
            }
        }
        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.signupButton) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }

        findViewById<LinearLayout>(R.id.likeButton) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }

    override fun setData() {

        if (myTable != null) {
            setMainData(myTable!!)
        }
    }

    override fun setMainData(table: Table) {

        showRows.clear()
        showRows.addAll(arrayListOf(
            ShowRow("arena", "arena1", "球館", myTable!!.arena!!.name),
        ))
        if (myTable!!.arena != null) {
            showRows.addAll(arrayListOf(
                ShowRow("city_show", "map", "縣市", myTable!!.arena!!.city_show),
                ShowRow("area_show", "map", "區域", myTable!!.arena!!.area_show)
            ))
        }
        showRows.addAll(arrayListOf(
            ShowRow("weekday", "date", "星期", myTable!!.weekdays_show),
            ShowRow("interval_show", "clock", "時段", myTable!!.interval_show),
            ShowRow("ball", "ball", "球種", myTable!!.ball),
            ShowRow("degree", "degree", "程度", myTable!!.degree_show),
            ShowRow("block_show", "arena1", "場地", myTable!!.block_show),
            ShowRow("temp_fee_M_show", "money", "費用-男", myTable!!.temp_fee_M_show),
            ShowRow("temp_fee_F_show", "money", "費用-女", myTable!!.temp_fee_F_show),
            ShowRow("leader", "group", "管理者", myTable!!.manager_nickname),
            ShowRow("mobile_show", "mobile", "行動電話", myTable!!.mobile_show),
            ShowRow("line", "line", "line", myTable!!.line),
            ShowRow("fb", "fb", "FB", myTable!!.fb),
            ShowRow("youtube", "youtube", "Youtube", myTable!!.youtube),
//            ShowRow("website", "website", "網站", myTable!!.website),
            ShowRow("email", "email1", "EMail", myTable!!.email),
            ShowRow("pv", "pv", "瀏覽數", myTable!!.pv.toString()),
            ShowRow("created_at_show", "date", "建立日期", myTable!!.created_at_show)
        ))
    }

    private fun setSignupData() {

        isTempPlayOnline()

        if (!isTempPlay) {
            tempPlayDataLbl.text = "目前球隊不開放臨打"
            tempPlayDateLbl.visibility = View.INVISIBLE
            tempPlayDeadlineLbl.visibility = View.INVISIBLE
            tempPlayTableView?.visibility = View.GONE
        } else {
            if (myTable != null && myTable!!.signupDate != null) {
                tempPlayDataLbl.text = "報名資料"
                tempPlayDateLbl.text =
                    "下次臨打時間：" + myTable!!.signupDate!!.date + " " + myTable!!.interval_show
                tempPlayDeadlineLbl.text = "報名截止時間：" + myTable!!.signupDate!!.deadline.noSec()
            }
        }

        if (myTable!!.people_limit == 0) {
            signupButton.visibility = View.GONE
            setBottomButtonPadding()
        } else {
            signupRows.clear()
            for (i in 0..myTable!!.people_limit - 1) {
                var name = ""
                if (myTable!!.signupNormalTables.count() > i) {
                    val tmp = myTable!!.signupNormalTables[i].member_name.let {
                        name = it
                    }
                }
                val signupRow: SignupRow = SignupRow((i+1).toString()+".", name)
                signupRows.add(signupRow)
            }

            if (myTable!!.signupStandbyTables.count() > 0) {
                for (i in 0 until myTable!!.signupStandbyTables.count()) {
                    var name = ""
                    val tmp = myTable!!.signupStandbyTables[i].member_name.let {
                        name = it
                    }
                    val signupRow: SignupRow = SignupRow("候補" + (i+1).toString()+".", name)
                    signupRows.add(signupRow)
                }
            }

            tempPlayAdapter.rows = signupRows
            tempPlayAdapter.notifyDataSetChanged()
        }

        if (myTable!!.isSignup) {
            showBottom?.setSubmitBtnTitle("取消報名")
        } else {
            val count = myTable!!.signupNormalTables.size
            if (count >= myTable!!.people_limit) {
                showBottom?.setSubmitBtnTitle("候補")
            } else {
                showBottom?.setSubmitBtnTitle("報名")
            }
        }
    }

    private fun isTempPlayOnline() {

        //1.如果臨打狀態是關閉，關閉臨打
        if (myTable!!.temp_status == "offline") {
            isTempPlay = false
        }

        //2.如果沒有設定臨打日期，關閉臨打
        if (myTable!!.signupDate != null) {
            val temp_date_string: String = myTable!!.signupDate!!.date

            //3.如果臨打日期超過現在的日期，關閉臨打
            var temp_date: Date = Date()
            temp_date_string.toDate("yyyy-MM-dd")?.let {
                temp_date = it
            }

            val now_string: String = Date().toMyString("yyyy-MM-dd")
            var now: Date = Date()
            now_string.toDate("yyyy-MM-dd")?.let {
                now = it
            }

            //(1)如果報名日期剛好也是臨打日期則可以報名
            if (temp_date.equals(now)) {
                isTempPlay = true
            } else {
                //(2)如果報名日期已經過了臨打日期則無法報名
                //(3)如果報名日期還沒過了臨打日期則無法報名
                isTempPlay = !temp_date.before(now)
            }
        } else {
            isTempPlay = false
        }

        //3.如果管理者設定報名臨打名額是0，關閉臨打
        if (myTable!!.people_limit == 0) {
            isTempPlay = false
        }
    }

    val cancel: ()-> Unit = {
        prev()
    }

    val like: ()-> Unit = {
        likeButtonPressed()
    }

    val signup: () ->Unit = {
        signupButtonPressed()
    }

    private fun signupButtonPressed() {

        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }

        if (!member.checkEMailValidate()) {
            warning("請先通過email認證")
            return
        }

        if (!member.checkMobileValidate()) {
            warning("請先通過行動電話認證")
            return
        }

        if (myTable != null && myTable!!.signupDate != null) {

            var deadline_time: Date? = null
            myTable!!.signupDate!!.deadline.toDate("yyyy-MM-dd HH:mm:ss")?.let {
                deadline_time = it
            }

            if (deadline_time != null) {
                val now: Date = Date()
                if (now.after(deadline_time)) {

                    msg = "已經超過報名截止時間，請下次再報名"
                    if (myTable!!.isSignup) {
                        msg = "已經超過取消報名截止時間，無法取消報名"
                    }
                    warning(msg)
                    return
                }
            } else {
                warning("無法取得報名截止時間，無法報名，請洽管理員")
                return
            }

            Loading.show(mask)
            dataService.signup(this, myTable!!.token, member.token!!, myTable!!.signupDate!!.token) { success->

                runOnUiThread {
                    Loading.hide(mask)
                }

                if (success) {
                    val jsonString: String = dataService.jsonString
                    val successTable: SuccessTable? = jsonToModel(jsonString)
                    if (successTable != null && successTable.success) {
                        runOnUiThread {
                            info(successTable.msg, "", "確定") {
                                refresh()
                            }
                        }
                    } else {
                        if (successTable != null) {
                            runOnUiThread {
                                warning(successTable.msg)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun setIcon() {
        showBottom?.setIcon(isLike)
    }

    override fun setCount() {
        likeCount = if (table!!.like) {
            if (isLike) {
                table!!.like_count
            } else {
                table!!.like_count - 1
            }
        } else {
            if (isLike) {
                table!!.like_count + 1
            } else {
                table!!.like_count
            }
        }
        showBottom?.setCount(likeCount)
    }

    //showSignupInfo is click signup data to call back function defined in BaseActivity
    override fun showSignupInfo(position: Int) {

        if (myTable != null) {
            if (myTable!!.manager_token == member.token) {
                val signupNormalCount: Int = myTable!!.signupNormalTables.size
                val people_limit = myTable!!.people_limit

                if (position < signupNormalCount) {
                    val signup_normal_model = myTable!!.signupNormalTables[position]
                    //print(signup_normal_model.member_token)
                    getMemberOne(signup_normal_model.member_token)

                }
                if (position > people_limit) {
                    val signup_standby_model = myTable!!.signupStandbyTables[position]
                    getMemberOne(signup_standby_model.member_token)
                }
            } else {
                warning("只有球隊管理員可以檢視報名者資訊")
            }
        }
    }

    private fun tabPressed(view: View) {
        val tab: TabSearch = view as TabSearch
        val idx: Int? = tab.tag as? Int
        if (idx != null) {
            val focusTag: HashMap<String, Any> = topTags[idx]
            val focused: Boolean = focusTag["focus"] as? Boolean == true
            if (!focused) {
                updateTabSelected(idx)
                focusTabIdx = idx
                _tabPressed(focusTabIdx)
            }
        }
    }

    private fun _tabPressed(idx: Int) {

        when (focusTabIdx) {
            0-> {
                introduceContainerLL?.visibility = View.VISIBLE
                teamMemberContainerLL?.visibility = View.GONE
                tempPlayContainerLL?.visibility = View.GONE

                showBottom?.showButton(false, true, false)
            }
            1-> {
                teamMemberContainerLL?.visibility = View.VISIBLE
                introduceContainerLL?.visibility = View.GONE
                tempPlayContainerLL?.visibility = View.GONE

                signupRows.clear()
                if (!isTeamMemberLoaded) {
                    getTeamMemberList(this.teamMemberPage, this.teamMemberPerpage)
                    isTeamMemberLoaded = true
                }

                showBottom?.showButton(false, true, false)
            }
            2-> {
                tempPlayContainerLL?.visibility = View.VISIBLE
                introduceContainerLL?.visibility = View.GONE
                teamMemberContainerLL?.visibility = View.GONE

                signupRows.clear()
                setSignupData()

                showBottom?.showButton(true, true, false)
            }
        }
    }

    override fun teamMemberInfo(idx: Int) {
        if (myTable != null) {
            if (myTable!!.manager_token == member.token) {
                val teamMemberRow: SignupRow = teamMemberRows[idx]
                getMemberOne(teamMemberRow.token)
            } else {
                warning("只有球隊管理員可以檢視報名者資訊")
            }
        }
    }

    private fun updateTabSelected(idx: Int) {

        // set user click which tag, set tag selected is true
        for ((i, topTag) in topTags.withIndex()) {
            topTag["focus"] = i == idx
            topTags[i] = topTag
        }
        setTabSelectedStyle()
    }

    private fun setTabSelectedStyle() {

        for (topTag in topTags) {
            if (topTag.containsKey("class")) {
                val tag: TabSearch? = topTag["class"] as? TabSearch
                if (tag != null) {
                    tag.isChecked = topTag["focus"] as Boolean
                    tag.setSelectedStyle()
                }
            }
        }
    }

//    override fun didSelectRowAt(view: View, position: Int) {
//
//    }

    inner class MemberTeamScrollListener(recyelerViewLinearLayoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener(recyelerViewLinearLayoutManager) {

        //page: 目前在第幾頁
        //totalItemCount: 已經下載幾頁
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            //page已經+1了
            if (page == totalPage) {
                teamMemberTableView?.removeOnScrollListener(teamMemberScrollListener)
            }
            getTeamMemberList(page, teamMemberPerpage)
        }
    }

    inner class TempPlayScrollListener(recyelerViewLinearLayoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener(recyelerViewLinearLayoutManager) {

        //page: 目前在第幾頁
        //totalItemCount: 已經下載幾頁
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            //page已經+1了
            if (page == totalPage) {
                tempPlayTableView?.removeOnScrollListener(tempPlayScrollListener)
            }
            //getTeamMemberList(page, teamMemberPerpage)
        }
    }
}

class TeamMemberAdapter(val context: Context, val delegate: BaseActivity?=null): RecyclerView.Adapter<SignupViewHolder>() {

    var rows: ArrayList<SignupRow> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.olcell, parent, false)

        return SignupViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: SignupViewHolder, position: Int) {

        val row: SignupRow = rows[position]
        holder.number.text = row.number
        holder.name.text = row.name

        holder.viewHolder.setOnClickListener {

            if (delegate != null) {
                delegate.teamMemberInfo(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return rows.size
    }

}
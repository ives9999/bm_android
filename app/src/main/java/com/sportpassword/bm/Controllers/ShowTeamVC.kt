package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.*
import com.sportpassword.bm.databinding.ActivityShowTeamVcBinding
import com.sportpassword.bm.extensions.avatar
import com.sportpassword.bm.member
import tw.com.bluemobile.hbc.utilities.getColor
import java.util.*
import java.lang.reflect.Type
import kotlin.collections.ArrayList

class ShowTeamVC: ShowVC(), ShowTab2Delegate, TapTextViewDelegate, IconText2Delegate {

    private lateinit var binding: ActivityShowTeamVcBinding
    private lateinit var view: ViewGroup

    var showTop2: ShowTop2? = null
    var showBottom: Bottom2? = null
    var showLike2: ShowLike2? = null
    private var showTab2: ShowTab2? = null

    var myTable: TeamTable? = null

    private var focusTabIdx: Int = 0
    var countTaps: ArrayList<TapTextView2> = arrayListOf()

    var introduceContainerLL: LinearLayout? = null
    var teamMemberContainerLL: LinearLayout? = null
    //var tempPlayContainerLL: LinearLayout? = null
    var teamMemberTableView: RecyclerView? = null
    //var tempPlayTableView: RecyclerView? = null
    private lateinit var linearLayoutManager: LinearLayoutManager


    //team member
    lateinit var teamMemberAdapter: TeamMemberAdapter
    private var teamMemberScrollListener: MemberTeamScrollListener? = null

    lateinit var teamMemberTotalTV: TapTextView2
    lateinit var teamMemberPlayTV: TapTextView2
    lateinit var teamMemberLeaveTV: TapTextView2
    private lateinit var nextDateIV: ImageView
    private lateinit var nextDateTV: TextView
    private lateinit var nextTimeIV: ImageView
    private lateinit var nextTimeTV: TextView
    private lateinit var likeIconIV: ImageView
    private lateinit var teamMemberListTV: TextView
    private lateinit var addIconText2: IconText2

    private var teamMemberPage: Int = 1
    var teamMemberPerPage: Int = PERPAGE
    var teamMemberTotalCount: Int = 0
    var teamMemberTotalPage: Int = 0
    var leaveCount: Int = 0

    //此會員的team member token
    var teamMemberToken: String? = null

    var nextDate: String = ""
    var nextDateWeek: String = ""
    var playInterval: String = ""
//    var play_start: String = ""
//    var play_end: String = ""

    //會員是否為球隊隊友
    var isTeamMember: Boolean = false
    //會員為隊友，會員是否已經請假
    var isTeamMemberLeave: Boolean = false
    //是否已經擷取網路隊員資料
    var isTeamMemberLoaded: Boolean = false

    var items1: ArrayList<TeamMemberTable> = arrayListOf()
    var filterItems: ArrayList<TeamMemberTable> = arrayListOf()

    //temp play
    lateinit var tempPlayAdapter: TempPlayAdapter
    //lateinit var tempPlayAdapter: SignupAdapter
    //private lateinit var tempPlayLinearLayoutManager: LinearLayoutManager
    private var tempPlayScrollListener: TempPlayScrollListener? = null

    //是否開放臨打
    var isTempPlay: Boolean = true
    //會員是否已經加入臨打
    var isAddTempPlay: Boolean = false
    //臨打列表資料是否經輸入
    var isTempPlayLoaded: Boolean = false

    var tempPlayPage: Int = 1
    var tempPlayPerPage: Int = PERPAGE
    var tempPlayTotalCount: Int = 1
    var tempPlayTotalPage: Int = 0

    var tempPlayCount: Int = 0

    var items2: ArrayList<TeamTempPlayTable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityShowTeamVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        dataService = TeamService

        refreshLayout = binding.refresh
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

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.showPrev(true)
        }

        findViewById<Bottom2>(R.id.bottom) ?. let {
            showBottom = it
            //it.showButton(false, true, false)
            it.setOnSubmitClickListener(signup)
            it.setOnLikeClickListener(like)
            it.setOnCancelClickListener(cancel)
        }

        findViewById<ShowTab2>(R.id.showTap2) ?. let {
            showTab2 = it
            it.delegate = this
            it.setOnClickListener()
        }

        findViewById<ShowLike2>(R.id.showLike2) ?. let {
            showLike2 = it
            it.setOnThisClickListener(like)
        }

        findViewById<LinearLayout>(R.id.introduceContainerLL) ?. let {
            introduceContainerLL = it
        }

        findViewById<LinearLayout>(R.id.teamMemberContainerLL) ?. let {
            teamMemberContainerLL = it
        }

        findViewById<TapTextView2>(R.id.teamMemberTotalTV) ?. let {
            teamMemberTotalTV = it
            it.delegate = this
            it.setOnThisClickListener()
            it.on()

            countTaps.add(it)
        }

        findViewById<TapTextView2>(R.id.teamMemberPlayTV) ?. let {
            teamMemberPlayTV = it
            it.delegate = this
            it.setOnThisClickListener()

            countTaps.add(it)
        }

        findViewById<TapTextView2>(R.id.teamMemberLeaveTV) ?. let {
            teamMemberLeaveTV = it
            it.delegate = this
            it.setOnThisClickListener()

            countTaps.add(it)
        }

        findViewById<ImageView>(R.id.nextDateIV) ?. let {
            nextDateIV = it
        }

        findViewById<TextView>(R.id.nextDateTV) ?. let {
            nextDateTV = it
            nextDateIV.setImage("calendar_svg")
        }

        findViewById<ImageView>(R.id.nextTimeIV) ?. let {
            nextTimeIV = it
            nextTimeIV.setImage("clock_svg")
        }

        findViewById<TextView>(R.id.nextTimeTV) ?. let {
            nextTimeTV = it
        }

        findViewById<ImageView>(R.id.likeIconIV) ?. let {
            likeIconIV = it
        }

//        findViewById<LinearLayout>(R.id.tempPlayContainerLL) ?. let {
//            tempPlayContainerLL = it
//        }
        findViewById<TextView>(R.id.teamMemberListTV) ?. let {
            teamMemberListTV = it
        }

        findViewById<IconText2>(R.id.addIconText2) ?. let {
            addIconText2 = it
            it.delegate = this
            it.setOnThisClickListener()
        }

        findViewById<RecyclerView>(R.id.teamMemberTableView) ?. let {
            teamMemberTableView = it
            //it.adapter = teamMemberAdapter
            linearLayoutManager = LinearLayoutManager(this)
            teamMemberTableView!!.layoutManager = linearLayoutManager

//            teamMemberLinearLayoutManager = LinearLayoutManager(this)
//            teamMemberTableView!!.layoutManager = teamMemberLinearLayoutManager
        }

        teamMemberAdapter = TeamMemberAdapter(this, this)
        tempPlayAdapter = TempPlayAdapter(this, this)
//        findViewById<RecyclerView>(R.id.tempPlayTableView) ?. let {
//            tempPlayTableView = it
//            it.adapter = tempPlayAdapter
//
//            tempPlayLinearLayoutManager = LinearLayoutManager(this)
//            it.layoutManager = tempPlayLinearLayoutManager
//            tempPlayScrollListener = TempPlayScrollListener(tempPlayLinearLayoutManager)
//            it.addOnScrollListener(tempPlayScrollListener)
//        }

        init()
        refresh()
    }

    override fun init() {
        super.init()
    }

    override fun refresh() {

        if (token != null) {
//            signupRows.clear()
//            showRows.clear()
            initData()
            loadingAnimation.start()
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
                            showLike2?.setLike(isLike, likeCount)

                            _tabPressed(focusTabIdx)
                        }
                    }
                }
                runOnUiThread {
                    closeRefresh()
                    loadingAnimation.stop()
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
            showTop2?.setTitle(myTable!!.name)
            showBottom?.setLike(myTable!!.like, myTable!!.like_count)

            tempPlayCount = myTable!!.number - myTable!!.teamMemberCount +  + myTable!!.leaveCount

            this.nextDate = myTable!!.nextDate
            this.nextDateWeek = myTable!!.nextDateWeek
            this.playInterval = myTable!!.interval_show

            nextDateTV.text = "${nextDate} ( ${nextDateWeek} )"
            nextTimeTV.text = playInterval

        } else {
            runOnUiThread {
                warning("解析伺服器所傳的字串失敗，請洽管理員")
            }
        }
    }

    fun countTeamMemberTotalPage() {
        val _totalPage: Int = teamMemberTotalCount / teamMemberPerPage
        teamMemberTotalPage = if (teamMemberTotalCount % teamMemberPerPage > 0) _totalPage+1 else _totalPage

        if (teamMemberTotalPage <= 1 && teamMemberScrollListener != null) {
            teamMemberTableView?.removeOnScrollListener(teamMemberScrollListener!!)
        }
    }

    fun countTempPlayTotalPage() {
        val _totalPage: Int = tempPlayTotalCount / teamMemberPerPage
        tempPlayTotalPage = if (tempPlayTotalCount % tempPlayPage > 0) _totalPage+1 else _totalPage

        if (tempPlayTotalPage <= 1 && tempPlayScrollListener != null) {
            teamMemberTableView?.removeOnScrollListener(tempPlayScrollListener!!)
        }
    }

    fun getTeamMemberList(page: Int, perPage: Int) {
        if (page == 1) {
            items1.clear()
        }
        loadingAnimation.start()
        loading = true

        TeamService.teamMemberList(this, token!!, page, perPage) { success ->
            runOnUiThread {
                loadingAnimation.stop()

                val tableType: Type = object : TypeToken<TeamMemberSuccessTables2>() {}.type
                val tables2: TeamMemberSuccessTables2? = jsonToModels2<TeamMemberSuccessTables2, TeamMemberTable>(TeamService.jsonString, tableType)
                if (tables2 == null) {
                    msg = "無法從伺服器取得正確的json資料，請洽管理員"
                } else {
                    if (tables2.success) {
                        tables2.filterRow()
                        if (tables2.rows.size > 0) {

                            for ((idx, row) in tables2.rows.withIndex()) {
                                row.filterRow()
                                items1.add(row)

                                //取得會員是否為隊友與會員是否已經請假
                                if (row.memberTable!!.token == member.token) {
                                    this.teamMemberToken = row.token
                                    this.isTeamMember = true
                                    this.isTeamMemberLeave = row.isLeave
                                }
                            }
                            filterItems = items1.clone() as ArrayList<TeamMemberTable>

                            if (page == 1) {
                                this.teamMemberPage = tables2.page
                                this.teamMemberPerPage = tables2.perPage
                                teamMemberTotalCount = tables2.totalCount
                                val _totalPage: Int = teamMemberTotalCount / teamMemberPerPage
                                teamMemberTotalPage = if (teamMemberTotalCount % teamMemberPerPage > 0) _totalPage + 1 else _totalPage
                                countTempPlayTotalPage()

                                this.leaveCount = tables2.leaveCount
                            } else {
                                this.teamMemberPage = page
                            }

                            binding.teamMemberDataTV.visibility = View.VISIBLE

                            this.teamMemberAdapter.rows = this.filterItems
                            this.teamMemberAdapter.notifyDataSetChanged()
                        } else {
//                            binding.teamMemberDataTV.visibility = View.INVISIBLE
//                            nextDateTV.visibility = View.INVISIBLE
                            //val rootView: ViewGroup = getRootView()
                            //rootView.setInfo(this, "目前暫無資料")
                        }
                    } else {
                        msg = "解析JSON字串時，沒有成功，系統傳回值錯誤，請洽管理員"
                    }
                    this.setTeamMemberSummary()
                    this.setTeamMemberBottom()
                }
            }
        }
    }

    fun getTempPlayList(page: Int, perPage: Int) {
        if (page == 1) {
            items2.clear()
        }
        loadingAnimation.start()
        loading = true

        TeamService.tempPlayList(this, token!!, myTable!!.nextDate, page, perPage) { success ->
            runOnUiThread {
                loadingAnimation.stop()

                val tableType: Type = object : TypeToken<TempPlaySuccessTables2>() {}.type
                val tables2: TempPlaySuccessTables2? = jsonToModels2<TempPlaySuccessTables2, TeamTempPlayTable>(TeamService.jsonString, tableType)
                if (tables2 == null) {
                    msg = "無法從伺服器取得正確的json資料，請洽管理員"
                } else {
                    if (tables2.success) {
                        tables2.filterRow()

                        if (page == 1) {
                            this.tempPlayPage = tables2.page
                            this.tempPlayPerPage = tables2.perPage
                            tempPlayTotalCount = tables2.totalCount
                            val _totalPage: Int = tempPlayTotalCount / perPage
                            tempPlayTotalPage = if (tempPlayTotalCount % perPage > 0) _totalPage + 1 else _totalPage
                            countTempPlayTotalPage()
                        } else {
                            this.tempPlayPage = page
                        }

                        if (tables2.rows.size > 0) {

                            for ((idx, row) in tables2.rows.withIndex()) {
                                row.filterRow()
                                val baseIdx: Int = (page-1)*perPage

                                val nickname: String =
                                    ((row.memberTable != null) then { row.memberTable!!.nickname })
                                        ?: ""
                                val token: String = ((row.memberTable != null) then { row.memberTable!!.token }) ?: ""
                                items2.add(row)
                            }

                            isAddTempPlay = false
                            for (item in items2) {
                                if (item.memberTable != null) {
                                    if (item.memberTable!!.token == member.token) {
                                        isAddTempPlay = true
                                        break
                                    }
                                }
                            }

                            this.tempPlayAdapter.rows = items2
                        } else {
                            items2.clear()
                            isAddTempPlay = false
//                            binding.teamMemberDataTV.visibility = View.INVISIBLE
//                            nextDateTV.visibility = View.INVISIBLE
//                            val rootView: ViewGroup = getRootView()
//                            rootView.setInfo(this, "目前暫無資料")
                        }
                        this.tempPlayAdapter.count = tempPlayCount
                        this.tempPlayAdapter.notifyDataSetChanged()
                    } else {
                        msg = "解析JSON字串時，沒有成功，系統傳回值錯誤，請洽管理員"
                    }
                    this.setTempPlaySummary()
                    this.setTempPlayBottom()
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
            ShowRow("arena", "ic_arena_svg", "球館", myTable!!.arena!!.name),
        ))
        if (myTable!!.arena != null) {
            showRows.addAll(arrayListOf(
                ShowRow("city_show", "ic_city_svg", "縣市", myTable!!.arena!!.city_show),
                ShowRow("area_show", "ic_area_svg", "區域", myTable!!.arena!!.area_show)
            ))
        }
        showRows.addAll(arrayListOf(
            ShowRow("weekday", "calendar_svg", "星期", myTable!!.weekdays_show),
            ShowRow("interval_show", "clock_svg", "時段", myTable!!.interval_show),
            ShowRow("ball", "ic_ball_svg", "球種", myTable!!.ball),
            ShowRow("degree", "ic_degree_svg", "程度", myTable!!.degree_show),
            ShowRow("block_show", "ic_block_svg", "場地", myTable!!.block_show),
            ShowRow("temp_fee_M_show", "ic_fee_svg", "費用-男", myTable!!.temp_fee_M_show),
            ShowRow("temp_fee_F_show", "ic_fee_svg", "費用-女", myTable!!.temp_fee_F_show),
            ShowRow("leader", "ic_manager_svg", "管理者", myTable!!.manager_nickname),
            ShowRow("mobile_show", "ic_mobile_svg", "行動電話", myTable!!.mobile_show),
            ShowRow("line", "ic_line_svg", "line", myTable!!.line),
            ShowRow("fb", "ic_fb_svg", "FB", myTable!!.fb),
            ShowRow("youtube", "ic_youtube_svg", "Youtube", myTable!!.youtube),
//            ShowRow("website", "website", "網站", myTable!!.website),
            ShowRow("email", "ic_email_svg", "EMail", myTable!!.email),
            ShowRow("pv", "ic_pv_svg", "瀏覽數", myTable!!.pv.toString()),
            ShowRow("created_at_show", "ic_createdat_svg", "建立日期", myTable!!.created_at_show)
        ))
    }

    private fun setSignupData() {

        isTempPlayOnline()

        if (!isTempPlay) {
//            binding.tempPlayDataLbl.text = "目前球隊不開放臨打"
//            binding.tempPlayDateLbl.visibility = View.INVISIBLE
//            binding.tempPlayDeadlineLbl.visibility = View.INVISIBLE
//            tempPlayTableView?.visibility = View.GONE
//            showBottom!!.showButton(false, false, false)
        } else {
            teamMemberTotalTV.setText("全部：${teamMemberTotalCount}")
//            if (myTable != null && myTable!!.signupDate != null) {
//                binding.tempPlayDataLbl.text = "報名資料"
//                binding.tempPlayDateLbl.text =
//                    "下次臨打時間：" + myTable!!.signupDate!!.date + " " + myTable!!.interval_show
//                binding.tempPlayDeadlineLbl.text = "報名截止時間：" + myTable!!.signupDate!!.deadline.noSec()
//            }
        }

//        if (myTable!!.people_limit == 0) {
//            showBottom?.submitBtn?.visibility = View.GONE
//            setBottomButtonPadding()
//        } else {
//            signupRows.clear()
//            for (i in 0..myTable!!.people_limit - 1) {
//                var name = ""
//                if (myTable!!.signupNormalTables.count() > i) {
//                    val tmp = myTable!!.signupNormalTables[i].member_name.let {
//                        name = it
//                    }
//                }
//                val signupRow: SignupRow = SignupRow((i+1).toString()+".", name)
//                signupRows.add(signupRow)
//            }
//
//            if (myTable!!.signupStandbyTables.count() > 0) {
//                for (i in 0 until myTable!!.signupStandbyTables.count()) {
//                    var name = ""
//                    val tmp = myTable!!.signupStandbyTables[i].member_name.let {
//                        name = it
//                    }
//                    val signupRow: SignupRow = SignupRow("候補" + (i+1).toString()+".", name)
//                    signupRows.add(signupRow)
//                }
//            }

//            tempPlayAdapter.rows = signupRows
//            tempPlayAdapter.notifyDataSetChanged()
//        }

        if (myTable!!.isSignup) {
            showBottom?.setSubmitBtnTitle("取消報名")
        } else {
            val count = myTable!!.signupNormalTables.size
            if (count >= this.tempPlayCount) {
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
//        if (myTable!!.signupDate != null) {
//            val temp_date_string: String = myTable!!.signupDate!!.date
//
//            //3.如果臨打日期超過現在的日期，關閉臨打
//            var temp_date: Date = Date()
//            temp_date_string.toDate("yyyy-MM-dd")?.let {
//                temp_date = it
//            }
//
//            val now_string: String = Date().toMyString("yyyy-MM-dd")
//            var now: Date = Date()
//            now_string.toDate("yyyy-MM-dd")?.let {
//                now = it
//            }
//
//            //(1)如果報名日期剛好也是臨打日期則可以報名
//            if (temp_date.equals(now)) {
//                isTempPlay = true
//            } else {
//                //(2)如果報名日期已經過了臨打日期則無法報名
//                //(3)如果報名日期還沒過了臨打日期則無法報名
//                isTempPlay = !temp_date.before(now)
//            }
//        } else {
//            isTempPlay = false
//        }

        //3.如果沒有臨打名額是0，關閉臨打
        if (this.tempPlayCount == 0) {
            isTempPlay = false
        }
    }

    val cancel: ()-> Unit = {
        prev()
    }

    val like: ()-> Unit = {
        likeButtonPressed()
    }

    val signup: () -> Unit = {
        submit()
    }

    private fun submit() {

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

        if (focusTabIdx == 1) {
            submitLeave()
        }
        else if (focusTabIdx == 2) {

            submitTempPlay()
//            if (myTable != null && myTable!!.signupDate != null) {
//
//                var deadline_time: Date? = null
//                myTable!!.signupDate!!.deadline.toDate("yyyy-MM-dd HH:mm:ss")?.let {
//                    deadline_time = it
//                }
//
//                if (deadline_time != null) {
//                    val now: Date = Date()
//                    if (now.after(deadline_time)) {
//
//                        msg = "已經超過報名截止時間，請下次再報名"
//                        if (myTable!!.isSignup) {
//                            msg = "已經超過取消報名截止時間，無法取消報名"
//                        }
//                        warning(msg)
//                        return
//                    }
//                } else {
//                    warning("無法取得報名截止時間，無法報名，請洽管理員")
//                    return
//                }
//
//                loadingAnimation.start()
//                dataService.signup(
//                    this,
//                    myTable!!.token,
//                    member.token!!,
//                    myTable!!.signupDate!!.token
//                ) { success ->
//
//                    runOnUiThread {
//                        loadingAnimation.stop()
//                    }
//
//                    if (success) {
//                        val jsonString: String = dataService.jsonString
//                        val successTable: SuccessTable? = jsonToModel(jsonString)
//                        if (successTable != null && successTable.success) {
//                            runOnUiThread {
//                                info(successTable.msg, "", "確定") {
//                                    refresh()
//                                }
//                            }
//                        } else {
//                            if (successTable != null) {
//                                runOnUiThread {
//                                    warning(successTable.msg)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    private fun submitLeave() {
        //如果要請假
        if (!isTeamMemberLeave) {
            warning("是否確定要請假？", "取消", "是") {
                if (this.teamMemberToken != null) {
                    this.teamMemberLeave(true)
                }
            }
        }
        // 如果要取消請假
        else {
            warning("是否確定要取消請假？", "關閉", "取消請假") {
                if (this.teamMemberToken != null) {
                    this.teamMemberLeave(false)
                }
            }
        }
    }

    private fun submitTempPlay() {
        //如果要報名
        if (!isAddTempPlay) {
            warning("是否確定要加入臨打？", "取消", "是") {
                this.tempPlayAdd(true)
            }
        }
        // 如果要取消請假
        else {
            warning("是否確定要取消臨打？", "關閉", "取消臨打") {
                this.tempPlayAdd(false)
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
                //val people_limit = myTable!!.people_limit

                if (position < signupNormalCount) {
                    val signup_normal_model = myTable!!.signupNormalTables[position]
                    //print(signup_normal_model.member_token)
                    getMemberOne(signup_normal_model.member_token)

                }
                if (position > this.tempPlayCount) {
                    val signup_standby_model = myTable!!.signupStandbyTables[position]
                    getMemberOne(signup_standby_model.member_token)
                }
            } else {
                warning("只有球隊管理員可以檢視報名者資訊")
            }
        }
    }

    private fun _tabPressed(idx: Int) {

        focusTabIdx = idx

        when (focusTabIdx) {
            0-> {
                teamMemberContainerLL?.visibility = View.GONE
                introduceContainerLL?.visibility = View.VISIBLE
                //tempPlayContainerLL?.visibility = View.GONE

                showBottom?.showButton(false, true, false)
            }
            1-> {
                introduceContainerLL?.visibility = View.GONE
                teamMemberContainerLL?.visibility = View.VISIBLE
                addIconText2.visibility = View.VISIBLE

                teamMemberTableView!!.adapter = teamMemberAdapter
                teamMemberListTV.text = "正式隊員："

                if (teamMemberScrollListener != null) {
                    teamMemberTableView!!.removeOnScrollListener(teamMemberScrollListener!!)
                }
                if (tempPlayScrollListener != null) {
                    teamMemberTableView!!.removeOnScrollListener(tempPlayScrollListener!!)
                }

                if (!isTeamMemberLoaded) {
                    teamMemberScrollListener = MemberTeamScrollListener(linearLayoutManager)
                    teamMemberTableView!!.addOnScrollListener(teamMemberScrollListener!!)

                    getTeamMemberList(this.teamMemberPage, this.teamMemberPerPage)
                    isTeamMemberLoaded = true
                } else {
                    setTeamMemberSummary()
                }

                //teamMemberTotalTV.on()

                this.setTeamMemberBottom()
            }
            2-> {
                introduceContainerLL?.visibility = View.GONE
                teamMemberContainerLL?.visibility = View.VISIBLE
                addIconText2.visibility = View.GONE

                teamMemberTableView!!.adapter = tempPlayAdapter
                teamMemberListTV.text = "臨打隊員："


                if (teamMemberScrollListener != null) {
                    teamMemberTableView!!.removeOnScrollListener(teamMemberScrollListener!!)
                }
                if (tempPlayScrollListener != null) {
                    teamMemberTableView!!.removeOnScrollListener(tempPlayScrollListener!!)
                }

                if (!isTempPlayLoaded) {

                    tempPlayScrollListener = TempPlayScrollListener(linearLayoutManager)
                    teamMemberTableView!!.addOnScrollListener(tempPlayScrollListener!!)
                    getTempPlayList(this.tempPlayPage, this.tempPlayPerPage)
                    isTempPlayLoaded = true
                } else {
                    setTempPlaySummary()
                }
                //setSignupData()

                setTempPlayBottom()
            }
        }
    }

    override fun teamMemberInfo(idx: Int) {
        if (myTable != null && (focusTabIdx == 1 || focusTabIdx == 2)) {
            if (myTable!!.manager_token == member.token) {
                val teamMemberRow: TeamMemberTable = items1[idx]
                getMemberOne(teamMemberRow.memberTable!!.token)
            } else {
                warning("只有球隊管理員可以檢視報名者資訊")
            }
        }
    }

    override fun setRefreshListener() {
        if (refreshLayout != null) {
            refreshListener = SwipeRefreshLayout.OnRefreshListener {
                refresh()
            }
            refreshLayout!!.setOnRefreshListener(refreshListener)
        }
    }

    inner class MemberTeamScrollListener(recyelerViewLinearLayoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener(recyelerViewLinearLayoutManager) {

        //page: 目前在第幾頁
        //totalItemCount: 已經下載幾頁
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            //page已經+1了
            if (page == teamMemberTotalPage && teamMemberScrollListener != null) {
                teamMemberTableView?.removeOnScrollListener(teamMemberScrollListener!!)
            }
            getTeamMemberList(page, teamMemberPerPage)
        }
    }

    inner class TempPlayScrollListener(recyelerViewLinearLayoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener(recyelerViewLinearLayoutManager) {

        //page: 目前在第幾頁
        //totalItemCount: 已經下載幾頁
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            //page已經+1了
            if (page == tempPlayTotalPage && tempPlayScrollListener != null) {
                teamMemberTableView?.removeOnScrollListener(tempPlayScrollListener!!)
            }
            getTempPlayList(page, tempPlayPerPage)
        }
    }

    override fun tabPressed(idx: Int) {
        //println(idx)
        _tabPressed(idx)
    }

    override fun tapPressed(idx: Int) {

        for ((i, tap) in countTaps.withIndex()) {
            ((i == idx) then { tap.on() }) ?: tap.off()
        }

        this.filterItems.clear()
        if (idx == 0) {
            this.filterItems = items1.clone() as ArrayList<TeamMemberTable>
        } else if (idx == 1) {
            this.filterItems = items1.filter { !it.isLeave } as ArrayList<TeamMemberTable>
        } else if (idx == 2) {
            this.filterItems = items1.filter { it.isLeave } as ArrayList<TeamMemberTable>
        }
        this.teamMemberAdapter.rows = this.filterItems
        this.teamMemberAdapter.notifyDataSetChanged()
    }

    override fun iconText2Pressed(iconStr: String) {
        if (iconStr == "ic_add_svg") {
            if (myTable != null) {
                if (myTable!!.manager_token == member.token) {
                    toManagerTeamMember(token!!)
                } else {
                    warning("只有球隊管理員可以新增隊員")
                }
            }
        }
    }
}

fun ShowTeamVC.setTeamMemberBottom() {
    if (this.isTeamMember && !this.isTeamMemberLeave) {
        showBottom!!.showButton(true, false, false)
        showBottom!!.setSubmitBtnTitle("請假")
        showBottom!!.changeSubmitToNormalBtn()
    } else if (this.isTeamMember && this.isTeamMember) {
        showBottom!!.showButton(true, false, false)
        showBottom!!.setSubmitBtnTitle("取消")
        showBottom!!.changeSubmitToCancelBtn()
    } else {
        showBottom!!.showButton(false, false, false)
    }
}

fun ShowTeamVC.setTempPlayBottom() {

    if (this.tempPlayCount == 0) {
        showBottom!!.showButton(false, false, false)
    } else {
        if (this.isAddTempPlay) {
            showBottom!!.showButton(true, false, false)
            showBottom!!.setSubmitBtnTitle("取消")
            showBottom!!.changeSubmitToCancelBtn()
        } else {
            showBottom!!.showButton(true, false, false)
            showBottom!!.setSubmitBtnTitle("報名")
            showBottom!!.changeSubmitToNormalBtn()
        }
    }
}

fun ShowTeamVC.setTeamMemberSummary() {
    teamMemberTotalTV.setText("全部：${teamMemberTotalCount}位")
    teamMemberPlayTV.setText("打球：${teamMemberTotalCount - leaveCount}位")
    teamMemberLeaveTV.setText("請假：${leaveCount}位")
}

fun ShowTeamVC.setTempPlaySummary() {
    teamMemberTotalTV.setText("全部：${tempPlayCount}位")
    teamMemberPlayTV.setText("報名：${items2.size}位")
    teamMemberLeaveTV.setText("候補：0位")
}

fun ShowTeamVC.teamMemberLeave(doLeave: Boolean) {
    val doLeaveWarning: String = ((doLeave) then {"已經請假了"}) ?: "已經取消請假了"
    loadingAnimation.start()

    TeamService.leave(this, this.teamMemberToken!!, this.nextDate) { success ->
        runOnUiThread {
            loadingAnimation.stop()
        }

        if (success) {
            val jsonString: String = TeamService.jsonString
            val successTable: SuccessTable? = jsonToModel(jsonString)
            if (successTable != null && successTable.success) {
                runOnUiThread {
                    this.info(doLeaveWarning, "關閉") {
                        this.getTeamMemberList(1, PERPAGE)
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

fun ShowTeamVC.tempPlayAdd(doAdd: Boolean) {
    val doAddWarning: String = ((doAdd) then {"報名臨打成功"}) ?: "已經取消臨打"
    loadingAnimation.start()

    TeamService.tempPlayAdd(this, token!!, member.token!!, this.nextDate) { success ->
        runOnUiThread {
            loadingAnimation.stop()
        }

        if (success) {
            val jsonString: String = TeamService.jsonString
            val successTable: SuccessTable? = jsonToModel(jsonString)
            if (successTable != null && successTable.success) {
                runOnUiThread {
                    this.info(doAddWarning, "關閉") {
                        this.getTempPlayList(1, PERPAGE)
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

class TeamMemberAdapter(val context: Context, val delegate: BaseActivity?=null): RecyclerView.Adapter<TeamMemberShowViewHolder>() {

    private var selectedItemPosition: Int = -1
    var rows: ArrayList<TeamMemberTable> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.team_member_list_show_cell, parent, false)

        return TeamMemberShowViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: TeamMemberShowViewHolder, position: Int) {

        val row: TeamMemberTable = rows[position]

        holder.noTV?.setNO(position + 1)
        if (row.memberTable != null && row.memberTable!!.featured_path != null) {
            holder.avatarIV?.avatar(row.memberTable!!.featured_path)
        }
        holder.nameTV?.text = row.memberTable?.nickname
        if (row.created_at != null) {
            holder.createdTV?.text = row.created_at.noSec()
        }

        if (row.isLeave && holder.leaveTV != null) {
            holder.leaveTV!!.visibility = View.VISIBLE
            holder.leaveTV!!.text = "請假\n${row.leaveTime.noSec().noYear()}"
        } else {
            holder.leaveTV?.visibility = View.INVISIBLE
        }

        holder.viewHolder.setOnClickListener {
            selectedItemPosition = position
            notifyDataSetChanged()
            delegate?.teamMemberInfo(position)
        }

        if (selectedItemPosition == position) {
            holder.viewHolder.setBackgroundColor(getColor(context, R.color.CELL_SELECTED))
        } else {
            holder.viewHolder.setBackgroundColor(getColor(context, R.color.MY_BLACK))
        }
    }

    override fun getItemCount(): Int {
        return rows.size
    }
}

class TempPlayAdapter(val context: Context, val delegate: BaseActivity?=null): RecyclerView.Adapter<TeamMemberShowViewHolder>() {

    private var selectedItemPosition: Int = -1
    var count: Int = 0
    var rows: ArrayList<TeamTempPlayTable> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.team_member_list_show_cell, parent, false)

        return TeamMemberShowViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: TeamMemberShowViewHolder, position: Int) {

        holder.noTV?.setNO(position + 1)

        if (position < rows.size) {
            val row: TeamTempPlayTable = rows[position]

            if (row.memberTable != null && row.memberTable!!.featured_path != null) {
                if (holder.avatarIV != null) {
                    holder.avatarIV?.avatar(row.memberTable!!.featured_path)
                }
            }

            holder.nameTV?.text = row.memberTable?.nickname
            if (row.created_at != null) {
                holder.createdTV?.text = row.created_at.noSec()
            }

            holder.viewHolder.setOnClickListener {
                selectedItemPosition = position
                notifyDataSetChanged()
                delegate?.teamMemberInfo(position)
            }

            if (selectedItemPosition == position) {
                holder.viewHolder.setBackgroundColor(getColor(context, R.color.CELL_SELECTED))
            } else {
                holder.viewHolder.setBackgroundColor(getColor(context, R.color.MY_BLACK))
            }
        }
    }

    override fun getItemCount(): Int {
        return count
    }
}

class TeamMemberShowViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var noTV: NOTV2? = null
    var avatarIV: ImageView? = null
    var nameTV: TextView? = null
    var createdTV: TextView? = null
    var leaveTV: TextView? = null

    init {
        viewHolder.findViewById<NOTV2>(R.id.noTV) ?. let {
            noTV = it
        }
        viewHolder.findViewById<com.github.siyamed.shapeimageview.CircularImageView>(R.id.avatarIV) ?. let {
            avatarIV = it
        }
        viewHolder.findViewById<TextView>(R.id.nameTV) ?. let {
            nameTV = it
        }
        viewHolder.findViewById<TextView>(R.id.createdATTV) ?. let {
            createdTV = it
        }
        viewHolder.findViewById<TextView>(R.id.leaveTV) ?. let {
            leaveTV = it
        }
    }
}

class TeamMemberSuccessTables2 : Tables2<TeamMemberTable>() {
    var leaveCount: Int = -1

    override fun filterRow() {
    }
}

class TempPlaySuccessTables2: Tables2<TeamTempPlayTable>() {

}
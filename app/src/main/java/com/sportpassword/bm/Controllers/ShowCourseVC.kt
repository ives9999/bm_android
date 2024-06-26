package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.ShowAdapter
import com.sportpassword.bm.Adapters.SignupAdapter
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Data.SignupRow
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.databinding.ActivityShowCourseVcBinding
import com.sportpassword.bm.extensions.*
import com.sportpassword.bm.member
import org.json.JSONObject
import com.sportpassword.bm.functions.jsonToModel

class ShowCourseVC : ShowVC() {

    private lateinit var binding: ActivityShowCourseVcBinding
    private lateinit var view: ViewGroup

    var showTop2: ShowTop2? = null

    var myTable: CourseTable? = null
    var coachTable: CoachTable? = null
    var dateTable: DateTable? = null

    lateinit var courseCoachAdapter: ShowAdapter
    var coachRows: ArrayList<ShowRow> = arrayListOf()

    lateinit var signupAdapter: SignupAdapter

//    lateinit var signupAdapter: GroupAdapter<GroupieViewHolder>
//    lateinit var coachAdapter: GroupAdapter<GroupieViewHolder>

    var signup_date: JSONObject = JSONObject()
    var isSignup: Boolean = false
    var isStandby: Boolean = false
    var canCancelSignup: Boolean = false
    var signup_id: Int = 0
    var course_date: String = ""
    var course_deadline: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityShowCourseVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        layoutInt = R.layout.activity_show_course_vc

        dataService = CourseService

        refreshLayout = binding.refresh
        setRefreshListener()
        bottom_button_count = 2

        super.onCreate(savedInstanceState)

//        tableRowKeys = mutableListOf("weekday_text","interval_show","date","price_text_long","people_limit_text","kind_text","pv","created_at_show")
//        tableRows = hashMapOf(
//            "weekday_text" to hashMapOf("icon" to "calendar","title" to "日期","content" to ""),
//            "interval_show" to hashMapOf( "icon" to "clock","title" to "時段","content" to ""),
//            "date" to hashMapOf( "icon" to "calendar","title" to "期間","content" to ""),
//            "price_text_long" to hashMapOf( "icon" to "money","title" to "收費","content" to ""),
//            "people_limit_text" to hashMapOf( "icon" to "group","title" to "接受報名人數","content" to ""),
//            "kind_text" to hashMapOf( "icon" to "cycle","title" to "週期","content" to ""),
////            "signup_count" to hashMapOf( "icon" to "group","title" to "已報名人數","content" to ""),
//            "pv" to hashMapOf( "icon" to "pv","title" to "瀏覽數","content" to ""),
//            "created_at_show" to hashMapOf( "icon" to "calendar","title" to "建立日期","content" to "")
//        )

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.showPrev(true)
        }

        courseCoachAdapter = ShowAdapter(this)
        binding.coachTableView.adapter = courseCoachAdapter

        signupAdapter = SignupAdapter(this, this)
        binding.signupTableView.adapter = signupAdapter

        init()
        refresh()
    }

    override fun init() {
        super.init()
    }

    override fun initData() {
        super.initData()
    }

//    override fun refresh() {
//        signupRows.clear()
//        super.refresh()
//    }

//    override fun initAdapter() {
//        super.initAdapter()

//        signupAdapter = GroupAdapter()
//        val signupItems = generateSignupItem()
//        signupAdapter.addAll(signupItems)
//        signupTableView.adapter = signupAdapter


//        coachAdapter = GroupAdapter()
//        coachAdapter.setOnItemClickListener { item, view ->
//
//        }
//        val coachItems = generateCoachItem()
//        coachAdapter.addAll(coachItems)
//        coachTableView.adapter = coachAdapter
//    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<CourseTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            runOnUiThread {
                warning(e.localizedMessage)
                //println(e.localizedMessage)
            }
        }
        if (table != null) {
            myTable = table as CourseTable
            myTable!!.filterRow()
            showTop2!!.setTitle(myTable!!.name)
        } else {
            runOnUiThread {
                warning("解析伺服器所傳的字串失敗，請洽管理員")
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

//    override fun refresh() {
//        //super.refresh()
//        if (token != null) {
//            Loading.show(mask)
//            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
//            CourseService.getOne(this, params) { success ->
//                if (success) {
//                    myTable = CourseService.superModel as SuperCourse
//                    if (myTable != null) {
//                        myTable!!.filter()
//                        //myTable!!.print()
//                        coachTable = myTable!!.coach
//                        setMainData()
//                        setSignupData()
//                        setCoachData()
//                        setFeatured()
//
//                        if (myTable!!.isSignup) {
//                            signupButton.setText("取消報名")
//                        } else {
//                            val count = myTable!!.signup_normal_models.count()
//                            if (count >= myTable!!.people_limit) {
//                                signupButton.setText("候補")
//                            } else {
//                                signupButton.setText("報名")
//                            }
//                        }
//                    }
//                }
//                closeRefresh()
//                Loading.hide(mask)
//            }
//        }
//    }

    override fun setData() {

        if (myTable != null) {

            showRows.clear()
            showRows.addAll(arrayListOf(
                ShowRow("weekday_text", "date", "星期幾", myTable!!.weekdays_show),
                ShowRow("interval_show", "clock", "時段", myTable!!.interval_show),
                //ShowRow("date", "date", "期間", myTable!!),
                ShowRow("price_text_long", "money", "收費", myTable!!.price_text_long),
                ShowRow("people_limit_text", "group", "接受報名人數", myTable!!.people_limit_text),
                ShowRow("kind_text", "cycle", "週期", myTable!!.kind_text),
                ShowRow("pv", "pv", "瀏覽數", myTable!!.pv.toString()),
                ShowRow("created_at_show", "date", "建立日期", myTable!!.created_at_show)
            ))

//            if (myTable!!.coach != null) {
//                if (myTable!!.start_date != null && myTable!!.start_date.isNotEmpty()) {
//                    val date = myTable!!.start_date + " ~ " + myTable!!.end_date
//                    val showRow: ShowRow = getRowFromKey("date")
//                    showRow.show = date
//                } else {
//                    val showRow: ShowRow = getRowFromKey("date")
//                    showRows.remove(showRow)
//                }
//
//                val interval = myTable!!.interval_show
//                val showRow: ShowRow = getRowFromKey("interval_show")
//                showRow.show = interval
//            }
            //setMainData(myTable!!)

            if (myTable!!.coach != null) {
                coachTable = myTable!!.coach
                setCoachData()
//                val items = generateCoachItem()
//                coachAdapter.update(items)
            }

            if (myTable!!.dateTable != null) {
                dateTable = myTable!!.dateTable
                //setNextTime()
                setSignupData()
            }

            if (myTable!!.isSignup) {
                binding.bottomViewShowInclude.signupButton.text = "取消報名"
            } else {
                val count: Int = myTable!!.signupNormalTables.size
                if (count >= myTable!!.people_limit) {
                    binding.bottomViewShowInclude.signupButton.text = "候補"
                } else {
                    binding.bottomViewShowInclude.signupButton.text = "報名"
                }
            }
        }
    }

    fun setSignupData() {

        val date: String = dateTable!!.date
        val start_time: String = myTable!!.start_time_show
        val end_time: String = myTable!!.end_time_show
        if (myTable!!.people_limit > 0) {
            binding.signupDateLbl.text = "下次上課時間：" + date + " " + start_time + " ~ " + end_time
        } else {
            binding.signupDateLbl.text = "未提供報名"
            binding.bottomViewShowInclude.signupButton.visibility = View.GONE
        }

        signupRows.clear()
        if (myTable != null) {
            for (i in 0..myTable!!.people_limit - 1) {
                var name = ""
                if (myTable!!.signupNormalTables.count() > i) {
                    val tmp = myTable!!.signupNormalTables[i].member_name?.let {
                        name = it
                    }
                }
                val signupRow: SignupRow = SignupRow((i+1).toString()+".", name)
                signupRows.add(signupRow)
            }
        }

        if (myTable!!.signupStandbyTables.count() > 0) {
            for (i in 0..myTable!!.signupStandbyTables.count() - 1) {
                var name = ""
                val tmp = myTable!!.signupStandbyTables[i].member_name?.let {
                    name = it
                }
                val signupRow: SignupRow = SignupRow("候補" + (i+1).toString()+".", name)
                signupRows.add(signupRow)
            }
        }

        signupAdapter.rows = signupRows
        signupAdapter.notifyDataSetChanged()
//        val items = generateSignupItem()
//        signupAdapter.update(items)

        isSignup = myTable!!.isSignup
        //isStandby
        //canCancelSignup

        if (isSignup) {
            binding.bottomViewShowInclude.signupButton.text = "取消報名"
        } else {
            binding.bottomViewShowInclude.signupButton.text = "報名"
        }
    }

    fun setCoachData() {

        coachRows.clear()
        coachRows.addAll(arrayListOf(
            ShowRow(NAME_KEY, "coach", "教練", coachTable!!.name),
            ShowRow(MOBILE_KEY, "mobile", "行動電話", coachTable!!.mobile_show),
            ShowRow(LINE_KEY, "line", "line", coachTable!!.line),
            ShowRow(FB_KEY, "fb", "fb", coachTable!!.fb),
            ShowRow(YOUTUBE_KEY, "youtube", "youtube", coachTable!!.youtube),
            ShowRow(WEBSITE_KEY, "website", "網站", coachTable!!.website),
            ShowRow(EMAIL_KEY, "email", "EMail", coachTable!!.email)
        ))

        courseCoachAdapter.rows = coachRows
        courseCoachAdapter.notifyDataSetChanged()
//        for (coachRow in coachRows) {
//            val key = coachRow.key
//            val kc = coachTable!!::class
//            kc.memberProperties.forEach {
//                if (key == it.name) {
//                    val value = it.getter.call(coachTable).toString()
//                    coachRow.show = value
//                }
//            }
//        }
//        courseCoachAdapter.rows = coachRows
//        courseCoachAdapter.notifyDataSetChanged()


        //println(coachTableRows)
//        val items = generateCoachItem()
//        coachAdapter.update(items)
    }

//    fun setNextTime() {
//        val date: String = dateTable!!.date
//        val start_time: String = myTable!!.start_time_show
//        val end_time: String = myTable!!.end_time_show
//        val next_time = "下次上課時間：${date} ${start_time} ~ ${end_time}"
//        signupDateLbl.text = next_time

//        let nextCourseTime: [String: String] = courseTable!.nextCourseTime
//        for key in signupTableRowKeys {
//            signupTableRows[key]!["content"] = nextCourseTime[key]
//        }
//    }



//    fun setSignupData() {
//        isSignup = false
//                    if (superCouse!!.signups.size > 0) {
//                        for (signup in superCouse!!.signups) {
//                            //signup.printRow()
//                            if (signup.member_id == member.id) {
//                                this.signup = signup
//                                if (signup.status == "normal") {
//                                    isSignup = true
//                                }
//                                break
//                            }
//                        }
//                    }
//                    if (isSignup) {
//                        signupBtn.text = "取消報名"
//                    } else {
//                        signupBtn.text = "報名"
//                    }
//    }

//    fun generateCourseItem(): ArrayList<Item> {
//
//        val items: ArrayList<Item> = arrayListOf()
//        var icon = ""
//        var title = ""
//        var content = ""
//        var isPressed: Boolean = false
//        for (key in tableRowKeys) {
//            if (tableRows.containsKey(key)) {
//                val row = tableRows[key]!!
//                if (row.containsKey("icon")) {
//                    icon = row["icon"]!!
//                }
//                if (row.containsKey("title")) {
//                    title = row["title"]!!
//                }
//                if (row.containsKey("content")) {
//                    content = row["content"]!!
//                }
//                if (row.containsKey("isPressed")) {
//                    isPressed = row["isPressed"]!!.toBoolean()
//                }
//                if (icon.length > 0 && title.length > 0) {
//                    val iconCell = IconCell(this, icon, title, content, isPressed)
//                    iconCell.delegate = this
//                    items.add(iconCell)
//                }
//            }
//        }
//
//        return items
//    }

//    fun generateSignupItem(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        var icon = ""
//        var title = ""
//        var content = ""
//        if (myTable != null) {
//            for (i in 0..myTable!!.people_limit - 1) {
////            if (signupTableRows.containsKey(key)) {
////                val row = signupTableRows[key]!!
////                if (row.containsKey("icon")) {
////                    icon = row["icon"]!!
////                }
////                if (row.containsKey("title")) {
////                    title = row["title"]!!
////                }
////                if (row.containsKey("content")) {
////                    content = row["content"]!!
////                    if (key == MOBILE_KEY) {
////                        content = content.mobileShow()
////                    }
////                }
//////                if (icon.length > 0 && title.length > 0) {
//////                    val iconCell = IconCell(this@ShowCourseVC, icon, title, content, false)
//////                    iconCell.delegate = this
//////                    items.add(iconCell)
//////                }
////            }
//                var name = ""
//                if (myTable!!.signup_normal_models.count() > i) {
//                    val tmp = myTable!!.signup_normal_models[i].member_name?.let {
//                        name = it
//                    }
//                }
//                val olCell = OlCell(this, (i + 1).toString(), name)
//                items.add(olCell)
//            }
//            if (myTable!!.signup_standby_models.count() > 0) {
//                for (i in 0..myTable!!.signup_standby_models.count() - 1) {
//                    var name = ""
//                    val tmp = myTable!!.signup_standby_models[i].member_name?.let {
//                        name = it
//                    }
//                    val olCell = OlCell(this, "候補" + (i + 1).toString(), name)
//                    items.add(olCell)
//                }
//            }
//        }
//
//        return items
//    }

//    fun generateCoachItem(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        var icon = ""
//        var title = ""
//        var content = ""
//        var isPressed: Boolean = false
//        for (key in coachTableRowKeys) {
//            if (coachTableRows.containsKey(key)) {
//                val row = coachTableRows[key]!!
//                if (row.containsKey("icon")) {
//                    icon = row["icon"]!!
//                }
//                if (row.containsKey("title")) {
//                    title = row["title"]!!
//                }
//                if (row.containsKey("content")) {
//                    content = row["content"]!!
//                    if (key == MOBILE_KEY) {
//                        content = content.mobileShow()
//                    }
//                }
//                if (row.containsKey("isPressed")) {
//                    isPressed = row["isPressed"]!!.toBoolean()
//                }
//                if (icon.length > 0 && title.length > 0) {
//                    val iconCell = IconCell(this@ShowCourseVC, icon, title, content, isPressed)
//                    iconCell.delegate = this
//                    items.add(iconCell)
//                }
//            }
//        }
//
//        return items
//    }

    fun didSelectRowAt(view: View, position: Int) {
        //println("delegate:"+position)
        val parent = view.parent
        if (parent is RecyclerView) {
            val p = parent as RecyclerView
            //println(p.getIDString())
            val id = p.getIDString()
            if (id == binding.coachTableView.getIDString()) {
                //println(position)
                val key = coachRows[position].key
                if (key == NAME_KEY) {
//                    val intent = Intent(this, ShowActivity::class.java)
//                    //intent.putExtra("type", source)
//                    intent.putExtra("token", coachTable!!.token)
//                    intent.putExtra("title", coachTable!!.name)
//                    startActivity(intent)
                } else if (key == MOBILE_KEY) {
                    val mobile = coachTable!!.mobile
                    this.mobile = mobile
                    val permission: String = android.Manifest.permission.CALL_PHONE
                    if (permissionExist(permission)) {
                        mobile.makeCall(this)
                    } else {
                        val permissions = arrayOf(permission)
                        requestPermission(permissions, REQUEST_PHONE_CALL)
                    }
                } else if (key == LINE_KEY) {
                    val line = coachTable!!.line
                    line.line(this)
                } else if (key == FB_KEY) {
                    val fb = coachTable!!.fb
                    fb.fb(this)
                } else if (key == YOUTUBE_KEY) {
                    val youtube = coachTable!!.youtube
                    youtube.youtube(this)
                } else if (key == WEBSITE_KEY) {
                    val website = coachTable!!.website
                    website.website(this)
                } else if (key == EMAIL_KEY) {
                    val email = coachTable!!.email
                    email.email(this)
                }
            }
        }
    }

    fun submitButtonPressed(view: View) {
        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }
        loadingAnimation.start()
        CourseService.signup_date(this, token!!, member.token!!, myTable!!.dateTable!!.token) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }

            if (success) {

                val jsonString: String = CourseService.jsonString
                val signupDateTable: SignupDateTable? = jsonToModel<SignupDateTable>(jsonString)
                if (signupDateTable != null) {
                    isSignup = signupDateTable.isSignup
                    isStandby = signupDateTable.isStandby
                    canCancelSignup = signupDateTable.cancel
                    course_date = signupDateTable.date
                    course_deadline = signupDateTable.deadline

                    runOnUiThread {
                        showSignupModal()
                    }
                } else {
                    runOnUiThread {
                        warning("無法解析傳回的signup date table")
                    }
                }


//                signup_date = CourseService.signup_date
//                isSignup = signup_date.getBoolean("isSignup")
//                isStandby = signup_date.getBoolean("isStandby")
//                canCancelSignup = signup_date.getBoolean("cancel")
//                course_date = signup_date.getString("date")
//                course_deadline = signup_date.getString("deadline")
            } else {
                runOnUiThread {
                    warning(CourseService.msg)
                }
            }
        }
    }

    fun showSignupModal() {

        val signup_html = "報名課程日期是：" + course_date + "\r\n" + "報名取消截止時間是：" + course_deadline.noSec()
        val cancel_signup_html = "報名課程日期是：" + course_date + "\r\n" + "報名取消截止時間是：" + course_deadline.noSec()
        val cant_cancel_signup_html = "已經超過取消報名期限，無法取消\r\n" + "報名課程日期是：" + course_date + "\r\n" + "報名取消截止時間是：" + course_deadline.noSec()
        val standby_html = "此課程報名已經額滿，請排候補" + "\r\n" + signup_html

        var title: String = ""
        var msg = signup_html

        if (isSignup) {
            title = "取消報名"
            if (canCancelSignup) {
                msg = cancel_signup_html
            } else {
                msg = cant_cancel_signup_html
            }
        } else {
            if (isStandby) {
                title = "候補報名"
                msg = standby_html
            } else {
                title = "報名"
            }
        }

        val alert = AlertDialog.Builder(this).create()
        alert.setTitle(title)
        alert.setMessage(msg)
        if ((!myTable!!.isSignup && !canCancelSignup) || (myTable!!.isSignup && canCancelSignup)) {
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, title) { _, _ ->
                signup()
            }
        }
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉") { _, _ ->
            //finish()
        }
        alert.show()
    }

    fun signup() {
        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }
        loadingAnimation.start()
        CourseService.signup(this, token!!, member.token!!, myTable!!.dateTable!!.token) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }

            if (success) {
                val jsonString: String = CourseService.jsonString
                val successTable: SuccessTable? = jsonToModel<SuccessTable>(jsonString)
                if (successTable != null && successTable.success) {
                    runOnUiThread {
                        info(successTable.msg, "", "確定") {
                            //signupAdapter.notifyDataSetChanged()
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

//            val msg = CourseService.msg
//            var title = "警告"
//            val alert = AlertDialog.Builder(this).create()
//            runOnUiThread {
//                if (CourseService.success) {
//                    title = "提示"
//                    alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉") { _, _ ->
//                        refresh()
//                    }
//                } else {
//                    alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉") { _, _ ->
//                    }
//                }
//                alert.setTitle(title)
//                alert.setMessage(msg)
//
//                alert.show()
//            }
        }
    }

    //showSignupInfo is click signup data to call back function defined in BaseActivity
    override fun showSignupInfo(position: Int) {

        if (coachTable != null) {
            if (coachTable!!.manager_id == member.id) {
                val people_limit = myTable!!.people_limit
                if (position < people_limit) {
                    val signup_normal_model = myTable!!.signupNormalTables[position]
                    getMemberOne(signup_normal_model.member_token)

                } else {
                    val idx: Int = position - myTable!!.signupNormalTables.size
                    val signup_standby_model = myTable!!.signupStandbyTables[idx]
                    getMemberOne(signup_standby_model.member_token)
                }
            } else {
                warning("只有球隊管理員可以檢視報名者資訊")
            }
        }
    }

//    fun signupListButtonPressed(view: View) {
//        //println("aaa")
//        val intent = Intent(this, SignupListVC::class.java)
//        intent.putExtra("able", "course")
//        intent.putExtra("able_token", token)
//        startActivity(intent)
//    }

//    fun likeButtonPressed(view: View) {
//        if (!member.isLoggedIn) {
//            toLogin()
//        }
//    }
}

//class CourseCoachAdapter(val context: Context): RecyclerView.Adapter<CourseCoachViewHolder>() {
//
//    var rows: ArrayList<ShowRow> = arrayListOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseCoachViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val viewHolder = inflater.inflate(R.layout.iconcell, parent, false)
//
//        return CourseCoachViewHolder(viewHolder)
//    }
//
//    override fun onBindViewHolder(holder: CourseCoachViewHolder, position: Int) {
//
//        val row: ShowRow = rows[position]
//        var iconID: Int = 0
//        iconID = context.resources.getIdentifier(row.icon, "drawable", context.packageName)
//        holder.icon.setImageResource(iconID)
//        holder.title.text = row.title
//        holder.show.text = row.show
//    }
//
//    override fun getItemCount(): Int {
//        return rows.size
//    }
//
//}

//class CourseCoachViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
//
//    var icon: ImageView = viewHolder.findViewById(R.id.icon)
//    var title: TextView = viewHolder.findViewById(R.id.title)
//    var show: TextView = viewHolder.findViewById(R.id.content)
//}

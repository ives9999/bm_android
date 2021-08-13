package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.IconCell
import com.sportpassword.bm.Adapters.IconCellDelegate
import com.sportpassword.bm.Adapters.OlCell
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.json.JSONObject
import kotlin.reflect.full.memberProperties

class ShowCourseVC : ShowVC() {

    //var course_id: Int? = null
    //var source: String  = "course" //course

    val coachTableRowKeys:Array<String> = arrayOf(NAME_KEY,MOBILE_KEY,LINE_KEY,FB_KEY,YOUTUBE_KEY,WEBSITE_KEY,EMAIL_KEY)
    var coachTableRows: HashMap<String, HashMap<String, String>> = hashMapOf(
        NAME_KEY to hashMapOf("icon" to "coach","title" to "教練","content" to "","isPressed" to "true"),
        MOBILE_KEY to hashMapOf("icon" to "mobile","title" to "行動電話","content" to "","isPressed" to "true"),
        LINE_KEY to hashMapOf("icon" to "lineicon","title" to "line id","content" to "","isPressed" to "false"),
        FB_KEY to hashMapOf("icon" to "fb","title" to "fb","content" to "","isPressed" to "true"),
        YOUTUBE_KEY to hashMapOf("icon" to "youtube","title" to "youtube","content" to "","isPressed" to "true"),
        WEBSITE_KEY to hashMapOf("icon" to "website","title" to "網站","content" to "","isPressed" to "true"),
        EMAIL_KEY to hashMapOf("icon" to "email","title" to "郵件","content" to "","isPressed" to "true")
    )

    val signupTableRowKeys:Array<String> = arrayOf("date", "deadline")
    var signupTableRows: HashMap<String, HashMap<String, String>> = hashMapOf(
            "date" to hashMapOf("icon" to "calendar","title" to "報名上課日期","content" to "","isPressed" to "false"),
            "deadline" to hashMapOf("icon" to "clock","title" to "報名截止時間","content" to "","isPressed" to "false")
    )

    var myTable: CourseTable? = null
    var coachTable: CoachTable? = null
    var dateTable: DateTable? = null

    lateinit var signupAdapter: GroupAdapter<GroupieViewHolder>
    lateinit var coachAdapter: GroupAdapter<GroupieViewHolder>

    var signup_date: JSONObject = JSONObject()
    var isSignup: Boolean = false
    var isStandby: Boolean = false
    var canCancelSignup: Boolean = false
    var signup_id: Int = 0
    var course_date: String = ""
    var course_deadline: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_show_course_vc)

        dataService = CourseService

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        super.onCreate(savedInstanceState)

        tableRowKeys = mutableListOf("weekday_text","interval_show","date","price_text_long","people_limit_text","kind_text","pv","created_at_show")
        tableRows = hashMapOf(
            "weekday_text" to hashMapOf("icon" to "calendar","title" to "日期","content" to ""),
            "interval_show" to hashMapOf( "icon" to "clock","title" to "時段","content" to ""),
            "date" to hashMapOf( "icon" to "calendar","title" to "期間","content" to ""),
            "price_text_long" to hashMapOf( "icon" to "money","title" to "收費","content" to ""),
            "people_limit_text" to hashMapOf( "icon" to "group","title" to "接受報名人數","content" to ""),
            "kind_text" to hashMapOf( "icon" to "cycle","title" to "週期","content" to ""),
//            "signup_count" to hashMapOf( "icon" to "group","title" to "已報名人數","content" to ""),
            "pv" to hashMapOf( "icon" to "pv","title" to "瀏覽數","content" to ""),
            "created_at_show" to hashMapOf( "icon" to "calendar","title" to "建立日期","content" to "")
        )

        refresh()
    }

    override fun initAdapter() {
        super.initAdapter()

        signupAdapter = GroupAdapter()
        val signupItems = generateSignupItem()
        signupAdapter.addAll(signupItems)
        signupTableView.adapter = signupAdapter


        coachAdapter = GroupAdapter()
        coachAdapter.setOnItemClickListener { item, view ->

        }
        val coachItems = generateCoachItem()
        coachAdapter.addAll(coachItems)
        coachTableView.adapter = coachAdapter
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<CourseTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            warning(e.localizedMessage)
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as CourseTable
            myTable!!.filterRow()
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
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

            if (myTable!!.coach != null) {
                if (myTable!!.start_date != null && myTable!!.start_date.isNotEmpty()) {
                    val date = myTable!!.start_date + " ~ " + myTable!!.end_date
                    tableRows["date"]!!["content"] = date
                } else {
                    tableRowKeys.remove("date");
                    //println(tableRowKeys);
                    tableRows.remove("date");
                }

                val interval = myTable!!.interval_show
                tableRows["interval_show"]!!["content"] = interval
            }
            setMainData(myTable!!)

            if (myTable!!.coach != null) {
                coachTable = myTable!!.coach
                setCoachData()
                val items = generateCoachItem()
                coachAdapter.update(items)
            }

            if (myTable!!.dateTable != null) {
                dateTable = myTable!!.dateTable
                //setNextTime()
                setSignupData()
            }

            if (myTable!!.isSignup) {
                signupButton.text = "取消報名"
            } else {
                val count: Int = myTable!!.signup_normal_models.size
                if (count >= myTable!!.people_limit) {
                    signupButton.text = "候補"
                } else {
                    signupButton.text = "報名"
                }
            }
        }
    }

    fun setSignupData() {
        val date: String = dateTable!!.date
        val start_time: String = myTable!!.start_time_show
        val end_time: String = myTable!!.end_time_show
        if (myTable!!.people_limit > 0) {
            signupDateLbl.text = "下次上課時間：" + date + " " + start_time + " ~ " + end_time
        } else {
            signupDateLbl.text = "未提供報名"
            signupButton.visibility = View.GONE
        }
        val items = generateSignupItem()
        signupAdapter.update(items)

        isSignup = myTable!!.isSignup
        //isStandby
        //canCancelSignup

        if (isSignup) {
            signupButton.text = "取消報名"
        } else {
            signupButton.text = "報名"
        }
    }

    fun setCoachData() {
        for (key in coachTableRowKeys) {
            val kc = coachTable!!::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(coachTable).toString()
                    coachTableRows[key]!!["content"] = value
                }
            }
        }
        //println(coachTableRows)
        val items = generateCoachItem()
        coachAdapter.update(items)
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

    fun generateSignupItem(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        var icon = ""
        var title = ""
        var content = ""
        if (myTable != null) {
            for (i in 0..myTable!!.people_limit - 1) {
//            if (signupTableRows.containsKey(key)) {
//                val row = signupTableRows[key]!!
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
////                if (icon.length > 0 && title.length > 0) {
////                    val iconCell = IconCell(this@ShowCourseVC, icon, title, content, false)
////                    iconCell.delegate = this
////                    items.add(iconCell)
////                }
//            }
                var name = ""
                if (myTable!!.signup_normal_models.count() > i) {
                    val tmp = myTable!!.signup_normal_models[i].member_name?.let {
                        name = it
                    }
                }
                val olCell = OlCell(this, (i + 1).toString(), name)
                items.add(olCell)
            }
            if (myTable!!.signup_standby_models.count() > 0) {
                for (i in 0..myTable!!.signup_standby_models.count() - 1) {
                    var name = ""
                    val tmp = myTable!!.signup_standby_models[i].member_name?.let {
                        name = it
                    }
                    val olCell = OlCell(this, "候補" + (i + 1).toString(), name)
                    items.add(olCell)
                }
            }
        }

        return items
    }

    fun generateCoachItem(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        var icon = ""
        var title = ""
        var content = ""
        var isPressed: Boolean = false
        for (key in coachTableRowKeys) {
            if (coachTableRows.containsKey(key)) {
                val row = coachTableRows[key]!!
                if (row.containsKey("icon")) {
                    icon = row["icon"]!!
                }
                if (row.containsKey("title")) {
                    title = row["title"]!!
                }
                if (row.containsKey("content")) {
                    content = row["content"]!!
                    if (key == MOBILE_KEY) {
                        content = content.mobileShow()
                    }
                }
                if (row.containsKey("isPressed")) {
                    isPressed = row["isPressed"]!!.toBoolean()
                }
                if (icon.length > 0 && title.length > 0) {
                    val iconCell = IconCell(this@ShowCourseVC, icon, title, content, isPressed)
                    iconCell.delegate = this
                    items.add(iconCell)
                }
            }
        }

        return items
    }

    override fun didSelectRowAt(view: View, position: Int) {
        //println("delegate:"+position)
        val parent = view.parent
        if (parent is RecyclerView) {
            val p = parent as RecyclerView
            //println(p.getIDString())
            val id = p.getIDString()
            if (id == coachTableView.getIDString()) {
                //println(position)
                val key = coachTableRowKeys[position]
                if (key == NAME_KEY) {
                    val intent = Intent(this, ShowActivity::class.java)
                    //intent.putExtra("type", source)
                    intent.putExtra("token", coachTable!!.token)
                    intent.putExtra("title", coachTable!!.name)
                    startActivity(intent)
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
        Loading.show(mask)
        CourseService.signup(this, token!!, member.token!!, myTable!!.dateTable!!.token, course_deadline) { success ->
            Loading.hide(mask)
            val msg = CourseService.msg
            var title = "警告"
            val alert = AlertDialog.Builder(this).create()
            if (CourseService.success) {
                title = "提示"
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉") { _, _ ->
                    refresh()
                }
            } else {
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉") { _, _ ->
                }
            }
            alert.setTitle(title)
            alert.setMessage(msg)

            alert.show()
        }
    }

    fun signupListButtonPressed(view: View) {
        //println("aaa")
        val intent = Intent(this, SignupListVC::class.java)
        intent.putExtra("able", "course")
        intent.putExtra("able_token", token)
        startActivity(intent)
    }

    fun signupButtonPressed(view: View) {
        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }
        Loading.show(mask)
        CourseService.signup_date(this, token!!, member.token, myTable!!.dateTable!!.token) { success ->
            Loading.hide(mask)
            if (success) {
                signup_date = CourseService.signup_date
                //println(signup_date)
                isSignup = signup_date.getBoolean("isSignup")
                isStandby = signup_date.getBoolean("isStandby")
                canCancelSignup = signup_date.getBoolean("cancel")
                //signup_id = signup_date.getInt("signup_id")
                course_date = signup_date.getString("date")
                course_deadline = signup_date.getString("deadline")

                showSignupModal()
            } else {
                warning(CourseService.msg)
            }
        }
    }

//    fun likeButtonPressed(view: View) {
//        if (!member.isLoggedIn) {
//            toLogin()
//        }
//    }
}

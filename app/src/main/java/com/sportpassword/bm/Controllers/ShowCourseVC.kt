package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Adapters.IconCell
import com.sportpassword.bm.Adapters.IconCellDelegate
import com.sportpassword.bm.Models.SuperCoach
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.json.JSONObject
import kotlin.reflect.full.memberProperties

class ShowCourseVC : BaseActivity(), IconCellDelegate {

    var course_id: Int? = null
    var source: String  = "course" //course
    var course_token: String? = null    // course token
    val tableRowKeys:Array<String> = arrayOf("weekday_text","date","interval","price_text_long","people_limit_text","kind_text","signup_count","pv","created_at_text")
    var tableRows: HashMap<String, HashMap<String,String>> = hashMapOf(
            "weekday_text" to hashMapOf("icon" to "calendar","title" to "日期","content" to ""),
            "date" to hashMapOf( "icon" to "calendar","title" to "期間","content" to ""),
            "interval" to hashMapOf( "icon" to "clock","title" to "時段","content" to ""),
            "price_text_long" to hashMapOf( "icon" to "money","title" to "收費","content" to ""),
            "people_limit_text" to hashMapOf( "icon" to "group","title" to "接受報名人數","content" to ""),
            "kind_text" to hashMapOf( "icon" to "cycle","title" to "週期","content" to ""),
//            "signup_count" to hashMapOf( "icon" to "group","title" to "已報名人數","content" to ""),
            "pv" to hashMapOf( "icon" to "pv","title" to "瀏覽數","content" to ""),
            "created_at_text" to hashMapOf( "icon" to "calendar","title" to "建立日期","content" to "")
    )

    val signupTableRowKeys:Array<String> = arrayOf("date", "deadline")
    var signupTableRows: HashMap<String, HashMap<String, String>> = hashMapOf(
            "date" to hashMapOf("icon" to "calendar","title" to "報名上課日期","content" to "","isPressed" to "false"),
            "deadline" to hashMapOf("icon" to "clock","title" to "報名截止時間","content" to "","isPressed" to "false")
    )

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
    var superCourse: SuperCourse? = null
    var superCoach: SuperCoach? = null

    lateinit var adapter: GroupAdapter<ViewHolder>
    lateinit var signupAdapter: GroupAdapter<ViewHolder>
    lateinit var coachAdapter: GroupAdapter<ViewHolder>

//    var isSignup: Boolean = false
//    var signup: Signup? = null

    var signup_date: JSONObject = JSONObject()
    var isSignup: Boolean = false
    var canCancelSignup: Boolean = false
    var signup_id: Int = 0
    var course_date: String = ""
    var course_deadline: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_course_vc)

        val title = intent.getStringExtra("title")
        setMyTitle(title)

        if (intent.hasExtra("course_token")) {
            course_token = intent.getStringExtra("course_token")
        }
        dataService = CourseService

        if (intent.hasExtra("sourse")) {
            source = intent.getStringExtra("source")
        }

        webViewSettings(this@ShowCourseVC, contentView)

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        refresh()
    }

    fun initAdapter() {
        adapter = GroupAdapter()
        val timetableItems = generateCourseItem()
        adapter.addAll(timetableItems)
        tableView.adapter = adapter

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

    override fun refresh() {
        //super.refresh()
        if (course_token != null) {
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf("token" to course_token!!, "member_token" to member.token)
            CourseService.getOne(this, params) { success ->
                if (success) {
                    superCourse = CourseService.superModel as SuperCourse
                    //superCourse!!.print()
                    if (superCourse != null) {
                        superCourse!!.filter()
                        superCourse!!.print()
                        superCoach = superCourse!!.coach
                        setMainData()
                        setSignupData()
                        setCoachData()
                        setFeatured()

                        if (superCourse!!.isSignup) {
                            signupButton.setText("取消報名")
                        } else {
                            signupButton.setText("報名")
                        }
                    }
                }
                closeRefresh()
                Loading.hide(mask)
            }
        }
    }

    fun setMainData() {
        for (key in tableRowKeys) {
            val kc = superCourse!!::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(superCourse).toString()
                    tableRows[key]!!["content"] = value
                }
            }
        }
        val content: String = "<html lang=\"zh-TW\"><head><meta charset=\"UTF-8\">"+superCourse!!.content_style+"</head><body><div class=\"content\">"+superCourse!!.content+"</div>"+"</body></html>"
        //println(content)
        contentView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
        //contentView.loadData(content, "text/html", "UTF-8")
        //contentView.loadData("<html><body style='background-color:#000;'>Hello, world!</body></html>", "text/html", "UTF-8")

        val date = superCourse!!.start_date + " ~ " + superCourse!!.end_date
        tableRows["date"]!!["content"] = date

        val interval = superCourse!!.start_time_text + " ~ " + superCourse!!.end_time_text
        tableRows["interval"]!!["content"] = interval

        //tableRows["signup_count"]!!["content"] = superCourse!!.signup_count.toString() + "人"

//                    println(tableRows)
        var items = generateCourseItem()
        adapter.update(items)
    }

    fun setSignupData() {
//        val nextCourseTime = superCourse!!.nextCourseTime
//        for (key in signupTableRowKeys) {
//            signupTableRows[key]!!["content"] = nextCourseTime[key]!!
//        }
        val items = generateSignupItem()
        signupAdapter.update(items)
    }

    fun setCoachData() {
        for (key in coachTableRowKeys) {
            val kc = superCoach!!::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(superCoach).toString()
                    coachTableRows[key]!!["content"] = value
                }
            }
        }
        //println(coachTableRows)
        val items = generateCoachItem()
        coachAdapter.update(items)
    }

    fun setFeatured() {
        if (superCourse!!.featured_path.isNotEmpty()) {
            var featured_path = superCourse!!.featured_path
            featured_path.image(this@ShowCourseVC, featured)
        } else {
            featured.setImageResource(R.drawable.loading_square_120)
        }
    }

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

    fun generateCourseItem(): ArrayList<Item> {

        val items: ArrayList<Item> = arrayListOf()
        var icon = ""
        var title = ""
        var content = ""
        var isPressed: Boolean = false
        for (key in tableRowKeys) {
            if (tableRows.containsKey(key)) {
                val row = tableRows[key]!!
                if (row.containsKey("icon")) {
                    icon = row["icon"]!!
                }
                if (row.containsKey("title")) {
                    title = row["title"]!!
                }
                if (row.containsKey("content")) {
                    content = row["content"]!!
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

    fun generateSignupItem(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        var icon = ""
        var title = ""
        var content = ""
        for (key in signupTableRowKeys) {
            if (signupTableRows.containsKey(key)) {
                val row = signupTableRows[key]!!
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
                if (icon.length > 0 && title.length > 0) {
                    val iconCell = IconCell(this@ShowCourseVC, icon, title, content, false)
                    iconCell.delegate = this
                    items.add(iconCell)
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
//        println("delegate:"+position)
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
                    intent.putExtra("type", source)
                    intent.putExtra("token", superCoach!!.token)
                    intent.putExtra("title", superCoach!!.name)
                    startActivity(intent)
                } else if (key == MOBILE_KEY) {
                    val mobile = superCoach!!.mobile
                    this.mobile = mobile
                    val permission: String = android.Manifest.permission.CALL_PHONE
                    if (permissionExist(permission)) {
                        mobile.makeCall(this)
                    } else {
                        val permissions = arrayOf(permission)
                        requestPermission(permissions, REQUEST_PHONE_CALL)
                    }
                } else if (key == LINE_KEY) {
                    val line = superCoach!!.line
                    line.line(this)
                } else if (key == FB_KEY) {
                    val fb = superCoach!!.fb
                    fb.fb(this)
                } else if (key == YOUTUBE_KEY) {
                    val youtube = superCoach!!.youtube
                    youtube.youtube(this)
                } else if (key == WEBSITE_KEY) {
                    val website = superCoach!!.website
                    website.website(this)
                } else if (key == EMAIL_KEY) {
                    val email = superCoach!!.email
                    email.email(this)
                }
            }
        }
    }

    fun showSignupModal() {
        var title: String = ""
        if (isSignup) {
            title = "取消報名"
        } else {
            title = "報名"
        }
        val signup_html = "報名課程日期是：" + course_date + "\r\n" + "報名取消截止時間是：" + course_deadline.noSec()
        val cancel_signup_html = "報名課程日期是：" + course_date + "\r\n" + "報名取消截止時間是：" + course_deadline.noSec()
        val cant_cancel_signup_html = "已經超過取消報名期限，無法取消\r\n" + "報名課程日期是：" + course_date + "\r\n" + "報名取消截止時間是：" + course_deadline.noSec()
        var msg = signup_html

        if (!isSignup && !canCancelSignup) {
            msg = signup_html
        } else {
            if (isSignup && canCancelSignup) {
                msg = cancel_signup_html
            } else {
                msg = cant_cancel_signup_html
                title = "警告"
            }
        }
        val alert = AlertDialog.Builder(this).create()
        alert.setTitle(title)
        alert.setMessage(msg)
        if ((!isSignup && !canCancelSignup) || (isSignup && canCancelSignup)) {
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, title, { Interface, j ->
                signup()
            })
        }
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉", { Interface, j ->
            //finish()
        })
        alert.show()
    }

    fun signup() {
        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }
        Loading.show(mask)
        CourseService.signup(this, course_token!!, member.token, signup_id, course_date, course_deadline) { success ->
            Loading.hide(mask)
            val msg = CourseService.msg
            var title = "警告"
            val alert = AlertDialog.Builder(this).create()
            if (CourseService.success) {
                title = "提示"
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉", { Interface, j ->
                    refresh()
                })
            } else {
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "關閉", { Interface, j ->
                })
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
        intent.putExtra("able_token", course_token)
        startActivity(intent)
    }

    fun signupButtonPressed(view: View) {
        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }
        Loading.show(mask)
        CourseService.signup_date(this, course_token!!, member.token) { success ->
            Loading.hide(mask)
            if (success) {
                signup_date = CourseService.signup_date
                //println(signup_date)
                isSignup = signup_date.getBoolean("isSignup")
                canCancelSignup = signup_date.getBoolean("cancel")
                signup_id = signup_date.getInt("signup_id")
                course_date = signup_date.getString("course_date")
                course_deadline = signup_date.getString("course_deadline")

                showSignupModal()
            }
        }
    }
}

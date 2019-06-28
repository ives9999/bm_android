package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Adapters.IconCell
import com.sportpassword.bm.Adapters.IconCellDelegate
import com.sportpassword.bm.Models.Signup
import com.sportpassword.bm.Models.SuperCoach
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlin.reflect.full.memberProperties

class ShowCourseVC : BaseActivity(), IconCellDelegate {

    var course_id: Int? = null
    var source: String  = "course" //course
    var course_token: String? = null    // course token
    val tableRowKeys:Array<String> = arrayOf("weekday_text","date","interval","charge_text","limit_text","signup_count")
    var tableRows: HashMap<String, HashMap<String,String>> = hashMapOf(
            "weekday_text" to hashMapOf("icon" to "calendar","title" to "日期","content" to ""),
            "date" to hashMapOf( "icon" to "calendar","title" to "期間","content" to ""),
            "interval" to hashMapOf( "icon" to "clock","title" to "時段","content" to ""),
            "charge_text" to hashMapOf( "icon" to "money","title" to "收費","content" to ""),
            "limit_text" to hashMapOf( "icon" to "group","title" to "接受報名人數","content" to ""),
            "signup_count" to hashMapOf( "icon" to "group","title" to "已報名人數","content" to "")
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
    lateinit var coachAdapter: GroupAdapter<ViewHolder>

    var isSignup: Boolean = false
    var signup: Signup? = null

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
            CourseService.getOne(this, course_token) { success ->
                if (success) {
                    superCourse = CourseService.superCourse
                    if (superCourse != null) {
                        superCoach = superCourse!!.coach
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
                        items = generateCoachItem()
                        coachAdapter.update(items)
                        if (superCourse!!.featured_path.isNotEmpty()) {
                            var featured_path = superCourse!!.featured_path
                            if (!featured_path.startsWith("http://") && !featured_path.startsWith("https://")) {
                                featured_path = BASE_URL + featured_path
                            }
                            Picasso.with(this@ShowCourseVC)
                                    .load(featured_path)
                                    .placeholder(R.drawable.loading_square_120)
                                    .error(R.drawable.loading_square_120)
                                    .into(featured)
                        } else {
                            featured.setImageResource(R.drawable.loading_square_120)
                        }
                    }

                    isSignup = false
//                    if (timetable!!.signups.size > 0) {
//                        for (signup in timetable!!.signups) {
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
                    if (isSignup) {
                        signupBtn.text = "取消報名"
                    } else {
                        signupBtn.text = "報名"
                    }
                }
                closeRefresh()
                Loading.hide(mask)
            }
        }
    }

    fun generateCourseItem(): ArrayList<Item> {

        var items: ArrayList<Item> = arrayListOf()
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

    fun generateCoachItem(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
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
}

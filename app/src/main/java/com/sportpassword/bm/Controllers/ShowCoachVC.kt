package com.sportpassword.bm.Controllers

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.Adapters.IconCell
import com.sportpassword.bm.Adapters.IconCellDelegate
import com.sportpassword.bm.Models.SuperCoach
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.Models.Timetables
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_show_coach_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import kotlin.reflect.full.memberProperties

class ShowCoachVC : BaseActivity(), IconCellDelegate {

    var type: String? = null
    var token: String? = null

    var superCoach: SuperCoach? = null
    var superCourses: SuperCourses? = null
    var timetables: Timetables? = null
    var city_id: Int = 0
    //var params: HashMap<String, Any> = hashMapOf() define in BaseActivity
    var featured: Bitmap? = null

    val startNum: Int = 6
    val endNum: Int = 23
    val columnNum: Int = 8
    val totalGridCount = (endNum-startNum)*columnNum
    var timetableCellWidth: Float = 150f
    var timetableCellHeight: Int = 150
    var timetableCellBorderWidth: Int = 1
    val gridViews: ArrayList<ViewGroup> = arrayListOf()
    val eventViews: ArrayList<ViewGroup> = arrayListOf()
    var eventTag: Int = 0

    lateinit var contactAdapter: GroupAdapter<ViewHolder>
    lateinit var courseAdapter: GroupAdapter<ViewHolder>
    val contactRowKeys:ArrayList<String> = arrayListOf(MOBILE_KEY,LINE_KEY,FB_KEY,YOUTUBE_KEY,WEBSITE_KEY,EMAIL_KEY,COACH_SENIORITY_KEY,CREATED_AT_KEY,PV_KEY)
    var contactRows: HashMap<String, HashMap<String,String>> = hashMapOf(
    MOBILE_KEY to hashMapOf("icon" to "mobile","title" to "行動電話","content" to "","isPressed" to "true"),
    LINE_KEY to hashMapOf("icon" to "lineicon","title" to "line id","content" to "","isPressed" to "false"),
    FB_KEY to hashMapOf("icon" to "fb","title" to "fb","content" to "","isPressed" to "true"),
    YOUTUBE_KEY to hashMapOf("icon" to "youtube","title" to "youtube","content" to "","isPressed" to "true"),
    WEBSITE_KEY to hashMapOf("icon" to "website","title" to "網站","content" to "","isPressed" to "true"),
    EMAIL_KEY to hashMapOf("icon" to "email","title" to "email","content" to "","isPressed" to "true"),
    COACH_SENIORITY_KEY to hashMapOf("icon" to "seniority","title" to "年資","content" to "","isPressed" to "false"),
    CREATED_AT_KEY to hashMapOf("icon" to "calendar","title" to "建立日期","content" to "","isPressed" to "false"),
    PV_KEY to hashMapOf("icon" to "pv","title" to "瀏覽數","content" to "","isPressed" to "false")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_coach_vc)

        type = intent.getStringExtra("type")
        token = intent.getStringExtra("token")
        val title = intent.getStringExtra("title")
        setMyTitle(title)
        //timetableCellWidth = screenWidth.toFloat() / columnNum.toFloat()
        //addGrid()

        webViewSettings(this@ShowCoachVC, chargeWebView)
        webViewSettings(this@ShowCoachVC, expWebView)
        webViewSettings(this@ShowCoachVC, licenseWebView)
        webViewSettings(this@ShowCoachVC, featWebView)
        webViewSettings(this@ShowCoachVC, detailWebView)

        refreshLayout = refresh
        setRefreshListener()

        initContactAdapter()
        initCourseAdapter()
        refresh()
    }

    override fun refresh() {
        Loading.show(mask)
        val params: HashMap<String, String> = hashMapOf("token" to token!!)
        CoachService.getOne(this, params) { success1 ->
            if (success1) {
                this.superCoach = CoachService.superModel as SuperCoach
                for (key in contactRowKeys) {
                    val kc = superCoach!!::class
                    kc.memberProperties.forEach {
                        if (key == it.name) {
                            val value = it.getter.call(superCoach).toString()
                            contactRows[key]!!["content"] = value
                        }
                    }
                }
                this.getFeatured()

                var filter: HashMap<String, Any>? = null
                if (superCoach != null) {
                    filter = hashMapOf("coach_id" to superCoach!!.id)
                }

                CourseService.getList(this, token, filter, 1, 100) { success ->

                    if (success) {
                        superCourses = CourseService.superModel as SuperCourses
                        val items = generateCourseItems()
                        courseAdapter.update(items)
                        courseAdapter.notifyDataSetChanged()
                    }
                }
                Loading.hide(mask)
                closeRefresh()
//                CoachService.getTT(this, token!!, type!!) { success2 ->
//                    if (success2) {
//                        this.timetables = CoachService.timetables
////                        for (row in this.timetables!!.rows) {
////                            row.print()
////                        }
//                        markEvent()
//                    }
//                }
            }
        }
    }

    fun initContactAdapter() {
        contactAdapter = GroupAdapter()
        val contactItems = generateContactItem()
        contactAdapter.addAll(contactItems)
        contactTableView.adapter = contactAdapter
    }
    fun initCourseAdapter() {
        courseAdapter = GroupAdapter()
        val courseItems = generateCourseItems()
        courseAdapter.addAll(courseItems)
        courseTableView.adapter = courseAdapter
    }

    fun generateContactItem(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        var icon = ""
        var title = ""
        var content = ""
        var isPressed: Boolean = false
        for (key in contactRowKeys) {
            if (contactRows.containsKey(key)) {
                val row = contactRows[key]!!
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
                    val iconCell = IconCell(this@ShowCoachVC, icon, title, content, isPressed)
                    iconCell.delegate = this
                    items.add(iconCell)
                }
            }
        }

        return items
    }

    fun generateCourseItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (superCourses != null) {
            for (row in superCourses!!.rows) {
                items.add(ManagerCourseItem(this@ShowCoachVC, row))
            }
        }

        return items
    }

    fun getFeatured() {
        val url = BASE_URL + superCoach!!.featured_path
        CoachService.getImage(url) { success ->
            if (success) {
                this.featured = CoachService.image
                if (this.featured != null) {
                    featuredView.layoutParams.height = featured!!.height
                    featuredView.setImageBitmap(featured!!)
                }
            }
        }
        setData()
    }

    fun setData() {
        if (superCoach != null) {
            if (superCoach!!.citys != null) {
                if (superCoach!!.citys.size > 0) {
                    for (city in superCoach!!.citys) {
                        city_id = city.id
                        params["city_id"] = arrayListOf(city_id)
                        params["city_type"] = "all"
                        buyBtn.setText(city.name)
                    }
                } else {
                    buyBtn.visibility = View.GONE
                }
            } else {
                buyBtn.visibility = View.GONE
            }
            setContact()
            setWeb(chargeWebView, superCoach!!.charge)
            setWeb(expWebView, superCoach!!.exp)
            setWeb(licenseWebView, superCoach!!.license)
            setWeb(featWebView, superCoach!!.feat)
            setWeb(detailWebView, superCoach!!.content)
        }
    }

    fun setContact() {
        val items = generateContactItem()
        contactAdapter.update(items)
    }

    fun setWeb(webView: WebView, content: String) {
        val html: String = "<html><HEAD><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">"+body_css+"</HEAD><body>"+content+"</body></html>"
        //println(content)
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }

//    fun addGrid() {
//        for (i in 0..totalGridCount-1) {
//            val startTime: Int = i / columnNum + startNum
//            val weekday: Int = i % columnNum
//
//            val grid = generateView(timetableCellWidth, timetableCellHeight.toFloat(), i)
//            grid.setBackgroundResource(R.drawable.timetable_item_border)
//
//            if (weekday == 0) {
//                addTimeLabel(grid, startTime)
//            }
//
//            timetableView.addView(grid)
//            gridViews.add(grid)
//            val c = ConstraintSet()
//            c.clone(timetableView)
//            if (startTime == startNum) {
//                c.connect(grid.id, ConstraintSet.TOP, timetableView.id, ConstraintSet.TOP, 0)
//                if (weekday == 0) {
//                    c.connect(grid.id, ConstraintSet.LEFT, timetableView.id, ConstraintSet.LEFT, 0)
//                } else {
//                    c.connect(grid.id, ConstraintSet.LEFT, gridViews[i-1].id, ConstraintSet.RIGHT, 0)
//                }
//            } else {
//                c.connect(grid.id, ConstraintSet.TOP, gridViews[i-8].id, ConstraintSet.BOTTOM, 0)
//                if (weekday == 0) {
//                    c.connect(grid.id, ConstraintSet.LEFT, timetableView.id, ConstraintSet.LEFT, 0)
//                } else {
//                    c.connect(grid.id, ConstraintSet.LEFT, gridViews[i-1].id, ConstraintSet.RIGHT, 0)
//                }
//            }
//            c.applyTo(timetableView)
//        }
//    }
    protected fun generateView(width: Float, height: Float, tag: Any?=null, bColor: Int?=null): ViewGroup {
        val view = ConstraintLayout(this)
        if (tag != null) {
            view.tag = tag
        }
        view.id = View.generateViewId()
        val lp = LinearLayout.LayoutParams(width.toInt(), height.toInt())
        view.layoutParams = lp
        if (bColor != null) {
            view.backgroundColor = bColor
        }

        view.setOnClickListener {
            //clickEvent(it)
        }
        return view
    }
    fun addTimeLabel(parent: ViewGroup, time: Int) {
        val t = TextView(this)
        t.id = View.generateViewId()
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        t.layoutParams = lp
        t.text = String.format("%d-%d", time, time+1)
        t.textColor = Color.WHITE
        t.gravity = Gravity.CENTER
        parent.addView(t)
    }

//    fun markEvent() {
//
//        for (i in 0..timetables!!.rows.size-1) {
//            val row = timetables!!.rows[i]
//            val hours = (row.end_time.toDateTime("HH:mm:ss")!!.timeIntervalSince(row.start_time.toDateTime("HH:mm:ss")!!)).toFloat()/(60*60)
//            //println(hours)
//
//            val eventViewWidth = timetableCellWidth-8*timetableCellBorderWidth
//            val eventViewHeight = timetableCellHeight*hours-8*timetableCellBorderWidth
//            val eventView = generateView(eventViewWidth, eventViewHeight, 1000+i , row._color.toColor())
//            timetableView.addView(eventView)
//            eventViews.add(eventView)
//            val c1 = ConstraintSet()
//            c1.clone(timetableView)
//            val weekday = row.weekday
//            val _start_hour = row._start_hour
//            val _start_minute = row._start_minute
//            var y: Float = timetableCellBorderWidth*2.toFloat()
//            if (_start_minute > 0) {
//                val a = _start_minute.toFloat()/60
//                val b = (timetableCellHeight-2*timetableCellBorderWidth).toFloat()
//                y = a * b
//            }
//            val idx = columnNum*(_start_hour-startNum) + weekday
//            c1.connect(eventView.id, ConstraintSet.TOP, gridViews[idx].id, ConstraintSet.TOP, y.toInt())
//            c1.connect(eventView.id, ConstraintSet.LEFT, gridViews[idx].id, ConstraintSet.LEFT, timetableCellBorderWidth*4)
//            c1.applyTo(timetableView)
//
//            val titleLbl = TextView(this)
//            var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//            lp.setMargins(3, 3, 3, 3)
//            titleLbl.layoutParams = lp
//            titleLbl.gravity = Gravity.CENTER
//            titleLbl.text = row.title
//            titleLbl.textSize = 12f
//            titleLbl.textColor = Color.BLACK
//            //titleLbl.setBackgroundColor(Color.WHITE)
//            eventView.addView(titleLbl)
//
//            /*
//            val line = DrawLine(this, 3f, 55f, eventViewWidth-6f, 55f)
//            eventView.addView(line)
//
//            val contentLbl = TextView(this)
//            lp = LinearLayout.LayoutParams(eventViewWidth, eventViewHeight-55)
//            lp.setMargins(3, 10, 0, 0)
//            contentLbl.layoutParams = lp
//            contentLbl.gravity = Gravity.CENTER
//            contentLbl.text = "人數：\n" + row.limit_text
//            contentLbl.textSize = 12f
//            contentLbl.textColor = Color.BLACK
//            eventView.addView(contentLbl)
//            */
//        }
//    }
//    protected fun clickEvent(view: View) {
//
//        val tag = view.tag as Int
////        println(tag)
//        //event
//        if (tag >= 1000) {
//            val idx = tag - 1000
//            val event = timetables!!.rows[idx]
//            val intent = Intent(this@ShowCoachVC, ShowTimetableVC::class.java)
//            intent.putExtra("tt_id", event.id)
//            intent.putExtra("source", type)
//            intent.putExtra("token", token)
//            startActivity(intent)
//        }
//    }

    override fun didSelectRowAt(view: View, position: Int) {
        val key = contactRowKeys[position]
        if (key == MOBILE_KEY) {
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

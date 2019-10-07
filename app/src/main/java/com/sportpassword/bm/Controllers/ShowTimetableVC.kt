package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.webkit.*
import com.sportpassword.bm.Adapters.IconCell
import com.sportpassword.bm.Adapters.IconCellDelegate
import com.sportpassword.bm.Models.SuperCoach
import com.sportpassword.bm.Models.Timetable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TimetableService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_show_timetable_vc.*
import kotlinx.android.synthetic.main.iconcell.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.makeCall
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.util.jar.Manifest
import kotlin.reflect.full.memberProperties

class ShowTimetableVC : BaseActivity(), IconCellDelegate {

    var tt_id: Int? = null
    var source: String?  = null //coach or arena
    var token: String? = null    // coach token or arena token
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
    var timetable: Timetable? = null
    var superCoach: SuperCoach? = null

    lateinit var timetableAdapter: GroupAdapter<ViewHolder>
    lateinit var coachAdapter: GroupAdapter<ViewHolder>

    var isSignup: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_timetable_vc)

        setMyTitle("時刻表")
        dataService = TimetableService

        tt_id = intent.getIntExtra("tt_id", 0)
        source = intent.getStringExtra("source")
        token = intent.getStringExtra("token")

        webViewSettings(this@ShowTimetableVC, contentView)

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        refresh()
    }

    override fun refresh() {
        //super.refresh()
        if (tt_id != null) {
            TimetableService.getOne(this, tt_id!!, source!!, token!!) { success ->
                if (success) {
                    timetable = TimetableService.timetable
                    superCoach = TimetableService.superCoach

                    for (key in tableRowKeys) {
                        val kc = timetable!!::class
                        kc.memberProperties.forEach {
                            if (key == it.name) {
                                val value = it.getter.call(timetable).toString()
                                tableRows[key]!!["content"] = value
                            }
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

                    timetableTitle.text = timetable!!.title
                    val content: String = "<html lang=\"zh-TW\"><head><meta charset=\"UTF-8\">"+timetable!!.content_style+"</head><body><div class=\"content\">"+timetable!!.content+"</div>"+"</body></html>"
                    //println(content)
                    contentView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
                    //contentView.loadData(content, "text/html", "UTF-8")
                    //contentView.loadData("<html><body style='background-color:#000;'>Hello, world!</body></html>", "text/html", "UTF-8")

                    val date = timetable!!.start_date + " ~ " + timetable!!.end_date
                    tableRows["date"]!!["content"] = date

                    val interval = timetable!!.start_time_text + " ~ " + timetable!!.end_time_text
                    tableRows["interval"]!!["content"] = interval

                    tableRows["signup_count"]!!["content"] = timetable!!.signup_count.toString() + "人"

//                    println(tableRows)
                    var items = generateTimetableItem()
                    timetableAdapter.update(items)

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
                }
                closeRefresh()
            }
        }
    }

    fun initAdapter() {
        timetableAdapter = GroupAdapter()
        val timetableItems = generateTimetableItem()
        timetableAdapter.addAll(timetableItems)
        tableView.adapter = timetableAdapter

        coachAdapter = GroupAdapter()
        coachAdapter.setOnItemClickListener { item, view ->

        }
        val coachItems = generateCoachItem()
        coachAdapter.addAll(coachItems)
        coachTableView.adapter = coachAdapter
    }

    fun generateTimetableItem(): ArrayList<Item> {
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
                    val iconCell = IconCell(this@ShowTimetableVC, icon, title, content, isPressed)
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
                    val iconCell = IconCell(this@ShowTimetableVC, icon, title, content, isPressed)
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
                    intent.putExtra("token", token)
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

    fun signupSubmit(view: View) {
        if (!member.isLoggedIn) {
            warning("請先登入")
        } else {
            if (timetable != null) {
                val tt_id = timetable!!.id
                Loading.show(mask)
                if (!isSignup) {//報名
//                    dataService.signup(this, "timetable", token!!, member.token, tt_id) { success ->
//                        Loading.hide(mask)
//                        if (!success) {
//                            warning(dataService.msg)
//                        } else {
//                            info("您已經報名成功")
//                            refresh()
//                        }
//                    }
                } else {//取消報名
//                    if (signup != null) {
//                        dataService.cancelSignup(this, "timetable", member.token, signup!!.id) { success ->
//                            unmask()
//                            if (!success) {
//                                warning(dataService.msg)
//                            } else {
//                                info("取消報名成功")
//                                refresh()
//                            }
//                        }
//                    } else {
//                        warning("沒有取得報名資料，無法取消報名，請洽管理員")
//                    }
                }
            } else {
                warning("沒有取得課程表，請重新進入")
            }
        }
    }
}



















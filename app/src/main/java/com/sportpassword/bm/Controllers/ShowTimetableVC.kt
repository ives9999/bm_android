package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.webkit.*
import com.sportpassword.bm.Models.SuperCoach
import com.sportpassword.bm.Models.Timetable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TimetableService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_show_timetable_vc.*
import kotlinx.android.synthetic.main.iconcell.*
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
    val coachTableRowKeys:Array<String> = arrayOf(NAME_KEY,MOBILE_KEY,LINE_KEY,FB_KEY,YOUTUBE_KEY,WEBSITE_KEY)
    var coachTableRows: HashMap<String, HashMap<String, String>> = hashMapOf(
        NAME_KEY to hashMapOf("icon" to "coach","title" to "教練","content" to "","isPressed" to "true"),
        MOBILE_KEY to hashMapOf("icon" to "mobile","title" to "行動電話","content" to "","isPressed" to "true"),
        LINE_KEY to hashMapOf("icon" to "lineicon","title" to "line id","content" to "","isPressed" to "true"),
        FB_KEY to hashMapOf("icon" to "fb","title" to "fb","content" to "","isPressed" to "true"),
        YOUTUBE_KEY to hashMapOf("icon" to "youtube","title" to "youtube","content" to "","isPressed" to "true"),
        WEBSITE_KEY to hashMapOf("icon" to "website","title" to "網站","content" to "","isPressed" to "true")
    )
    var timetable: Timetable? = null
    var superCoach: SuperCoach? = null

    lateinit var timetableAdapter: GroupAdapter<ViewHolder>
    lateinit var coachAdapter: GroupAdapter<ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_timetable_vc)

        setMyTitle("時刻表")
        dataService = TimetableService

        tt_id = intent.getIntExtra("tt_id", 0)
        source = intent.getStringExtra("source")
        token = intent.getStringExtra("token")

        val settings = contentView.settings
        settings.javaScriptEnabled = true
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true
        settings.textZoom = 125
        settings.blockNetworkImage = false
        settings.loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true
        }
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false
        settings.domStorageEnabled = true
//        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        contentView.fitsSystemWindows = true
        contentView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        contentView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                //toast("Page loading.")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                //toast("Page loaded complete")
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request!!.url.toString()
                //println(url)
                url.website(this@ShowTimetableVC)
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                //println(url)
                url!!.website(this@ShowTimetableVC)
                return true
            }
        }
        contentView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {

            }
        }
        contentView.setBackgroundColor(Color.TRANSPARENT)

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
                }
            }
        }
    }
}


class IconCell(val context: Context, val icon: String, val title: String, val content: String, val isPressed: Boolean=false): Item() {

    var delegate: IconCellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.icon.setImage(icon)
        viewHolder.title.text = title + "："
        viewHolder.content.text = content
        viewHolder.itemView.setOnClickListener {
            if (delegate != null) {
                delegate!!.didSelectRowAt(it, position)
            }
        }

        if (isPressed) {
            val color = ContextCompat.getColor(context, R.color.MY_GREEN)
            viewHolder.content.textColor = color
        }
    }

    override fun getLayout() = R.layout.iconcell
}

interface IconCellDelegate {

    fun didSelectRowAt(view: View, position: Int)
}



















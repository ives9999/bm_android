package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.google.gson.JsonParseException
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Models.StoreTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.StoreService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_show_course_vc.refresh

class ShowStoreVC : ShowVC() {

    var myTable: StoreTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_show_store_vc)

        dataService = StoreService

        refreshLayout = refresh
        setRefreshListener()

//        initAdapter()
        super.onCreate(savedInstanceState)

//        tableRowKeys = mutableListOf("tel_show","mobile_show","address","fb","line","website","email","interval_show","pv","created_at_show")
//        tableRows = hashMapOf(
//            "tel_show" to hashMapOf("icon" to "tel","title" to "市內電話","content" to ""),
//            "mobile_show" to hashMapOf( "icon" to "mobile","title" to "行動電話","content" to ""),
//            "address" to hashMapOf( "icon" to "marker","title" to "住址","content" to ""),
//            "fb" to hashMapOf( "icon" to "fb","title" to "FB","content" to ""),
//            "line" to hashMapOf( "icon" to "lineicon","title" to "line","content" to ""),
//            "website" to hashMapOf( "icon" to "website","title" to "網站","content" to ""),
//            "email" to hashMapOf( "icon" to "email1","title" to "email","content" to ""),
//            "interval_show" to hashMapOf( "icon" to "clock","title" to "營業時間","content" to ""),
//            "pv" to hashMapOf( "icon" to "pv","title" to "瀏覽數","content" to ""),
//            "created_at_show" to hashMapOf( "icon" to "calendar","title" to "建立日期","content" to "")
//        )
        init()
        refresh()
    }

    override fun init() {
        super.init()
    }

    override fun initData() {
        super.initData()
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<StoreTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            warning(e.localizedMessage!!)
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as StoreTable
            myTable!!.filterRow()
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

    override fun setData() {

        if (myTable != null) {
            showRows.clear()
            showRows.addAll(arrayListOf(
                ShowRow("tel_show", "tel", "市內電話", myTable!!.tel_show),
                ShowRow("mobile_show", "mobile", "行動電話", myTable!!.mobile_show),
                ShowRow("address", "map", "住址", myTable!!.address),
                ShowRow("fb", "fb", "FB", myTable!!.fb),
                ShowRow("line", "line", "Line", myTable!!.line),
                ShowRow("website", "website", "網站", myTable!!.website),
                ShowRow("email", "email1", "EMail", myTable!!.email),
                ShowRow("interval_show", "clock", "營業時間", myTable!!.interval_show),
                ShowRow("pv", "pv", "瀏覽數", myTable!!.pv.toString()),
                ShowRow("created_at_show", "date", "建立日期", myTable!!.created_at_show)
            ))
//            setMainData(myTable!!)
        }
    }

//    fun initAdapter() {
//        adapter = GroupAdapter()
//        val storeItems = generateStoreItem()
//        adapter.addAll(storeItems)
//        tableView.adapter = adapter
//    }
//
//    override fun refresh() {
//        //super.refresh()
//        if (store_token != null) {
//            Loading.show(mask)
//            val params: HashMap<String, String> = hashMapOf("token" to store_token!!, "member_token" to member.token!!)
//            StoreService.getOne(this, params) { success ->
//                if (success) {
//                    superStore = StoreService.superModel as SuperStore
//                    if (superStore != null) {
//                        superStore!!.filter()
//                        //superCourse!!.print()
//                        setMainData()
//                        setFeatured()
//                    }
//                }
//                closeRefresh()
//                Loading.hide(mask)
//            }
//        }
//    }
//
//    fun setMainData() {
//        for (key in tableRowKeys) {
//            val kc = superStore!!::class
//            kc.memberProperties.forEach {
//                if (key == it.name) {
//                    val value = it.getter.call(superStore).toString()
//                    tableRows[key]!!["content"] = value
//                }
//            }
//        }
//        //superStore!!.print();
//        val content: String = "<html lang=\"zh-TW\"><head><meta charset=\"UTF-8\">"+superStore!!.content_style+"</head><body><div class=\"content\">"+superStore!!.content+"</div>"+"</body></html>"
//        //val content: String = "<html lang=\"zh-TW\"><head><meta charset=\"UTF-8\"></head><body style=\"background-color: #000;color:#fff;font-size:28px;\">"+superCourse!!.content+"</body></html>"
//        //println(content)
//        contentView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
//        //contentView.loadData(strHtml, "text/html; charset=utf-8", "UTF-8")
//        //contentView.loadData("<html><body style='background-color:#000;'>Hello, world!</body></html>", "text/html", "UTF-8")
//
//        if (superStore!!.open_time.length > 0) {
//            val business_time = superStore!!.open_time_text + " ~ " + superStore!!.close_time_text
//            tableRows["business_time"]!!["content"] = business_time
//        } else {
//            tableRowKeys.remove("business_time");
//            //println(tableRowKeys);
//            tableRows.remove("business_time");
//        }
//
////                    println(tableRows)
//        val items = generateStoreItem()
//        adapter.update(items)
//    }
//
//    fun setFeatured() {
//        if (superStore!!.featured_path.isNotEmpty()) {
//            var featured_path = superStore!!.featured_path
//            featured_path.image(this, featured)
//        } else {
//            featured.setImageResource(R.drawable.loading_square_120)
//        }
//    }

//    fun generateStoreItem(): ArrayList<Item> {
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
//                if (key == "website" && content.isNotEmpty()) {
//                    content = "連結請按此"
//                }
//                if (key == "fb" && content.isNotEmpty()) {
//                    content = "連結請按此"
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

    fun didSelectRowAt(view: View, position: Int) {
        val key = tableRowKeys[position]
        if (key == MOBILE_KEY) {
            val mobile = myTable!!.mobile
            this.mobile = mobile
            val permission: String = android.Manifest.permission.CALL_PHONE
            if (permissionExist(permission)) {
                mobile.makeCall(this)
            } else {
                val permissions = arrayOf(permission)
                requestPermission(permissions, REQUEST_PHONE_CALL)
            }
        } else if (key == LINE_KEY) {
            val line = myTable!!.line
            line.line(this)
        } else if (key == FB_KEY) {
            val fb = myTable!!.fb
            fb.fb(this)
        } else if (key == WEBSITE_KEY) {
            val website = myTable!!.website
            website.website(this)
        } else if (key == EMAIL_KEY) {
            val email = myTable!!.email
            email.email(this)
        }
    }

}
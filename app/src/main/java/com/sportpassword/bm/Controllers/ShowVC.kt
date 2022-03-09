package com.sportpassword.bm.Controllers

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.ShowAdapter
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Data.SignupRow
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.activity_show_course_vc.contentView
import kotlinx.android.synthetic.main.activity_show_course_vc.featured
import kotlinx.android.synthetic.main.activity_show_course_vc.likeButton
import kotlinx.android.synthetic.main.activity_show_course_vc.refresh
import kotlinx.android.synthetic.main.activity_show_course_vc.tableView
import kotlinx.android.synthetic.main.activity_show_team_vc.*
import kotlinx.android.synthetic.main.mask.*
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import kotlin.reflect.full.memberProperties

open class ShowVC: BaseActivity() {

    var token: String? = null    // course token
    var tableRowKeys:MutableList<String> = mutableListOf()
    var tableRows: HashMap<String, HashMap<String,String>> = hashMapOf()

    val showRows: ArrayList<ShowRow> = arrayListOf()

    var table: Table? = null

    var isLike: Boolean = false
    var likeCount: Int = 0
    var signupRows: ArrayList<SignupRow> = arrayListOf()

    lateinit var showAdapter: ShowAdapter

    var bottom_button_count: Int = 1
    val button_width: Int = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

        refreshLayout = refresh
        setRefreshListener()

        showAdapter = ShowAdapter(this)
        if (tableView != null) {
            tableView.adapter = showAdapter
        }

        setBottomButtonPadding()
        //initAdapter()
        //refresh()
    }

//    open fun initAdapter() {
//        adapter = GroupAdapter()

//        val items = generateMainItem()
//        adapter.addAll(items)
//        tableView.adapter = adapter
//    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    open fun initData() {}

    open fun genericTable() {}

    override fun refresh() {

        if (token != null) {
//            signupRows.clear()
//            showRows.clear()
            initData()
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
            dataService.getOne(this, params) { success ->
                if (success) {
                    genericTable()
                    if (table != null) {
                        table!!.filterRow()

                        runOnUiThread {
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

//    fun generateMainItem(): ArrayList<Item> {
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

    fun setFeatured() {
        if (featured != null) {
            if (table != null && table!!.featured_path.isNotEmpty()) {
                var featured_path = table!!.featured_path
                //featured_path.image(this, featured)
                if (!featured_path.startsWith("http://") && !featured_path.startsWith("https://")) {
                    featured_path = BASE_URL + featured_path
                }

                // get device dimensions
                //val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics

                val url: URL = URL(featured_path)
                val inputStream: InputStream = url.openConnection().getInputStream()
                if (inputStream != null) {
                    val bmp: Bitmap = BitmapFactory.decodeStream(inputStream)
                    val image_width: Float = bmp.width.toFloat()
                    val image_height: Float = bmp.height.toFloat()

                    val featured_w: Float =
                        (image_width >= screenWidth.toFloat()) then { screenWidth.toFloat() } ?: image_width
                    var featured_h: Float = image_height
                    var marginStart: Int = 0
                    if (image_width > 0 && image_height > 0) {
                        if (image_width > screenWidth.toFloat()) {
                            val scale: Float =
                                (image_width > image_height) then { screenWidth / image_width }
                                    ?: screenWidth / image_height
                            featured_h = image_height * scale
                        } else {
                            marginStart = ((screenWidth - featured_w) / 2).toInt()
                        }
                    }

                    val params: ViewGroup.MarginLayoutParams =
                        featured.layoutParams as ViewGroup.MarginLayoutParams
                    params.width = featured_w.toInt()
                    params.height = featured_h.toInt()
                    if (marginStart > 0) {
                        params.marginStart = marginStart
                    }
                    featured.layoutParams = params

//                    val dataContainerConstraintTop: Int = ((featured_h - 30) * -1).toInt()
//                    params = data_container.layoutParams as ViewGroup.MarginLayoutParams
//                    params.topMargin = dataContainerConstraintTop
//                    data_container.layoutParams = params
                }

                Picasso.with(context)
                    .load(featured_path)
                    .placeholder(R.drawable.loading_square_120)
                    .error(R.drawable.loading_square_120)
                    .into(featured)
            } else {
                featured.setImageResource(R.drawable.loading_square_120)
            }
        }
    }

    open fun setBottomButtonPadding() {
        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        val likeButtonConstraintLeading: Int = bottom_button_count * padding + (bottom_button_count - 1)*button_width
        findViewById<Button>(R.id.likeButton) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.marginStart = likeButtonConstraintLeading
            it.layoutParams = params
        }
    }

    open fun setMainData(table: Table) {
        for (showRow in showRows) {
            val key: String = showRow.key
            val kc = table::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(table).toString()
                    showRow.show = value
                    //tableRows[key]!!["content"] = value
                }
            }
        }

//        val items = generateMainItem()
//        adapter.update(items)

    }

    open fun setContent() {
        var content: String = ""
        if (table != null) {
            content =
                "<html><HEAD><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">"+body_css+"</HEAD><body>"+table!!.content+"</body></html>"
        }
        //val content: String = "<html lang=\"zh-TW\"><head><meta charset=\"UTF-8\"></head><body style=\"background-color: #000;color:#fff;font-size:28px;\">"+superCourse!!.content+"</body></html>"
        //println(content)
        contentView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
        //contentView.loadData(strHtml, "text/html; charset=utf-8", "UTF-8")
        //contentView.loadData("<html><body style='background-color:#000;'>Hello, world!</body></html>", "text/html", "UTF-8")
    }

    fun setLike() {
        //likeButton.initStatus(isLike, table!.like_count)
        //val _likeButton = view as Button

        setIcon()
        setCount()
    }

    private fun setIcon() {
        var res: Int = R.drawable.like_show
        if (isLike) {
            res = R.drawable.like_show1
        }
        likeButton.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0)
    }

    private fun setCount() {
        if (table!!.like) {
            if (isLike) {
                likeCount = table!!.like_count
            } else {
                likeCount = table!!.like_count - 1
            }
        } else {
            if (isLike) {
                likeCount = table!!.like_count + 1
            } else {
                likeCount = table!!.like_count
            }
        }

        likeButton.text = "${likeCount.toString()}人"
    }

    fun getRowFromKey(key: String): ShowRow {
        for (showRow in showRows) {
            if (showRow.key == key) {
                return showRow
            }
        }
        return ShowRow()
    }

    fun likeButtonPressed(view: View) {

        if (!member.isLoggedIn) {
            toLogin()
        } else {
            if (table != null) {
                isLike = !isLike
                setLike()
                dataService.like(this, table!!.token, table!!.id)
            } else {
                warning("沒有取得內容資料值，請稍後再試或洽管理員")
            }
        }
    }

//    override fun didSelectRowAt(view: View, position: Int) {}

    open fun setData() {}

    fun getMemberOne(member_token: String) {

        Loading.show(mask)
        MemberService.getOne(this, hashMapOf("token" to member_token)) { success ->

            runOnUiThread {
                Loading.hide(mask)
            }
            if (success) {
                var t: SuccessTable? = null
                try {
                    t = Gson().fromJson<SuccessTable>(MemberService.jsonString, SuccessTable::class.java)
                } catch (e: JsonParseException) {
                    runOnUiThread {
                        warning(e.localizedMessage!!)
                    }
                }
                var t_success: Boolean = false
                var t_msg: String = ""
                if (t != null) {
                    t_success = t.success
                    t_msg = t.msg
                }
                if (!t_success) {
                    runOnUiThread {
                        warning(t_msg)
                    }
                } else {
                    val memberTable = jsonToModel<MemberTable>(MemberService.jsonString)
                    runOnUiThread {
                        if (memberTable != null) {
                            memberTable.filterRow()
                            showTempMemberInfo(memberTable)
                        } else {
                            warning(MemberService.msg)
                        }
                    }
                }
            }
        }
    }

    fun showTempMemberInfo(memberTable: MemberTable) {

        val content: String = "姓名：" + memberTable.name + "\n" + "電話：" + memberTable.mobile_show + "\n" + "EMail：" + memberTable.email

        AlertDialog.Builder(this)
            .setTitle(memberTable.nickname)
            .setMessage(content)
            .setPositiveButton("打電話") { _, _ ->
                if (memberTable.mobile != null && memberTable.mobile.isNotEmpty())
                    memberTable.mobile.makeCall(this)
            }
            .setNegativeButton("關閉") { _, _ ->

            }
            .show()
//        println(memberTable.nickname)
//        println(memberTable.mobile)
    }
}


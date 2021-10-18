package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import com.sportpassword.bm.Adapters.IconCell
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.image
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

open class ShowVC: BaseActivity() {

    var token: String? = null    // course token
    var tableRowKeys:MutableList<String> = mutableListOf()
    var tableRows: HashMap<String, HashMap<String,String>> = hashMapOf()

    var table: Table? = null

    var isLike: Boolean = false
    var likeCount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        //refresh()
    }

    open fun initAdapter() {
//        adapter = GroupAdapter()

//        val items = generateMainItem()
//        adapter.addAll(items)
//        tableView.adapter = adapter
    }

    open fun genericTable() {}

    override fun refresh() {
        if (token != null) {
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
            dataService.getOne(this, params) { success ->
                if (success) {
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

                        isLike = table!!.like
                        likeCount = table!!.like_count

                        setLike()

                    }
                }
                closeRefresh()
                Loading.hide(mask)
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
                val featured_path = table!!.featured_path
                featured_path.image(this, featured)
            } else {
                featured.setImageResource(R.drawable.loading_square_120)
            }
        }
    }

    open fun setMainData(table: Table) {
        for (key in tableRowKeys) {
            val kc = table::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(table).toString()
                    tableRows[key]!!["content"] = value
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
}
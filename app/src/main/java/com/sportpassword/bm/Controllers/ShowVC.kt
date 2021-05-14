package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.image
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.member
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
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

    lateinit var adapter: GroupAdapter<ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")
        }

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        refresh()
    }

    open fun initAdapter() {
        adapter = GroupAdapter()
    }

    open fun genericTable() {}

    override fun refresh() {
        if (token != null) {
            Loading.show(mask)
            val params: HashMap<String, String> = hashMapOf("token" to token!!, "member_token" to member.token!!)
            dataService.getOne1(this, params) { success ->
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

                    }

//                    if (superCourse != null) {
//                        superCourse!!.filter()
//                        //superCourse!!.print()
//                        superCoach = superCourse!!.coach
//                        setMainData()
//                        setSignupData()
//                        setCoachData()
//                        setFeatured()
//
//                        if (superCourse!!.isSignup) {
//                            signupButton.setText("取消報名")
//                        } else {
//                            val count = superCourse!!.signup_normal_models.count()
//                            if (count >= superCourse!!.people_limit) {
//                                signupButton.setText("候補")
//                            } else {
//                                signupButton.setText("報名")
//                            }
//                        }
//                    }
                }
                closeRefresh()
                Loading.hide(mask)
            }
        }
    }

    fun setFeatured() {
        if (table != null && table!!.featured_path.isNotEmpty()) {
            var featured_path = table!!.featured_path
            featured_path.image(this, featured)
        } else {
            featured.setImageResource(R.drawable.loading_square_120)
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
    }

    open fun setContent() {
        var content: String = ""
        if (table != null) {
            content =
                "<html lang=\"zh-TW\"><head><meta charset=\"UTF-8\">" + body_css + "</head><body><div class=\"content\">" + table!!.content + "</div>" + "</body></html>"
        }
        //val content: String = "<html lang=\"zh-TW\"><head><meta charset=\"UTF-8\"></head><body style=\"background-color: #000;color:#fff;font-size:28px;\">"+superCourse!!.content+"</body></html>"
        //println(content)
        contentView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
        //contentView.loadData(strHtml, "text/html; charset=utf-8", "UTF-8")
        //contentView.loadData("<html><body style='background-color:#000;'>Hello, world!</body></html>", "text/html", "UTF-8")
    }

    fun setLike() {
        isLike = !isLike
        //likeButton.initStatus(isLike, table!.like_count)
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

    open fun setData() {}
}
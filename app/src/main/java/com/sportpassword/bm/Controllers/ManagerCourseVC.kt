package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.ManagerAdapter
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.BASE_URL
import com.sportpassword.bm.Utilities.GENERAL_REQUEST_CODE
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.manager_course_item.*
import kotlinx.android.synthetic.main.manager_course_vc.*
import kotlinx.android.synthetic.main.manager_vc.*
import kotlinx.android.synthetic.main.mask.*
import com.xwray.groupie.ViewHolder
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class ManagerCourseVC: MyTableVC() {
    var token: String? = null //coach token
    var name: String? = null  //coach name
    var manager_token: String? = null

    var superCourses: SuperCourses? = null

    lateinit var dialog: DialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_course_vc)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")
        }
        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name")
        }
        if (intent.hasExtra("manager_token")) {
            manager_token = intent.getStringExtra("manager_token")
        }

        if (name == null) {
            name = "課程"
        }
        setMyTitle(name!!)

        recyclerView = list
        initAdapter()

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.refresh)
        setRefreshListener()
        refresh()
    }

//    override fun onResume() {
//        super.onResume()
//        refresh()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GENERAL_REQUEST_CODE -> {

                if (resultCode == Activity.RESULT_OK) {
                    refresh()
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add, menu)
        return true
    }

    override fun refresh() {
        getCourseList()
    }

    private fun getCourseList() {
        var filter: HashMap<String, Any>? = null
        if (manager_token != null && manager_token != null) {
            filter = hashMapOf("manager_token" to manager_token!!)
        }

        Loading.show(mask)
        CourseService.getList(this, token, filter, 1, 100) { success ->

            if (success) {
                superCourses = CourseService.superModel as SuperCourses
                val items = generateItems()
                adapter.update(items)
                adapter.notifyDataSetChanged()
                closeRefresh()
            }
            Loading.hide(mask)
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (superCourses != null) {
            for (row in superCourses!!.rows) {
                items.add(ManagerCourseItem(this@ManagerCourseVC, row))
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {
        val managerCourseItem = item as ManagerCourseItem
        val row = managerCourseItem.superCourse
        dialog = alert {
            title = "選項"
            customView {
                verticalLayout {
                    button("檢視") {
                        onClick {
                            dialog.dismiss()
                            val intent = Intent(this@ManagerCourseVC, ShowTimetableVC::class.java)
//                            intent.putExtra("tt_id", event.id)
//                            intent.putExtra("source", source)
//                            intent.putExtra("token", token)
                            startActivity(intent)
                        }
                    }
                    button("編輯") {
                        onClick {
                            dialog.dismiss()
                            //if (token != null) {
                                goEditCourse(row.title, row.token, row.coach.token)
                            //}
                        }
                    }
                    button("刪除") {
                        onClick {
                            dialog.dismiss()
                            goDelete1("course", row.token!!)
                        }
                    }
                    button("取消") {
                        onClick {dialog.dismiss()}
                    }
                }
            }
        }.show()
    }

    fun add(view: View) {
        if (member.validate < 1) {
            Alert.show(this@ManagerCourseVC, "錯誤", "未通過EMail認證，無法新增課程，認證完後，請先登出再登入")
        } else {
            if (token != null) {
                goEditCourse("新增課程", "", token!!)
            }
        }
    }
}

class ManagerCourseItem(val context: Context, val superCourse: SuperCourse): Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        viewHolder.title.text = superCourse.title
        Picasso.with(context)
                .load(BASE_URL + superCourse.featured_path)
                .placeholder(R.drawable.loading_square)
                .error(R.drawable.load_failed_square)
                .into(viewHolder.featured)
    }

    override fun getLayout() = R.layout.manager_course_item

}
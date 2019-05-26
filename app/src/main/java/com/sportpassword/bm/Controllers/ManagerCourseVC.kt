package com.sportpassword.bm.Controllers

import android.content.Context
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
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.manager_course_item.*
import kotlinx.android.synthetic.main.manager_course_vc.*
import kotlinx.android.synthetic.main.manager_vc.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.contentView

class ManagerCourseVC: MyTableVC() {
    var token: String? = null
    var name: String? = null

    var superCourses: SuperCourses? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_course_vc)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")
        }
        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add, menu)
        return true
    }

    override fun refresh() {
        getCourseList()
    }

    private fun getCourseList() {
        val filter: Array<Array<Any>> = arrayOf()

        Loading.show(mask)
        CourseService.getList(this, token, filter, 1, 100) { success ->

            if (success) {
                superCourses = CourseService.superCourses
                val items = generateItems()
                adapter.update(items)
                adapter.notifyDataSetChanged()
                closeRefresh()
            }
            Loading.hide(mask)
        }
    }

    override fun generateItems(): ArrayList<Item> {
        var items: ArrayList<Item> = arrayListOf()
        if (superCourses != null) {
            for (row in superCourses!!.rows) {
                items.add(ManagerCourseItem(this@ManagerCourseVC, row))
            }
        }

        return items
    }

    fun add(view: View) {
        if (member.validate < 1) {
            Alert.show(this@ManagerCourseVC, "錯誤", "未通過EMail認證，無法新增課程，認證完後，請先登出再登入")
        } else {
            //goEdit(source)
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
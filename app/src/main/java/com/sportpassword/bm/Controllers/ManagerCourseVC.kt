package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Adapters.CourseAdapter
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.CoursesTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.manager_course_item.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.*

class ManagerCourseVC: ManagerVC() {

    //var superCourses: SuperCourses? = null
    var mysTable: CoursesTable? = null
    lateinit var tableAdapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        this.dataService = CourseService
        resource = R.layout.activity_manager_course_vc

        super.onCreate(savedInstanceState)

        tableAdapter = CourseAdapter(R.layout.manager_team_item, this)
        recyclerView.adapter = tableAdapter
        //initAdapter()
//        init()

//        refresh()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            GENERAL_REQUEST_CODE -> {
//
//                if (resultCode == Activity.RESULT_OK) {
//                    refresh()
//                }
//
//            }
//        }
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.add, menu)
//
//        //android:onClick="showSearchPanel" define in layout/menu_add.xml
//        return true
//    }

    override fun genericTable() {
        mysTable = jsonToModels<CoursesTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            if (page == 1) {
                getPage()
            }
            tableLists += generateItems1(CourseTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun cellEdit(row: Table) {

        val _row: CourseTable = row as CourseTable
        toEditCourse(row.title, row.token, row.coach!!.token, this)
    }

    override fun cellDelete(row: Table) {

    }

    override fun cellSignup(row: Table) {

        toManagerSignup(able_type, row.token, row.title)
    }

    override fun cellClick(row: Table) {

        toShowCourse(row.token)
//        val _row: CourseTable = row as CourseTable
//
//        dialog = alert {
//            title = "選項"
//            customView {
//                verticalLayout {
//                    button("檢視") {
//                        onClick {
//                            dialog.dismiss()
//                            toShowCourse(row.token)
//                        }
//                    }
//                    button("編輯") {
//                        onClick {
//                            dialog.dismiss()
//                            //if (token != null) {
//                            toEditCourse(row.title, row.token, row.coach!!.token)
//                            //}
//                        }
//                    }
//                    button("刪除") {
//                        onClick {
//                            dialog.dismiss()
//                            //toDelete1("course", row.token)
//                        }
//                    }
//                    button("取消") {
//                        onClick {dialog.dismiss()}
//                    }
//                }
//            }
//        }.show()
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                //row.print()
//                row.filterRow()
//                val myItem = ManagerCourseItem(this, row)
//                //myItem.list1CellDelegate = this
//                items.add(myItem)
//            }
//        }
//
//        return items
//    }

//    override fun refresh() {
//        getCourseList()
//    }
//
//    private fun getCourseList() {
//        var filter: HashMap<String, Any>? = null
//        if (manager_token != null && manager_token != null) {
//            filter = hashMapOf("manager_token" to manager_token!!)
//        }
//
//        Loading.show(mask)
//        CourseService.getList1(this, token, filter, 1, 100) { success ->
//
//            if (success) {
//                superCourses = CourseService.superModel as SuperCourses
//                val items = generateItems()
//                adapter.update(items)
//                adapter.notifyDataSetChanged()
//                closeRefresh()
//            }
//            Loading.hide(mask)
//        }
//    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        if (superCourses != null) {
//            for (row in superCourses!!.rows) {
//                //items.add(ManagerCourseItem(this@ManagerCourseVC1, row))
//            }
//        }
//
//        return items
//    }

//    override fun rowClick(item: com.xwray.groupie.Item<GroupieViewHolder>, view: View) {
//        val managerCourseItem = item as ManagerCourseItem
//        val row = managerCourseItem.courseTable
//        dialog = alert {
//            title = "選項"
//            customView {
//                verticalLayout {
//                    button("檢視") {
//                        onClick {
//                            dialog.dismiss()
//                            toShowCourse(row.token)
//                        }
//                    }
//                    button("編輯") {
//                        onClick {
//                            dialog.dismiss()
//                            //if (token != null) {
//                                toEditCourse(row.title, row.token, row.coach!!.token)
//                            //}
//                        }
//                    }
//                    button("刪除") {
//                        onClick {
//                            dialog.dismiss()
//                            //toDelete1("course", row.token)
//                        }
//                    }
//                    button("取消") {
//                        onClick {dialog.dismiss()}
//                    }
//                }
//            }
//        }.show()
//    }

    fun add(view: View) {
        if (member.validate < 1) {
            Alert.show(this@ManagerCourseVC, "錯誤", "未通過EMail認證，無法新增課程，認證完後，請先登出再登入")
        } else {
//            if (manager_token != null) {
//                toEditCourse("新增課程", "", manager_token!!)
//            }
        }
    }
}

//class ManagerCourseItem(val context: Context, val courseTable: CourseTable): Item() {
//    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder, position: Int) {
//        viewHolder.title.text = courseTable.title
//        Picasso.with(context)
//                .load(courseTable.featured_path)
//                .placeholder(R.drawable.loading_square)
//                .error(R.drawable.load_failed_square)
//                .into(viewHolder.featured)
//    }
//
//    override fun getLayout() = R.layout.manager_course_item
//
//}
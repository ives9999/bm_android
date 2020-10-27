package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Fragments.CourseFragment
import com.sportpassword.bm.Models.SuperStore
import com.sportpassword.bm.Models.SuperStores
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.course_calendar_item.*
import kotlinx.android.synthetic.main.tab_list_item.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StoreVC : MyTableVC1() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
            hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY)
    )

    var superStores: SuperStores? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        val title_field = intent.getStringExtra("titleField")
        setMyTitle("體育用品店")

        dataService = CourseService
        recyclerView = store_list
        refreshLayout = store_refresh
        setRefreshListener()

        initAdapter()
        refresh()
    }

    override fun initAdapter(include_section: Boolean) {
        adapter = GroupAdapter()
        val items = generateItems()
        adapter.addAll(items)
        recyclerView.adapter = adapter
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        //Loading.show(maskView)
        loading = true

        dataService.getList(this, null, params, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {
                superCourses = dataService.superModel as SuperCourses
                if (superCourses != null) {
                    page = superCourses!!.page
                    perPage = superCourses!!.perPage
                    totalCount = superCourses!!.totalCount
                    var _totalPage: Int = totalCount / perPage
                    totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                    theFirstTime = false
                    val items = generateItems()
                    println(items);
                    adapter.update(items)
                    adapter.notifyDataSetChanged()
                }
            }

            //notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        //Loading.hide(maskView)
        loading = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (superCourses != null) {
            for (row in superCourses!!.rows) {
                items.add(StoreItem(this, row))
            }
        }

        return items
    }
}

class StoreItem(val context: Context, val superCourse: SuperCourse): Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val citys = superCourse.coach.citys
        if (citys.size > 0) {
            viewHolder.listCityBtn.text = citys[0].name
        }
        viewHolder.title.text = superCourse.title
        Picasso.with(context)
                .load(BASE_URL + superCourse.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)
        viewHolder.listArenaTxt.text = superCourse.price_text_short
        viewHolder.listDayTxt.text = superCourse.weekday_text
        viewHolder.listIntervalTxt.text = superCourse.start_time_text+"~"+superCourse.end_time_text
        viewHolder.marker.visibility = View.INVISIBLE
    }

    override fun getLayout() = R.layout.tab_list_item
}
package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import kotlinx.android.synthetic.main.activity_course_vc.*
import org.jetbrains.anko.contentView

class CourseVC : MoreVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_vc)

        setMyTitle("教學")
        recyclerView = course_list
        dataService = CourseService
        refreshLayout = course_refresh
        initAdapter()

        refresh()
    }
}

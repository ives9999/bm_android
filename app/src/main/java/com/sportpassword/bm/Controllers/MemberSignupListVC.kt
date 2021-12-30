package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService

class MemberSignupListVC : MyTableVC() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_signup_list_vc)

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        if (able_type == "team") {
            setMyTitle("球隊報名列表")
            dataService = TeamService
        } else if (able_type == "course") {
            setMyTitle("課程報名列表")
            dataService = CourseService
        }

        init()

        //recyclerView = list_container
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }
}
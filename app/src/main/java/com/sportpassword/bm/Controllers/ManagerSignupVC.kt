package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService

class ManagerSignupVC : MyTableVC() {

    var able_token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_signup_vc)

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        if (intent.hasExtra("able_token")) {
            able_token = intent.getStringExtra("able_token")!!
        }

        if (able_type == "team") {
            setMyTitle("管理球隊報名")
            dataService = TeamService
        } else if (able_type == "course") {
            setMyTitle("管理課程報名")
            dataService = CourseService
        }

        init()
    }

    override fun init() {

        isPrevIconShow = true
        super.init()
    }
}
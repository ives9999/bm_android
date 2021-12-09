package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.MemberTable
import com.sportpassword.bm.R

class SelectManagerVC : SelectVC() {

    var selected: Int = 0
    var manager_id: Int = 0
    var manager_token: String = ""

    var managerTable: MemberTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_manager_vc)

        if (intent.hasExtra("manager_id")) {
            manager_id = intent.getIntExtra("manager_id", 0)
        }

        if (intent.hasExtra("manager_token")) {
            manager_token = intent.getStringExtra("manager_token")!!
        }

        setMyTitle("管理者")

        init()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }
}
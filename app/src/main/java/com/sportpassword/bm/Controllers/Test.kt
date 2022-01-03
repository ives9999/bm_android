package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.R

class Test : MyTableVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_team_vc)

        init()
    }

    override fun init() {
        isPrevIconShow = true
        isAddIconShow = true
        super.init()
    }
}
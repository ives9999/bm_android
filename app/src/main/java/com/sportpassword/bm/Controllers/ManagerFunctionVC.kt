package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R

class ManagerFunctionVC : BaseActivity() {

    var token: String = ""
    var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_function_vc)

        title = intent.getStringExtra("title")
        token = intent.getStringExtra("token")
        setMyTitle(title)
    }

    fun edit(view: View) {
        goEdit(token)
    }

    fun delete(view: View) {
        goDeleteTeam(token)
    }
    fun tempPlay(view: View) {
        goTeamTempPlayEdit(token)
    }
    fun tempPlayDate(view: View) {
        goTempPlayDate(title, token)
    }
}

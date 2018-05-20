package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R

class TeamManagerFunctionActivity : BaseActivity() {

    var token: String = ""
    var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_manager_function)

        title = intent.getStringExtra("title")
        token = intent.getStringExtra("token")
        setMyTitle(title)
    }

    fun edit(view: View) {
        goEditTeam(token)
    }

    fun delete(view: View) {
        goDeleteTeam(token)
    }
    fun tempPlay(view: View) {
        goTeamTempPlayEdit(token)
    }
}

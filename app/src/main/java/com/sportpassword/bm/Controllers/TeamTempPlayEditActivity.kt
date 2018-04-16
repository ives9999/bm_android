package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.activity_team_temp_play_edit.*

class TeamTempPlayEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_temp_play_edit)

        onoff.isChecked = true
    }

    fun submit(view: View) {

    }
}

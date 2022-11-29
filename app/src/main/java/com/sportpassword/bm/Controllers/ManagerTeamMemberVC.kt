package com.sportpassword.bm.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sportpassword.bm.Adapters.MoreAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Views.Top
import kotlinx.android.synthetic.main.activity_manager_signup_vc.*

class ManagerTeamMemberVC : BaseActivity() {

    var token: String? = null
    var top: Top? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_team_member)

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }

        setMyTitle("球隊隊員")
        findViewById<Top>(R.id.top) ?. let {
            top = it
            //it.showPrev(true)
        }

//        recyclerView = list_container
//        tableAdapter = MoreAdapter(this)
//        setRecyclerViewScrollListener()
//
//        managerRows = initRows()
//        tableAdapter.moreRow = managerRows
//        recyclerView.adapter = tableAdapter

        init()
    }
}
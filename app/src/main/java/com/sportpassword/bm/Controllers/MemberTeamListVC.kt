package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.R
import com.sportpassword.bm.Views.Top

class MemberTeamListVC : BaseActivity(), MyTable2IF {

    var top: Top? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_team_list_vc)

        findViewById<Top>(R.id.top) ?. let {
            top = it
            it.showPrev(true)
            it.setTitle("球隊隊員")
        }
    }
}
package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.core.content.pm.PackageInfoCompat
import com.sportpassword.bm.Adapters.MoreAdapter
import com.sportpassword.bm.Data.MoreRow
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.TO_BLACKLIST
import com.sportpassword.bm.Utilities.TO_MANAGER_SIGNUPLIST
import kotlinx.android.synthetic.main.activity_manager_signup_vc.*

class ManagerSignupVC : MyTableVC() {

    var able_token: String = ""
    lateinit var tableAdapter: MoreAdapter
    var managerRows: ArrayList<MoreRow> = arrayListOf()

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

        recyclerView = list_container
        tableAdapter = MoreAdapter(this)
        setRecyclerViewScrollListener()

        managerRows = initRows()
        tableAdapter.moreRow = managerRows
        recyclerView.adapter = tableAdapter

        init()
    }

    override fun init() {

        isPrevIconShow = true
        super.init()
    }

    fun initRows(): ArrayList<MoreRow> {

        var rows: ArrayList<MoreRow> = arrayListOf()

        val r1: MoreRow = MoreRow("臨打報名列表", TO_MANAGER_SIGNUPLIST, "signup", R.color.MY_LIGHT_RED)
        rows.add(r1)

        val r2: MoreRow = MoreRow("黑名單", TO_BLACKLIST, "blacklist", R.color.MY_LIGHT_WHITE)
        rows.add(r2)

        return rows
    }

    override fun cellClick(idx: Int) {
        val row: MoreRow = managerRows[idx]
        val key: String = row.key
        when (key) {
            TO_MANAGER_SIGNUPLIST-> this.toManagerSignupList(able_type, able_token)
            //TO_BLACKLIST-> this.toCoach()
        }
    }
}
package com.sportpassword.bm.Controllers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.pm.PackageInfoCompat
import com.sportpassword.bm.Adapters.MoreAdapter
import com.sportpassword.bm.Data.MoreRow
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.TO_BLACKLIST
import com.sportpassword.bm.Utilities.TO_MANAGER_SIGNUPLIST
import com.sportpassword.bm.databinding.ActivityManagerSignupVcBinding
import com.sportpassword.bm.databinding.MytablevcBinding

class ManagerSignupVC : MyTableVC() {

    private lateinit var binding: ActivityManagerSignupVcBinding
    //private lateinit var view: ViewGroup

    var able_token: String = ""
    var able_title: String = ""

    lateinit var tableAdapter: MoreAdapter
    var managerRows: ArrayList<MoreRow> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityManagerSignupVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
        }

        if (intent.hasExtra("able_token")) {
            able_token = intent.getStringExtra("able_token")!!
        }

        if (intent.hasExtra("able_title")) {
            able_title = intent.getStringExtra("able_title")!!
        }

        setMyTitle(able_title + "報名管理")

        if (able_type == "team") {
            dataService = TeamService
        } else if (able_type == "course") {
            dataService = CourseService
        }

        recyclerView = binding.listContainer
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
            TO_MANAGER_SIGNUPLIST-> this.toManagerSignupList(able_type, able_token, able_title)
            //TO_BLACKLIST-> this.toCoach()
        }
    }
}
package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sportpassword.bm.Adapters.ManagerAdapter
import kotlinx.android.synthetic.main.activity_manager_course_vc.*
import kotlinx.android.synthetic.main.mask.*

var token: String? = null //coach token
var name: String? = null  //coach name
var manager_token: String? = null

open class ManagerVC : MyTableVC() {

//    lateinit var managerAdapter: ManagerAdapter
//    var source: String = "team"

    lateinit var dialog: DialogInterface

    override var editCourseResult: ActivityResultLauncher<Intent>? = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { res ->

        if (res.resultCode == Activity.RESULT_OK) {

            if (res.data != null) {
                val i: Intent? = res.data

                if (i != null) {
                    if (i.hasExtra("manager_token")) {
                        manager_token = i.getStringExtra("manager_token")!!
                        params["manager_token"] = manager_token!!
                        refresh()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (intent.hasExtra("manager_token")) {
            manager_token = intent.getStringExtra("manager_token")!!
            params["manager_token"] = manager_token!!
        }

        recyclerView = list
        refreshLayout = refresh
        maskView = mask
        setRefreshListener()

        setMyTitle(name!!)
    }


    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    override fun refresh() {
        page = 1
        theFirstTime = true
//        adapter.clear()
//        items.clear()

        params.clear()
        params.put("manager_token", manager_token!!)
        params.put("status", "all")
        tableLists.clear()
        getDataStart(page, perPage)
    }

}

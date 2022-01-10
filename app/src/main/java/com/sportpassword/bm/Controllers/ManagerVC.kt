package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sportpassword.bm.Adapters.ManagerAdapter
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.activity_manager_course_vc.*
import kotlinx.android.synthetic.main.mask.*


open class ManagerVC : MyTableVC() {

//    lateinit var managerAdapter: ManagerAdapter
//    var source: String = "team"
    var manager_token: String? = null
    var resource: Int = 0

//    lateinit var dialog: DialogInterface

//    override var editCourseResult: ActivityResultLauncher<Intent>? = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()) { res ->
//
//        if (res.resultCode == Activity.RESULT_OK) {
//
//            if (res.data != null) {
//                val i: Intent? = res.data
//
//                if (i != null) {
//                    if (i.hasExtra("manager_token")) {
//                        manager_token = i.getStringExtra("manager_token")!!
//                        params["manager_token"] = manager_token!!
//                        refresh()
//                    }
//                }
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(resource)

        if (intent.hasExtra("manager_token")) {
            manager_token = intent.getStringExtra("manager_token")!!
//            params["manager_token"] = manager_token!!
        }

        if (intent.hasExtra("able_type")) {
            able_type = intent.getStringExtra("able_type")!!
//            params["able_type"] = able_type
        }

        recyclerView = list
        refreshLayout = refresh
        maskView = mask
        setRefreshListener()

        if (able_type == "team") {
            setMyTitle("球隊管理列表")
        } else if (able_type == "course") {
            setMyTitle("課程管理列表")
        }

        init()
        //必須指定status，預設是只會出現上線的
        refresh()
    }


    override fun init() {
        isPrevIconShow = true
        isAddIconShow = true
        super.init()
    }

    override fun refresh() {
        page = 1
//        theFirstTime = true
//        adapter.clear()
//        items.clear()

        params.clear()
        params.put("manager_token", manager_token!!)
        params.put("able_type", able_type)
        params.put("status", "online,offline")
        tableLists.clear()
        getDataStart(page, perPage)
    }
}

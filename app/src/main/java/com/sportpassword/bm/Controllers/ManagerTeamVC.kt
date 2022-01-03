package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Adapters.TeamAdapter
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.jsonToModels

class ManagerTeamVC : ManagerVC() {

    var mysTable: TeamsTable? = null
    lateinit var tableAdapter: TeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

//        setContentView(R.layout.activity_manager_team_vc)
        this.dataService = TeamService
        resource = R.layout.activity_manager_team_vc

        super.onCreate(savedInstanceState)

        tableAdapter = TeamAdapter(R.layout.manager_team_item, this)
        recyclerView.adapter = tableAdapter
        //initAdapter()
//        init()

//        refresh()
    }

    override fun genericTable() {
        mysTable = jsonToModels<TeamsTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            if (page == 1) {
                getPage()
            }
            tableLists += generateItems1(TeamTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun cellEdit(row: Table) {

        toEditTeam(row.token, this)
    }

    override fun cellDelete(row: Table) {

    }

    override fun cellSignup(row: Table) {

        toManagerSignup(able_type, row.token, row.name)
    }

    override fun cellClick(row: Table) {

        toShowTeam(row.token)

//        val _row: TeamTable = row as TeamTable
//
//        dialog = alert {
//            title = "選項"
//            customView {
//                verticalLayout {
//                    button("檢視") {
//                        onClick {
//                            dialog.dismiss()
//                            toShowTeam(row.token)
//                        }
//                    }
//                    button("編輯") {
//                        onClick {
//                            dialog.dismiss()
//                            //if (token != null) {
//                            toEditCourse(row.title, row.token, row.token)
//                            //}
//                        }
//                    }
//                    button("刪除") {
//                        onClick {
//                            dialog.dismiss()
//                            //toDelete1("course", row.token)
//                        }
//                    }
//                    button("取消") {
//                        onClick {dialog.dismiss()}
//                    }
//                }
//            }
//        }.show()
    }

    override fun addPressed(view: View) {

        toEditTeam()
    }
}
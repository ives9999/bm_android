package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import com.sportpassword.bm.Adapters.ManagerTeamListAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Alert
import com.sportpassword.bm.Utilities.CHANNEL
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_team_manager.*
import kotlinx.android.synthetic.main.menu_team_list.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.sdk25.coroutines.onClick

class TeamManagerActivity : BaseActivity() {

    lateinit var managerTeamListAdapter: ManagerTeamListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_manager)
        setMyTitle("管理球隊")

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempManager_refresh)
        setRefreshListener()
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_team, menu)
        return true
    }

    override fun refresh() {
        initTeamList()
        super.refresh()
    }

    private fun initTeamList() {
        val filter1: Array<Any> = arrayOf("channel", "=", CHANNEL)
        val filter2: Array<Any> = arrayOf("manager_id", "=", member.id)
        val filter: Array<Array<Any>> = arrayOf(filter1, filter2)

        TeamService.getList(this, "team", "name", 1, 100, filter) { success ->
            if (success) {
                this.managerTeamListAdapter = ManagerTeamListAdapter(this, TeamService.dataLists,
                        { title, token ->
                            goTeamManagerFunction(title, token)
                        }
                )
                manager_team_list.adapter = this.managerTeamListAdapter

                val layoutManager = LinearLayoutManager(this)
                manager_team_list.layoutManager = layoutManager
                closeRefresh()
            }
        }
//        menu_team_add.onClick {
//        }
    }

    fun add_team(view: View) {
        if (member.validate < 1) {
            Alert.show(this@TeamManagerActivity, "錯誤", "未通過EMail認證，無法新增球隊，認證完後，請先登出再登入")
        } else {
            goEditTeam()
        }
    }
}

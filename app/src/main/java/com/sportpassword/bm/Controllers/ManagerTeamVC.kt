package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Adapters.TeamAdapter
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.jsonToModels
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.Views.*
import com.sportpassword.bm.databinding.ActivityManagerTeamVcBinding
import com.sportpassword.bm.databinding.ActivityShowTeamVcBinding
import com.sportpassword.bm.member
import java.lang.reflect.Type

class ManagerTeamVC : BaseActivity(), MyTable2IF, ShowTop2Delegate {

    private lateinit var binding: ActivityManagerTeamVcBinding

    var manager_token: String? = null
    var mysTable: TeamsTable? = null
    protected lateinit var recyclerView: RecyclerView
    lateinit var tableAdapter: TeamAdapter

    var showTop2: ShowTop2? = null

    private val tableType: Type = object : TypeToken<Tables2<TeamTable>>() {}.type
    lateinit var tableView: MyTable2VC<ManagerTeamViewHolder, TeamTable, ManagerTeamVC>

    override fun onCreate(savedInstanceState: Bundle?) {

//        setContentView(R.layout.activity_manager_team_vc)
        this.dataService = TeamService
        //resource = R.layout.activity_manager_team_vc

        super.onCreate(savedInstanceState)

        binding = ActivityManagerTeamVcBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.hasExtra("manager_token")) {
            manager_token = intent.getStringExtra("manager_token")!!
//            params["manager_token"] = manager_token!!
        }

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.delegate = this
            it.setTitle("我的球隊")
            it.showPrev(true)
            it.showRefresh()
            it.showAdd(72)
        }

        init()
        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()

        findViewById<SwipeRefreshLayout>(R.id.refreshSR) ?. let {
            refreshLayout = it
        }
        val recyclerView: RecyclerView = findViewById(R.id.list)
        tableView = MyTable2VC(recyclerView, refreshLayout, R.layout.manager_team_item, ::ManagerTeamViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)
    }

    override fun refresh() {
        page = 1
        params.clear()
        params.put("manager_token", manager_token!!)
        params.put("able_type", able_type)
        params.put("status", "online,offline")
        tableLists.clear()
        tableView.getDataFromServer(page)
    }

    fun didSelect(row: MemberCoinTable, idx: Int) {}

    fun tableViewSetSelected(row: TeamTable): Boolean { return false }

    private fun getDataFromServer(page: Int) {
        loadingAnimation.start()
        loading = true

        dataService.getList(this, member.token!!, params, page, tableView.perPage) { success ->
            runOnUiThread {
                loadingAnimation.stop()

                //MyTable2IF
                val b: Boolean = showTableView(tableView, dataService.jsonString)
                if (b) {
                    this.totalPage = tableView.totalPage
                    refreshLayout?.isRefreshing = false
                } else {
                    val rootView: ViewGroup = getRootView()
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

    fun cellEdit(row: TeamTable) {

        toEditTeam(row.token, this)
    }

    fun cellDelete(row: TeamTable) {

        msg = "是否確定要刪除此球隊？"
        warning(msg, true, "刪除") {
            loadingAnimation.start()
            dataService.delete(this, able_type, row.token, "trash") { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                }

                if (success) {
                    try {
                        val successTable: SuccessTable = Gson().fromJson(dataService.jsonString, SuccessTable::class.java)
                        if (!successTable.success) {
                            runOnUiThread {
                                warning(successTable.msg)
                            }
                        } else {
                            runOnUiThread {
                                refresh()
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        runOnUiThread {
                            warning("解析JSON字串時，得到空值，請洽管理員")
                        }
                    }
                } else {
                    runOnUiThread {
                        warning("刪除失敗，請洽管理員")
                    }
                }
            }
        }
    }

    fun cellSignup(row: TeamTable) {

        toManagerSignup(able_type, row.token, row.name)
    }

    fun cellTeamMember(row: TeamTable) {
        //println("aaa")
        toManagerTeamMember(row.token)
    }

    override fun cellClick(idx: Int) {

        val row: TeamTable = tableView.rows[idx]
        toShowTeam(row.token)
    }

//    override fun addPressed(view: View) {
//
//        toEditTeam(null, this)
//    }

    override fun showTop2Refresh() {
        this.refresh()
    }

    override fun showTop2Add() {
        toEditTeam(null, this)
    }
}

class ManagerTeamViewHolder(
    context: Context,
    view: View,
    delegate: ManagerTeamVC
): MyViewHolder2<TeamTable, ManagerTeamVC>(context, view, delegate), ShowButton2Delegate, IconView2Delegate {

    var item: TeamTable? = null
    override fun bind(row: TeamTable, idx: Int) {

        this.item = row
        setTV(R.id.noTV, "${idx+1}.")

        setTV(R.id.playWeekTV, row.weekdays_show)
        setTV(R.id.playTimeTV, row.interval_show)

        setFeatured(R.id.featuredIV, row.featured_path)
        setTV(R.id.nameTV, row.name)
        setTV(R.id.nextDateTV, row.nextDate)
        setTV(R.id.numberTV, "隊員：${row.number.toString()}")
        setTV(R.id.leaveTV, "請假：${row.leaveCount.toString()}")

        view.findViewById<ShowButton2>(R.id.showButton2) ?. let {
            it.delegate = this
            it.idx = idx
        }

        view.findViewById<IconView2>(R.id.editIcon) ?. let {
            it.delegate = this
        }
        view.findViewById<IconView2>(R.id.deleteIcon) ?. let {
            it.delegate = this
        }
        view.findViewById<IconView2>(R.id.signupIcon) ?. let {
            it.delegate = this
        }
        view.findViewById<IconView2>(R.id.teamMemberIcon) ?. let {
            it.delegate = this
        }
    }

    override fun iconPressed(icon: String) {
        if (item != null) {
            when (icon) {
                "ic_edit_svg" -> delegate.cellEdit(item!!)
                "ic_delete_svg" -> delegate.cellDelete(item!!)
                "ic_check_svg" -> delegate.cellSignup(item!!)
                "ic_member_svg" -> delegate.cellTeamMember(item!!)
                else -> delegate.cellEdit(item!!)
            }
        }
    }

    override fun pressed(idx: Int) {
        delegate.cellClick(idx)
    }
}
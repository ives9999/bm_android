package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Interface.MyTable2IF
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.Models.TeamMemberTable
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.Utilities.then
import com.sportpassword.bm.Views.Top
import com.sportpassword.bm.databinding.ActivityMemberTeamListVcBinding
import com.sportpassword.bm.member
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import java.lang.reflect.Type

class MemberTeamListVC : BaseActivity(), MyTable2IF {

    private lateinit var binding: ActivityMemberTeamListVcBinding
    private lateinit var view: ViewGroup

    var top: Top? = null
    private var infoTV: TextView? = null

    private val tableType: Type = object : TypeToken<Tables2<TeamMemberTable>>() {}.type
    lateinit var tableView: MyTable2VC<MemberTeamViewHolder, TeamMemberTable, MemberTeamListVC>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemberTeamListVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        findViewById<Top>(R.id.top) ?. let {
            top = it
            it.showPrev(true)
            it.setTitle("加入球隊")
        }

        init()
        refresh()
    }

    override fun init() {
        //super.init()

        findViewById<SwipeRefreshLayout>(R.id.refreshSR) ?. let {
            refreshLayout = it
        }

        var recyclerView: RecyclerView? = null
        findViewById<RecyclerView>(R.id.list) ?. let {
            recyclerView = it
            tableView = MyTable2VC(recyclerView!!, refreshLayout, R.layout.member_team_list_cell, ::MemberTeamViewHolder, tableType, this::tableViewSetSelected, this::getDataFromServer, this)
        }
    }

    override fun refresh() {
        page = 1
        tableView.getDataFromServer(page)
    }

//    val getDataFromServer: (Int)-> Unit = { page ->
//        Loading.show(mask)
//        loading = true
//
//        MemberService.teamlist(this, member.token!!, page, tableView.perPage) { success ->
//            runOnUiThread {
//                Loading.hide(mask)
//
//                //MyTable2IF
////                println(MemberService.jsonString)
//                val b: Boolean = showTableView(tableView, MemberService.jsonString)
//                if (b) {
//                    this.totalPage = tableView.totalPage
//                    refreshLayout?.isRefreshing = false
//                } else {
//                    val rootView: ViewGroup = getRootView()
//                    rootView.setInfo(this, "目前暫無資料")
//                }
//            }
//            //showTableView(myTable, MemberService.jsonString)
//        }
//    }

    private fun getDataFromServer(page: Int) {
        loadingAnimation.start()
        loading = true

        MemberService.teamlist(this, member.token!!, page, tableView.perPage) { success ->
            runOnUiThread {
                loadingAnimation.stop()

                //MyTable2IF
//                println(MemberService.jsonString)
                val b: Boolean = showTableView(tableView, MemberService.jsonString)
                if (b) {
                    this.totalPage = tableView.totalPage
                    infoTV?.visibility = View.GONE
                } else {
                    val rootView: ViewGroup = getRootView()
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
            //showTableView(myTable, MemberService.jsonString)
        }
    }

    override fun cellClick(row: Table) {
        val _row: TeamMemberTable = row as TeamMemberTable
        _row.teamTable ?. let {
            toShowTeam(it.token)
        }
    }

    override fun cellDelete(row: Table) {
//        println(row.id)
        warning("確定要退出嗎？", "取消", "退出") {

            loadingAnimation.start()
            loading = true
            MemberService.deleteMemberTeam(this, row.token) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                    refresh()
                    //val b: Boolean = showTableView(tableView, MemberService.jsonString)
//                        if (b) {
//                            tableView.notifyDataSetChanged()
//                        } else {
//                            val rootView: ViewGroup = getRootView()
//                            rootView.setInfo(this, "目前暫無資料")
//                        }
                }
            }
        }
    }

    private fun tableViewSetSelected(row: TeamMemberTable): Boolean { return false }
}

class MemberTeamViewHolder(
    context: Context,
    view: View,
    delegate: MemberTeamListVC
): MyViewHolder2<TeamMemberTable, MemberTeamListVC>(context, view, delegate) {

    override fun bind(row: TeamMemberTable, idx: Int) {

        super.bind(row, idx)

        row.filterRow()
        val no: String = (idx + 1).toString() + "."

        view.findViewById<TextView>(R.id.noTV) ?. let {
            it.text = no
        }

        if (row.teamTable != null) {
            val teamTable: TeamTable = row.teamTable!!

            view.findViewById<ImageView>(R.id.featuredIV) ?. let {
                Picasso.with(context)
                    .load(teamTable.featured_path)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(it)
            }
            view.findViewById<TextView>(R.id.nameTV) ?.let {
                it.text = teamTable.name
            }

            view.findViewById<Button>(R.id.cityBtn) ?. let {
                it.text = teamTable.city_show
            }

            view.findViewById<Button>(R.id.arenaBtn) ?. let {
                if (teamTable.arena != null) {
                    var name: String = teamTable.arena!!.name
//                    val n = name.length
                    name = (((name.length) > 7) then { name.substring(0, 6) + "..." }) ?: name
                    it.text = name
                }
            }

            view.findViewById<TextView>(R.id.weekdayTV) ?. let {
                it.text = teamTable.weekdays_show
            }

            view.findViewById<TextView>(R.id.intervalTV) ?. let {
                it.text = teamTable.interval_show
            }

            view.findViewById<TextView>(R.id.joinTimeTV) ?.let {
                it.text = row.created_at.noSec()
            }

            view.findViewById<ImageView>(R.id.deleteIV) ?. let {
                it.setOnClickListener { icon ->
                    delegate.cellDelete(row)
                }
            }
        }
    }
}
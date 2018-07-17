package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.BlakListAdapter
import com.sportpassword.bm.Models.BlackList
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_black_list_vc.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.makeCall

class BlackListVC : BaseActivity() {

    lateinit var blackList: BlackList
    lateinit var blacklistAdapter: BlakListAdapter
    var memberToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_list_vc)
        setMyTitle("黑名單")
        memberToken = member.token

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.blacklist_refresh)
        setRefreshListener()
        refresh()
    }

    override fun refresh() {
        super.refresh()
        val loading = Loading.show(this)
        MemberService.blacklist(this, memberToken) { success ->
            loading.dismiss()
            if (success) {
                blackList = MemberService.blackList
                //println(rows)
                blacklistAdapter = BlakListAdapter(this, blackList.rows,
                        { position -> },
                        { mobile ->  call(mobile)},
                        { position -> cancel(position)})
                black_list.adapter = blacklistAdapter
                val layoutManager = LinearLayoutManager(this)
                black_list.layoutManager = layoutManager
                closeRefresh()
            } else {
                warning(MemberService.msg)
            }
        }
    }

    private fun cancel(position: Int) {
        warning("是否真的要將此球友移出黑名單？", "取消", "確定", {_cancel(position)})
    }
    private fun _cancel(position: Int) {
        val row = blackList.rows[position]
        val memberToken = row.token
        val teamToken = row.team.get("token") as String

        val loading = Loading.show(this)
        TeamService.removeBlackList(this, teamToken, memberToken, member.token) { success ->
            loading.dismiss()
            if (success) {
                info("移除黑名單成功")
                refresh()
            } else {
                warning(TeamService.msg)
            }
        }
    }
    private fun call(mobile: String) {
        myMakeCall(mobile)
    }
}

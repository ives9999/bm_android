package com.sportpassword.bm.Controllers

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.BlakListAdapter
import com.sportpassword.bm.Models.BlackLists
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.databinding.ActivityBlackListVcBinding
import com.sportpassword.bm.member
import org.jetbrains.anko.contentView
import org.jetbrains.anko.runOnUiThread

class BlackListVC : BaseActivity() {

    private lateinit var binding: ActivityBlackListVcBinding
    private lateinit var view: ViewGroup

    lateinit var blackLists: BlackLists
    lateinit var blacklistAdapter: BlakListAdapter
    var memberToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBlackListVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("黑名單")
        memberToken = member.token.toString()

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.blacklist_refresh)
        setRefreshListener()
        refresh()
    }

    override fun refresh() {
        super.refresh()
        loadingAnimation.start()
        MemberService.blacklist(this, memberToken) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }
            if (success) {
                blackLists = MemberService.blackLists
                if (blackLists.rows.size > 0) {
                    binding.emptyContainer .visibility = View.GONE
                    blacklistAdapter = BlakListAdapter(this, blackLists.rows,
                            { position -> },
                            { mobile -> call(mobile) },
                            { position -> cancel(position) })
                    binding.blackList.adapter = blacklistAdapter
                    closeRefresh()
                } else {
                    binding.emptyContainer.visibility = View.VISIBLE
                }
            } else {
                warning(MemberService.msg)
            }
        }
    }

    private fun cancel(position: Int) {
        warning("是否真的要將此球友移出黑名單？", "取消", "確定", {_cancel(position)})
    }
    private fun _cancel(position: Int) {
        val row = blackLists.rows[position]
        val memberToken = row.token
        val teamToken = row.team.get("token") as String

        val token = member.token
        if (token != null) {
            loadingAnimation.start()
            TeamService.removeBlackList(this, teamToken, memberToken, token) { success ->
                runOnUiThread {
                    loadingAnimation.stop()
                }
                if (success) {
                    info("移除黑名單成功")
                    refresh()
                } else {
                    warning(TeamService.msg)
                }
            }
        }
    }
    private fun call(mobile: String) {
        myMakeCall(mobile)
    }
}

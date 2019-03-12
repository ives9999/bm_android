package com.sportpassword.bm.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.sportpassword.bm.Adapters.BlakListAdapter
import com.sportpassword.bm.Models.BlackList
import com.sportpassword.bm.Models.BlackLists
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_black_list_vc.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.makeCall
import kotlinx.android.synthetic.main.mask.*

class BlackListVC : BaseActivity() {

    lateinit var blackLists: BlackLists
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
        Loading.show(mask)
        MemberService.blacklist(this, memberToken) { success ->
            Loading.hide(mask)
            if (success) {
                blackLists = MemberService.blackLists
                if (blackLists.rows.size > 0) {
                    empty_container.visibility = View.GONE
                    blacklistAdapter = BlakListAdapter(this, blackLists.rows,
                            { position -> },
                            { mobile -> call(mobile) },
                            { position -> cancel(position) })
                    black_list.adapter = blacklistAdapter
                    closeRefresh()
                } else {
                    empty_container.visibility = View.VISIBLE
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

        Loading.show(mask)
        TeamService.removeBlackList(this, teamToken, memberToken, member.token) { success ->
            Loading.hide(mask)
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

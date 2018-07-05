package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.TempPlayDatePlayerAdapter
import com.sportpassword.bm.Models.TempPlayDatePlayer
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import kotlinx.android.synthetic.main.activity_temp_play_date_player.*
import org.jetbrains.anko.*

class TempPlayDatePlayerVC : BaseActivity() {

    var date: String = ""
    var teamName: String = ""
    var teamToken: String = ""
    lateinit var tempPlayDatePlayer: TempPlayDatePlayer
    lateinit var tempPlayDatePlayerAdapter: TempPlayDatePlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_play_date_player)

        date = intent.getStringExtra("date")
        teamName = intent.getStringExtra("teamName")
        teamToken = intent.getStringExtra("teamToken")
        setMyTitle(teamName + date + "臨打")

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempPlayDatePlayer_refresh)
        setRefreshListener()
        refresh()
    }

    override fun refresh() {
        super.refresh()
        val loading = Loading.show(this)
        TeamService.tempPlay_datePlayer(this, date, teamToken) { success ->
            loading.dismiss()
            if (success) {
                tempPlayDatePlayer = TeamService.tempPlayDatePlayer
                //println(rows)
                tempPlayDatePlayerAdapter = TempPlayDatePlayerAdapter(this, tempPlayDatePlayer.rows, { position ->
                    val row = tempPlayDatePlayer.rows[position]
                    //println(row.id)
                    alert("動作") {
                        title = "訊息"
                        negativeButton("關閉"){}
                        customView {
                            verticalLayout {
                                button("黑名單"){
                                    addBlackList(row.name, row.token, teamToken)
                                }
                                //button("test"){}
                            }
                        }
                    }.show()
                }, { mobile ->
                    println(mobile)
                    makeCall(mobile)
                })
                temp_play_date_player_list.adapter = tempPlayDatePlayerAdapter
                val layoutManager = LinearLayoutManager(this)
                temp_play_date_player_list.layoutManager = layoutManager
                closeRefresh()
            } else {
                warning(TeamService.msg)
            }
        }
    }
}
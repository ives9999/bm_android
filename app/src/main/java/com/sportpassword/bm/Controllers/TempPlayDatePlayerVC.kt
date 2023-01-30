package com.sportpassword.bm.Controllers

import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.TempPlayDatePlayerAdapter
import com.sportpassword.bm.Models.TempPlayDatePlayers
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.databinding.ActivityTempPlayDateBinding
import com.sportpassword.bm.databinding.ActivityTempPlayDatePlayerBinding
import org.jetbrains.anko.*

class TempPlayDatePlayerVC : BaseActivity() {

    private lateinit var binding: ActivityTempPlayDatePlayerBinding
    private lateinit var view: ViewGroup

    var date: String = ""
    var teamName: String = ""
    var teamToken: String = ""
    lateinit var tempPlayDatePlayers: TempPlayDatePlayers
    lateinit var tempPlayDatePlayerAdapter: TempPlayDatePlayerAdapter
    lateinit var dialog: DialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTempPlayDatePlayerBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("date")) {
            date = intent.getStringExtra("date")!!
        }

        if (intent.hasExtra("teamName")) {
            teamName = intent.getStringExtra("teamName")!!
        }

        if (intent.hasExtra("token")) {
            teamToken = intent.getStringExtra("token")!!
        }
        setMyTitle(teamName + date + "臨打")

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempPlayDatePlayer_refresh)
        setRefreshListener()
        refresh()
    }

    override fun refresh() {
        super.refresh()
        loadingAnimation.start()
        TeamService.tempPlay_datePlayer(this, date, teamToken) { success ->
            runOnUiThread {
                loadingAnimation.stop()
            }
            if (success) {
                tempPlayDatePlayers = TeamService.tempPlayDatePlayers
                //println(rows)
                tempPlayDatePlayerAdapter = TempPlayDatePlayerAdapter(this, tempPlayDatePlayers.rows, { position ->
                    val row = tempPlayDatePlayers.rows[position]
                    //println(row.id)
                    dialog = alert("動作") {
                        title = "訊息"
                        negativeButton("關閉"){}
                        customView {
                            verticalLayout {
                                button("黑名單"){
                                    this.setOnClickListener {
                                        dialog.dismiss()
                                        addBlackList(row.name, row.token, teamToken)
                                    }
                                }
                                //button("test"){}
                            }
                        }
                    }.show()
                }, { mobile ->
                    //println(mobile)
                    myMakeCall(mobile)
                })
                binding.tempPlayDatePlayerList.adapter = tempPlayDatePlayerAdapter
                val layoutManager = LinearLayoutManager(this)
                binding.tempPlayDatePlayerList.layoutManager = layoutManager
                closeRefresh()
            } else {
                warning(TeamService.msg)
            }
        }
    }
}

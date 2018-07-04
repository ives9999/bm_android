package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.TempPlayDateAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import kotlinx.android.synthetic.main.activity_temp_play_date.*
import org.jetbrains.anko.contentView

class TempPlayDateVC : BaseActivity() {

    var name: String = ""
    var token: String = ""
    var rows: ArrayList<String> = arrayListOf()
    lateinit var tempPlayDateAdapter: TempPlayDateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_play_date)

        name = intent.getStringExtra("name")
        token = intent.getStringExtra("token")
        setMyTitle(name + "開放臨打日期")

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempPlayDate_refresh)
        setRefreshListener()
        refresh()
    }

    override fun refresh() {
        super.refresh()
        val loading = Loading.show(this)
        TeamService.tempPlay_date(this, token) { success ->
            loading.dismiss()
            if (success) {
                rows = TeamService.tempPlayDate.rows
                //println(rows)
                tempPlayDateAdapter = TempPlayDateAdapter(this, rows, { position ->
                    val date = rows[position]
                    //println(date)
                    goTempPlayDatePlayer(date, name, token)
                })
                temp_play_date_list.adapter = tempPlayDateAdapter
                val layoutManager = LinearLayoutManager(this)
                temp_play_date_list.layoutManager = layoutManager
                closeRefresh()
            } else {
                warning(TeamService.msg)
            }
        }
    }
}

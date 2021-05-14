package com.sportpassword.bm.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.TempPlayDateAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import kotlinx.android.synthetic.main.activity_temp_play_date.*
import kotlinx.android.synthetic.main.mask.*
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
        Loading.show(mask)
        TeamService.tempPlay_date(this, token) { success ->
            Loading.hide(mask)
            if (success) {
                rows = TeamService.tempPlayDate.rows
                //println(rows)
                tempPlayDateAdapter = TempPlayDateAdapter(this, rows, { position ->
                    val date = rows[position]
                    //println(date)
                    toTempPlayDatePlayer(date, name, token)
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

package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.TempPlayDateAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.databinding.ActivityTempPlayDateBinding
import org.jetbrains.anko.contentView

class TempPlayDateVC : BaseActivity() {

    private lateinit var binding: ActivityTempPlayDateBinding
    private lateinit var view: ViewGroup

    var name: String = ""
    var token: String = ""
    var rows: ArrayList<String> = arrayListOf()
    lateinit var tempPlayDateAdapter: TempPlayDateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTempPlayDateBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name")!!
        }

        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")!!
        }
        setMyTitle(name + "開放臨打日期")

        refreshLayout = contentView!!.findViewById<SwipeRefreshLayout>(R.id.tempPlayDate_refresh)
        setRefreshListener()
        refresh()
    }

    override fun refresh() {
        super.refresh()
        Loading.show(view)
        TeamService.tempPlay_date(this, token) { success ->
            runOnUiThread {
                Loading.hide(view)
            }
            if (success) {
                rows = TeamService.tempPlayDate.rows
                //println(rows)
                tempPlayDateAdapter = TempPlayDateAdapter(this, rows, { position ->
                    val date = rows[position]
                    //println(date)
                    toTempPlayDatePlayer(date, name, token)
                })
                binding.tempPlayDateList.adapter = tempPlayDateAdapter
                val layoutManager = LinearLayoutManager(this)
                binding.tempPlayDateList.layoutManager = layoutManager
                closeRefresh()
            } else {
                warning(TeamService.msg)
            }
        }
    }
}

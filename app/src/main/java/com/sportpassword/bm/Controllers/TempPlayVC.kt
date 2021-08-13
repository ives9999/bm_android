package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Adapters.TempPlayListAdapter
import com.sportpassword.bm.Fragments.TeamItem
//import com.sportpassword.bm.Models.Arena
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.ProductsTable
import com.sportpassword.bm.Models.TeamsTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Utilities.jsonToModel
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_temp_play_vc.*
import kotlinx.android.synthetic.main.mask.*

class TempPlayVC : MyTableVC() {

//    protected lateinit var tempPlayListAdapter: TempPlayListAdapter
//    protected var dataLists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()

    var mysTable: TeamsTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_play_vc)

        setMyTitle("臨打")

//        citys = intent.getSerializableExtra("citys") as ArrayList<City>
//        arenas = intent.getSerializableExtra("arenas") as ArrayList<Arena>
//        degrees = intent.getSerializableExtra("degrees") as ArrayList<DEGREE>
//
        if (intent.hasExtra("weekdays")) {
            weekdays = intent.getIntegerArrayListExtra("weekdays")!!
        }
        times = intent.getSerializableExtra("times") as HashMap<String, Any>
        keyword = intent.getStringExtra("keyword")!!

        prepareParams()

        recyclerView = tempplay_list
        dataService = TeamService
        refreshLayout = tempplay_refresh
        initAdapter()

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun genericTable() {
        mysTable = jsonToModels<TeamsTable>(dataService.jsonString)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                row.filterRow()
                val myItem = TeamItem(this, row)
                myItem.list1CellDelegate = this
                items.add(myItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {

        val myItem = item as TeamItem
        val table = myItem.row
        val intent = Intent(this, ShowTempPlayActivity::class.java)
        //intent.putExtra("position", position)
        intent.putExtra(TOKEN_KEY, table.token)
        startActivity(intent)
    }

    private fun show(data: Map<String, Map<String, Any>>) {
        val position = data["position"]!!["value"] as Int
        val token = data[TOKEN_KEY]!!["value"] as String
        //val intent = Intent(activity, TestActivity::class.java)
        val intent = Intent(this, ShowTempPlayActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra(TOKEN_KEY, token)
        startActivity(intent)
    }

    private fun arenaBtnPressed(arena_id: Int) {
//        resetParams()
//        arenas.add(Arena(arena_id, ""))
//        prepareParams()
//        refresh()
    }
}

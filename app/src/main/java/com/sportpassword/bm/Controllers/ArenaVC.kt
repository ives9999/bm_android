package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Adapters.SearchItemDelegate
import com.sportpassword.bm.Fragments.ListItem
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import kotlinx.android.synthetic.main.activity_arena_vc.*
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.arena_list_cell.*
import kotlinx.android.synthetic.main.arena_list_cell.cityBtn
import kotlinx.android.synthetic.main.arena_list_cell.intervalLbl
import kotlinx.android.synthetic.main.arena_list_cell.telIcon
import kotlinx.android.synthetic.main.course_list_cell.*
import org.jetbrains.anko.makeCall


class ArenaVC : MyTableVC1() {

    var arenasTable: ArenasTable? = null
    var t: ArrayList<ArenaTable> = arrayListOf()

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
        hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY),
        hashMapOf("title" to "區域","detail" to "全部","key" to AREA_KEY),
        hashMapOf("title" to "空調","detail" to "全部","key" to ARENA_AIR_CONDITION_KEY,"switch" to "true"),
        hashMapOf("title" to "盥洗室","detail" to "全部","key" to ARENA_BATHROOM_KEY,"switch" to "true"),
        hashMapOf("title" to "停車場","detail" to "全部","key" to ARENA_PARKING_KEY,"switch" to "true")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        setMyTitle("球館")
        able_type = "arena"

        dataService = ArenaService
        searchRows = _searchRows
        recyclerView = list_container
        refreshLayout = refresh
        initAdapter()

        refresh()
    }

    override fun genericTable() {
        //storesTable = jsonToModel<StoresTable>(dataService.jsonString)
        arenasTable = jsonToModels<ArenasTable>(jsonString!!)
        if (arenasTable != null) {
            tables = arenasTable
            t.addAll(arenasTable!!.rows)
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (arenasTable != null) {
            for (row in t) {
                //row.print()
                row.filterRow()
                val myItem = ArenaItem(this, row)
                //val coachItem = CoachItem(this, row)
                myItem.list1CellDelegate = this
                items.add(myItem)
            }
        }

        return items
    }

    override fun setRecyclerViewScrollListener() {

        var pos: Int = 0

//        scrollerListenr = object: RecyclerView.OnScrollListener() {
//
//        }

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (rows.size < totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                    //println("pos:${pos}")
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                println("tables.rows.size:${t.size}")
                if (t.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && t.size < totalCount && !loading) {
                    getDataStart1(page, perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val arenaItem = item as ArenaItem
        val table = arenaItem.row
        //superCourse.print()
        toShowArena(table.token)
    }

    override fun remove(indexPath: IndexPath) {
        val row = _searchRows[indexPath.row]
        val key = row["key"]!!
        when (key) {
            CITY_KEY -> citys.clear()
            AREA_KEY -> areas.clear()
        }
        _searchRows[indexPath.row]["detail"] = "全部"
//        val rows = generateSearchItems(type!!)
//        searchAdapter.update(rows)
    }
}

class ArenaItem(override var context: Context, var _row: ArenaTable): ListItem<Table>(context, _row) {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //println(superStore);

        super.bind(viewHolder, position)

        val row: ArenaTable = _row

        if (row.city_show.isNotEmpty()) {
            viewHolder.cityBtn.text = row.city_show
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.area_show.isNotEmpty()) {
            viewHolder.areaBtn.text = row.area_show
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.tel_show.isNotEmpty()) {
            viewHolder.telLbl.text = row.tel_show
        } else {
            viewHolder.telLbl.text = "電話：未提供"
        }

        viewHolder.parkingLbl.text = "停車場:${row.parking_show}"

        if (row.interval_show.isNotEmpty()) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.intervalLbl.text = "未提供"
        }

        viewHolder.air_conditionLbl.text = "空調:${row.air_condition_show}"
    }

    override fun getLayout() = R.layout.arena_list_cell
}

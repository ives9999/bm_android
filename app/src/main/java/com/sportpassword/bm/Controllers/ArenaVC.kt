package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.sportpassword.bm.Adapters.SearchItemDelegate
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Models.ArenasTable
import com.sportpassword.bm.Models.CoachTable
import com.sportpassword.bm.Models.CoachesTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import kotlinx.android.synthetic.main.activity_arena_vc.*
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.arena_list_cell.*


class ArenaVC : MyTableVC1() {

    var arenasTable: ArenasTable? = null

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

        dataService = ArenaService
        searchRows = _searchRows
        recyclerView = store_list
        refreshLayout = store_refresh
        initAdapter()

        refresh()
    }

    override fun genericTable() {
        //storesTable = jsonToModel<StoresTable>(dataService.jsonString)
        arenasTable = jsonToModel<ArenasTable>(dataService.jsonString)
        if (arenasTable != null) {
            tables = arenasTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (arenasTable != null) {
            for (row in arenasTable!!.rows) {
                //row.print()
                row.filterRow()
                val arenaItem = ArenaItem(this, row)
                //val coachItem = CoachItem(this, row)
                arenaItem.list1CellDelegate = this
                items.add(arenaItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val arenaItem = item as ArenaItem
        val arenaTable = arenaItem.row
        //superCourse.print()
        val intent = Intent(this, ShowCoachVC::class.java)
        intent.putExtra("arena_token", arenaTable.token)
        intent.putExtra("title", arenaTable.name)
        startActivity(intent)
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

class ArenaItem(val context: Context, val row: ArenaTable): Item() {

    var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //println(superStore);

        viewHolder.titleTxt.text = row.name
        Picasso.with(context)
                .load(row.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)

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


        if (row.tel != null && row.tel.isNotEmpty()) {
            viewHolder.telLbl.text = row.tel_show
        } else {
            viewHolder.telLbl.text = "電話:未提供"
        }

        viewHolder.parkingLbl.text = "停車場:${row.parking_show}"

        if (row.interval_show.isNotEmpty()) {
            viewHolder.intervalLbl.text = row.interval_show
        } else {
            viewHolder.intervalLbl.text = "未提供"
        }

        viewHolder.air_conditionLbl.text = row.air_condition_show

        viewHolder.likeIcon.setOnClickListener {
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellLike(row)
            }
        }

        viewHolder.refreshIcon.setOnClickListener {
//            println(position)
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellRefresh()
            }
        }

        if (row.tel == null || row.tel.isEmpty()) {
            viewHolder.telIcon.visibility = View.GONE
        } else {
            viewHolder.telIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellMobile(row.tel)
                }
            }
        }

        if (row.address.isEmpty()) {
            viewHolder.mapIcon.visibility = View.GONE
        } else {
            viewHolder.mapIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellShowMap(row)
                }
            }
        }

    }

    override fun getLayout() = R.layout.arena_list_cell
}

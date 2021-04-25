package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import com.sportpassword.bm.Models.CoachTable
import com.sportpassword.bm.Models.CoachesTable
import com.sportpassword.bm.Models.StoresTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
//import com.sportpassword.bm.Services.StoreService
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.IndexPath
import com.sportpassword.bm.Utilities.KEYWORD_KEY
import com.sportpassword.bm.Utilities.jsonToModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_coach_vc.*
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.coach_list_cell.*

class CoachVC : MyTableVC1() {

    var coachesTable: CoachesTable? = null
    //var storesTable: StoresTable? = null

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
            hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)
        //setContentView(R.layout.activity_coach_vc)

        setMyTitle("教練")
        dataService = CoachService
        //dataService = CoachService
        searchRows = _searchRows
        recyclerView = store_list
        refreshLayout = store_refresh
        initAdapter()

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_manager, menu)
        val memuView = menu!!.findItem(R.id.menu_search_manager).actionView

        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)

//        searchBtn.tag = type
//        ManagerBtn.tag = type

        return true
    }

    override fun genericTable() {
        //storesTable = jsonToModel<StoresTable>(dataService.jsonString)
        coachesTable = jsonToModel<CoachesTable>(dataService.jsonString)
        if (coachesTable != null) {
            tables = coachesTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (coachesTable != null) {
            for (row in coachesTable!!.rows) {
                //row.print()
                row.filterRow()
                val coachItem = CoachItem(this, row)
                //val coachItem = CoachItem(this, row)
                //coachItem.list1CellDelegate = this
                items.add(coachItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val coachItem = item as CoachItem
        val coachTable = coachItem.row
        //superCourse.print()
        val intent = Intent(this, ShowCoachVC::class.java)
        intent.putExtra("coach_token", coachTable.token)
        intent.putExtra("title", coachTable.name)
        startActivity(intent)
    }

    override fun remove(indexPath: IndexPath) {
        val row = _searchRows[indexPath.row]
        val key = row["key"]!!
        when (key) {
            CITY_KEY -> citys.clear()
        }
        _searchRows[indexPath.row]["detail"] = "全部"
//        val rows = generateSearchItems(type!!)
//        searchAdapter.update(rows)
    }
}

class CoachItem(val context: Context, val row: CoachTable): Item() {

    var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //println(superStore);
        if (row.city_show.isNotEmpty()) {
            viewHolder.cityBtn.text = row.city_show
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }
        viewHolder.titleTxt.text = row.name
        Picasso.with(context)
                .load(row.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)

        if (row.mobile != null && row.mobile.isNotEmpty()) {
            viewHolder.mobileLbl.text = row.mobile_show
        } else {
            viewHolder.mobileLbl.text = "行動電話:未提供"
        }

        if (row.seniority >= 0) {
            viewHolder.seniorityLbl.text = "年資:${row.seniority_show}"
        } else {
            viewHolder.seniorityLbl.text = "年資:未提供"
        }

        if (row.line != null && row.line.isNotEmpty()) {
            viewHolder.line.text = "Line:${row.line}"
        } else {
            viewHolder.line.text = "Line:未提供"
        }

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

        if (row.mobile == null || row.mobile.isEmpty()) {
            viewHolder.telIcon.visibility = View.GONE
        } else {
            viewHolder.telIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellMobile(row.mobile)
                }
            }
        }

    }

    override fun getLayout() = R.layout.coach_list_cell
}

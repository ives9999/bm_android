package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Fragments.ListItem
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.StoreService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.arena_list_cell.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.store_list_cell.*
import kotlinx.android.synthetic.main.store_list_cell.cityBtn
import kotlinx.android.synthetic.main.store_list_cell.telLbl
import kotlinx.android.synthetic.main.store_list_cell.titleLbl
import kotlinx.android.synthetic.main.team_list_cell.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StoreVC : MyTableVC() {

    var mysTable: StoresTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to "")
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        //source_activity = "store"
        //val title_field = intent.getStringExtra("titleField")
        able_type = "store"
        setMyTitle("體育用品店")

        dataService = StoreService
        recyclerView = list_container
        refreshLayout = refresh
        maskView = mask

        initAdapter()
        refresh()
    }

    override fun genericTable() {
        mysTable = jsonToModels<StoresTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                //row.print()
                row.filterRow()
                val myItem = StoreItem(this, row)
                myItem.list1CellDelegate = this
                items.add(myItem)
            }
        }

        return items
    }

    override fun prepare(idx: Int) {

        val row = searchRows.get(idx)
        var key: String = ""
        if (row.containsKey("key")) {
            key = row["key"]!!
        }

        var value: String = ""
        if (row.containsKey("value") && row["value"]!!.isNotEmpty()) {
            value = row["value"]!!
        }


        when (key) {
            CITY_KEY -> {
                toSelectCity(value, this)
            }
        }
    }


    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {

        val storeItem = item as StoreItem
        val table = storeItem.row
        toShowStore(table.token)
    }

    override fun remove(indexPath: IndexPath) {
        val row = searchRows[indexPath.row]
        val key = row["key"]!!
        when (key) {
            CITY_KEY -> {
                citys.clear()
                row["show"] = "全部"
            }
        }
        row["value"] = ""
    }

}

class StoreItem(override var context: Context, var _row: StoreTable): ListItem<Table>(context, _row) {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        super.bind(viewHolder, position)

        val row: StoreTable = _row
        //println(superStore);
        viewHolder.cityBtn.text = row.city_show
        viewHolder.cityBtn.setOnClickListener {
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellCity(row)
            }
        }

        viewHolder.titleLbl.text = row.name

        viewHolder.business_timeTxt.text = "${row.open_time_show}~${row.close_time_show}"

        viewHolder.addressTxt.text = row.address

        if (row.tel_show.isNotEmpty()) {
            viewHolder.telLbl.text = row.tel_show
        } else {
            viewHolder.telLbl.text = "電話：未提供"
        }

//如果要啟動管理功能，請打開這個註解
//        var showManager = false
//        val managers = row.managers
//        if (managers.count() > 0) {
//            val member_id = member.id
//            for (manager in managers) {
//                //print(manager)
//                if (manager.containsKey("id") && manager["id"] != null) {
//                    val manager_id = manager["id"] as Int
//                    if (member_id == manager_id) {
//                        showManager = true
//                        break
//                    }
//                }
//            }
//        }
//        if (showManager) {
//            viewHolder.editIcon.setOnClickListener {
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellEdit(position)
//                }
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellDelete(position)
//                }
//            }
//        } else {
//            viewHolder.editIcon.visibility = View.INVISIBLE
//            viewHolder.deleteIcon.visibility = View.INVISIBLE
//        }

    }

    override fun getLayout() = R.layout.store_list_cell
}




























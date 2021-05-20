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
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.arena_list_cell.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.store_list_cell.*
import kotlinx.android.synthetic.main.store_list_cell.cityBtn
import kotlinx.android.synthetic.main.store_list_cell.telLbl
import kotlinx.android.synthetic.main.store_list_cell.titleLbl
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StoreVC : MyTableVC1() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
    )

    var mysTable: StoresTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        source_activity = "store"
        searchRows = _searchRows
        val title_field = intent.getStringExtra("titleField")
        setMyTitle("體育用品店")

        dataService = StoreService
        searchRows = _searchRows
        recyclerView = list_container
        refreshLayout = refresh
        maskView = mask
        setRefreshListener()

        initAdapter()
        refresh()
    }

    override fun genericTable() {
        mysTable = jsonToModels<StoresTable>(dataService.jsonString)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun prepareParams(city_type: String) {
        params.clear()
        if (keyword.length > 0) {
            val row = getSearchRow(KEYWORD_KEY)
            if (row != null && row.containsKey("value")) {
                row["value"] = keyword
                updateSearchRow(KEYWORD_KEY, row)
            }
        }
        for (searchRow in _searchRows) {
            var value_type: String? = null
            if (searchRow.containsKey("value_type")) {
                value_type = searchRow.get("value_type")
            }
            var value: String = ""
            if (searchRow.containsKey("value")) {
                value = searchRow.get("value")!!
            }
            var key: String? = null
            if (searchRow.containsKey("key")) {
                key = searchRow.get("key")!!
            }
            if (value_type != null && key != null && value.length > 0) {
                var values: Array<String>? = null
                if (value_type == "String") {
                    params[key] = value
                } else if (value_type == "Array") {
                    value = searchRow.get("value")!!
                    values = value.split(",").toTypedArray()
                }
                if (values != null) {
                    params[key] = values
                }
            }
        }
    }

    fun updateSearchRow(idx: Int, row: HashMap<String, String>) {
        _searchRows[idx] = row
    }

    fun updateSearchRow(key: String, row: HashMap<String, String>) {
        var idx: Int = -1
        for ((i, searchRow) in _searchRows.withIndex()) {
            if (searchRow.containsKey("key")) {
                if (key == searchRow.get("key")) {
                    idx = i
                    break
                }
            }
        }
        if (idx >= 0) {
            _searchRows[idx] = row
        }
    }

    override fun remove(indexPath: IndexPath) {
        var row: HashMap<String, String>? = null
        if (_searchRows.size >= indexPath.row) {
            row = _searchRows[indexPath.row]
        }
        var key: String? = null
        if (row != null && row.containsKey("key") && row.get("key")!!.length > 0) {
            key = row!!.get("key")
        }
        if (row != null) {
            row["value"] = ""
            row["show"] = "不限"
            updateSearchRow(indexPath.row, row)
        }
    }

    fun getSearchRow(key: String): HashMap<String, String>? {
        var row: HashMap<String, String>? = null
        for ((i, searchRow) in _searchRows.withIndex()) {
            if (searchRow.containsKey("key")) {
                if (key == searchRow.get("key")) {
                    row = searchRow
                    break
                }
            }
        }

        return row
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

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val storeItem = item as StoreItem
        val table = storeItem.row
        toShowStore(table!!.token)
    }
}

class StoreItem(override var context: Context, var _row: StoreTable): ListItem<Table>(context, _row) {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        super.bind(viewHolder, position)

        val row: StoreTable = _row
        //println(superStore);
        viewHolder.cityBtn.text = row.city_show
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




























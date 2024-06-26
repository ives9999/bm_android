package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sportpassword.bm.Adapters.StoreAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.StoreService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityStoreVcBinding
import tw.com.bluemobile.hbc.extensions.setInfo
import com.sportpassword.bm.functions.jsonToModels
import kotlin.collections.ArrayList

class StoreVC : MyTableVC() {

    private lateinit var binding: ActivityStoreVcBinding
    //private lateinit var view: ViewGroup

    var mysTable: StoresTable? = null
    lateinit var tableAdapter: StoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

//        searchRows = arrayListOf(
//            hashMapOf("title" to "關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to ""),
//            hashMapOf("title" to "縣市","show" to "全部","key" to CITY_KEY,"value" to "")
//        )

        super.onCreate(savedInstanceState)

        binding = ActivityStoreVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        //source_activity = "store"
        //val title_field = intent.getStringExtra("titleField")
        able_type = "store"
        setMyTitle("體育用品店")

        dataService = StoreService
        recyclerView = binding.listContainer
        refreshLayout = binding.refresh

        //initAdapter()
        tableAdapter = StoreAdapter(R.layout.store_list_cell, this)
        recyclerView.adapter = tableAdapter

        init()
        refresh()
    }

    override fun init() {
        isSearchIconShow = true
        isPrevIconShow = true
        super.init()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        isSearchIconShow = true
//        super.onCreateOptionsMenu(menu)
//
//        return true
//    }

    override fun genericTable() {
        mysTable = jsonToModels<StoresTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            if (mysTable!!.rows.count() > 0) {
                getPage()
                tableLists += generateItems1(StoreTable::class, mysTable!!.rows)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
                    tableAdapter.notifyDataSetChanged()
                }
            } else {
                val rootView: ViewGroup = getRootView()
                runOnUiThread {
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

    override fun makeSection0Row(isExpanded: Boolean): OneSection {
        val rows: ArrayList<OneRow> = arrayListOf()
        val r1: OneRow = OneRow("關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)
        val r2: OneRow = OneRow("縣市", "", "全部", CITY_KEY, "more")
        rows.add(r2)

        val s: OneSection = OneSection("一般", "general", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val row1: StoreTable = row as StoreTable
        val row2: OneRow = getOneRowFromKey(key)
        row2.value = row1.city_id.toString()
        row2.show = row1.city_show
        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                //row.print()
//                row.filterRow()
//                val myItem = StoreItem(this, row)
//                myItem.list1CellDelegate = this
//                items.add(myItem)
//            }
//        }
//
//        return items
//    }

//    override fun prepare(idx: Int) {
//
//        val row = searchRows.get(idx)
//        var key: String = ""
//        if (row.containsKey("key")) {
//            key = row["key"]!!
//        }
//
//        var value: String = ""
//        if (row.containsKey("value") && row["value"]!!.isNotEmpty()) {
//            value = row["value"]!!
//        }
//
//
//        when (key) {
//            CITY_KEY -> {
//                toSelectCity(value, this)
//            }
//        }
//    }


//    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {
//
//        val storeItem = item as StoreItem
//        val table = storeItem.row
//        toShowStore(table.token)
//    }

//    fun remove(indexPath: IndexPath) {
//        val row = searchRows[indexPath.row]
//        val key = row["key"]!!
//        when (key) {
//            CITY_KEY -> {
//                citys.clear()
//                row["show"] = "全部"
//            }
//        }
//        row["value"] = ""
//    }
}

//class StoreItem(override var context: Context, var _row: StoreTable): ListItem<Table>(context, _row) {
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//
//        super.bind(viewHolder, position)
//
//        val row: StoreTable = _row
//        //println(superStore);
//        viewHolder.cityBtn.text = row.city_show
//        viewHolder.cityBtn.setOnClickListener {
//            if (list1CellDelegate != null) {
//                list1CellDelegate!!.cellCity(row)
//            }
//        }
//
//        viewHolder.titleLbl.text = row.name
//
//        viewHolder.business_timeTxt.text = "${row.open_time_show}~${row.close_time_show}"
//
//        viewHolder.addressTxt.text = row.address
//
//        if (row.tel_show.isNotEmpty()) {
//            viewHolder.telLbl.text = row.tel_show
//        } else {
//            viewHolder.telLbl.text = "電話：未提供"
//        }

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

//    }
//
//    override fun getLayout() = R.layout.store_list_cell
//}




























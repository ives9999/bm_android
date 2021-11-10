package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Adapters.TeachAdapter
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeachService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_store_vc.*

class TeachVC : MyTableVC() {

    //lateinit var collectionAdapter: CollectionAdapter
    var mysTable: TeachesTable? = null
    lateinit var tableAdapter: TeachAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        this.dataService = TeachService
        able_type = "teach"

        searchRows = arrayListOf(
            hashMapOf("title" to "標題關鍵字","show" to "全部","key" to KEYWORD_KEY,"value" to "")
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        setMyTitle("教學")

        if (intent.hasExtra("params")) {
            val t = intent.getSerializableExtra("params")
            if (t != null) {
                params = t as HashMap<String, String>
            }
        }

        recyclerView = list_container
        refreshLayout = refresh
        //initAdapter()
        tableAdapter = TeachAdapter(R.layout.teach_list_cell, this)
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
        mysTable = jsonToModels(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            getPage()
            tableLists += generateItems1(TeachTable::class, mysTable!!.rows)
            tableAdapter.setMyTableList(tableLists)
            runOnUiThread {
                tableAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun makeSection0Row(isExpanded: Boolean): SearchSection {
        val rows: ArrayList<SearchRow> = arrayListOf()
        val r1: SearchRow = SearchRow("標題關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)

        val s: SearchSection = SearchSection("一般", isExpanded)
        s.items.addAll(rows)
        return s
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                //row.print()
//                row.filterRow()
//                val myItem = TeachItem(this, row)
//                myItem.list1CellDelegate = this
//                items.add(myItem)
//            }
//        }
//
//        return items
//    }
//
//    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {
//
//        val teachItem = item as TeachItem
//        val table = teachItem.row as TeachTable
//        //toShowTeach(table.token)
//        toYoutubePlayer(table.youtube)
//    }
}

//class TeachItem(override var context: Context, var _row: TeachTable): ListItem<Table>(context, _row) {
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//
//        super.bind(viewHolder, position)
//
//        val row: TeachTable = _row
//
//        //is bind in ListItem
//        //viewHolder.titleLbl.text = row.title
//
//        viewHolder.pvLbl.text = row.pv.toString()
//        viewHolder.dateLbl.text = row.created_at_show
//
////
////        if (row.tel_show.isNotEmpty()) {
////            viewHolder.telLbl.text = row.tel_show
////        } else {
////            viewHolder.telLbl.text = "電話：未提供"
////        }
//
//    }
//
//    override fun getLayout() = R.layout.teach_list_cell
//}

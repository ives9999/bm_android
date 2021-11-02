package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Fragments.MyAdapter
import com.sportpassword.bm.Fragments.MyViewHolder
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_arena_vc.*
import kotlinx.android.synthetic.main.activity_arena_vc.list_container
import kotlinx.android.synthetic.main.activity_arena_vc.page_refresh
import kotlinx.android.synthetic.main.activity_course_vc.*
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.arena_list_cell.view.*
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.olcell.view.*
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.backgroundColor

class ArenaVC : MyTableVC() {

    var mysTable: ArenasTable? = null
    lateinit var tableAdapter: ArenaAdapter

//    lateinit var youAdapter: YouAdapter
//    val youItems: ArrayList<YouItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_arena_vc)

        this.dataService = ArenaService
        able_type = "arena"


//        youAdapter = YouAdapter()
//        youAdapter.items = youItems

        recyclerView = list_container
        refreshLayout = page_refresh
        maskView = mask
        setRefreshListener()
//        val l = LinearLayoutManager(this)
//        l.orientation = LinearLayoutManager.VERTICAL
//        list.layoutManager = l
//        list_container.adapter = youAdapter
//        val myColorGreen = ContextCompat.getColor(this, R.color.MY_GREEN)
        arenaTabLine.backgroundColor = myColorGreen
        topTitleLbl.setText("課程")
//
        tableAdapter = ArenaAdapter(R.layout.arena_list_cell, this)
//        val a1: ArenaTable = ArenaTable()
//        a1.title = "aaa"
//        val a2: ArenaTable = ArenaTable()
//        a2.title = "bbb"
//        val a: ArrayList<ArenaTable> = arrayListOf(
//            a1, a2
//        )
//        tableAdapter.tableList = a
        list_container.adapter = tableAdapter

        init()
        refresh()
    }

    override fun init() {
        isSearchIconShow = true
        super.init()
    }

    override fun genericTable() {
        mysTable = jsonToModels<ArenasTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            getPage()
            tableLists += generateItems1(ArenaTable::class, mysTable!!.rows)
//            val a: ArrayList<Table> = arrayListOf()
//            for (tableList in tableLists) {
//                val a1: ArenaTable = ArenaTable()
//                a1.title = tableList.name
//                a.add(a1)
//            }
//            tableAdapter.tableList = a
            tableAdapter.tableList = tableLists
//            for (i in 0..5) {
//                youItems.add(YouItem(i.toString()))
//            }
//            youAdapter.items = youItems
            runOnUiThread {
//                youAdapter.notifyDataSetChanged()
                tableAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun makeSection0Row(isExpanded: Boolean): SearchSection {
        val rows: ArrayList<SearchRow> = arrayListOf()
        //if (isExpanded) {
        val r1: SearchRow = SearchRow("關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)
        val r2: SearchRow = SearchRow("縣市", "", "全部", CITY_KEY, "more")
        rows.add(r2)
        val r3: SearchRow = SearchRow("區域", "", "全部", AREA_KEY, "more")
        rows.add(r3)
        val r4: SearchRow = SearchRow("空調", "", "全部", ARENA_AIR_CONDITION_KEY, "switch")
        rows.add(r4)
        val r5: SearchRow = SearchRow("盥洗室", "", "全部", ARENA_BATHROOM_KEY, "switch")
        rows.add(r5)
        val r6: SearchRow = SearchRow("停車場", "", "全部", ARENA_PARKING_KEY, "switch")
        rows.add(r6)
        //}

        val s: SearchSection = SearchSection("一般", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val row1: ArenaTable = row as ArenaTable
        val row2: SearchRow = getSearchRowFromKey(key)
        row2.value = row1.city_id.toString()
        row2.show = row1.city_show
        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }

    override fun cellArea(row: Table) {

        val key: String = AREA_KEY
        val row1: ArenaTable = row as ArenaTable
        val row2 = getSearchRowFromKey(key)
        val row3 = getSearchRowFromKey(CITY_KEY)
        row2.value = row1.area_id.toString()
        row2.show = row1.area_show
        row3.value = row1.city_id.toString()
        row3.show = row1.city_show
        prepareParams()
        page = 1
        tableLists.clear()
        getDataStart(page, perPage)
    }
}

//data class YouItem(val name: String)
//
//class YouAdapter: RecyclerView.Adapter<YouAdapter.ViewHolder>() {
//
//    var items: ArrayList<YouItem> = arrayListOf()
//
//    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        val name: TextView = itemView.name
//
//        fun bind(item: YouItem) {
//            name.text = item.name
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val example = inflater.inflate(R.layout.you, parent, false)
//        return ViewHolder(example)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//}

class ArenaAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ArenaViewHolder>(resource, ::ArenaViewHolder, list1CellDelegate) {}

class ArenaViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    override fun bind(_row: Table, idx: Int) {
        super.bind(_row, idx)

        val row: ArenaTable = _row as ArenaTable

        if (row.city_show.isNotEmpty()) {
            viewHolder.cityBtn.text = row.city_show
            viewHolder.cityBtn.setOnClickListener {
                list1CellDelegate?.cellCity(row)
            }
        } else {
            viewHolder.cityBtn.visibility = View.GONE
        }

        if (row.area_show.isNotEmpty()) {
            viewHolder.areaBtn.text = row.area_show
            viewHolder.areaBtn.setOnClickListener {
                list1CellDelegate?.cellArea(row)
            }
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
}

package com.sportpassword.bm.Controllers

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.sportpassword.bm.Adapters.ArenaAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_arena_vc.list_container
import kotlinx.android.synthetic.main.activity_arena_vc.page_refresh
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

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

        if (intent.hasExtra("isPrevIconShow")) {
            isPrevIconShow = intent.getBooleanExtra("isPrevIconShow", false)
        }

        if (intent.hasExtra("isSearchIconShow")) {
            isSearchIconShow = intent.getBooleanExtra("isSearchIconShow", false)
        }

//        youAdapter = YouAdapter()
//        youAdapter.items = youItems

        recyclerView = list_container
        refreshLayout = page_refresh
        maskView = mask
        setRefreshListener()

        topTitleLbl.setText("球館")
        setBottomTabFocus()

        tableAdapter = ArenaAdapter(R.layout.arena_list_cell, this)
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

    override fun makeSection0Row(isExpanded: Boolean): OneSection {
        val rows: ArrayList<OneRow> = arrayListOf()
        //if (isExpanded) {
        val r1: OneRow = OneRow("關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)
        val r2: OneRow = OneRow("縣市", "", "全部", CITY_KEY, "more")
        rows.add(r2)
        val r3: OneRow = OneRow("區域", "", "全部", AREA_KEY, "more")
        rows.add(r3)
        val r4: OneRow = OneRow("空調", "", "全部", ARENA_AIR_CONDITION_KEY, "switch")
        rows.add(r4)
        val r5: OneRow = OneRow("盥洗室", "", "全部", ARENA_BATHROOM_KEY, "switch")
        rows.add(r5)
        val r6: OneRow = OneRow("停車場", "", "全部", ARENA_PARKING_KEY, "switch")
        rows.add(r6)
        //}

        val s: OneSection = OneSection("一般", "general", isExpanded)
        s.items.addAll(rows)
        return s
    }

    override fun cellCity(row: Table) {
        val key: String = CITY_KEY
        val row1: ArenaTable = row as ArenaTable
        val row2: OneRow = getOneRowFromKey(key)
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
        val row2 = getOneRowFromKey(key)
        val row3 = getOneRowFromKey(CITY_KEY)
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

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
import com.sportpassword.bm.Fragments.MyViewHolder
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_arena_vc.*
import kotlinx.android.synthetic.main.activity_arena_vc.page_refresh
import kotlinx.android.synthetic.main.activity_course_vc.*
import kotlinx.android.synthetic.main.arena_list_cell.view.*
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.olcell.view.*
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.backgroundColor

class ArenaVC : MyTableVC() {

    var mysTable: ArenasTable? = null
    lateinit var tableAdapter: ArenaAdapter

    lateinit var youAdapter: YouAdapter
    val youItems: ArrayList<YouItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_arena_vc)

        this.dataService = ArenaService
        able_type = "arena"

        refreshLayout = page_refresh


        youAdapter = YouAdapter()
        youAdapter.items = youItems
//        val l = LinearLayoutManager(this)
//        l.orientation = LinearLayoutManager.VERTICAL
//        list.layoutManager = l
        list.adapter = youAdapter
//        val myColorGreen = ContextCompat.getColor(this, R.color.MY_GREEN)
//        arenaTabLine.backgroundColor = myColorGreen
//        topTitleLbl.setText("課程")
//
//        tableAdapter = ArenaAdapter()
//        val a1: ArenaTable = ArenaTable()
//        a1.title = "aaa"
//        val a2: ArenaTable = ArenaTable()
//        a2.title = "bbb"
//        val a: ArrayList<ArenaTable> = arrayListOf(
//            a1, a2
//        )
//        tableAdapter.tableList = a

        refresh()
    }

    override fun genericTable() {
        mysTable = jsonToModels<ArenasTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            getPage()
//            tableLists += generateItems1(CourseTable::class, mysTable!!.rows)
//            tableAdapter.setMyTableList(tableLists)
            for (i in 0..5) {
                youItems.add(YouItem(i.toString()))
            }
            youAdapter.items = youItems
            runOnUiThread {
                youAdapter.notifyDataSetChanged()
//                tableAdapter.notifyDataSetChanged()
            }
        }
    }

}

data class YouItem(val name: String)

class YouAdapter: RecyclerView.Adapter<YouAdapter.ViewHolder>() {

    var items: ArrayList<YouItem> = arrayListOf()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.name

        fun bind(item: YouItem) {
            name.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val example = inflater.inflate(R.layout.you, parent, false)
        return ViewHolder(example)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}



class ArenaAdapter: RecyclerView.Adapter<ArenaViewHolder>() {

    var tableList: ArrayList<ArenaTable> = arrayListOf()
//class ArenaAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<ArenaViewHolder>(resource, ::ArenaViewHolder, list1CellDelegate) {

    override fun getItemCount(): Int {
        return tableList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArenaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.arena_list_cell, parent, false)

        return ArenaViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ArenaViewHolder, position: Int) {
        holder.bind(tableList[position])
    }


}

class ArenaViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView? = viewHolder.findViewById(R.id.titleLbl)

    fun bind(row: ArenaTable) {

        titleLbl!!.text = row.title

    }
}

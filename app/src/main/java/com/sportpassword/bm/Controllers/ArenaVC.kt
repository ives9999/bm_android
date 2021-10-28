package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Fragments.MyViewHolder
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_arena_vc.*
import kotlinx.android.synthetic.main.arena_list_cell.view.*
import kotlinx.android.synthetic.main.bottom_view.*
import kotlinx.android.synthetic.main.top_view.*
import org.jetbrains.anko.backgroundColor

class ArenaVC : AppCompatActivity() {

    var mysTable: ArenasTable? = null
    lateinit var tableAdapter: ArenaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_arena_vc)
        val myColorGreen = ContextCompat.getColor(this, R.color.MY_GREEN)
        arenaTabLine.backgroundColor = myColorGreen
        topTitleLbl.setText("課程")

        tableAdapter = ArenaAdapter()
        val a1: ArenaTable = ArenaTable()
        a1.title = "aaa"
        val a2: ArenaTable = ArenaTable()
        a2.title = "bbb"
        val a: ArrayList<ArenaTable> = arrayListOf(
            a1, a2
        )
        tableAdapter.tableList = a

        //refresh()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        isSearchIconShow = true
//        super.onCreateOptionsMenu(menu)
//
//        return true
//    }


//    override fun generateItems(): ArrayList<Item> {
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                //row.print()
//                row.filterRow()
//                val myItem = ArenaItem(this, row)
//                //val coachItem = CoachItem(this, row)
//                myItem.list1CellDelegate = this
//                items.add(myItem)
//            }
//        }
//
//        return items
//    }



//    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {
//
//        val arenaItem = item as ArenaItem
//        val table = arenaItem.row
//        //superCourse.print()
//        toShowArena(table.token)
//    }


}

class ArenaAdapter(): RecyclerView.Adapter<ArenaViewHolder>() {

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

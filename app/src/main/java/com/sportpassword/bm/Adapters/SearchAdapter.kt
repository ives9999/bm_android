package com.sportpassword.bm.Adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage

class SearchSectionAdapter(val context: Context, private val resource: Int, var delegate: List1CellDelegate): RecyclerView.Adapter<SearchSectionViewHolder>() {
    private var searchSections: ArrayList<SearchSection> = arrayListOf()
    //lateinit var adapter: TeamSearchItemAdapter

    fun setSearchSection(searchSections: ArrayList<SearchSection>) {
        this.searchSections = searchSections
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(resource, parent, false)

        return SearchSectionViewHolder(viewHolder)

    }

    override fun onBindViewHolder(holder: SearchSectionViewHolder, position: Int) {
        val section: SearchSection = searchSections[position]
        holder.titleLbl.text = section.title

        var iconID: Int = 0
        if (section.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)

        val adapter =
            SearchItemAdapter(context, position, searchSections[position], delegate)
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        holder.greater.setOnClickListener {
            delegate.handleSearchSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return searchSections.size
    }
}

class SearchSectionViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView = viewHolder.findViewById(R.id.titleLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class SearchItemAdapter(val context: Context, private val sectionIdx: Int, private val searchSection: SearchSection, var delegate: List1CellDelegate): RecyclerView.Adapter<SearchItemViewHolder>() {

    var searchRows: ArrayList<SearchRow> = searchSection.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.search_row_item, parent, false)

        return SearchItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {

        val row: SearchRow = searchRows[position]
        holder.title.text = row.title
        holder.show.text = row.show

        val cell = row.cell
        if (cell == "textField") {
            holder.show.visibility = View.INVISIBLE
            holder.greater.visibility = View.INVISIBLE
            holder.keyword.visibility = View.VISIBLE
            if (row.show.isNotEmpty()) {
                holder.keyword.setText(row.show)
            }
            holder.keyword.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    delegate.cellTextChanged(sectionIdx, position, p0.toString())
                }
            })
        } else if (cell == "switch") {
            holder.show.visibility = View.INVISIBLE
            holder.greater.visibility = View.INVISIBLE
            holder.keyword.visibility = View.INVISIBLE
            holder.switch.visibility = View.VISIBLE
            holder.clear.visibility = View.INVISIBLE

            holder.switch.isChecked = row.value == "1"
        }

        holder.viewHolder.setOnClickListener {
            delegate.cellClick(sectionIdx, position)
        }

        holder.switch.setOnCheckedChangeListener { compoundButton, b ->
            delegate.cellSwitchChanged(sectionIdx, position, b)
        }

        holder.clear.setOnClickListener {
            delegate.cellClear(sectionIdx, position)
        }
    }

    override fun getItemCount(): Int {
        if (searchSection.isExpanded) {
            return searchRows.size
        } else {
            return 0
        }
    }

}

class SearchItemViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var title: TextView = viewHolder.findViewById(R.id.row_title)
    var show: TextView = viewHolder.findViewById(R.id.row_detail)
    var clear: ImageView = viewHolder.findViewById(R.id.clearBtn)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var keyword: EditText = viewHolder.findViewById(R.id.keywordTxt)
    var switch: SwitchCompat = viewHolder.findViewById(R.id.search_switch)
}

//interface inter {
//    fun prepare(section: Int, row: Int)
//}
//
//class SearchAdapter(): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
//
//    var data: ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> = arrayListOf()
//    lateinit var rowAdatper: SearchRowAdatper
//    var collapse: Boolean = true
//
//    var delegate: inter? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.search_section_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val section = data[position]
//        holder.titleView.text = section.keys.elementAt(0)
//
//        holder.rv.layoutManager = LinearLayoutManager(holder.rv.context, LinearLayout.VERTICAL, false)
//        rowAdatper = SearchRowAdatper(position) { row ->
//            delegate!!.prepare(position, row)
//        }
//        if (position == 0) {
//            rowAdatper.rows = section.values.elementAt(0)
//            holder.sectionView.visibility = View.GONE
//        } else {
//            //rowAdatper.rows = arrayListOf()
//            rowAdatper.rows = section.values.elementAt(0)
//        }
//        holder.rv.adapter = rowAdatper
//
//        holder.bind(position)
//    }
//
//
//    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//
//        var titleView = itemView.findViewById<TextView>(R.id.section_title)
//        var collapseView = itemView.findViewById<ImageView>(R.id.collapse)
//        var sectionView = itemView.findViewById<ConstraintLayout>(R.id.section_container)
//        var rv = itemView.findViewById<RecyclerView>(R.id.row_container)
//
//        fun bind(position: Int) {
//
//            itemView.setOnClickListener{
//                if (!collapse) {
//                    rowAdatper.rows = arrayListOf()
//                    collapseView.setImage("to_right")
//                } else {
//                    val section = data[position]
//                    rowAdatper.rows = section.values.elementAt(0)
//                    collapseView.setImage("to_down")
//                }
////                rowAdatper.collapsed()
//                rowAdatper.notifyDataSetChanged()
//                collapse = !collapse
//            }
//        }
//    }
//}
package com.sportpassword.bm.Controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.PERPAGE
import com.sportpassword.bm.Utilities.jsonToModels2
import com.sportpassword.bm.Utilities.setInfo
import com.sportpassword.bm.Views.EndlessRecyclerViewScrollListener
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.cell_section.*
import kotlinx.android.synthetic.main.mask.*
import org.jetbrains.anko.backgroundColor
import java.lang.reflect.Type

typealias viewHolder<T, U, V> = (Context, View, V)-> T
typealias selectedClosure<U> = ((U) -> Boolean)?
typealias getDataFromServerClosure = (Int) -> Unit

class MyTable2VC<T: MyViewHolder2<U, V>, U: Table, V: BaseActivity>(
    private val recyclerView: RecyclerView,
    private val refreshLayout: SwipeRefreshLayout? = null,
    cellResource: Int,
    viewHolderConstructor: viewHolder<T, U, V>,
    private val tableType: Type,
    private val selected: selectedClosure<U>,
    private val getDataFromServer: getDataFromServerClosure,
    private val delegate: V
) {

    //var recyclerView: RecyclerView
    val adapter: MyAdapter2<T, U, V>
    var msg: String = ""
    val rows: ArrayList<U> = arrayListOf()

    var page: Int = 1
    var perPage: Int = PERPAGE
    var totalCount: Int = 0
    var totalPage: Int = 0

    var scrollListener: ScrollListener? = null

    init {
        //recyclerView = findViewById<RecyclerView>(resource)
        adapter = MyAdapter2<T, U, V>(cellResource, viewHolderConstructor, delegate)
        recyclerView.adapter = adapter

        val refreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            delegate.refresh()
        }
        refreshLayout?.setOnRefreshListener(refreshListener)
    }

    fun getDataFromServer(page: Int) {
        this.page = page
        if (page == 1) {
            rows.clear()
        }
        if (page == totalPage) {
            if (scrollListener != null) {
                recyclerView.removeOnScrollListener(scrollListener!!)
            }
        }

        getDataFromServer.invoke(page)
    }

    fun parseJSON(jsonString: String): Boolean {
        var res: Boolean = true
        val rows = genericTable2(jsonString)
        if (rows.size == 0) {
            res = false
        } else {
            this.rows.addAll(rows)
            setItems()
        }
        refreshLayout?.isRefreshing = false
        notifyDataSetChanged()

        return res
    }

    private fun genericTable2(jsonString: String): ArrayList<U> {

        val rows: ArrayList<U> = arrayListOf()
        val tables2: Tables2<U>? = jsonToModels2<Tables2<U>, U>(jsonString, tableType)

        if (tables2 == null) {
            msg = "無法從伺服器取得正確的json資料，請洽管理員"
        } else {
            if (tables2.success) {
                if (tables2.rows.size > 0) {

                    for ((idx, row) in tables2.rows.withIndex()) {
                        row.filterRow()

                        row.no = idx + 1 + (page - 1)*perPage

                        selected ?. let {
                            it(row)
                        } .let {
                            row.selected = it!!
                        }
                    }

                    if (page == 1) {
                        page = tables2.page
                        perPage = tables2.perPage
                        totalCount = tables2.totalCount
                        val _totalPage: Int = totalCount / perPage
                        totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage

                        if (totalPage > 1) {
                            val layoutManager = LinearLayoutManager(delegate)
                            recyclerView.layoutManager = layoutManager
                            scrollListener = ScrollListener(layoutManager)
                            recyclerView.addOnScrollListener(scrollListener!!)
                        }
                    }

                    rows.addAll(tables2.rows)
                }
            } else {
                msg = "解析JSON字串時，沒有成功，系統傳回值錯誤，請洽管理員"
            }
        }

        return rows
    }

    fun setItems() {
        adapter.items = this.rows
    }

    fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    inner class ScrollListener(recyelerViewLinearLayoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener(recyelerViewLinearLayoutManager) {

        //page: 目前在第幾頁
        //totalItemCount: 已經下載幾頁
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            //page已經+1了
            getDataFromServer(page)
        }
    }
}

open class MyAdapter2<T: MyViewHolder2<U, V>, U: Table, V: BaseActivity> (
    private val resource: Int,
    private val viewHolderConstructor: (Context, View, V)-> T,
    private val delegate: V
) : RecyclerView.Adapter<T>() {

    var items: ArrayList<U> = arrayListOf()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        val row: U = items[position]

        holder.bind(row, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(resource, parent, false)
        //return T(parent.context, viewHolder, list1CellDelegate)
        return viewHolderConstructor(parent.context, view, delegate)
    }
}

open class MyViewHolder2<U: Table, V: BaseActivity>(
    val context: Context,
    val view: View,
    val delegate: V
) : RecyclerView.ViewHolder(view) {

    open fun bind(row: U, idx: Int) {

        view.setOnClickListener {
            //didSelect?.let { it1 -> it1(row, idx) }
            //list2CellDelegate?.cellClick(row)
            delegate.cellClick(row)
        }

        var color: Int = ContextCompat.getColor(context, R.color.MY_BLACK)
        if (row.selected) {
            color = ContextCompat.getColor(context, R.color.CELL_SELECTED)
        }
        view.backgroundColor = color
    }
}

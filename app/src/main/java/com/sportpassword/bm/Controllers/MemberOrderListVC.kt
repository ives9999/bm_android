package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.BASE_URL
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.Utilities.jsonToModels
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_member_order_list_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.order_list_cell.*

class MemberOrderListVC : MyTableVC1() {

    var mysTable: OrdersTable? = null
    //protected lateinit var superModels: SuperModel

    //取代superDataLists(define in BaseActivity)，放置所有拿到的SuperModel，分頁時會使用到
    var allSuperModels: ArrayList<SuperOrder> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_order_list_vc)

        setMyTitle("會員訂單")

        dataService = OrderService
        recyclerView = order_list
        refreshLayout = order_list_refresh
        maskView = mask
        setRefreshListener()
        setRecyclerViewScrollListener()

        initAdapter()
        perPage = 10
        refresh()
    }

    override fun refresh() {
//        page = 1
//        theFirstTime = true
//        getDataStart(page, perPage)

        page = 1
        theFirstTime = true
        adapter.clear()
        items.clear()
        getDataStart1(page, perPage, member.token)
        params.clear()
    }

    override fun genericTable() {
        mysTable = jsonToModels<OrdersTable>(dataService.jsonString)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                row.filterRow()
                val orderItem = OrderItem(this, row)
                //productItem.list1CellDelegate = this
                items.add(orderItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val orderItem = item as OrderItem
        val table = orderItem.row
        toPayment(table.token)
    }

    //分頁時使用，當往下移動到第n-1筆時，就向server取得下一頁的筆數
//    override fun setRecyclerViewScrollListener() {
//
//        var pos: Int = 0
//
//        scrollerListenr = object: RecyclerView.OnScrollListener() {
//
//        }
//
//        scrollerListenr = object: RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val layoutManager = recyclerView.layoutManager as GridLayoutManager
//                if (allSuperModels.size < totalCount) {
//                    pos = layoutManager.findLastVisibleItemPosition()
//                    //println("pos:${pos}")
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                //println("allSuperModels.size:${allSuperModels.size}")
//                //pos表示目前顯示到第幾筆
//                if (allSuperModels.size == pos + 1 &&
//                        newState == RecyclerView.SCROLL_STATE_IDLE &&
//                        allSuperModels.size < totalCount &&
//                        !loading) {
//                    getDataStart(page, perPage)
//                }
//            }
//        }
//        recyclerView.addOnScrollListener(scrollerListenr)
//    }
}

class OrderItem(val context: Context, val row: OrderTable): Item() {

    //var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.nameLbl.text = row.product!!.name
        viewHolder.dateLbl.text = row.created_at_show
        viewHolder.priceLbl.text = row.amount_show
        viewHolder.orderNoLbl.text = row.order_no
        viewHolder.noLbl.text = (position+1).toString()
    }

    override fun getLayout() = R.layout.order_list_cell
}



















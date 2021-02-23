package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Models.SuperOrder
import com.sportpassword.bm.Models.SuperOrders
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.BASE_URL
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_member_order_list_vc.*
import kotlinx.android.synthetic.main.list1_cell.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.order_list_cell.*

class MemberOrderListVC : MyTableVC1() {

    var superOrders: SuperOrders? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_order_list_vc)

        source_activity = "order"
        setMyTitle("會員訂單")

        dataService = OrderService
        recyclerView = order_list
        refreshLayout = order_list_refresh
        maskView = mask
        setRefreshListener()
        setRecyclerViewScrollListener()

        initAdapter()
        perPage = 20
        refresh()
    }

    override fun refresh() {
        page = 1
        theFirstTime = true
        getDataStart(page, perPage)
        params.clear()
    }

    override fun initAdapter(include_section: Boolean) {
        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }
        val items = generateItems()
        adapter.addAll(items)
        recyclerView.adapter = adapter
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        Loading.show(maskView)
        loading = true

        dataService.getList(this, member.token, params, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {
                superOrders = dataService.superModel as SuperOrders
                if (superOrders != null) {
                    page = superOrders!!.page
                    perPage = superOrders!!.perPage
                    totalCount = superOrders!!.totalCount
                    val _totalPage: Int = totalCount / perPage
                    totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                    theFirstTime = false

                    for ((idx, superOrder) in superOrders!!.rows.withIndex()) {
                        superOrder.filter()
                    }
                    val items = generateItems()
//                    println(items);
                    adapter.update(items)
                    adapter.notifyDataSetChanged()
                }
            }

            page++
        } else {
            warning(dataService.msg)
        }
//        mask?.let { mask?.dismiss() }
        Loading.hide(maskView)
        loading = false
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (superOrders != null) {
            for (row in superOrders!!.rows) {
//                row.print()
                val orderItem = OrderItem(this, row)
                //productItem.list1CellDelegate = this
                items.add(orderItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val productItem = item as ProductItem
        val superProduct = productItem.row
        //superCourse.print()
        goShowProduct(superProduct.token, superProduct.name)
    }

    override fun setRecyclerViewScrollListener() {

        var pos: Int = 0

        scrollerListenr = object: RecyclerView.OnScrollListener() {

        }

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (superDataLists.size < totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                    println("pos:${pos}")
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                println("superDataLists.size:${superDataLists.size}")
                if (superDataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && superDataLists.size < totalCount && !loading) {
                    getDataStart(page, perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }
}

class OrderItem(val context: Context, val row: SuperOrder): Item() {

    //var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.nameLbl.text = row.product.name
        viewHolder.dateLbl.text = row.created_at_show
        viewHolder.priceLbl.text = row.amount_show
        viewHolder.orderNoLbl.text = row.order_no
        viewHolder.noLbl.text = (position+1).toString()
    }

    override fun getLayout() = R.layout.order_list_cell
}



















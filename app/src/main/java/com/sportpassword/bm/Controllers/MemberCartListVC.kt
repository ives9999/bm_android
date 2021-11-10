package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sportpassword.bm.Adapters.MemberCartAdapter
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Utilities.jsonToModels
import com.sportpassword.bm.member
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_member_cart_list_vc.*
import kotlinx.android.synthetic.main.cart_list_cell.view.*
import kotlinx.android.synthetic.main.cart_list_cell.view.listFeatured
import kotlinx.android.synthetic.main.cart_list_cell.view.titleLbl

class MemberCartListVC : MyTableVC() {

    var mysTable: CartsTable? = null
    lateinit var tableAdapter: MemberCartAdapter

    var myTable: CartTable? = null
    var cartItemsTable: ArrayList<CartItemTable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = CartService

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_cart_list_vc)

        setMyTitle("購物車")

        recyclerView = cart_list
        refreshLayout = cart_refresh

//        initAdapter()
        tableAdapter = MemberCartAdapter(R.layout.cart_list_cell, this)
        recyclerView.adapter = tableAdapter

        init()
        refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        isSearchIconShow = false
//        super.onCreateOptionsMenu(menu)
//        return true
//    }

    override fun refresh() {

        page = 1
        theFirstTime = true
        //adapter.clear()
        //items.clear()
        tableLists.clear()
        getDataStart(page, perPage, member.token)
    }

    override fun genericTable() {
        mysTable = jsonToModels<CartsTable>(dataService.jsonString)
        tables = mysTable
        if (mysTable != null) {
            if (mysTable!!.rows.size > 0) {
                myTable = mysTable!!.rows[0]
                cartItemsTable = myTable!!.items

                getPage()
                tableLists += generateItems1(CartItemTable::class, myTable!!.items)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
                    submitBtn.visibility = (cartItemsTable.size > 0) then { View.VISIBLE } ?: View.GONE
                    tableAdapter.notifyDataSetChanged()
                }
            } else {
                runOnUiThread {
                    submitBtn.visibility = View.GONE
                }
            }
        }
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        for (row in cartItemsTable) {
//            row.filterRow()
//            val cartItemItem = CartItemItem(this, row)
//            cartItemItem.list1CellDelegate = this
//            items.add(cartItemItem)
//        }
//
//        return items
//    }

    override fun cellEdit(row: Table) {
        toAddCart(null, row.token, this)
    }

    override fun cellDelete(row: Table) {
        warning("是否確定要刪除呢？", "取消", "刪除") {
            dataService.delete(this, "cart_item", row.token) { success ->
                if (success) {
                    refresh()

                    //是否要顯示購物車的圖示在top
                    cartItemCount -= 1
                    session.edit().putInt("cartItemCount", cartItemCount).apply()
                } else {
                    warning(dataService.msg)
                }
            }
        }
    }

//    override fun rowClick(
//        item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>,
//        view: View
//    ) {
//        val cartItemItem = item as CartItemItem
//        val row: CartItemTable = cartItemItem._row
//        toShowProduct(row.product!!.token)
//    }

    fun submitBtnPressed(view: View) {
        toOrder()
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}

//class CartItemItem(override var context: Context, var _row: CartItemTable): ListItem<Table>(context, _row) {
//
//    constructor(context: Context, sectionKey: String, rowKey: String, title: String, featured_path: String, attribute: String, amount: String, quantity: String, list1CellDelegate: List1CellDelegate?=null): this(context, CartItemTable()) {
//        this.sectionKey = sectionKey
//        this.rowKey = rowKey
//        this.title = title
//        this.featured_path = featured_path
//        this.attribute = attribute
//        this.amount = amount
//        this.quantity = quantity
//    }
//    var sectionKey: String = ""
//    var rowKey: String = ""
//    var title: String = ""
//    var featured_path: String = ""
//    var attribute: String = ""
//    var amount: String = ""
//    var quantity: String = ""
//
//    init {
//        if (_row.cart_id > 0) {
//            val row: CartItemTable = _row
//
//            if (row.product != null && row.product!!.featured_path.isNotEmpty()) {
//                featured_path = row.product!!.featured_path
//            }
//
//            if (row.product != null) {
//                title = row.product!!.name
//            }
//
//            var attribute_text: String = ""
//            if (row.attributes.size > 0) {
//
//                for ((idx, attribute) in row.attributes.withIndex()) {
//                    attribute_text += attribute["name"]!! + ":" + attribute["value"]!!
//                    if (idx < row.attributes.size - 1) {
//                        attribute_text += " | "
//                    }
//                }
//            }
//            attribute = attribute_text
//            amount = row.amount_show
//            quantity = row.quantity.toString()
//        }
//    }
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//
//        //super.bind(viewHolder, position)
//
//        Picasso.with(context)
//            .load(featured_path)
//            .placeholder(R.drawable.loading_square_120)
//            .error(R.drawable.loading_square_120)
//            .into(viewHolder.listFeatured)
//
//        viewHolder.titleLbl.text = title
//        viewHolder.attributeLbl.text = attribute
//
//        viewHolder.amountLbl.text = amount
//        viewHolder.quantityLbl.text = "數量：${quantity}"
//
//        if (list1CellDelegate != null) {
//            viewHolder.editIcon.setOnClickListener {
//
//                list1CellDelegate!!.cellEdit(row)
//            }
//
//            viewHolder.deleteIcon.setOnClickListener {
//
//                list1CellDelegate!!.cellDelete(row)
//            }
//        } else {
//            viewHolder.iconView.visibility = View.GONE
//        }
//    }
//
//    override fun getLayout() = R.layout.cart_list_cell
//}
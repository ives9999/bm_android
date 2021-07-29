package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Fragments.ListItem
import com.sportpassword.bm.Models.CartItemTable
import com.sportpassword.bm.Models.CartTable
import com.sportpassword.bm.Models.CartsTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Utilities.jsonToModels
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_member_cart_list_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.cart_list_cell.*
import kotlinx.android.synthetic.main.cart_list_cell.listFeatured
import kotlinx.android.synthetic.main.cart_list_cell.titleLbl

class MemberCartListVC : MyTableVC() {

    var mysTable: CartsTable? = null

    var myTable: CartTable? = null
    var cartItemTable: ArrayList<CartTable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = CartService

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_cart_list_vc)

        setMyTitle("購物車")

        recyclerView = cart_list
        refreshLayout = cart_list_refresh
        maskView = mask
        setRefreshListener()
        setRecyclerViewScrollListener()

        initAdapter()
        perPage = 10
        refresh()
    }

    override fun refresh() {

        page = 1
        page = 1
        theFirstTime = true
        adapter.clear()
        items.clear()
        getDataStart1(page, perPage, member.token)
    }

    override fun genericTable() {
        mysTable = jsonToModels<CartsTable>(dataService.jsonString)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (mysTable != null) {
            if (mysTable!!.rows.size > 0) {
                val cartTable: CartTable = mysTable!!.rows[0]
                val rows: ArrayList<CartItemTable> = cartTable.items
                for (row in rows) {
                    val cartItemItem = CartItemItem(this, row)
                    cartItemItem.list1CellDelegate = this
                    items.add(cartItemItem)
                }
            }
        }

        return items
    }

    override fun cellEdit(row: Table) {
        toAddCart(row.token)
    }

    override fun cellDelete(row: Table) {

    }

    fun submitBtnPressed(view: View) {

    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}

class CartItemItem(override var context: Context, var _row: CartItemTable): ListItem<Table>(context, _row) {


    override fun bind(viewHolder: ViewHolder, position: Int) {

        super.bind(viewHolder, position)

        val row: CartItemTable = _row
        row.filterRow()

        if (row.product != null && row.product!!.featured_path.isNotEmpty()) {
            Picasso.with(context)
                .load(row.product!!.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)
        }
        if (row.product != null) {
            viewHolder.titleLbl.text = row.product!!.name
        }

        var attribute_text: String = ""
        if (row.attributes.size > 0) {

            for ((idx, attribute) in row.attributes.withIndex()) {
                attribute_text += attribute["name"]!! + ":" + attribute["value"]!!
                if (idx < row.attributes.size - 1) {
                    attribute_text += " | "
                }
            }
        }
        viewHolder.attributeLbl.text = attribute_text

        viewHolder.amountLbl.text = row.amount_show
        viewHolder.quantityLbl.text = "數量：${row.quantity}"

        viewHolder.editIcon.setOnClickListener {

            if (list1CellDelegate != null) {

                list1CellDelegate!!.cellEdit(row)
            }
        }

        viewHolder.deleteIcon.setOnClickListener {

            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellDelete(row)
            }
        }
    }

    override fun getLayout() = R.layout.cart_list_cell
}
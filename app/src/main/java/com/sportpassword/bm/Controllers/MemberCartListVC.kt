package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sportpassword.bm.Fragments.MyAdapter
import com.sportpassword.bm.Fragments.MyViewHolder
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Utilities.jsonToModels
import com.sportpassword.bm.member
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

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        isSearchIconShow = false
        super.onCreateOptionsMenu(menu)
        return true
    }

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
                if (cartItemsTable.size > 0) {
                    submitBtn.visibility = View.VISIBLE
                } else {
                    submitBtn.visibility = View.GONE
                }
                getPage()
                tableLists += generateItems1(CartItemTable::class, myTable!!.items)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
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

class MemberCartAdapter(resource: Int, list1CellDelegate: List1CellDelegate?): MyAdapter<MemberCartViewHolder>(resource, ::MemberCartViewHolder, list1CellDelegate) {

    override fun onBindViewHolder(holder: MemberCartViewHolder, position: Int) {

        val row: CartItemTable = tableList[position] as CartItemTable

        holder.bind(row, position)
    }
}

class MemberCartViewHolder(context: Context, viewHolder: View, list1CellDelegate: List1CellDelegate? = null): MyViewHolder(context, viewHolder, list1CellDelegate) {

    val title: TextView = viewHolder.titleLbl
    val featured_path: ImageView = viewHolder.listFeatured
    val attribute: TextView = viewHolder.attributeLbl
    val amount: TextView = viewHolder.amountLbl
    val quantity: TextView = viewHolder.quantityLbl

    //_row is cartTable
    override fun bind(row: Table, idx: Int) {

        val _row: CartItemTable = row as CartItemTable
        _row.filterRow()
        //super.bind(_row, idx)
        val productTable: ProductTable = _row.product!!
        titleLbl?.text = productTable.name
        if (productTable.featured_path.isNotEmpty()) {
            Picasso.with(context)
                .load(productTable.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(listFeatured)
        }
        var attribute_text: String = ""
        if (_row.attributes.size > 0) {

            for ((idx, attribute) in _row.attributes.withIndex()) {
                attribute_text += attribute["name"]!! + ":" + attribute["value"]!!
                if (idx < _row.attributes.size - 1) {
                    attribute_text += " | "
                }
            }
        }
        attribute.text = attribute_text
        amount.text = row.amount_show
        quantity.text = "數量：${row.quantity}"

        if (list1CellDelegate != null) {
            refreshIcon?.setOnClickListener {
                list1CellDelegate.cellRefresh()
            }

            viewHolder.editIcon.setOnClickListener {
                list1CellDelegate.cellEdit(row)
            }

            viewHolder.deleteIcon.setOnClickListener {
                list1CellDelegate.cellDelete(row)
            }
        } else {
            viewHolder.iconView.visibility = View.GONE
        }
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
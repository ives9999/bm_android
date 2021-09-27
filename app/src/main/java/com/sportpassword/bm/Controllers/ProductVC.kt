package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.sportpassword.bm.Fragments.ListItem
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_product_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.product_list_cell.*
import kotlinx.android.synthetic.main.product_list_cell.buyBtn
import kotlinx.android.synthetic.main.product_list_cell.listFeatured
import kotlinx.android.synthetic.main.product_list_cell.priceLbl
import kotlinx.android.synthetic.main.product_list_cell.refreshIcon
import kotlinx.android.synthetic.main.tab_course.*

class ProductVC : MyTableVC() {

    var mysTable: ProductsTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "標題關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_vc)

//        source_activity = "product"
//        val title_field = intent.getStringExtra("titleField")
        able_type = "product"
        setMyTitle("商店")

        dataService = ProductService
        recyclerView = product_list
        refreshLayout = product_refresh
        maskView = mask

        //initAdapter()
        adapter = GroupAdapter()
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
        recyclerView.adapter = adapter

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        isSearchIconShow = true
        super.onCreateOptionsMenu(menu)

        return true
    }

    override fun genericTable() {
        mysTable = jsonToModels<ProductsTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
        }
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (mysTable != null) {
            for (row in mysTable!!.rows) {
                row.filterRow()
                val productItem = ProductItem(this, row)
                productItem.list1CellDelegate = this
                items.add(productItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {

        val productItem = item as ProductItem
        val myTable = productItem.row
        //superCourse.print()
        toShowProduct(myTable.token)
    }
}

class ProductItem(override var context: Context, var _row: ProductTable): ListItem<Table>(context, _row) {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {


        super.bind(viewHolder, position)

        val row: ProductTable = _row
        viewHolder.buyBtn.setOnClickListener {
            val a: ProductVC = context as ProductVC
            a.toAddCart(row.token)
        }

        if (row.prices.size > 0) {
            val tmp: String = (row.prices[0].price_member).formattedWithSeparator()
            val price: String = "NT$ ${tmp}"
            viewHolder.priceLbl.text = price
        } else {
            viewHolder.priceLbl.text = "未提供"
        }
    }

    override fun getLayout() = R.layout.product_list_cell
}
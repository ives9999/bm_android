package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Fragments.ListItem
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_product_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.product_list_cell.*
import kotlinx.android.synthetic.main.product_list_cell.buyBtn
import kotlinx.android.synthetic.main.product_list_cell.listFeatured
import kotlinx.android.synthetic.main.product_list_cell.priceLbl
import kotlinx.android.synthetic.main.product_list_cell.refreshIcon

class ProductVC : MyTableVC1() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
    )

    var productsTable: ProductsTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_vc)

        source_activity = "product"
        val title_field = intent.getStringExtra("titleField")
        setMyTitle("商店")

        dataService = ProductService
        searchRows = _searchRows
        recyclerView = product_list
        refreshLayout = product_refresh
        maskView = mask
        setRefreshListener()

        initAdapter()
        refresh()
    }

    override fun genericTable() {
        productsTable = jsonToModels<ProductsTable>(dataService.jsonString)
        if (productsTable != null) {
            tables = productsTable
        }
    }

    override fun prepareParams(city_type: String) {
        params.clear()
        if (keyword.length > 0) {
            val row = getSearchRow(KEYWORD_KEY)
            if (row != null && row.containsKey("value")) {
                row["value"] = keyword
                updateSearchRow(KEYWORD_KEY, row)
            }
        }
        for (searchRow in _searchRows) {
            var value_type: String? = null
            if (searchRow.containsKey("value_type")) {
                value_type = searchRow.get("value_type")
            }
            var value: String = ""
            if (searchRow.containsKey("value")) {
                value = searchRow.get("value")!!
            }
            var key: String? = null
            if (searchRow.containsKey("key")) {
                key = searchRow.get("key")!!
            }
            if (value_type != null && key != null && value.isNotEmpty()) {
                var values: Array<String>? = null
                if (value_type == "String") {
                    params[key] = value
                } else if (value_type == "Array") {
                    value = searchRow.get("value")!!
                    values = value.split(",").toTypedArray()
                }
                if (values != null) {
                    params[key] = values
                }
            }
        }
    }

    fun updateSearchRow(idx: Int, row: HashMap<String, String>) {
        _searchRows[idx] = row
    }

    fun updateSearchRow(key: String, row: HashMap<String, String>) {
        var idx: Int = -1
        for ((i, searchRow) in _searchRows.withIndex()) {
            if (searchRow.containsKey("key")) {
                if (key == searchRow.get("key")) {
                    idx = i
                    break
                }
            }
        }
        if (idx >= 0) {
            _searchRows[idx] = row
        }
    }

    override fun remove(indexPath: IndexPath) {
        var row: HashMap<String, String>? = null
        if (_searchRows.size >= indexPath.row) {
            row = _searchRows[indexPath.row]
        }
        var key: String? = null
        if (row != null && row.containsKey("key") && row.get("key")!!.isNotEmpty()) {
            key = row.get("key")
        }
        if (row != null) {
            row["value"] = ""
            row["show"] = "不限"
            updateSearchRow(indexPath.row, row)
        }
    }

    fun getSearchRow(key: String): HashMap<String, String>? {
        var row: HashMap<String, String>? = null
        for ((i, searchRow) in _searchRows.withIndex()) {
            if (searchRow.containsKey("key")) {
                if (key == searchRow.get("key")) {
                    row = searchRow
                    break
                }
            }
        }

        return row
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (productsTable != null) {
            for (row in productsTable!!.rows) {
                row.filterRow()
                val productItem = ProductItem(this, row)
                productItem.list1CellDelegate = this
                items.add(productItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val productItem = item as ProductItem
        val superProduct = productItem.row
        //superCourse.print()
        toShowProduct(superProduct.token, superProduct.name)
    }
}

class ProductItem(override var context: Context, var _row: ProductTable): ListItem<Table>(context, _row) {

    override fun bind(viewHolder: ViewHolder, position: Int) {


        super.bind(viewHolder, position)

        val row: ProductTable = _row
        viewHolder.buyBtn.setOnClickListener {
            val a: ProductVC = context as ProductVC
            a.toOrder(row.token)
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
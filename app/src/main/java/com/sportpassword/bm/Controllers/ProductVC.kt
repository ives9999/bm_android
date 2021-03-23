package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Models.SuperProduct
import com.sportpassword.bm.Models.SuperProducts
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_product_vc.*
import kotlinx.android.synthetic.main.list1_cell.*
import kotlinx.android.synthetic.main.mask.*

class ProductVC : MyTableVC1() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
    )

    var superProducts: SuperProducts? = null

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

    override fun refresh() {
        page = 1
        theFirstTime = true
        getDataStart(page, perPage)
        searchRows = _searchRows
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

        dataService.getList(this, null, params, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {
                superProducts = dataService.superModel as SuperProducts
                if (superProducts != null) {
                    page = superProducts!!.page
                    perPage = superProducts!!.perPage
                    totalCount = superProducts!!.totalCount
                    val _totalPage: Int = totalCount / perPage
                    totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                    theFirstTime = false
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
        if (superProducts != null) {
            for (row in superProducts!!.rows) {
//                row.print()
                val productItem = ProductItem(this, row)
                //productItem.list1CellDelegate = this
                items.add(productItem)
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
}

class ProductItem(val context: Context, val row: SuperProduct): Item() {

    //var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //println(superStore);
        viewHolder.cityBtn.text = "購買"
        viewHolder.cityBtn.setOnClickListener {
            val a: ProductVC = context as ProductVC
            a.goOrder(row.token)
        }

        viewHolder.titleTxt.text = row.name
        Picasso.with(context)
                .load(BASE_URL + row.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)
        viewHolder.telTxt.text = "價格： " + row.prices[0].price_member + " 元"
        viewHolder.business_timeTxt.visibility = View.GONE
        viewHolder.addressTxt.visibility = View.GONE

        viewHolder.mapIcon.visibility = View.GONE
        viewHolder.telIcon.visibility = View.GONE
        viewHolder.mobileIcon.visibility = View.GONE
        viewHolder.refreshIcon.visibility = View.GONE
        viewHolder.iconView.visibility = View.GONE
    }

    override fun getLayout() = R.layout.list1_cell
}
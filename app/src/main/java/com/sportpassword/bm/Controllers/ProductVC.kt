package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.ProductAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_product_vc.*
import kotlinx.android.synthetic.main.mask.*

class ProductVC : MyTableVC() {

    var mysTable: ProductsTable? = null
    lateinit var tableAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

//        searchRows = arrayListOf(
//            hashMapOf("title" to "標題關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
//        )

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
//        adapter = GroupAdapter()
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
//        recyclerView.adapter = adapter
        tableAdapter = ProductAdapter(R.layout.product_list_cell, this)
        recyclerView.adapter = tableAdapter

        init()
        refresh()
    }

    override fun init() {
        isSearchIconShow = true
        isPrevIconShow = true
        super.init()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        isSearchIconShow = true
//        super.onCreateOptionsMenu(menu)
//
//        return true
//    }

    override fun genericTable() {
        mysTable = jsonToModels<ProductsTable>(jsonString!!)
        if (mysTable != null) {
            tables = mysTable
            if (mysTable!!.rows.count() > 0) {
                getPage()
                tableLists += generateItems1(ProductTable::class, mysTable!!.rows)
                tableAdapter.setMyTableList(tableLists)
                runOnUiThread {
                    tableAdapter.notifyDataSetChanged()
                }
            } else {
                val rootView: ViewGroup = getRootView()
                runOnUiThread {
                    rootView.setInfo(this, "目前暫無資料")
                }
            }
        }
    }

    override fun makeSection0Row(isExpanded: Boolean): OneSection {
        val rows: ArrayList<OneRow> = arrayListOf()
        val r1: OneRow = OneRow("關鍵字", "", "", KEYWORD_KEY, "textField")
        rows.add(r1)

        val s: OneSection = OneSection("一般", "general", isExpanded)
        s.items.addAll(rows)
        return s
    }

//    override fun generateItems(): ArrayList<Item> {
//        val items: ArrayList<Item> = arrayListOf()
//        if (mysTable != null) {
//            for (row in mysTable!!.rows) {
//                row.filterRow()
//                val productItem = ProductItem(this, row)
//                productItem.list1CellDelegate = this
//                items.add(productItem)
//            }
//        }
//
//        return items
//    }
//
//    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {
//
//        val productItem = item as ProductItem
//        val myTable = productItem.row
//        //superCourse.print()
//        toShowProduct(myTable.token)
//    }
}

//class ProductItem(override var context: Context, var _row: ProductTable): ListItem<Table>(context, _row) {
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//
//
//        super.bind(viewHolder, position)
//
//        val row: ProductTable = _row
//        viewHolder.buyBtn.setOnClickListener {
//            val a: ProductVC = context as ProductVC
//            a.toAddCart(row.token)
//        }
//
//        if (row.prices.size > 0) {
//            val tmp: String = (row.prices[0].price_member).formattedWithSeparator()
//            val price: String = "NT$ ${tmp}"
//            viewHolder.priceLbl.text = price
//        } else {
//            viewHolder.priceLbl.text = "未提供"
//        }
//    }
//
//    override fun getLayout() = R.layout.product_list_cell
//}
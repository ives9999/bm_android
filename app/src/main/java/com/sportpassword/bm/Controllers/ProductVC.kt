package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sportpassword.bm.Adapters.ProductAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityProductVcBinding

class ProductVC : MyTableVC() {

    private lateinit var binding: ActivityProductVcBinding
    private lateinit var view: ViewGroup

    var mysTable: ProductsTable? = null
    lateinit var tableAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

//        searchRows = arrayListOf(
//            hashMapOf("title" to "標題關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
//        )

        super.onCreate(savedInstanceState)

        binding = ActivityProductVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

//        source_activity = "product"
//        val title_field = intent.getStringExtra("titleField")
        able_type = "product"
        setMyTitle("商店")

        dataService = ProductService
        recyclerView = binding.productList
        refreshLayout = binding.productRefresh

        findViewById<FrameLayout>(R.id.mask) ?. let { mask ->
            maskView = mask
        }

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

    fun buyButtonPressed(row: ProductTable) {

        //toAddCart(row.token)
        val type: String = row.type
        if (type == "coin") {
            toOrder(row.token)
        } else {
            toAddCart(row.token)
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
}
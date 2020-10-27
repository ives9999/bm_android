package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Models.SuperStore
import com.sportpassword.bm.Models.SuperStores
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.StoreService
import com.sportpassword.bm.Utilities.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_list_item.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StoreVC : MyTableVC1() {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
            hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY)
    )

    var superStores: SuperStores? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        val title_field = intent.getStringExtra("titleField")
        setMyTitle("體育用品店")

        dataService = StoreService
        recyclerView = store_list
        refreshLayout = store_refresh
        maskView = mask
        setRefreshListener()

        initAdapter()
        refresh()
    }

    override fun initAdapter(include_section: Boolean) {
        adapter = GroupAdapter()
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
                superStores = dataService.superModel as SuperStores
                if (superStores != null) {
                    page = superStores!!.page
                    perPage = superStores!!.perPage
                    totalCount = superStores!!.totalCount
                    var _totalPage: Int = totalCount / perPage
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
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    override fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (superStores != null) {
            for (row in superStores!!.rows) {
                items.add(StoreItem(this, row))
            }
        }

        return items
    }
}

class StoreItem(val context: Context, val superStore: SuperStore): Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //println(superStore);
        viewHolder.listCityBtn.text = superStore.city
        viewHolder.title.text = superStore.name
        Picasso.with(context)
                .load(BASE_URL + superStore.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)
        viewHolder.listBallTxt.visibility = View.INVISIBLE
        viewHolder.listArenaTxt.text = superStore.address
        viewHolder.listDayTxt.text = superStore.tel_text
        viewHolder.listIntervalTxt.text = superStore.open_time_text+"~"+superStore.close_time_text
        viewHolder.marker.visibility = View.INVISIBLE
    }

    override fun getLayout() = R.layout.tab_list_item
}




























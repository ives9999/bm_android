package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Form.BaseForm
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.PERPAGE
import com.sportpassword.bm.Utilities.makeCall
import com.sportpassword.bm.member
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.mask.*

abstract class MyTableVC1 : BaseActivity(), List1CellDelegate {

    var tables: Tables? = null

    var sections: ArrayList<String> = arrayListOf()
    var rows: ArrayList<HashMap<String, String>> = arrayListOf()

    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected val adapterSections: ArrayList<Section> = arrayListOf()
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var listAdapter: ListAdapter

    protected var loading: Boolean = false
    protected lateinit var maskView: View

    protected lateinit var form: BaseForm

    protected var theFirstTime: Boolean = true
    protected var page: Int = 1
    protected var perPage: Int = PERPAGE
    protected var totalCount: Int = 0
    protected var totalPage: Int = 0

    //protected lateinit var superModels: SuperModel

    //取代superDataLists(define in BaseActivity)，放置所有拿到的SuperModel，分頁時會使用到
    //var <T> allSuperModels: ArrayList<T> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_manager, menu)
        val memuView = menu!!.findItem(R.id.menu_search_manager).actionView

        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)
        ManagerBtn.visibility = View.GONE

        searchBtn.tag = "store"

        return true
    }

    open fun initAdapter(include_section: Boolean=false) {
        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }
        if (include_section) {
            for (section in sections) {
                adapterSections.add(Section())
            }
            for ((idx, title) in sections.withIndex()) {
                val expandableGroup = ExpandableGroup(GroupSection(title), true)
                val items = generateItems(idx)
                adapterSections[idx].addAll(items)
                expandableGroup.add(adapterSections[idx])
                adapter.add(expandableGroup)
            }
        } else {
            val items = generateItems()
            //println(items.size)
            adapter.addAll(items)
        }
        recyclerView.adapter = adapter
    }

    override fun refresh() {
//        page = 1
//        theFirstTime = true
//        getDataStart(page, perPage)

        page = 1
        theFirstTime = true
        getDataStart1(page, perPage)
        params.clear()
    }

    open fun getDataStart1(_page: Int, _perPage: Int) {
        Loading.show(mask)

        dataService.getList1(this, null, params, _page, _perPage) { success ->
            getDataEnd1(success)
        }
    }

    open fun getDataEnd1(success: Boolean) {
        if (success) {
            if (theFirstTime) {

                if (dataService.jsonString.isNotEmpty()) {
                    //println(dataService.jsonString)
                    genericTable()

                    //superCourses = dataService.superModel as SuperCourses
                    if (tables != null) {
                        page = tables!!.page
                        perPage = tables!!.perPage
                        totalCount = tables!!.totalCount
                        val _totalPage: Int = totalCount / perPage
                        totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                        theFirstTime = false

                        val items = generateItems()
                        adapter.update(items)
                        adapter.notifyDataSetChanged()
                        page++
                    } else {
                        warning(Global.message)
                        Global.message = ""
                    }
                } else {
                    warning("沒有取得回傳的json字串，請洽管理員")
                }

            }

            //notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        Loading.hide(mask)
        loading = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    protected open fun getDataStart(_page: Int, _perPage: Int) {}

    protected open fun getDataEnd(success: Boolean) {}

    protected open fun notifyDataSetChanged() {
        if (page == 1) {
            superDataLists = arrayListOf()
        }
        superDataLists.addAll(dataService.superDataLists)
//        for (data in superDataLists) {
//            data.print()
//            println("===================")
//        }
        listAdapter.lists = superDataLists
        listAdapter.notifyDataSetChanged()
    }

    open fun notifyChanged(include_section: Boolean=false) {
        if (include_section) {
            for ((idx, _) in sections.withIndex()) {
                val items = generateItems(idx)
                adapterSections[idx].update(items)
            }
        } else {
            val items = generateItems()
            adapter.update(items)
        }
        adapter.notifyDataSetChanged()
    }

    open fun generateItems(): ArrayList<Item> {
        return arrayListOf()
    }

    open fun generateItems(section: Int): ArrayList<Item> {
        return arrayListOf()
    }

    open fun genericTable() {}
    open fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {}



    protected open fun setRecyclerViewScrollListener() {

        var pos: Int = 0

//        scrollerListenr = object: RecyclerView.OnScrollListener() {
//
//        }

        scrollerListenr = object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (superDataLists.size < totalCount) {
                    pos = layoutManager.findLastVisibleItemPosition()
                    //println("pos:${pos}")
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //println("superDataLists.size:${superDataLists.size}")
                if (superDataLists.size == pos + 1 && newState == RecyclerView.SCROLL_STATE_IDLE && superDataLists.size < totalCount && !loading) {
                    getDataStart(page, perPage)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollerListenr)
    }

    protected open fun setRecyclerViewRefreshListener() {
        refreshListener = SwipeRefreshLayout.OnRefreshListener {
            refresh()

            refreshLayout.isRefreshing = false
        }
        refreshLayout.setOnRefreshListener(refreshListener)
    }

    open fun getFormItemFromKey(key: String): FormItem? {

        var res: FormItem? = null
        for (formItem in form.formItems) {
            if (formItem.name == key) {
                res = formItem
                break
            }
        }

        return res
    }

    override fun cellRefresh() {
        refresh()
    }

    override fun cellLike(row: Table) {
        if (!member.isLoggedIn) {
            goLogin()
        } else {
            dataService.like(this, row.token, row.id)
        }
    }

    override fun cellShowMap(row: Table) {
        println(row.address)
//        val intent = Intent(this, MyMapVC::class.java)
//        var name: String = ""
//        if (row.name.isNotEmpty()) {
//            name = row.name
//        } else if (row.title.isNotEmpty()) {
//            name = row.title
//        }
//        intent.putExtra("title", name)
//        intent.putExtra("address", row.address)
//        startActivity(intent)
    }

    override fun cellMobile(row: Table) {
        if (row.tel_show.isNotEmpty()) {
            println(row.tel)
        } else if (row.mobile_show.isNotEmpty()) {
            println(row.mobile)
        }
    }

    override fun cellEdit(row: Table) {
//        if (storesTable != null && storesTable!!.totalCount > index) {
//            val row = storesTable!!.rows[index]
//
//        }
    }

    override fun cellDelete(row: Table) {
//        if (storesTable != null && storesTable!!.totalCount > index) {
//            val row = storesTable!!.rows[index]
//
//        }
    }
}

interface List1CellDelegate {
    fun cellRefresh(){}
    fun cellLike(row: Table){}
    fun cellShowMap(row: Table){}
    fun cellMobile(row: Table){}
    fun cellEdit(row: Table){}
    fun cellDelete(row: Table){}
}
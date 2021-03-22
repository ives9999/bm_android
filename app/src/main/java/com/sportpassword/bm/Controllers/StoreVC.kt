package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.sportpassword.bm.Models.SuperStore
import com.sportpassword.bm.Models.SuperStores
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.StoreService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_store_vc.*
import kotlinx.android.synthetic.main.list1_cell.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_list_item.*
import kotlinx.android.synthetic.main.tab_list_item.listFeatured
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.reflect.jvm.internal.impl.util.MemberKindCheck

class StoreVC : MyTableVC1(), List1CellDelegate {

    val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to ""),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
    )

    var superStores: SuperStores? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_vc)

        source_activity = "store"
        val title_field = intent.getStringExtra("titleField")
        setMyTitle("體育用品店")

        dataService = StoreService
        searchRows = _searchRows
        recyclerView = store_list
        refreshLayout = store_refresh
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
            Loading.hide(maskView)
            loading = false
            page++
        } else {
            warning(dataService.msg)
            Loading.hide(maskView)
            loading = false
        }
//        mask?.let { mask?.dismiss() }
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

//    fun layerSubmit() {
//        //prepareParams()
//        page = 1
//        theFirstTime = true
//        refresh()
//    }

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
            if (value_type != null && key != null && value.length > 0) {
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
        if (row != null && row.containsKey("key") && row.get("key")!!.length > 0) {
            key = row!!.get("key")
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
        if (superStores != null) {
            for (row in superStores!!.rows) {
                //row.print()
                row.filter()
                val storeItem = StoreItem(this, row)
                storeItem.list1CellDelegate = this
                items.add(storeItem)
            }
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.ViewHolder>, view: View) {

        val storeItem = item as StoreItem
        val superStore = storeItem.row
        //superCourse.print()
        val intent = Intent(this, ShowStoreVC::class.java)
        intent.putExtra("store_token", superStore.token)
        intent.putExtra("title", superStore.name)
        startActivity(intent)
    }

    override fun cellRefresh(index: Int) {
        refresh()
    }

    override fun cellShowMap(index: Int) {
        if (superStores != null && superStores!!.totalCount > index) {
            val row = superStores!!.rows[index]
            val intent = Intent(this, MyMapVC::class.java)
            intent.putExtra("title", row.name)
            intent.putExtra("address", row.address)
            startActivity(intent)
        }
    }

    override fun cellTel(index: Int) {
        if (superStores != null && superStores!!.totalCount > index) {
            val row = superStores!!.rows[index]
            if (row.tel.length > 0) {
                row.tel.makeCall(this)
//                if (!permissionExist(android.Manifest.permission.CALL_PHONE)){
//                    requestPermission(arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
//                } else {
//                    row.tel.makeCall(this)
//                }
            }
        }
    }

    override fun cellMobile(index: Int) {
        if (superStores != null && superStores!!.totalCount > index) {
            val row = superStores!!.rows[index]
            if (row.mobile.length > 0) {
                row.mobile.makeCall(this)
            }
        }
    }

    override fun cellEdit(index: Int) {
        if (superStores != null && superStores!!.totalCount > index) {
            val row = superStores!!.rows[index]

        }
    }

    override fun cellDelete(index: Int) {
        if (superStores != null && superStores!!.totalCount > index) {
            val row = superStores!!.rows[index]

        }
    }
}

interface List1CellDelegate {
    fun cellRefresh(row: Int)
    fun cellShowMap(row: Int)
    fun cellTel(row: Int)
    fun cellMobile(row: Int)
    fun cellEdit(row: Int)
    fun cellDelete(row: Int)
}

class StoreItem(val context: Context, val row: SuperStore): Item() {

    var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //println(superStore);
        viewHolder.cityBtn.text = row.city_show
        viewHolder.titleTxt.text = row.name
        Picasso.with(context)
                .load(BASE_URL + row.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)
        viewHolder.telTxt.text = row.tel_show
        viewHolder.business_timeTxt.text = row.open_time_show+"~"+row.close_time_show
        viewHolder.addressTxt.text = row.address

        viewHolder.refreshIcon.setOnClickListener {
//            println(position)
            if (list1CellDelegate != null) {
                list1CellDelegate!!.cellRefresh(position)
            }
        }

        if (row.address.isEmpty()) {
            viewHolder.mapIcon.visibility = View.GONE
        } else {
            viewHolder.mapIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellShowMap(position)
                }
            }
        }

        if (row.tel.isEmpty()) {
            viewHolder.telIcon.visibility = View.GONE
        } else {
            viewHolder.telIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellTel(position)
                }
            }
        }

        if (row.mobile.isEmpty()) {
            //viewHolder.mobileIcon.visibility = View.GONE
        } else {
            viewHolder.mobileIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellMobile(position)
                }
            }
        }


//如果要啟動管理功能，請打開這個註解
//        var showManager = false
//        val managers = row.managers
//        if (managers.count() > 0) {
//            val member_id = member.id
//            for (manager in managers) {
//                //print(manager)
//                if (manager.containsKey("id") && manager["id"] != null) {
//                    val manager_id = manager["id"] as Int
//                    if (member_id == manager_id) {
//                        showManager = true
//                        break
//                    }
//                }
//            }
//        }
//        if (showManager) {
//            viewHolder.editIcon.setOnClickListener {
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellEdit(position)
//                }
//                if (list1CellDelegate != null) {
//                    list1CellDelegate!!.cellDelete(position)
//                }
//            }
//        } else {
//            viewHolder.editIcon.visibility = View.INVISIBLE
//            viewHolder.deleteIcon.visibility = View.INVISIBLE
//        }

    }

    override fun getLayout() = R.layout.list1_cell
}




























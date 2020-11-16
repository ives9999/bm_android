package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Fragments.CourseFragment
import com.sportpassword.bm.Models.Member
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
                //row.print()
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
        viewHolder.cityBtn.text = row.city
        viewHolder.titleTxt.text = row.name
        Picasso.with(context)
                .load(BASE_URL + row.featured_path)
                .placeholder(R.drawable.loading_square_120)
                .error(R.drawable.loading_square_120)
                .into(viewHolder.listFeatured)
        viewHolder.telTxt.text = row.tel_text
        viewHolder.business_timeTxt.text = row.open_time_text+"~"+row.close_time_text
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

        var showManager = false
        val managers = row.managers
        if (managers.count() > 0) {
            val member_id = member.id
            for (manager in managers) {
                //print(manager)
                if (manager.containsKey("id") && manager["id"] != null) {
                    val manager_id = manager["id"] as Int
                    if (member_id == manager_id) {
                        showManager = true
                        break
                    }
                }
            }
        }
        if (showManager) {
            viewHolder.editIcon.setOnClickListener {
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellEdit(position)
                }
                if (list1CellDelegate != null) {
                    list1CellDelegate!!.cellDelete(position)
                }
            }
        } else {
            viewHolder.editIcon.visibility = View.INVISIBLE
            viewHolder.deleteIcon.visibility = View.INVISIBLE
        }

    }

    override fun getLayout() = R.layout.list1_cell
}




























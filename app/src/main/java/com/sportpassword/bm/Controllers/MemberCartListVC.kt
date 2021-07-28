package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Models.CartTable
import com.sportpassword.bm.Models.CartsTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_member_cart_list_vc.*
import kotlinx.android.synthetic.main.mask.*

class MemberCartListVC : MyTableVC() {

    var mysTable: CartsTable? = null

    var myTable: CartTable? = null
    var cartItemTable: ArrayList<CartTable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = CartService

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_cart_list_vc)

        recyclerView = cart_list
        refreshLayout = cart_list_refresh
        maskView = mask
        setRefreshListener()
        setRecyclerViewScrollListener()

        initAdapter()
        perPage = 10
        refresh()
    }

    override fun refresh() {

        page = 1
        page = 1
        theFirstTime = true
        adapter.clear()
        items.clear()
        getDataStart1(page, perPage, member.token)
    }

    fun submitBtnPressed(view: View) {

    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}
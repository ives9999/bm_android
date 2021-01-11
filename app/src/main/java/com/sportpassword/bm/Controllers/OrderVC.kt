package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Form.OrderForm
import com.sportpassword.bm.Form.RegisterForm
import com.sportpassword.bm.Models.SuperProduct
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.activity_order_vc.*

class OrderVC : MyTableVC1() {

    var superProduct: SuperProduct? = null

    //Form
    lateinit var form: OrderForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_vc)

        if (intent.hasExtra("superProduct")) {
            superProduct = intent.getSerializableExtra("superProduct") as? SuperProduct
            setMyTitle(superProduct!!.name)
            hidekeyboard(order_layout)

            form = OrderForm(this)
            sections = form.getSections()
            //section_keys = form.getSectionKeys()

            initData()

            recyclerView = editTableView
            initAdapter(true)

            refreshLayout = refresh
            setRefreshListener()

        } else {
            warning("傳送商品資料錯誤，請洽管理員，或稍後再試")
        }
    }

    fun initData() {

    }

    fun submitBtnPressed(view: View) {
        //print("purchase")
        goOrder(superProduct!!)
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}
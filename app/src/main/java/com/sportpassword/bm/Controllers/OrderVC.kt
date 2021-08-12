package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_order_vc.*
import java.util.*

class OrderVC : MyTableVC() {

    var product_token: String? = null
    var productTable: ProductTable? = null
    var cartsTable: CartsTable? = null
    var cartTable: CartTable? = null
    var cartitemsTable: ArrayList<CartItemTable> = arrayListOf()

    var sub_total: Int = 0
    var shippingFee: Int = 0
    var total: Int = 0
    var selected_number: Int = 1
    var selected_price: Int = 0
    var selected_idx: Int = 0

    var productRows: ArrayList<HashMap<String, String>> = arrayListOf()
    var amountRows: ArrayList<HashMap<String, String>> = arrayListOf()
    var gatewayRows: ArrayList<HashMap<String, String>> = arrayListOf()
    var shippingRows: ArrayList<HashMap<String, String>> = arrayListOf()
    var invoiceRows: ArrayList<HashMap<String, String>> = arrayListOf()

    var invoiceFixedRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "發片(本商城目前僅提供電子發票)","key" to INVOICE_KEY,"value" to "","show" to "","cell" to "more")
    )

    var invoiceOptionRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "個人","key" to PERSONAL_KEY,"value" to "true","show" to "","cell" to "radio"),
        hashMapOf("title" to "公司","key" to COMPANY_KEY,"value" to "false","show" to "","cell" to "radio")
    )

    val invoicePersonalRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "EMail","key" to INVOICE_EMAIL_KEY,"value" to "${member.email}","show" to "${member.email}","cell" to "textField")
    )

    val invoiceCompanyRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "統一編編","key" to INVOICE_COMPANY_TAX_KEY,"value" to "","show" to "","cell" to "textField"),
        hashMapOf("title" to "公司行號抬頭","key" to INVOICE_COMPANY_NAME_KEY,"value" to "","show" to "","cell" to "textField"),
        hashMapOf("title" to "EMail","key" to INVOICE_EMAIL_KEY,"value" to "${member.email}","show" to "${member.email}","cell" to "textField")
    )

    var memberRows: ArrayList<HashMap<String, String>> = arrayListOf()

    val memoRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "留言","key" to MEMO_KEY,"value" to "","show" to "","cell" to "memo")
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = OrderService

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_vc)

        setMyTitle("訂單")

        recyclerView = order_list
        refreshLayout = order_refresh

        initAdapter()
        refresh()
    }

    override fun refresh() {

        page = 1
        theFirstTime = true
        adapter.clear()
        items.clear()
        getDataStart(page, perPage, member.token)
    }

    override fun genericTable() {
        cartsTable = jsonToModels<CartsTable>(dataService.jsonString)
        if (cartsTable == null) {
            warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
        } else {
            if (cartsTable!!.rows.size != 1) {
                warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
            } else {
                var amount: Int = 0
                cartTable = cartsTable!!.rows[0]
                cartitemsTable = cartTable!!.items
                for (cartItemTable in cartitemsTable) {

                    cartItemTable.filterRow()
                    amount += cartItemTable.amount

                    productTable = cartItemTable.product

                    var attribute_text: String = ""
                    if (cartItemTable.attributes.size > 0) {

                        for ((idx, attribute) in cartItemTable.attributes.withIndex()) {
                            attribute_text += attribute["name"]!! + ":" + attribute["value"]!!
                            if (idx < cartItemTable.attributes.count() - 1) {
                                attribute_text += " | "
                            }
                        }
                    }

                    val row:HashMap<String, String> = hashMapOf("title" to productTable!!.name,"key" to PRODUCT_KEY,"value" to "","show" to "","cell" to "cart","featured_path" to productTable!!.featured_path,"attribute" to attribute_text,"amount" to cartItemTable.amount_show,"quantity" to cartItemTable.quantity.toString())
                    productRows.add(row)

                    initData()
                }
            }
        }
    }

    fun initData() {
        
    }

    fun submitBtnPressed(view: View) {
        toOrder()
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}
package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_vc)
    }


    fun submitBtnPressed(view: View) {
        toOrder()
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}
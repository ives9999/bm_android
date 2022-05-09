package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.gson.Gson
import com.sportpassword.bm.Adapters.OneItemAdapter
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_payment_vc.*
import kotlinx.android.synthetic.main.bottom_view_general.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.payment_cell.*
import tw.com.ecpay.paymentgatewaykit.manager.*

class PaymentVC : MyTableVC() {

    var ecpay_token: String = ""
    var order_token: String = ""
    var ecpay_token_ExpireDate: String = ""
    var orderTable: OrderTable? = null

//    var productRows: ArrayList<HashMap<String, String>> = arrayListOf()
//
//    var orderRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf(TITLE_KEY to "訂單編號", KEY_KEY to ORDER_NO_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "商品金額", KEY_KEY to AMOUNT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "運費", KEY_KEY to SHIPPING_FEE_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "稅", KEY_KEY to TAX_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "訂單金額", KEY_KEY to TOTAL_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "訂單建立時間", KEY_KEY to CREATED_AT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "訂單狀態", KEY_KEY to ORDER_PROCESS_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
//    )
//
//    var gatewayRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf(TITLE_KEY to "付款方式", KEY_KEY to GATEWAY_METHOD_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "more"),
//        hashMapOf(TITLE_KEY to "付款狀態", KEY_KEY to GATEWAY_PROCESS_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "付款時間", KEY_KEY to GATEWAY_AT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
//    )
//
//    var shippingRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf(TITLE_KEY to "到貨方式", KEY_KEY to SHIPPING_METHOD_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "到貨狀態", KEY_KEY to SHIPPING_PROCESS_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "出貨時間", KEY_KEY to SHIPPING_AT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
//    )
//
//    var invoiceRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf(TITLE_KEY to "發票種類", KEY_KEY to INVOICE_TYPE_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "寄送EMail", KEY_KEY to INVOICE_EMAIL_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
//    )
//
//    var memberRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf(TITLE_KEY to "姓名",KEY_KEY to NAME_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "電話",KEY_KEY to MOBILE_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "EMail",KEY_KEY to EMAIL_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text"),
//        hashMapOf(TITLE_KEY to "住址",KEY_KEY to ADDRESS_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text")
//    )
//
//    val memoRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf(TITLE_KEY to "留言",KEY_KEY to MEMO_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text")
//    )

    var gateway: GATEWAY = GATEWAY.credit_card
    var payment_no: String = ""
    var expire_at: String = ""
    var payment_url: String = ""
    var barcode1: String = ""
    var barcode2: String = ""
    var barcode3: String = ""
    var bank_code: String = ""
    var bank_account: String = ""

    var handle_fee: String = ""
    var trade_no: String = ""
    var card6No: String = ""
    var card4No: String = ""
    var gateway_at: String = ""

//    var popupRows: ArrayList<HashMap<String, String>> = arrayListOf()

    var title: String = ""

    var bottom_button_count: Int = 2
    val button_width: Int = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_vc)

        if (intent.hasExtra("ecpay_token")) {
            ecpay_token = intent.getStringExtra("ecpay_token")!!
        }
        if (intent.hasExtra("order_token")) {
            order_token = intent.getStringExtra("order_token")!!
        }
        if (intent.hasExtra("tokenExpireDate")) {
            ecpay_token_ExpireDate = intent.getStringExtra("ecpay_token_ExpireDate")!!
        }

        title = getString(R.string.app_name)

        setMyTitle(title)

        dataService = OrderService
        recyclerView = payment_list
//        refreshLayout = refresh
        maskView = mask
        setRefreshListener()

        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf("product_icon_view" to "false"))
        oneSectionAdapter.setOneSection(oneSections)
        recyclerView.adapter = oneSectionAdapter

        //initAdapter()
        //refresh()

        setBottomButtonPadding()

        init()
        if (ecpay_token.length > 0) {
            toECPay()
        } else {
            refresh()
        }
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun setBottomButtonPadding() {

        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.submitBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }

    fun toECPay() {

        //PaymentkitManager.initialize(this, ServerType.Stage)
        PaymentkitManager.initialize(this, ServerType.Prod)
        PaymentkitManager.createPayment(this, ecpay_token, LanguageCode.zhTW, true, title, PaymentkitManager.RequestCode_CreatePayment)
    }

    override fun refresh() {
        runOnUiThread {
            Loading.show(mask)
        }
        val params: HashMap<String, String> = hashMapOf("token" to order_token, "member_token" to member.token!!)
        dataService.getOne(this, params) { success ->
            if (success) {
                jsonString = dataService.jsonString
                if (jsonString != null && jsonString!!.isNotEmpty()) {
                    runOnUiThread {
                        genericTable()
                    }
                }
            }
            closeRefresh()
            runOnUiThread {
                Loading.hide(mask)
            }
        }
    }

    override fun genericTable() {
        try {
            orderTable = Gson().fromJson(jsonString, OrderTable::class.java)
        } catch (e: java.lang.Exception) {
            runOnUiThread {
                warning(e.localizedMessage)
            }
        }
        if (orderTable == null) {
            warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
        } else {
            orderTable!!.filterRow()
            runOnUiThread {
                if (orderTable!!.all_process > 1) {//已經付費了
                    footer.visibility = View.GONE
                } else {
                    footer.visibility = View.VISIBLE
                }
            }

            initData()
            oneSectionAdapter.setOneSection(oneSections)
            runOnUiThread {
                oneSectionAdapter.notifyDataSetChanged()
            }

        }
    }

    fun initData() {

        setMyTitle(orderTable!!.order_no)

        var rows: ArrayList<OneRow> = arrayListOf()

//        mySections = arrayListOf(
//            hashMapOf("name" to "商品", "isExpanded" to true, KEY_KEY to PRODUCT_KEY),
//            hashMapOf("name" to "訂單", "isExpanded" to true, KEY_KEY to ORDER_KEY),
//            hashMapOf("name" to "付款方式", "isExpanded" to true, KEY_KEY to GATEWAY_KEY),
//            hashMapOf("name" to "寄送方式", "isExpanded" to true, KEY_KEY to SHIPPING_KEY),
//            hashMapOf("name" to "電子發票", "isExpanded" to true, KEY_KEY to INVOICE_KEY),
//            hashMapOf("name" to "訂購人資料", "isExpanded" to true, KEY_KEY to MEMBER_KEY),
//            hashMapOf("name" to "其他留言", "isExpanded" to true, KEY_KEY to MEMO_KEY)
//        )

        val orderItemsTable = orderTable!!.items
        for (orderItemTable in orderItemsTable) {

            orderItemTable.filterRow()
            val productTable = orderItemTable.product

            var attribute_text: String = ""
            if (orderItemTable.attributes.size > 0) {

                for ((idx, attribute) in orderItemTable.attributes.withIndex()) {
                    attribute_text += attribute["name"]!! + ":" + attribute[VALUE_KEY]!!
                    if (idx < orderItemTable.attributes.size - 1) {
                        attribute_text += " | "
                    }
                }
            }

            oneSections.clear()

            val row = OneRow(
                productTable!!.name,
                "",
                "",
                PRODUCT_KEY,
                "cart",
                KEYBOARD.default,
                "",
                "",
                false,
                false,
                productTable.featured_path,
                attribute_text,
                orderItemTable.amount_show,
                orderItemTable.quantity.toString()
            )
            rows.add(row)

//            val row: HashMap<String, String> = hashMapOf(TITLE_KEY to productTable!!.name,KEY_KEY to PRODUCT_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "cart","featured_path" to productTable!!.featured_path,"attribute" to attribute_text,"amount" to orderItemTable.amount_show,"quantity" to orderItemTable.quantity.toString())
//            productRows.add(row)
        }
        var section = makeSectionRow("商品", PRODUCT_KEY, rows, true)
        oneSections.add(section)

//        myRows = arrayListOf(
//            hashMapOf(KEY_KEY to PRODUCT_KEY, "rows" to productRows),
//            hashMapOf(KEY_KEY to ORDER_KEY, "rows" to orderRows),
//            hashMapOf(KEY_KEY to GATEWAY_KEY, "rows" to gatewayRows),
//            hashMapOf(KEY_KEY to SHIPPING_KEY, "rows" to shippingRows),
//            hashMapOf(KEY_KEY to INVOICE_KEY, "rows" to invoiceRows),
//            hashMapOf(KEY_KEY to MEMBER_KEY, "rows" to memberRows),
//            hashMapOf(KEY_KEY to MEMO_KEY, "rows" to memoRows)
//        )

        //order
        rows = arrayListOf()
        var row = OneRow("訂單編號", orderTable!!.order_no, orderTable!!.order_no, ORDER_NO_KEY, "text")
        rows.add(row)
        row = OneRow("商品金額", orderTable!!.amount.toString(), orderTable!!.amount_show, AMOUNT_KEY, "text")
        rows.add(row)
        row = OneRow("運費", orderTable!!.shipping_fee.toString(), orderTable!!.shipping_fee_show, SHIPPING_FEE_KEY, "text")
        rows.add(row)
        row = OneRow("税", orderTable!!.tax.toString(), orderTable!!.tax_show, TAX_KEY, "text")
        rows.add(row)
        row = OneRow("訂單金額", orderTable!!.total.toString(), orderTable!!.total_show, TOTAL_KEY, "text")
        rows.add(row)
        row = OneRow("訂單建立時間", orderTable!!.created_at, orderTable!!.created_at_show, CREATED_AT_KEY, "text")
        rows.add(row)
        row = OneRow("訂單狀態", orderTable!!.process, orderTable!!.order_process_show, ORDER_PROCESS_KEY, "text")
        rows.add(row)
        section = makeSectionRow("訂單", ORDER_KEY, rows, true)
        oneSections.add(section)



//        var row: HashMap<String, String> = getRowRowsFromMyRowsByKey1(ORDER_NO_KEY)
//        row[VALUE_KEY] = orderTable!!.order_no
//        row[SHOW_KEY] = orderTable!!.order_no
//        replaceRowByKey(ORDER_KEY, ORDER_NO_KEY,row)

//        row = getRowRowsFromMyRowsByKey1(AMOUNT_KEY)
//        row[VALUE_KEY] = orderTable!!.amount.toString()
//        row[SHOW_KEY] = orderTable!!.amount_show
//        replaceRowByKey(ORDER_KEY, AMOUNT_KEY,row)

//        row = getRowRowsFromMyRowsByKey1(SHIPPING_FEE_KEY)
//        row[VALUE_KEY] = orderTable!!.shipping_fee.toString()
//        row[SHOW_KEY] = orderTable!!.shipping_fee_show
//        replaceRowByKey(ORDER_KEY, SHIPPING_FEE_KEY,row)

//        row = getRowRowsFromMyRowsByKey1(TAX_KEY)
//        row[VALUE_KEY] = orderTable!!.tax.toString()
//        row[SHOW_KEY] = orderTable!!.tax_show
//        replaceRowByKey(ORDER_KEY, TAX_KEY,row)

//        row = getRowRowsFromMyRowsByKey1(TOTAL_KEY)
//        row[VALUE_KEY] = orderTable!!.total.toString()
//        row[SHOW_KEY] = orderTable!!.total_show
//        replaceRowByKey(ORDER_KEY, TOTAL_KEY,row)

//        row = getRowRowsFromMyRowsByKey1(CREATED_AT_KEY)
//        row[VALUE_KEY] = orderTable!!.created_at
//        row[SHOW_KEY] = orderTable!!.created_at_show
//        replaceRowByKey(ORDER_KEY, CREATED_AT_KEY,row)

//        row = getRowRowsFromMyRowsByKey1(ORDER_PROCESS_KEY)
//        row[VALUE_KEY] = orderTable!!.process
//        row[SHOW_KEY] = orderTable!!.order_process_show
//        replaceRowByKey(ORDER_KEY, ORDER_PROCESS_KEY,row)

        //gateway
        rows = arrayListOf()
        row = OneRow("付款方式", orderTable!!.gateway!!.method, orderTable!!.gateway!!.method_show, GATEWAY_METHOD_KEY, "more", KEYBOARD.default, "", "", false, false)
        rows.add(row)
        row = OneRow("付款狀態", orderTable!!.gateway!!.process, orderTable!!.gateway!!.process_show, GATEWAY_PROCESS_KEY, "text")
        rows.add(row)
        row = OneRow("付款時間", orderTable!!.gateway!!.gateway_at, orderTable!!.gateway!!.gateway_at_show, GATEWAY_AT_KEY, "text")
        rows.add(row)
        section = makeSectionRow("付款方式", GATEWAY_KEY, rows, true)
        oneSections.add(section)

//        row = getRowRowsFromMyRowsByKey1(GATEWAY_METHOD_KEY)
//        row[VALUE_KEY] = orderTable!!.gateway!!.method
//        row[SHOW_KEY] = orderTable!!.gateway!!.method_show
//        replaceRowByKey(GATEWAY_KEY, GATEWAY_METHOD_KEY,row)
//
//        row = getRowRowsFromMyRowsByKey1(GATEWAY_AT_KEY)
//        row[VALUE_KEY] = orderTable!!.gateway!!.gateway_at
//        row[SHOW_KEY] = orderTable!!.gateway!!.gateway_at_show
//        replaceRowByKey(GATEWAY_KEY, GATEWAY_AT_KEY,row)
//
//        row = getRowRowsFromMyRowsByKey1(GATEWAY_PROCESS_KEY)
//        row[VALUE_KEY] = orderTable!!.gateway!!.process
//        row[SHOW_KEY] = orderTable!!.gateway!!.process_show
//        replaceRowByKey(GATEWAY_KEY, GATEWAY_PROCESS_KEY,row)

        //shipping
        rows = arrayListOf()
        row = OneRow("到貨方式", orderTable!!.shipping!!.method, orderTable!!.shipping!!.method_show, SHIPPING_METHOD_KEY, "text")
        rows.add(row)
        row = OneRow("到貨狀態", orderTable!!.shipping!!.process, orderTable!!.shipping!!.process_show, SHIPPING_PROCESS_KEY, "text")
        rows.add(row)
        row = OneRow("出貨時間", orderTable!!.shipping!!.shipping_at, orderTable!!.shipping!!.shipping_at_show, SHIPPING_AT_KEY, "text")
        rows.add(row)
        if (orderTable!!.shipping!!.method == "store_711" || orderTable!!.shipping?.method == "store_family") {
            row = OneRow(
                "到達商店時間",
                orderTable!!.shipping!!.store_at,
                orderTable!!.shipping!!.store_at_show,
                SHIPPING_STORE_AT_KEY,
                "text"
            )
            rows.add(row)
        }
        row = OneRow("到貨時間", orderTable!!.shipping!!.complete_at, orderTable!!.shipping!!.complete_at_show, SHIPPING_COMPLETE_AT_KEY, "text")
        rows.add(row)
        if (orderTable!!.shipping!!.back_at.length > 0) {
            row = OneRow(
                "退貨時間",
                orderTable!!.shipping!!.back_at,
                orderTable!!.shipping!!.back_at_show,
                SHIPPING_BACK_AT_KEY,
                "text"
            )
            rows.add(row)

        }
        section = makeSectionRow("到貨方式", SHIPPING_KEY, rows, true)
        oneSections.add(section)

//        row = getRowRowsFromMyRowsByKey1(SHIPPING_METHOD_KEY)
//        row[VALUE_KEY] = orderTable!!.shipping!!.method
//        row[SHOW_KEY] = orderTable!!.shipping!!.method_show
//        shippingRows = replaceRowByKey(shippingRows, SHIPPING_METHOD_KEY, row)
//        //replaceRowByKey(sectionSHIPPING_KEY, rowSHIPPING_METHOD_KEY,row)
//
//        row = getRowRowsFromMyRowsByKey1(SHIPPING_AT_KEY)
//        row[VALUE_KEY] = orderTable!!.shipping!!.shipping_at
//        row[SHOW_KEY] = orderTable!!.shipping!!.shipping_at_show
//        shippingRows = replaceRowByKey(shippingRows, SHIPPING_AT_KEY, row)
//        //replaceRowByKey(sectionSHIPPING_KEY, rowSHIPPING_AT_KEY,row)
//
//        row = getRowRowsFromMyRowsByKey1(SHIPPING_PROCESS_KEY)
//        row[VALUE_KEY] = orderTable!!.shipping!!.process
//        row[SHOW_KEY] = orderTable!!.shipping!!.process_show
//        shippingRows = replaceRowByKey(shippingRows, SHIPPING_PROCESS_KEY, row)
//        //replaceRowByKey(sectionSHIPPING_KEY, rowSHIPPING_PROCESS_KEY,row)
//
//        if (orderTable!!.shipping!!.method == "store_711" || orderTable!!.shipping?.method == "store_family") {
//            row = hashMapOf(TITLE_KEY to "到達商店時間", KEY_KEY to SHIPPING_STORE_AT_KEY, VALUE_KEY to orderTable!!.shipping!!.store_at, SHOW_KEY to orderTable!!.shipping!!.store_at_show, CELL_KEY to "text")
//            shippingRows.add(row)
//        }
//
//        row = hashMapOf(TITLE_KEY to "到貨時間", KEY_KEY to SHIPPING_COMPLETE_AT_KEY, VALUE_KEY to orderTable!!.shipping!!.complete_at, SHOW_KEY to orderTable!!.shipping!!.complete_at_show, CELL_KEY to "text")
//        shippingRows.add(row)
//
//        if (orderTable!!.shipping!!.back_at.length > 0) {
//            row = hashMapOf(TITLE_KEY to "退貨時間", KEY_KEY to SHIPPING_BACK_AT_KEY, VALUE_KEY to orderTable!!.shipping!!.back_at, SHOW_KEY to orderTable!!.shipping!!.back_at_show, CELL_KEY to "text")
//            shippingRows.add(row)
//        }
//        replaceRowsByKey(SHIPPING_KEY, shippingRows)

        //invoice
        val invoice_type: String = orderTable!!.invoice_type
        rows = arrayListOf()
        row = OneRow("發票種類", invoice_type, orderTable!!.invoice_type_show, INVOICE_TYPE_KEY, "text")
        rows.add(row)
        if (invoice_type == "company") {
            row = OneRow("公司或行號名稱", orderTable!!.invoice_company_name, orderTable!!.invoice_company_name, INVOICE_COMPANY_NAME_KEY, "text")
            rows.add(row)
            row = OneRow("統一編號", orderTable!!.invoice_company_tax, orderTable!!.invoice_company_tax, INVOICE_COMPANY_TAX_KEY, "text")
            rows.add(row)
        }
        row = OneRow("寄送EMail", orderTable!!.invoice_email, orderTable!!.invoice_email, INVOICE_EMAIL_KEY, "text")
        rows.add(row)
        section = makeSectionRow("電子發票", SHIPPING_KEY, rows, true)
        oneSections.add(section)

//        row = getRowFromKey(invoiceRows, INVOICE_TYPE_KEY)
//        row[VALUE_KEY] = invoice_type
//        row[SHOW_KEY] = orderTable!!.invoice_type_show
//        invoiceRows = replaceRowByKey(invoiceRows, INVOICE_TYPE_KEY, row)

//        if (invoice_type == "company") {
//            row = hashMapOf(TITLE_KEY to "公司或行號名稱", KEY_KEY to INVOICE_COMPANY_NAME_KEY, VALUE_KEY to orderTable!!.invoice_company_name, SHOW_KEY to orderTable!!.invoice_company_name, CELL_KEY to "text")
//            invoiceRows.add(row)
//            row = hashMapOf(TITLE_KEY to "統一編號", KEY_KEY to INVOICE_COMPANY_TAX_KEY, VALUE_KEY to orderTable!!.invoice_company_tax, SHOW_KEY to orderTable!!.invoice_company_tax, CELL_KEY to "text")
//            invoiceRows.add(row)
//        }

//        row = getRowFromKey(invoiceRows, INVOICE_EMAIL_KEY)
//        row[VALUE_KEY] = orderTable!!.invoice_email
//        row[SHOW_KEY] = orderTable!!.invoice_email
//        invoiceRows = replaceRowByKey(invoiceRows, INVOICE_EMAIL_KEY, row)
//
//        replaceRowsByKey(INVOICE_KEY, invoiceRows)
        //myRows[4]["rows"] = invoiceRows

        //member
        rows = arrayListOf()
        row = OneRow("姓名", orderTable!!.order_name, orderTable!!.order_name, NAME_KEY, "text")
        rows.add(row)
        row = OneRow("電話", orderTable!!.order_tel, orderTable!!.order_tel_show, MOBILE_KEY, "text")
        rows.add(row)
        row = OneRow("EMail", orderTable!!.order_email, orderTable!!.order_email, EMAIL_KEY, "text")
        rows.add(row)
        row = OneRow("住址", orderTable!!.order_address, orderTable!!.order_address, ADDRESS_KEY, "text")
        rows.add(row)

        section = makeSectionRow("訂購人資料", ORDER_KEY, rows, true)
        oneSections.add(section)

//        row = getRowRowsFromMyRowsByKey1(NAME_KEY)
//        row[VALUE_KEY] = orderTable!!.order_name
//        row[SHOW_KEY] = orderTable!!.order_name
//        replaceRowByKey(MEMBER_KEY, NAME_KEY,row)

//        row = getRowRowsFromMyRowsByKey1(MOBILE_KEY)
//        row[VALUE_KEY] = orderTable!!.order_tel
//        row[SHOW_KEY] = orderTable!!.order_tel_show
//        replaceRowByKey(MEMBER_KEY, MOBILE_KEY,row)
//
//        row = getRowRowsFromMyRowsByKey1(EMAIL_KEY)
//        row[VALUE_KEY] = orderTable!!.order_email
//        row[SHOW_KEY] = orderTable!!.order_email
//        replaceRowByKey(MEMBER_KEY, EMAIL_KEY,row)
//
//        row = getRowRowsFromMyRowsByKey1(ADDRESS_KEY)
//        row[VALUE_KEY] = orderTable!!.order_address
//        row[SHOW_KEY] = orderTable!!.order_address
//        replaceRowByKey(MEMBER_KEY, ADDRESS_KEY,row)
//
        rows = arrayListOf()
        row = OneRow("留言", orderTable!!.memo, orderTable!!.memo, MEMO_KEY, "text")
        rows.add(row)
        section = makeSectionRow("其他留言", ORDER_KEY, rows, true)
        oneSections.add(section)

//        //memo
//        row = getRowRowsFromMyRowsByKey1(MEMO_KEY)
//        row[VALUE_KEY] = orderTable!!.memo
//        row[SHOW_KEY] = orderTable!!.memo
//        replaceRowByKey(MEMO_KEY, MEMO_KEY,row)

//        initAdapter(true)
        //        recyclerView.setHasFixedSize(true)
//        if (refreshLayout != null) {
//            setRefreshListener()
//        }
//        setRecyclerViewScrollListener()
    }



//    override fun initAdapter(include_section: Boolean) {
//        //adapter.clear()
//        if (include_section) {
//            for ((idx, mySection) in mySections.withIndex()) {
//                val section = Section()
//                //adapterSections.add(section)
//                val title: String = mySection["name"] as String
//                val isExpanded: Boolean = mySection["isExpanded"] as Boolean
//                val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)
//                val items = generateItems(idx)
//                section.addAll(items)
//                expandableGroup.add(section)
//
//                //adapter.add(expandableGroup)
//            }
//        }
//
//        //recyclerView.adapter = adapter
//    }
//
//    override fun generateItems(section: Int): ArrayList<Item> {
//
//        if (myRows.size == 0) {
//            return arrayListOf()
//        }
//        //items.clear()
//        var sectionKey: String = ""
//        val sectionRow: HashMap<String, Any> = myRows[section]
//        val tmp: String? = sectionRow["key"] as? String
//        if (tmp != null) {
//            sectionKey = tmp
//        }
//
//        if (!sectionRow.containsKey("rows")) {
//            return arrayListOf()
//        }
//
//        @Suppress("UNCHECKED_CAST")
//        val rows: ArrayList<HashMap<String, String>> =
//            sectionRow["rows"] as ArrayList<HashMap<String, String>>
//
//        for ((idx, row) in rows.withIndex()) {
//
//            var rowKey: String = ""
//            if (row.containsKey("key") && row["key"] != null) {
//                rowKey = row["key"]!!
//            }
//            var title: String = ""
//            if (row.containsKey("title") && row["title"] != null) {
//                title = row["title"]!!
//            }
//            var value: String = ""
//            if (row.containsKey("value") && row["value"] != null) {
//                value = row["value"]!!
//            }
//            var show: String = ""
//            if (row.containsKey("show") && row["show"] != null) {
//                show = row["show"]!!
//            }
//
//            val cell_type: String? = row["cell"]
//
//            //var formItemAdapter: FormItemAdapter1? = null
//            if (cell_type == "cart") {
//                var featured_path = FEATURED_PATH
//                if (row.containsKey("featured_path") && row["featured_path"] != null && row["featured_path"]!!.length > 0) {
//                    featured_path = row["featured_path"]!!
//                }
//                var attribute = ""
//                if (row.containsKey("attribute") && row["attribute"] != null) {
//                    attribute = row["attribute"]!!
//                }
//                var amount = ""
//                if (row.containsKey("amount") && row["amount"] != null) {
//                    amount = row["amount"]!!
//                }
//                var quantity = ""
//                if (row.containsKey("quantity") && row["quantity"] != null) {
//                    quantity = row["quantity"]!!
//                }
////                val cartItemItem = CartItemItem(
////                    this,
////                    sectionKey,
////                    rowKey,
////                    title,
////                    featured_path,
////                    attribute,
////                    amount,
////                    quantity
////                )
////                items.add(cartItemItem)
//            } else if (cell_type == "text") {
//                val item = PlainAdapter1(title, show)
//                //items.add(item)
//            } else if (cell_type == "more") {
//                val item = MoreAdapter1(sectionKey, rowKey, title, value, show, this, false)
//                //items.add(item)
//            }
//        }
//
//        return arrayListOf()
//    }

    override fun cellMoreClick(sectionIdx: Int, rowIdx: Int) {

//        fillPopupRows()
        val rows: ArrayList<OneRow> = fillPopupRows()
        val section: OneSection = OneSection("付款", GATEWAY_KEY, true, rows)

        val tableViewHeight: Int = rowHeight * rows.size
        showTableLayer(tableViewHeight)

        val panelAdapter = OneItemAdapter(this, sectionIdx, section, this, hashMapOf())
        //val panelAdapter = PanelAdapter(this, sectionIdx, this)

        layerTableView!!.adapter = panelAdapter


//        val panelAdapter = GroupAdapter<com.xwray.groupie.GroupieViewHolder>()
//        tableView!!.adapter = panelAdapter

        //items.clear()
//        for (row in popupRows) {
//            val title: String = row[TITLE_KEY] ?: run { "" }
//            val show: String = row[SHOW_KEY] ?: run { "" }
//            val cell_type: String = row[CELL_KEY] ?: run { "text" }
//
//            //text and barcode cell
//            if (cell_type == "text") {
//                val item = PlainAdapter1(title, show)
//                //items.add(item)
//            } else if (cell_type == "barcode") {
//                if (show.length == 0) {
//                    warning("沒有取得條碼，代碼或帳號，請重新下單")
//                } else {
//                    val item = BarcodeAdapter(title, show)
//                    //items.add(item)
//                }
//            }
//        }
        //panelAdapter.addAll(items)
    }

    private fun fillPopupRows(): ArrayList<OneRow> {
        val method: GATEWAY = GATEWAY.stringToEnum(orderTable!!.gateway!!.method)
        val rows: ArrayList<OneRow> = arrayListOf()
        var row: OneRow = OneRow()
        if (method == GATEWAY.ATM) {
            val bank_code: String = orderTable!!.gateway!!.bank_code
            val bank_account: String = orderTable!!.gateway!!.bank_account
            val expire_at: String = orderTable!!.gateway!!.expire_at_show
            row = OneRow("銀行代號", bank_code, bank_code, BANK_CODE_KEY, "text")
            rows.add(row)
            row = OneRow("銀行帳號", bank_account, bank_account, BANK_ACCOUNT_KEY, "text")
            rows.add(row)
            row = OneRow("到期日", expire_at, expire_at, EXPIRE_AT_KEY, "text")
            rows.add(row)
//            popupRows = arrayListOf(
//                hashMapOf(TITLE_KEY to "銀行代號", KEY_KEY to BANK_CODE_KEY, VALUE_KEY to bank_code, SHOW_KEY to bank_code, CELL_KEY to "text"),
//                hashMapOf(TITLE_KEY to "銀行帳號", KEY_KEY to BANK_ACCOUNT_KEY, VALUE_KEY to bank_account, SHOW_KEY to bank_account, CELL_KEY to "text"),
//                hashMapOf(TITLE_KEY to "到期日", KEY_KEY to EXPIRE_AT_KEY, VALUE_KEY to expire_at, SHOW_KEY to expire_at, CELL_KEY to "text")
//            )
        } else if (method == GATEWAY.store_cvs) {
            val payment_no: String = orderTable!!.gateway!!.payment_no
            val expire_at: String = orderTable!!.gateway!!.expire_at_show
            row = OneRow("繳款代碼", payment_no, payment_no, PAYMENT_NO_KEY, "text")
            rows.add(row)
            row = OneRow("到期日", expire_at, expire_at, EXPIRE_AT_KEY, "text")
            rows.add(row)
//            popupRows = arrayListOf(
//                hashMapOf(TITLE_KEY to "繳款代碼",KEY_KEY to PAYMENT_NO_KEY,VALUE_KEY to payment_no,SHOW_KEY to payment_no,CELL_KEY to "text"),
//                hashMapOf(TITLE_KEY to "到期日",KEY_KEY to EXPIRE_AT_KEY,VALUE_KEY to expire_at,SHOW_KEY to expire_at,CELL_KEY to "text")
//            )
        } else if (method == GATEWAY.store_barcode) {
            val barcode1: String = orderTable!!.gateway!!.barcode1
            val barcode2: String = orderTable!!.gateway!!.barcode2
            val barcode3: String = orderTable!!.gateway!!.barcode3
            val expire_at: String = orderTable!!.gateway!!.expire_at_show
            row = OneRow("繳款條碼1", barcode1, barcode1, BARCODE1_KEY, "barcode")
            rows.add(row)
            row = OneRow("繳款條碼2", barcode2, barcode2, BARCODE2_KEY, "barcode")
            rows.add(row)
            row = OneRow("繳款條碼3", barcode3, barcode3, BARCODE3_KEY, "barcode")
            rows.add(row)
            row = OneRow("到期日", expire_at, expire_at, EXPIRE_AT_KEY, "text")
            rows.add(row)
//            popupRows = arrayListOf(
//                hashMapOf(TITLE_KEY to "繳款條碼1",KEY_KEY to BARCODE1_KEY,VALUE_KEY to barcode1,SHOW_KEY to barcode1,CELL_KEY to "barcode"),
//                hashMapOf(TITLE_KEY to "繳款條碼2",KEY_KEY to BARCODE2_KEY,VALUE_KEY to barcode2,SHOW_KEY to barcode2,CELL_KEY to "barcode"),
//                hashMapOf(TITLE_KEY to "繳款條碼3",KEY_KEY to BARCODE3_KEY,VALUE_KEY to barcode3,SHOW_KEY to barcode3,CELL_KEY to "barcode"),
//                hashMapOf(TITLE_KEY to "到期日",KEY_KEY to EXPIRE_AT_KEY,VALUE_KEY to expire_at,SHOW_KEY to expire_at,CELL_KEY to "text")
//            )
        } else if (method == GATEWAY.credit_card) {
            val card6No: String = orderTable!!.gateway!!.card6No
            val card4No: String = orderTable!!.gateway!!.card4No
            row = OneRow("信用卡前6碼", card6No, card6No, "card6No", "text")
            rows.add(row)
            row = OneRow("信用卡前6碼", card4No, card4No, "card4No", "text")
            rows.add(row)
//            popupRows = arrayListOf(
//                hashMapOf(TITLE_KEY to "信用卡前6碼",KEY_KEY to PAYMENT_NO_KEY,VALUE_KEY to card6No,SHOW_KEY to card6No,CELL_KEY to "text"),
//                hashMapOf(TITLE_KEY to "信用卡後4碼",KEY_KEY to EXPIRE_AT_KEY,VALUE_KEY to card4No,SHOW_KEY to card4No,CELL_KEY to "text")
//            )
        }

        return rows
    }

    fun updateOrder() {
        val params: HashMap<String, String> = hashMapOf("token" to order_token, "member_token" to member.token!!,"do" to "update")
        params["expire_at"] = expire_at
        params["trade_no"] = trade_no
        if (gateway == GATEWAY.store_cvs) {
            params["gateway_process"] = GATEWAY_PROCESS.code.toEnglishString()
            params["payment_no"] = payment_no
            params["payment_url"] = payment_url
        } else if (gateway == GATEWAY.store_barcode) {
            params["gateway_process"] = GATEWAY_PROCESS.code.toEnglishString()
            params["barcode1"] = barcode1
            params["barcode2"] = barcode2
            params["barcode3"] = barcode3
        } else if (gateway == GATEWAY.ATM) {
            params["gateway_process"] = GATEWAY_PROCESS.code.toEnglishString()
            params["bank_code"] = bank_code
            params["bank_account"] = bank_account
        } else if (gateway == GATEWAY.credit_card) {
            params["gateway_process"] = GATEWAY_PROCESS.complete.toEnglishString()
            params["gateway_at"] = gateway_at
            params["handle_fee"] = handle_fee
            params["card4No"] = card4No
            params["card6No"] = card6No
        }

        OrderService.update(this, params) { success ->
            if (success) {
                finish()
                toProduct()
            } else {
                warning(OrderService.msg, false, "關閉") {
                    finish()
                    toProduct()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println("onActivityResult(), requestCode:" + requestCode + ", resultCode" + resultCode)
        if (requestCode == PaymentkitManager.RequestCode_CreatePayment) {
            PaymentkitManager.createPaymentResult(this, resultCode, data, CallbackFunction {
                when (it.callbackStatus) {
                    CallbackStatus.Success -> if (it.getRtnCode() == 1) {

                        val order = it.orderInfo
                        if (order != null) {
                            trade_no = order.tradeNo
                            handle_fee = order.chargeFee.toString()
                            gateway_at = order.paymentDate
                        }

                        val card = it.cardInfo
                        if (card != null) {
                            card4No = card.card4No
                            card6No = card.card6No
                        }

                        val cvs = it.cvsInfo
                        if (cvs != null) {
                            payment_no = cvs.paymentNo
                            payment_url = cvs.paymentURL
                            if (payment_no.length > 0) {
                                gateway = GATEWAY.store_cvs
                                expire_at = cvs.expireDate
                            }
                        }

                        val barcode = it.barcodeInfo
                        if (barcode != null) {
                            barcode1 = barcode.barcode1
                            barcode2 = barcode.barcode2
                            barcode3 = barcode.barcode3
                            if (barcode1.length > 0 && barcode2.length > 0 && barcode3.length > 0) {
                                gateway = GATEWAY.store_barcode
                                expire_at = barcode.expireDate
                            }
                        }

                        val ATM = it.atmInfo
                        if (ATM != null) {
                            bank_code = ATM.bankCode
                            bank_account = ATM.vAccount
                            if (bank_account.length > 0) {
                                gateway = GATEWAY.ATM
                                expire_at = ATM.expireDate
                            }
                        }

                        if (gateway == GATEWAY.credit_card) {
                            info("完成付款，我們將儘速將商品運送給您，謝謝您的光顧!!", "", "關閉") {
                                //refresh()
                                updateOrder()
                            }
                        } else {
                            updateOrder()
                        }
                    } else {
                        val sb = StringBuffer()
                        sb.append(it.getRtnCode())
                        sb.append("\r\n")
                        sb.append(it.getRtnMsg())
                        warning(sb.toString())
                    }

                    CallbackStatus.Fail -> {
                        warning("Fail Code=" + it.getRtnCode() +
                                ", Msg=" + it.getRtnMsg(), false, "關閉") {
                            finish()
                            toProduct()
                        }
                    }

                    CallbackStatus.Cancel -> {
                        warning("交易取消", false, "關閉") {
                            finish()
                            toProduct()
                        }
                    }
                    else -> {
                        warning("回傳值無法解析，請洽管理員", false, "關閉") {
                            finish()
                            toProduct()
                        }
                    }
                }
            })
        }
    }

    fun getPaymentTypeName(paymentType: PaymentType): String {
        return when(paymentType) {
            PaymentType.CreditCard -> "信用卡"
            PaymentType.CreditInstallment -> "信用卡分期"
            PaymentType.ATM -> "ATM虛擬帳號"
            PaymentType.CVS -> "超商代碼"
            PaymentType.Barcode -> "超商條碼"
            PaymentType.PeriodicFixedAmount -> "信用卡定期定額"
            PaymentType.NationalTravelCard -> "國旅卡"
            else -> ""
        }
    }

    fun signupButtonPressed(view: View) {
        ecpay_token = orderTable!!.ecpay_token
        toECPay()
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }

    fun successMsg(it: CreatePaymentCallbackData): String {
        val sb = StringBuffer()
        sb.append("PaymentType:")
        sb.append("\r\n")
        sb.append(getPaymentTypeName(it.getPaymentType()))
        sb.append("\r\n")
        sb.append("\r\n")
        sb.append("OrderInfo.MerchantTradeNo")
        sb.append("\r\n")
        sb.append(it.getOrderInfo().getMerchantTradeNo())
        sb.append("\r\n")
        sb.append("OrderInfo.TradeNo")
        sb.append("\r\n")
        sb.append(it.getOrderInfo().getMerchantTradeNo())
        if (it.getPaymentType() == PaymentType.CreditCard ||
                it.getPaymentType() == PaymentType.CreditInstallment ||
                it.getPaymentType() == PaymentType.PeriodicFixedAmount ||
                it.getPaymentType() == PaymentType.NationalTravelCard
        ) {
            sb.append("\r\n")
            sb.append("\r\n")
            sb.append("CardInfo.AuthCode")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getAuthCode())
            sb.append("\r\n")
            sb.append("CardInfo.Gwsr")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getGwsr())
            sb.append("\r\n")
            sb.append("CardInfo.ProcessDate")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getProcessDate())
            sb.append("\r\n")
            sb.append("CardInfo.Amount")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getAmount())
            sb.append("\r\n")
            sb.append("CardInfo.Eci")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getEci())
            sb.append("\r\n")
            sb.append("CardInfo.Card6No")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getCard6No())
            sb.append("\r\n")
            sb.append("CardInfo.Card4No")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getCard4No())
        }
        if (it.getPaymentType() == PaymentType.CreditCard) {
            sb.append("\r\n")
            sb.append("CardInfo.RedDan")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getRedDan())
            sb.append("\r\n")
            sb.append("CardInfo.RedDeAmt")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getRedDeAmt())
            sb.append("\r\n")
            sb.append("CardInfo.RedOkAmt")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getRedOkAmt())
            sb.append("\r\n")
            sb.append("CardInfo.RedYet")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getRedYet())
        }
        if (it.getPaymentType() == PaymentType.CreditInstallment) {
            sb.append("\r\n")
            sb.append("CardInfo.Stage")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getStage())
            sb.append("\r\n")
            sb.append("CardInfo.Stast")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getStast())
            sb.append("\r\n")
            sb.append("CardInfo.Staed")
            sb.append("\r\n")
            sb.append(it.getCardInfo().getStaed())
        }
        if (it.getPaymentType() == PaymentType.ATM) {
            sb.append("\r\n")
            sb.append("\r\n")
            sb.append("ATMInfo.BankCode")
            sb.append("\r\n")
            sb.append(it.getAtmInfo().getBankCode())
            sb.append("\r\n")
            sb.append("ATMInfo.vAccount")
            sb.append("\r\n")
            sb.append(it.getAtmInfo().getvAccount())
            sb.append("\r\n")
            sb.append("ATMInfo.ExpireDate")
            sb.append("\r\n")
            sb.append(it.getAtmInfo().getExpireDate())
        }
        if (it.getPaymentType() == PaymentType.CVS) {
            sb.append("\r\n")
            sb.append("\r\n")
            sb.append("CVSInfo.PaymentNo")
            sb.append("\r\n")
            sb.append(it.getCvsInfo().getPaymentNo())
            sb.append("\r\n")
            sb.append("CVSInfo.ExpireDate")
            sb.append("\r\n")
            sb.append(it.getCvsInfo().getExpireDate())
            sb.append("\r\n")
            sb.append("CVSInfo.PaymentURL")
            sb.append("\r\n")
            sb.append(it.getCvsInfo().getPaymentURL())
        }
        if (it.getPaymentType() == PaymentType.Barcode) {
            sb.append("\r\n")
            sb.append("\r\n")
            sb.append("BarcodeInfo.ExpireDate")
            sb.append("\r\n")
            sb.append(it.getBarcodeInfo().getExpireDate())
            sb.append("\r\n")
            sb.append("BarcodeInfo.Barcode1")
            sb.append("\r\n")
            sb.append(it.getBarcodeInfo().getBarcode1())
            sb.append("\r\n")
            sb.append("BarcodeInfo.Barcode2")
            sb.append("\r\n")
            sb.append(it.getBarcodeInfo().getBarcode2())
            sb.append("\r\n")
            sb.append("BarcodeInfo.Barcode3")
            sb.append("\r\n")
            sb.append(it.getBarcodeInfo().getBarcode3())
        }

        return sb.toString()
    }
}

//class PaymentItem(val context: Context, val row: HashMap<String, String>): Item() {
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        viewHolder.titleLbl.text = row["name"]
//        viewHolder.contentLbl.text = row["value"]
//    }
//
//    override fun getLayout() = R.layout.payment_cell
//}



























package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.sportpassword.bm.Adapters.Form.MoreAdapter1
import com.sportpassword.bm.Adapters.Form.PlainAdapter1
import com.sportpassword.bm.Adapters.Form.TextFieldAdapter1
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_payment_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.payment_cell.*
import tw.com.ecpay.paymentgatewaykit.manager.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class PaymentVC : MyTableVC() {

    var ecpay_token: String = ""
    var order_token: String = ""
    var ecpay_token_ExpireDate: String = ""
    var orderTable: OrderTable? = null

    var productRows: ArrayList<HashMap<String, String>> = arrayListOf()

    var orderRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf(TITLE_KEY to "訂單編號", KEY_KEY to ORDER_NO_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "商品金額", KEY_KEY to AMOUNT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "運費", KEY_KEY to SHIPPING_FEE_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "稅", KEY_KEY to TAX_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "訂單金額", KEY_KEY to TOTAL_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "訂單建立時間", KEY_KEY to CREATED_AT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "訂單狀態", KEY_KEY to ORDER_PROCESS_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
    )

    var gatewayRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf(TITLE_KEY to "付款方式", KEY_KEY to GATEWAY_METHOD_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "more"),
        hashMapOf(TITLE_KEY to "付款狀態", KEY_KEY to GATEWAY_PROCESS_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "付款時間", KEY_KEY to GATEWAY_AT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
    )

    var shippingRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf(TITLE_KEY to "到貨方式", KEY_KEY to SHIPPING_METHOD_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "到貨狀態", KEY_KEY to SHIPPING_PROCESS_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "出貨時間", KEY_KEY to SHIPPING_AT_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
    )

    var invoiceRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf(TITLE_KEY to "發票種類", KEY_KEY to INVOICE_TYPE_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "寄送EMail", KEY_KEY to INVOICE_EMAIL_KEY, VALUE_KEY to "", SHOW_KEY to "", CELL_KEY to "text")
    )

    var memberRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf(TITLE_KEY to "姓名",KEY_KEY to NAME_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "電話",KEY_KEY to MOBILE_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "EMail",KEY_KEY to EMAIL_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text"),
        hashMapOf(TITLE_KEY to "住址",KEY_KEY to ADDRESS_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text")
    )

    val memoRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf(TITLE_KEY to "留言",KEY_KEY to MEMO_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "text")
    )

    var gateway: GATEWAY = GATEWAY.credit_card
    var payment_no: String = ""
    var expire_at: String = ""
    var payment_url: String = ""
    var barcode1: String = ""
    var barcode2: String = ""
    var barcode3: String = ""
    var bank_code: String = ""
    var bank_account: String = ""

    var trade_no: String = ""

    var popupRows: ArrayList<HashMap<String, String>> = arrayListOf()

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

        val title: String = getString(R.string.app_name)
        setMyTitle(title)

        dataService = OrderService
        recyclerView = payment_list
        refreshLayout = refresh
        maskView = mask
        setRefreshListener()

        initAdapter()
        //refresh()

        if (ecpay_token.length > 0) {
            PaymentkitManager.initialize(this, ServerType.Stage)
            //PaymentkitManager.initialize(this, ServerType.Prod)
            PaymentkitManager.createPayment(this, ecpay_token, LanguageCode.zhTW, false, title, PaymentkitManager.RequestCode_CreatePayment)
        } else {
            refresh()
        }
    }

    override fun refresh() {
        Loading.show(mask)
        val params: HashMap<String, String> = hashMapOf("token" to order_token, "member_token" to member.token)
        dataService.getOne(this, params) { success ->
            if (success) {
                jsonString = dataService.jsonString
                if (jsonString != null && jsonString!!.isNotEmpty()) {
                    genericTable()
                }
            }
            closeRefresh()
            Loading.hide(mask)
        }
    }

    override fun genericTable() {
        try {
            orderTable = Gson().fromJson(jsonString, OrderTable::class.java)
        } catch (e: java.lang.Exception) {
            warning(e.localizedMessage)
        }
        if (orderTable == null) {
            warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
        } else {
            initData()
        }
    }

    fun initData() {

        setMyTitle(orderTable!!.order_no)

        mySections = arrayListOf(
            hashMapOf("name" to "商品", "isExpanded" to true, KEY_KEY to PRODUCT_KEY),
            hashMapOf("name" to "訂單", "isExpanded" to true, KEY_KEY to ORDER_KEY),
            hashMapOf("name" to "付款方式", "isExpanded" to true, KEY_KEY to GATEWAY_KEY),
            hashMapOf("name" to "寄送方式", "isExpanded" to true, KEY_KEY to SHIPPING_KEY),
            hashMapOf("name" to "電子發票", "isExpanded" to true, KEY_KEY to INVOICE_KEY),
            hashMapOf("name" to "訂購人資料", "isExpanded" to true, KEY_KEY to MEMBER_KEY),
            hashMapOf("name" to "其他留言", "isExpanded" to true, KEY_KEY to MEMO_KEY)
        )


        orderTable!!.filterRow()
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

            val row: HashMap<String, String> = hashMapOf(TITLE_KEY to productTable!!.name,KEY_KEY to PRODUCT_KEY,VALUE_KEY to "",SHOW_KEY to "",CELL_KEY to "cart","featured_path" to productTable!!.featured_path,"attribute" to attribute_text,"amount" to orderItemTable.amount_show,"quantity" to orderItemTable.quantity.toString())
            productRows.add(row)
        }

        myRows = arrayListOf(
            hashMapOf(KEY_KEY to PRODUCT_KEY, "rows" to productRows),
            hashMapOf(KEY_KEY to ORDER_KEY, "rows" to orderRows),
            hashMapOf(KEY_KEY to GATEWAY_KEY, "rows" to gatewayRows),
            hashMapOf(KEY_KEY to SHIPPING_KEY, "rows" to shippingRows),
            hashMapOf(KEY_KEY to INVOICE_KEY, "rows" to invoiceRows),
            hashMapOf(KEY_KEY to MEMBER_KEY, "rows" to memberRows),
            hashMapOf(KEY_KEY to MEMO_KEY, "rows" to memoRows)
        )

        //order
        var row: HashMap<String, String> = getRowRowsFromMyRowsByKey1(ORDER_NO_KEY)
        row[VALUE_KEY] = orderTable!!.order_no
        row[SHOW_KEY] = orderTable!!.order_no
        replaceRowByKey(ORDER_KEY, ORDER_NO_KEY,row)

        row = getRowRowsFromMyRowsByKey1(AMOUNT_KEY)
        row[VALUE_KEY] = orderTable!!.amount.toString()
        row[SHOW_KEY] = orderTable!!.amount_show
        replaceRowByKey(ORDER_KEY, AMOUNT_KEY,row)

        row = getRowRowsFromMyRowsByKey1(SHIPPING_FEE_KEY)
        row[VALUE_KEY] = orderTable!!.shipping_fee.toString()
        row[SHOW_KEY] = orderTable!!.shipping_fee_show
        replaceRowByKey(ORDER_KEY, SHIPPING_FEE_KEY,row)

        row = getRowRowsFromMyRowsByKey1(TAX_KEY)
        row[VALUE_KEY] = orderTable!!.tax.toString()
        row[SHOW_KEY] = orderTable!!.tax_show
        replaceRowByKey(ORDER_KEY, TAX_KEY,row)

        row = getRowRowsFromMyRowsByKey1(TOTAL_KEY)
        row[VALUE_KEY] = orderTable!!.total.toString()
        row[SHOW_KEY] = orderTable!!.total_show
        replaceRowByKey(ORDER_KEY, TOTAL_KEY,row)

        row = getRowRowsFromMyRowsByKey1(CREATED_AT_KEY)
        row[VALUE_KEY] = orderTable!!.created_at
        row[SHOW_KEY] = orderTable!!.created_at_show
        replaceRowByKey(ORDER_KEY, CREATED_AT_KEY,row)

        row = getRowRowsFromMyRowsByKey1(ORDER_PROCESS_KEY)
        row[VALUE_KEY] = orderTable!!.process
        row[SHOW_KEY] = orderTable!!.order_process_show
        replaceRowByKey(ORDER_KEY, ORDER_PROCESS_KEY,row)

        //gateway
        row = getRowRowsFromMyRowsByKey1(GATEWAY_METHOD_KEY)
        row[VALUE_KEY] = orderTable!!.gateway!!.method
        row[SHOW_KEY] = orderTable!!.gateway!!.method_show
        replaceRowByKey(GATEWAY_KEY, GATEWAY_METHOD_KEY,row)

        row = getRowRowsFromMyRowsByKey1(GATEWAY_AT_KEY)
        row[VALUE_KEY] = orderTable!!.gateway!!.gateway_at
        row[SHOW_KEY] = orderTable!!.gateway!!.gateway_at_show
        replaceRowByKey(GATEWAY_KEY, GATEWAY_AT_KEY,row)

        row = getRowRowsFromMyRowsByKey1(GATEWAY_PROCESS_KEY)
        row[VALUE_KEY] = orderTable!!.gateway!!.process
        row[SHOW_KEY] = orderTable!!.gateway!!.process_show
        replaceRowByKey(GATEWAY_KEY, GATEWAY_PROCESS_KEY,row)

        //shipping
        row = getRowRowsFromMyRowsByKey1(SHIPPING_METHOD_KEY)
        row[VALUE_KEY] = orderTable!!.shipping!!.method
        row[SHOW_KEY] = orderTable!!.shipping!!.method_show
        shippingRows = replaceRowByKey(shippingRows, SHIPPING_METHOD_KEY, row)
        //replaceRowByKey(sectionSHIPPING_KEY, rowSHIPPING_METHOD_KEY,row)

        row = getRowRowsFromMyRowsByKey1(SHIPPING_AT_KEY)
        row[VALUE_KEY] = orderTable!!.shipping!!.shipping_at
        row[SHOW_KEY] = orderTable!!.shipping!!.shipping_at_show
        shippingRows = replaceRowByKey(shippingRows, SHIPPING_AT_KEY, row)
        //replaceRowByKey(sectionSHIPPING_KEY, rowSHIPPING_AT_KEY,row)

        row = getRowRowsFromMyRowsByKey1(SHIPPING_PROCESS_KEY)
        row[VALUE_KEY] = orderTable!!.shipping!!.process
        row[SHOW_KEY] = orderTable!!.shipping!!.process_show
        shippingRows = replaceRowByKey(shippingRows, SHIPPING_PROCESS_KEY, row)
        //replaceRowByKey(sectionSHIPPING_KEY, rowSHIPPING_PROCESS_KEY,row)

        if (orderTable!!.shipping!!.method == "store_711" || orderTable!!.shipping?.method == "store_family") {
            row = hashMapOf(TITLE_KEY to "到達商店時間", KEY_KEY to SHIPPING_STORE_AT_KEY, VALUE_KEY to orderTable!!.shipping!!.store_at, SHOW_KEY to orderTable!!.shipping!!.store_at_show, CELL_KEY to "text")
            shippingRows.add(row)
        }

        row = hashMapOf(TITLE_KEY to "到貨時間", KEY_KEY to SHIPPING_COMPLETE_AT_KEY, VALUE_KEY to orderTable!!.shipping!!.complete_at, SHOW_KEY to orderTable!!.shipping!!.complete_at_show, CELL_KEY to "text")
        shippingRows.add(row)

        if (orderTable!!.shipping!!.back_at.length > 0) {
            row = hashMapOf(TITLE_KEY to "退貨時間", KEY_KEY to SHIPPING_BACK_AT_KEY, VALUE_KEY to orderTable!!.shipping!!.back_at, SHOW_KEY to orderTable!!.shipping!!.back_at_show, CELL_KEY to "text")
            shippingRows.add(row)
        }
        replaceRowsByKey(SHIPPING_KEY, shippingRows)

        //invoice
        val invoice_type: String = orderTable!!.invoice_type
        row = getRowFromKey(invoiceRows, INVOICE_TYPE_KEY)
        row[VALUE_KEY] = invoice_type
        row[SHOW_KEY] = orderTable!!.invoice_type_show
        invoiceRows = replaceRowByKey(invoiceRows, INVOICE_TYPE_KEY, row)

        if (invoice_type == "company") {
            row = hashMapOf(TITLE_KEY to "公司或行號名稱", KEY_KEY to INVOICE_COMPANY_NAME_KEY, VALUE_KEY to orderTable!!.invoice_company_name, SHOW_KEY to orderTable!!.invoice_company_name, CELL_KEY to "text")
            invoiceRows.add(row)
            row = hashMapOf(TITLE_KEY to "統一編號", KEY_KEY to INVOICE_COMPANY_TAX_KEY, VALUE_KEY to orderTable!!.invoice_company_tax, SHOW_KEY to orderTable!!.invoice_company_tax, CELL_KEY to "text")
            invoiceRows.add(row)
        }

        row = getRowFromKey(invoiceRows, INVOICE_EMAIL_KEY)
        row[VALUE_KEY] = orderTable!!.invoice_email
        row[SHOW_KEY] = orderTable!!.invoice_email
        invoiceRows = replaceRowByKey(invoiceRows, INVOICE_EMAIL_KEY, row)

        replaceRowsByKey(INVOICE_KEY, invoiceRows)
        //myRows[4]["rows"] = invoiceRows

        //member
        row = getRowRowsFromMyRowsByKey1(NAME_KEY)
        row[VALUE_KEY] = orderTable!!.order_name
        row[SHOW_KEY] = orderTable!!.order_name
        replaceRowByKey(MEMBER_KEY, NAME_KEY,row)

        row = getRowRowsFromMyRowsByKey1(MOBILE_KEY)
        row[VALUE_KEY] = orderTable!!.order_tel
        row[SHOW_KEY] = orderTable!!.order_tel_show
        replaceRowByKey(MEMBER_KEY, MOBILE_KEY,row)

        row = getRowRowsFromMyRowsByKey1(EMAIL_KEY)
        row[VALUE_KEY] = orderTable!!.order_email
        row[SHOW_KEY] = orderTable!!.order_email
        replaceRowByKey(MEMBER_KEY, EMAIL_KEY,row)

        row = getRowRowsFromMyRowsByKey1(ADDRESS_KEY)
        row[VALUE_KEY] = orderTable!!.order_address
        row[SHOW_KEY] = orderTable!!.order_address
        replaceRowByKey(MEMBER_KEY, ADDRESS_KEY,row)

        //memo
        row = getRowRowsFromMyRowsByKey1(MEMO_KEY)
        row[VALUE_KEY] = orderTable!!.memo
        row[SHOW_KEY] = orderTable!!.memo
        replaceRowByKey(MEMO_KEY, MEMO_KEY,row)

        initAdapter(true)
        //        recyclerView.setHasFixedSize(true)
        if (refreshLayout != null) {
            setRefreshListener()
        }
        setRecyclerViewScrollListener()
    }

    override fun initAdapter(include_section: Boolean) {
        adapter.clear()
        if (include_section) {
            for ((idx, mySection) in mySections.withIndex()) {
                val section = Section()
                adapterSections.add(section)
                val title: String = mySection["name"] as String
                val isExpanded: Boolean = mySection["isExpanded"] as Boolean
                val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)
                val items = generateItems(idx)
                section.addAll(items)
                expandableGroup.add(section)

                adapter.add(expandableGroup)
            }
        }

        recyclerView.adapter = adapter
    }

    override fun generateItems(section: Int): ArrayList<Item> {

        if (myRows.size == 0) {
            return arrayListOf()
        }
        items.clear()
        var sectionKey: String = ""
        val sectionRow: HashMap<String, Any> = myRows[section]
        val tmp: String? = sectionRow["key"] as? String
        if (tmp != null) {
            sectionKey = tmp
        }

        if (!sectionRow.containsKey("rows")) {
            return arrayListOf()
        }

        @Suppress("UNCHECKED_CAST")
        val rows: ArrayList<HashMap<String, String>> =
            sectionRow["rows"] as ArrayList<HashMap<String, String>>

        for ((idx, row) in rows.withIndex()) {

            var rowKey: String = ""
            if (row.containsKey("key")) {
                rowKey = row["key"]!!
            }
            var title: String = ""
            if (row.containsKey("title")) {
                title = row["title"]!!
            }
            var value: String = ""
            if (row.containsKey("value")) {
                value = row["value"]!!
            }
            var show: String = ""
            if (row.containsKey("show")) {
                show = row["show"]!!
            }

            val cell_type: String? = row["cell"]

            //var formItemAdapter: FormItemAdapter1? = null
            if (cell_type == "cart") {
                var featured_path = FEATURED_PATH
                if (row.containsKey("featured_path") && row["featured_path"]!!.length > 0) {
                    featured_path = row["featured_path"]!!
                }
                var attribute = ""
                if (row.containsKey("attribute")) {
                    attribute = row["attribute"]!!
                }
                var amount = ""
                if (row.containsKey("amount")) {
                    amount = row["amount"]!!
                }
                var quantity = ""
                if (row.containsKey("quantity")) {
                    quantity = row["quantity"]!!
                }
                val cartItemItem = CartItemItem(
                    this,
                    sectionKey,
                    rowKey,
                    title,
                    featured_path,
                    attribute,
                    amount,
                    quantity
                )
                items.add(cartItemItem)
            } else if (cell_type == "text") {
                val item = PlainAdapter1(title, show)
                items.add(item)
            } else if (cell_type == "more") {
                val item = MoreAdapter1(sectionKey, rowKey, title, value, show, this)
                items.add(item)
            }
        }

        return items
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println("onActivityResult(), requestCode:" + requestCode + ", resultCode" + resultCode)
        if (requestCode == PaymentkitManager.RequestCode_CreatePayment) {
            PaymentkitManager.createPaymentResult(this, resultCode, data, CallbackFunction {
                when (it.callbackStatus) {
                    CallbackStatus.Success -> if (it.getRtnCode() == 1) {
                        info("完成付款，我們將儘速將商品運送給您，謝謝您的光顧!!", "", "關閉") {
                            refresh()
                            //goProduct()
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
                                ", Msg=" + it.getRtnMsg())
                    }

                    CallbackStatus.Cancel -> {
                        warning("交易取消")
                    }
                    else -> {
                        warning("回傳值無法解析，請洽管理員")
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

    fun getClassType(it: KProperty1<out SuperModel, Any?>): String {
        val type = it.returnType.toString()
        val tmps = JSONParse.getSubType(type)
        val subType = tmps.get("type")!!

        return subType
    }
}

class PaymentItem(val context: Context, val row: HashMap<String, String>): Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.titleLbl.text = row["name"]
        viewHolder.contentLbl.text = row["value"]
    }

    override fun getLayout() = R.layout.payment_cell
}



























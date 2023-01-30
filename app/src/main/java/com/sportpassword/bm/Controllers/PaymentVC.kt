package com.sportpassword.bm.Controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.OneItemAdapter
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.databinding.ActivityPaymentVcBinding
import com.sportpassword.bm.member
import tw.com.ecpay.paymentgatewaykit.manager.*

class PaymentVC : MyTableVC() {

    private lateinit var binding: ActivityPaymentVcBinding
    //private lateinit var view: ViewGroup

    var ecpay_token: String = ""
    var order_token: String = ""
    var ecpay_token_ExpireDate: String = ""
    var orderTable: OrderTable? = null

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

    var bottom_button_count: Int = 3
    val button_width: Int = 400

    var source: String = "order"

    var isECPay: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        if (intent.hasExtra("ecpay_token")) {
            ecpay_token = intent.getStringExtra("ecpay_token")!!
        }
        if (intent.hasExtra("order_token")) {
            order_token = intent.getStringExtra("order_token")!!
        }
        if (intent.hasExtra("tokenExpireDate")) {
            ecpay_token_ExpireDate = intent.getStringExtra("ecpay_token_ExpireDate")!!
        }
        if (intent.hasExtra("source")) {
            source = intent.getStringExtra("source")!!
        }

        title = getString(R.string.app_name)

        setMyTitle(title)

        dataService = OrderService
        recyclerView = binding.paymentList
//        refreshLayout = refresh

        findViewById<FrameLayout>(R.id.mask) ?. let { mask ->
            maskView = mask
        }
        setRefreshListener()

        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf("product_icon_view" to "false"))
        oneSectionAdapter.setOneSection(oneSections)
        recyclerView.adapter = oneSectionAdapter

        //initAdapter()
        //refresh()

        setBottomButtonPadding()

        findViewById<Button>(R.id.submitBtn) ?. let {
            it.text = "付款"
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            it.setOnClickListener {
                backBtnPressed()
            }
        }

        init()
        (ecpay_token.isNotEmpty() then {
            toECPay()
            isECPay = true
        }) ?: refresh()
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

    fun setBottomButtonPadding() {

        val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
        //val leading: Int = bottom_button_count * padding + (bottom_button_count - 1) * button_width

        findViewById<Button>(R.id.submitBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.threeBtn) ?. let {
            if (it.visibility == View.VISIBLE) {
                val params: ViewGroup.MarginLayoutParams =
                    it.layoutParams as ViewGroup.MarginLayoutParams
                params.width = button_width
                params.marginStart = padding
                it.layoutParams = params
            }
        }

        findViewById<Button>(R.id.cancelBtn) ?. let {
            val params: ViewGroup.MarginLayoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
            params.width = button_width
            params.marginStart = padding
            it.layoutParams = params
        }
    }

    fun toECPay() {

        PaymentkitManager.initialize(this, ServerType.Stage)
        //PaymentkitManager.initialize(this, ServerType.Prod)
        PaymentkitManager.createPayment(this, ecpay_token, LanguageCode.zhTW, true, title, PaymentkitManager.RequestCode_CreatePayment)
    }

    override fun refresh() {
        runOnUiThread {
            Loading.show(view)
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
                Loading.hide(view)
            }
        }
    }

    override fun genericTable() {
        try {
            //println(jsonString)
            orderTable = Gson().fromJson(jsonString, OrderTable::class.java)
        } catch (e: java.lang.Exception) {
            warning(e.localizedMessage)
        }
        if (orderTable == null) {
            warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
        } else {
            orderTable!!.filterRow()
            //println(orderTable!!.canReturn)
            runOnUiThread {
                if (orderTable!!.all_process > 1) {//已經付費了
                    //footer.visibility = View.GONE
                    findViewById<Button>(R.id.submitBtn) ?. let {
                        it.visibility = View.GONE
                        bottom_button_count--
                    }

                    if (!orderTable!!.canReturn) {
                        findViewById<Button>(R.id.threeBtn) ?. let {
                            it.visibility = View.GONE
                            bottom_button_count--
                        }
                    }
                    setBottomButtonPadding()
                } else {
                    //footer.visibility = View.VISIBLE
                    if (!orderTable!!.canReturn) {
                        findViewById<Button>(R.id.threeBtn) ?. let {
                            it.visibility = View.GONE
                            bottom_button_count--
                        }
                    }
                    setBottomButtonPadding()
                }
            }

            updateMemberCoin()
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

        if (source == "member") {
            findViewById<Button>(R.id.cancelBtn) ?. let {
                it.text = "上一頁"
            }
        } else if (source == "order") {
            findViewById<Button>(R.id.cancelBtn) ?. let {
                it.text = "結束"
            }
        } else {
            findViewById<Button>(R.id.cancelBtn) ?. let {
                it.text = "取消"
            }
        }

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

        //order
        rows.clear()
        var row = OneRow("編號", orderTable!!.order_no, orderTable!!.order_no, ORDER_NO_KEY, "text")
        rows.add(row)
        row = OneRow("商品金額", orderTable!!.amount.toString(), orderTable!!.amount_show, AMOUNT_KEY, "text")
        rows.add(row)
        row = OneRow("運費", orderTable!!.shipping_fee.toString(), orderTable!!.shipping_fee_show, SHIPPING_FEE_KEY, "text")
        rows.add(row)
        row = OneRow("税", orderTable!!.tax.toString(), orderTable!!.tax_show, TAX_KEY, "text")
        rows.add(row)
        row = OneRow("總金額", orderTable!!.total.toString(), orderTable!!.total_show, TOTAL_KEY, "text")
        rows.add(row)
        row = OneRow("建立時間", orderTable!!.created_at, orderTable!!.created_at_show, CREATED_AT_KEY, "text")
        rows.add(row)
        row = OneRow("狀態", orderTable!!.process, orderTable!!.order_process_show, ORDER_PROCESS_KEY, "text")
        rows.add(row)
        section = makeSectionRow("訂單", ORDER_KEY, rows, true)
        oneSections.add(section)

        //gateway
        rows.clear()
        row = OneRow("付款方式", orderTable!!.gateway!!.method, orderTable!!.gateway!!.method_show, GATEWAY_METHOD_KEY, "more", KEYBOARD.default, "", "", false, false)
        rows.add(row)
        row = OneRow("付款狀態", orderTable!!.gateway!!.process, orderTable!!.gateway!!.process_show, GATEWAY_PROCESS_KEY, "text")
        rows.add(row)
        row = OneRow("付款時間", orderTable!!.gateway!!.complete_at, orderTable!!.gateway!!.complete_at_show, GATEWAY_AT_KEY, "text")
        rows.add(row)
        section = makeSectionRow("付款方式", GATEWAY_KEY, rows, true)
        oneSections.add(section)

        //shipping
        rows.clear()
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
        if (orderTable!!.shipping!!.return_at.length > 0) {
            row = OneRow(
                "退貨時間",
                orderTable!!.shipping!!.return_at,
                orderTable!!.shipping!!.return_at_show,
                SHIPPING_BACK_AT_KEY,
                "text"
            )
            rows.add(row)

        }
        section = makeSectionRow("到貨方式", SHIPPING_KEY, rows, true)
        oneSections.add(section)

        //invoice
        val invoice_type: String = orderTable!!.invoice_type
        rows.clear()
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
        row = OneRow("發票號碼", orderTable!!.invoice_no, orderTable!!.invoice_no, INVOICE_NO_KEY, "text")
        rows.add(row)
        row = OneRow("開立時間", orderTable!!.invoice_at, orderTable!!.invoice_at, INVOICE_AT_KEY, "text")
        rows.add(row)

        section = makeSectionRow("電子發票", SHIPPING_KEY, rows, true)
        oneSections.add(section)

        //member
        rows.clear()
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

        //memo
        rows.clear()
        row = OneRow("留言", orderTable!!.memo, orderTable!!.memo, MEMO_KEY, "text")
        rows.add(row)
        section = makeSectionRow("其他留言", ORDER_KEY, rows, true)
        oneSections.add(section)

        //return
        if (orderTable!!.`return` != null) {
            rows.clear()
            row = OneRow("退貨編號", orderTable!!.`return`!!.sn_id, orderTable!!.`return`!!.sn_id, RETURN_SN_ID_KEY, "text")
            rows.add(row)
            row = OneRow("編號到期時間", orderTable!!.`return`!!.expire_at, orderTable!!.`return`!!.expire_at_show, RETURN_EXPIRE_AT_KEY, "text")
            rows.add(row)
            row = OneRow("退貨時間", orderTable!!.`return`!!.created_at, orderTable!!.`return`!!.created_at_show, RETURN_CREATED_AT_KEY, "text")
            rows.add(row)
            section = makeSectionRow("退貨", RETURN_KEY, rows, true)
            oneSections.add(section)
        }
    }

    override fun cellMoreClick(sectionIdx: Int, rowIdx: Int) {

//        fillPopupRows()
        val rows: ArrayList<OneRow> = fillPopupRows()
        val section: OneSection = OneSection("付款", GATEWAY_KEY, true, rows)

        val tableViewHeight: Int = rowHeight * rows.size
        showTableLayer(tableViewHeight)

        val panelAdapter = OneItemAdapter(this, sectionIdx, section, this, hashMapOf())

        layerTableView!!.adapter = panelAdapter
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
        } else if (method == GATEWAY.store_cvs) {
            val payment_no: String = orderTable!!.gateway!!.payment_no
            val expire_at: String = orderTable!!.gateway!!.expire_at_show
            row = OneRow("繳款代碼", payment_no, payment_no, PAYMENT_NO_KEY, "text")
            rows.add(row)
            row = OneRow("到期日", expire_at, expire_at, EXPIRE_AT_KEY, "text")
            rows.add(row)
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
        } else if (method == GATEWAY.credit_card) {
            val card6No: String = orderTable!!.gateway!!.card6No
            val card4No: String = orderTable!!.gateway!!.card4No
            row = OneRow("信用卡前6碼", card6No, card6No, "card6No", "text")
            rows.add(row)
            row = OneRow("信用卡前6碼", card4No, card4No, "card4No", "text")
            rows.add(row)
        } else if (
            method == GATEWAY.store_pay_711 ||
            method == GATEWAY.store_pay_family ||
            method == GATEWAY.store_pay_hilife ||
            method == GATEWAY.store_pay_ok
        ) {
            val CVSPaymentNo: String = orderTable!!.shipping!!.CVSPaymentNo
            val CVSValidationNo: String = orderTable!!.shipping!!.CVSValidationNo
            val paymentNo: String = CVSPaymentNo + CVSValidationNo
            row = OneRow("條碼", paymentNo, paymentNo, "paymentNo", "text")
            rows.add(row)
        }

        return rows
    }

    fun updateOrder() {

        //mask()
        val params: HashMap<String, String> = hashMapOf("token" to order_token, "member_token" to member.token!!,"do" to "update")
        params["expire_at"] = expire_at
        params["trade_no"] = trade_no

        //1.信用卡回傳，(1)刷卡時間，(2)處理費，(3)信用卡前6碼，(4)信用卡後4碼
        //2.超商代碼回傳，(1)代碼，(2)代碼檢視網址
        //3.barcode已經停止使用
        //4.虛擬匯款帳號，(1)銀行代號，(2)銀行帳號
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
            //unmask()
            if (success) {

                try {
                    val successTable: UpdateResTable = Gson().fromJson<UpdateResTable>(
                        OrderService.jsonString,
                        UpdateResTable::class.java
                    )
                    if (!successTable.success) {
                        val msg: String = "錯誤訊息：" + successTable.msg
                        info(successTable.msg)
                    } else {
                        successTable.model ?. let {
                            val shippingTable: ShippingTable = it.shipping!!
                            val method: SHIPPING = SHIPPING.stringToEnum(shippingTable.method)
                            if (method == SHIPPING.store_711 || method == SHIPPING.store_family || method == SHIPPING.store_hilife || method == SHIPPING.store_ok) {
                                runOnUiThread {
                                    info("訂單已經完成付款，是否前往選擇超商門市？", "關閉", "是") {
                                        toWebView(it.token, this)
                                    }
                                }
                            } else {
                                runOnUiThread {
                                    info("完成付款，我們將儘速將商品運送給您，謝謝您的光顧!!", "", "關閉") {
                                        finishAffinity()
                                        toPayment(order_token)
                                    }
                                }
                            }
                        }
                    }
                } catch (e: JsonParseException) {
                    warning(e.localizedMessage!!)
                    //println(e.localizedMessage)
                }
            } else {
                warning(OrderService.msg, false, "關閉") {
                    finishAffinity()
                    toPayment(order_token)
                }
            }
        }
    }

    fun updateMemberCoin() {

        if (orderTable != null) {
            val items: ArrayList<OrderItemTable> = orderTable!!.items
            if (items.size > 0) {
                val item: OrderItemTable = items[0]
                val product: ProductTable = item.product!!
                if (product.type == "coin") {
                    member.updateMemberCoin(product.coin)
                }
            }
        }

    }

    @Deprecated("Deprecated in Java")
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
                        updateOrder()
                    } else {
                        if (it.getRtnCode() == 10100058) {
                            msg = "付款失敗，有可能是信用卡刷卡錯誤，請檢查信用卡資訊是否有誤"
                        }
                        warning("Fail Code=" + it.getRtnCode() +
                                ", Msg=" + msg, false, "關閉") {
                            finishAffinity()
                            toPayment(order_token)
                        }
                    }

                    CallbackStatus.Fail -> {
                        if (it.getRtnCode() == 10100058) {
                            msg = "付款失敗，有可能是信用卡刷卡錯誤，請檢查信用卡資訊是否有誤"
                        }
                        warning("Fail Code=" + it.getRtnCode() +
                                ", Msg=" + msg, false, "關閉") {
                            finishAffinity()
                            toPayment(order_token)
                        }
                    }

                    CallbackStatus.Cancel -> {
                        warning("交易取消", false, "關閉") {
                            finishAffinity()
                            toPayment(order_token)
                        }
                    }
                    else -> {
                        warning("回傳值無法解析，請洽管理員", false, "關閉") {
                            finishAffinity()
                            toPayment(order_token)
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

    fun submitButtonPressed(view: View) {
        ecpay_token = orderTable!!.ecpay_token
        toECPay()
    }

    override fun prev() {
        if (source == "order") {
            finishAffinity()
            toProduct()
        } else {
            super.prev()
        }
    }

    fun backBtnPressed() {
        if (order_token.isNotEmpty()) {
            Loading.show(view)
            OrderService.ezshipReturnCode(this, order_token) { success ->
                runOnUiThread {
                    Loading.hide(view)
                    try {
                        val successTable: BackResTable = Gson().fromJson<BackResTable>(
                            OrderService.jsonString,
                            BackResTable::class.java
                        )
                        if (!successTable.success) {
                            val msg: String = "錯誤訊息：" + successTable.msg + "\n" + "錯誤編號：" + successTable.error_code
                            info(successTable.msg)
                        } else {
                            val msg: String = "退貨編號：" + successTable.sn_id + "\n" + "退貨編號使用期限：" + successTable.expire_at
                            info(msg)
                        }
                    } catch (e: JsonParseException) {
                        warning(e.localizedMessage!!)
                        //println(e.localizedMessage)
                    }
                }
            }
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
}

class BackResTable {

    var success: Boolean = false
    var msg: String = ""
    var sn_id: String = ""
    var expire_at: String = ""
    var order_id: Int = 0
    var error_code: String = ""
    var url: String = ""
}

class UpdateResTable {

    var success: Boolean = false
    var msg: String = ""
    var id: Int = 0
    var update: String = ""
    var model: OrderTable? = null
}




























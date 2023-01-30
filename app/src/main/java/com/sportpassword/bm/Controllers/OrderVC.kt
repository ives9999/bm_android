package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.view.*
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.get
import androidx.core.view.size
import com.google.gson.Gson
import com.sportpassword.bm.Adapters.OneItemAdapter
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Data.*
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.databinding.ActivityOrderVcBinding

class OrderVC : MyTableVC() {

    private lateinit var binding: ActivityOrderVcBinding
    //private lateinit var view: ViewGroup

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

    var bottom_button_count: Int = 2
    val button_width: Int = 400

    var grand_total: Int = 0

//    var tableView: RecyclerView? = null


//    var productRows: ArrayList<HashMap<String, String>> = arrayListOf()
//    var amountRows: ArrayList<HashMap<String, String>> = arrayListOf()
//    var gatewayRows: ArrayList<HashMap<String, String>> = arrayListOf()
//    var shippingRows: ArrayList<HashMap<String, String>> = arrayListOf()
//    var invoiceRows: ArrayList<HashMap<String, String>> = arrayListOf()
//
//    var invoiceFixedRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf("title" to "發票(目前僅提供電子發票)","key" to INVOICE_KEY,"value" to "","show" to "","cell" to "more")
//    )
//
//    var invoiceOptionRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf("title" to "個人","key" to PERSONAL_KEY,"value" to "true","show" to "","cell" to "radio"),
//        hashMapOf("title" to "公司","key" to COMPANY_KEY,"value" to "false","show" to "","cell" to "radio")
//    )
    val invoiceOptionRows: ArrayList<OneRow> = arrayListOf(
        OneRow("個人,公司", INVOICE_PERSONAL_KEY, "${INVOICE_PERSONAL_KEY},${INVOICE_COMPANY_KEY}", INVOICE_KEY, "radio")
    )

    val invoicePersonalRows: ArrayList<OneRow> = arrayListOf(
        OneRow("EMail", member.email!!, member.email!!, INVOICE_EMAIL_KEY, "textField", KEYBOARD.default, "service@bm.com")
//        hashMapOf("title" to "EMail","key" to INVOICE_EMAIL_KEY,"value" to "${member.email}","show" to "${member.email}","cell" to "textField","keyboard" to KEYBOARD.emailAddress.toString())
    )

    var invoiceCompanyRows: ArrayList<OneRow> = arrayListOf(
        OneRow("統一編號", "", "", INVOICE_COMPANY_TAX_KEY, "textField", KEYBOARD.numberPad, "12345678"),
        OneRow("公司行號抬頭", "", "", INVOICE_COMPANY_NAME_KEY, "textField", KEYBOARD.default, "羽球有限公司"),
        OneRow("EMail", member.email!!, member.email!!, INVOICE_EMAIL_KEY, "textField", KEYBOARD.emailAddress, "service@bm.com")
    )
//
//    var memberRows: ArrayList<HashMap<String, String>> = arrayListOf()
//
//    val memoRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf("title" to "留言","key" to MEMO_KEY,"value" to "","show" to "","cell" to "memo","keyboard" to KEYBOARD.default.toString())
//    )


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityOrderVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setMyTitle("訂單")

        if (intent.hasExtra("product_token")) {
            product_token = intent.getStringExtra("product_token")
        }

        //initAdapter(true)
        dataService = CartService
        recyclerView = binding.orderList

        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf())
        oneSectionAdapter.setOneSection(oneSections)
        recyclerView.adapter = oneSectionAdapter

        refreshLayout = binding.refresh
        setRefreshListener()
        setBottomButtonPadding()

        init()
        refresh()
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

//    override fun initAdapter(include_section: Boolean) {
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
//            }
//        }
//    }

    override fun refresh() {

        page = 1
        theFirstTime = true
//        adapter.clear()
//        items.clear()
        oneSections.clear()
        getDataStart(page, perPage, member.token)
    }

    override fun getDataStart(_page: Int, _perPage: Int, token: String?) {
        Loading.show(view)
        loading = true

        //單一品項購買，沒有使用購物車，直接結帳
        if (product_token != null) {
            val params: HashMap<String, String> = hashMapOf("token" to product_token!!, "member_token" to member.token!!)
            ProductService.getOne(this, params) { success ->
                jsonString = ProductService.jsonString
                getDataEnd(success)
            }

        } else {
            dataService.getList(this, token, params, _page, _perPage) { success ->
                jsonString = dataService.jsonString
                getDataEnd(success)
            }
        }
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            //if (theFirstTime) {

            if (jsonString != null && jsonString!!.isNotEmpty()) {
                //println(dataService.jsonString)
                runOnUiThread {
                    genericTable()
                    initData()
                    oneSectionAdapter.setOneSection(oneSections)
                    oneSectionAdapter.notifyDataSetChanged()
                }
            } else {
                warning("沒有取得回傳的json字串，請洽管理員")
            }

            //}

            //notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        runOnUiThread {
            Loading.hide(view)
        }
        loading = false
        refreshLayout!!.isRefreshing = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    override fun genericTable() {

        if (product_token != null) {
            productTable = jsonToModel<ProductTable>(ProductService.jsonString)
            if (productTable == null) {
                warning("無此商品，請洽管理員")
            }
        } else {
            cartsTable = jsonToModels<CartsTable>(CartService.jsonString)
            if (cartsTable == null) {
                warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
            } else {
                if (cartsTable!!.rows.size != 1) {
                    warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
                } else {
                    //var amount: Int = 0
                    cartTable = cartsTable!!.rows[0]
                    cartitemsTable = cartTable!!.items
//                productRows.clear()
                }
            }
        }
    }

    fun initData() {

        var amount: Int = 0

        var rows: ArrayList<OneRow> = arrayListOf()
        var needShipping: Boolean = true
        var row: OneRow = OneRow()
        var section = makeSectionRow("", "", rows)

        //cart
        if (cartitemsTable.size > 0) {
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

    //            val row:HashMap<String, String> = hashMapOf("title" to productTable!!.name,"key" to PRODUCT_KEY,"value" to "","show" to "","cell" to "cart","featured_path" to productTable!!.featured_path,"attribute" to attribute_text,"amount" to cartItemTable.amount_show,"quantity" to cartItemTable.quantity.toString())
                row = OneRow(
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
                    productTable!!.featured_path,
                    attribute_text,
                    cartItemTable.amount_show,
                    cartItemTable.quantity.toString()
                )
                rows.add(row)
                //productRows.add(row)
            }
            section = makeSectionRow("商品", PRODUCT_KEY, rows, true)
            oneSections.add(section)

            //price
            rows.clear()
            val amount_show: String = amount.formattedWithSeparator()
            row = OneRow("商品金額", amount.toString(), "NT$ ${amount_show}", "amount", "text")
            rows.add(row)
            //var shipping_fee: Int = 60
            var shipping_fee: Int = 0
            if (amount > 1000) { shipping_fee = 0}
            val shipping_fee_show: String = shipping_fee.formattedWithSeparator()

            row = OneRow("運費", shipping_fee.toString(), "NT$ ${shipping_fee_show}", SHIPPING_FEE_KEY, "text")
            rows.add(row)

            //val tax: Int = (amount.toDouble() * 0.05).toInt()
            val tax: Int = 0
            val tax_show: String = tax.formattedWithSeparator()
            row = OneRow("税", tax.toString(), "NT$ ${tax_show}", TAX_KEY, "text")
            rows.add(row)

            val total: Int = amount + shipping_fee + tax
            val total_show: String = total.formattedWithSeparator()
            row = OneRow("總金額", total.toString(), "NT$ ${total_show}", TOTAL_KEY, "text")
            rows.add(row)
            section = makeSectionRow("金額", AMOUNT_KEY, rows, true)
            oneSections.add(section)
        } else if (productTable != null) {
            productTable!!.filterRow()
            amount += productTable!!.prices[0].price_member
            if (productTable!!.type == "coin") {
                needShipping = false
            }

            row = OneRow(productTable!!.name, "", "", PRODUCT_KEY, "cart")
            row.feature_path = productTable!!.featured_path
            row.amount = "NT$" + amount.toString()
            row.quantity = "1"

            rows.add(row)
            section = makeSectionRow("商品名稱", PRODUCT_KEY, rows, true)
            oneSections.add(section)

            if (productTable!!.attributes.size > 0) {
                rows = arrayListOf()
                for (attribute in productTable!!.attributes) {
                    var tmp: String = attribute.attribute
                    tmp = tmp.replace("{", "")
                    tmp = tmp.replace("}", "")
                    tmp = tmp.replace("\"", "")

                    //show is 湖水綠,極致黑,經典白,太空灰
                    //name is 顏色
                    //key is color
                    val value: String = ""
                    val alias: String = attribute.alias
                    row = OneRow(attribute.name, value, tmp, alias, "tag")
                    rows.add(row)
                }
                section = makeSectionRow("商品屬性", ATTRIBUTE_KEY, rows, true)
                oneSections.add(section)
            }

            rows.clear()
            val min: String = productTable!!.order_min.toString()
            val max: String = productTable!!.order_max.toString()
            val quantity: String = "1"
            row = OneRow("數量", quantity, "${min},${max}", QUANTITY_KEY, "number")
            rows.add(row)

            row = OneRow("小計", "", "", SUBTOTAL_KEY, "text")
            rows.add(row)
            row = OneRow("總計", "", "", TOTAL_KEY, "text")
            rows.add(row)
            section = makeSectionRow("款項", AMOUNT_KEY, rows, true)
            oneSections.add(section)

            selected_price = productTable!!.prices[selected_idx].price_member
            updateSubTotal()
        }

        //gateway
//        gatewayRows.clear()
        rows.clear()
        var titles: Array<String> = productTable!!.gateway.toArray()
        var tmp: ArrayList<String> = arrayListOf()
        for (title in titles) {
            tmp.add(GATEWAY.stringToEnum(title).chineseName)
        }

        row = OneRow(tmp.joinToString(","), GATEWAY.credit_card.toEnglishString(), productTable!!.gateway, GATEWAY_KEY, "radio")
        rows.add(row)
        section = makeSectionRow("付款方式", GATEWAY_KEY, rows, true)
        oneSections.add(section)

        if (needShipping) {
            //shipping
            rows.clear()
            titles = productTable!!.shipping.toArray()
            tmp = arrayListOf()
            for (title in titles) {
                tmp.add(SHIPPING.stringToEnum(title).chineseName)
            }
            row = OneRow(
                tmp.joinToString(","),
                SHIPPING.direct.toEnglishString(),
                productTable!!.shipping,
                SHIPPING_KEY,
                "radio"
            )
            rows.add(row)
            section = makeSectionRow("到貨方式", SHIPPING_KEY, rows, true)
            oneSections.add(section)
        }

        rows.clear()
        row = OneRow("發票(目前僅提供電子發票)", "personal", "個人", INVOICE_KEY, "more", KEYBOARD.default, "", "", false, false)
        rows.add(row)
        rows.addAll(invoicePersonalRows)
        section = makeSectionRow("電子發票", INVOICE_KEY, rows, true)
        oneSections.add(section)

        //member
        if (needShipping) {
            rows.clear()
            row = OneRow(
                "姓名",
                member.name!!,
                member.name!!,
                NAME_KEY,
                "textField",
                KEYBOARD.default,
                "王大明"
            )
            rows.add(row)
            row = OneRow(
                "電話",
                member.mobile!!,
                member.mobile!!,
                MOBILE_KEY,
                "textField",
                KEYBOARD.numberPad,
                "0939123456"
            )
            rows.add(row)
            row = OneRow(
                "EMail",
                member.email!!,
                member.email!!,
                EMAIL_KEY,
                "textField",
                KEYBOARD.emailAddress,
                "service@bm.com"
            )
            rows.add(row)
            row = OneRow(
                "住址",
                member.address!!,
                member.address!!,
                ADDRESS_KEY,
                "textField",
                KEYBOARD.default,
                "台北市信義區中山路60號"
            )
            rows.add(row)
            section = makeSectionRow("收件人資料", MEMBER_KEY, rows, true)
            oneSections.add(section)

            //memo
            rows = arrayListOf()
            row = OneRow("留言", "", "", MEMO_KEY, "textField", KEYBOARD.default, "請於上班時間送達")
            rows.add(row)
            section = makeSectionRow("其他留言", MEMO_KEY, rows, true)
            oneSections.add(section)
        }
    }

    fun updateSubTotal() {

        sub_total = selected_price * selected_number
        val row = getRowFromRowKey(SUBTOTAL_KEY)
        row.value = sub_total.toString()
        row.show = "NT$ " + sub_total.toString() + "元"
        updateTotal()
    }

    fun updateTotal() {

        total = sub_total + shippingFee
        val row = getRowFromRowKey(TOTAL_KEY)
        row.value = total.toString()
        row.show = "NT$ " + total.toString() + "元"
    }

    override fun cellEdit(sectionIdx: Int, rowIdx: Int) {

        val token: String = cartitemsTable[rowIdx].token
        toAddCart(null, token, this)
    }

    override fun cellDelete(sectionIdx: Int, rowIdx: Int) {

        val token: String = cartitemsTable[rowIdx].token
        warning("是否確定要刪除呢？", "取消", "刪除") {
            dataService.delete(this, "cart_item", token) { success ->
                if (success) {
                    refresh()

                    //是否要顯示購物車的圖示在top
                    cartItemCount -= 1
                    session.edit().putInt("cartItemCount", cartItemCount).apply()
                } else {
                    warning(dataService.msg)
                }
            }
        }
    }

    override fun cellTextChanged(sectionIdx: Int, rowIdx: Int, str: String) {
        oneSections[sectionIdx].items[rowIdx].value = str
    }

    override fun cellSetTag(sectionIdx: Int, rowIdx: Int, value: String, isChecked: Boolean) {
        val row: OneRow = oneSections[sectionIdx].items[rowIdx]
        //replaceRowFromIdx(sectionIdx, rowIdx, row)
        row.value = value
    }

    override fun cellRadioChanged(key: String, sectionIdx: Int, rowIdx: Int, idx: Int, radioGroup: RadioGroup) {
        var row: OneRow = oneSections[sectionIdx].items[rowIdx]

        val tmp: Array<String> = oneRowShowToArray(row)
        for ((idx1, value) in tmp.withIndex()) {

            if (idx1 == idx) {
                row.value = value
                break
            }
        }

        if (key == GATEWAY_KEY) {

            row = oneSections[sectionIdx].items[rowIdx]

            //1.先取得該商品的所有付款方式的陣列
            val shows: Array<String> = oneRowShowToArray(row)
            val gateway_enum: GATEWAY = GATEWAY.stringToEnum(shows[idx])

            //2.將選擇付款的方式索引改為gateway enum然後判斷是否等於gateway enum的值
            //如果付款方式選擇超商取貨付款，則到貨方式也自動更改為超商取貨
            if (key == GATEWAY_KEY &&
                (gateway_enum == GATEWAY.store_pay_711 ||
                        gateway_enum == GATEWAY.store_pay_family ||
                        gateway_enum == GATEWAY.store_pay_hilife ||
                        gateway_enum == GATEWAY.store_pay_ok)
            ) {
                val row1: OneRow = getRowFromRowKey(SHIPPING_KEY)
                if (GATEWAY.stringToEnum(shows[idx]) == GATEWAY.store_pay_711) {
                    row1.value = SHIPPING.store_711.englishName
                } else if (GATEWAY.stringToEnum(shows[idx]) == GATEWAY.store_pay_family) {
                    row1.value = SHIPPING.store_family.englishName
                } else if (GATEWAY.stringToEnum(shows[idx]) == GATEWAY.store_pay_hilife) {
                    row1.value = SHIPPING.store_hilife.englishName
                } else if (GATEWAY.stringToEnum(shows[idx]) == GATEWAY.store_pay_ok) {
                    row1.value = SHIPPING.store_ok.englishName
                }
                oneSectionAdapter.notifyItemChanged(sectionIdx+1)
            }
        } else if (key == SHIPPING_KEY) {


            val gateway_enum: GATEWAY = _gatewayChecked()
            //如果是更改選擇「到貨方式」，則檢查是否為超商貨到付款，如果是不允許更改預設值
            if (gateway_enum == GATEWAY.store_pay_711 ||
                    gateway_enum == GATEWAY.store_pay_family ||
                    gateway_enum == GATEWAY.store_pay_hilife ||
                    gateway_enum == GATEWAY.store_pay_ok) {

                //row.value = SHIPPING.store_711.englishName
                //oneSectionAdapter.notifyItemChanged(sectionIdx+1)

                _shippingCheckedForC2C(gateway_enum, GATEWAY.store_pay_711, "7-11超商貨到付款，到貨方式只能選取7-11到貨", row, radioGroup)
            }

        } else if (key == INVOICE_KEY) {
            row = invoiceOptionRows[rowIdx]
        }

        if (key == INVOICE_KEY) {
            val section: OneSection = getOneSectionFromIdx(sectionIdx)
            section.items.clear()
            row = OneRow("發票(目前僅提供電子發票)", "", "", INVOICE_KEY, "more")
            section.items.add(row)
            if (idx == 0) {
                row.value = "personal"
                section.items.addAll(invoicePersonalRows)
            } else {
                row.value = "company"
                section.items.addAll(invoiceCompanyRows)
            }
            view.unmask()
            oneSectionAdapter.notifyItemChanged(sectionIdx)
        }
    }

    fun _gatewayChecked(): GATEWAY {
        val row: OneRow = getRowFromRowKey(GATEWAY_KEY)
        val value: GATEWAY = GATEWAY.stringToEnum(row.value)

        return value
    }

    fun _shippingCheckedForC2C(gateway_enumA: GATEWAY, gateway_enumB: GATEWAY, msg: String, row: OneRow, radioGroup: RadioGroup) {

        //1.先由付款方式取得對應的到貨方式，例如「全家貨到付款」，到貨方式就是「全家取貨」
        val shipping_enum: SHIPPING = gateway_enumB.mapToShipping()

        //2.判斷選擇的到貨方式是否不對
        if (gateway_enumA == gateway_enumB && SHIPPING.stringToEnum(row.key) != shipping_enum) {
            //3.如果不對，顯示警告視窗
            warning(msg, false, "關閉") {
                //4.先更改對應資料為正確的到貨方式
                row.value = shipping_enum.englishName

                //5.改回對的到貨方式
                val title: String = shipping_enum.toChineseString()
                for (i in 0 until radioGroup.size) {
                    val radio: RadioButton = radioGroup[i] as RadioButton
                    if (radio.text == title) {
                        radio.isChecked = true
                        break
                    }
                }

                //oneSectionAdapter.notifyItemChanged(sectionIdx+1)
            }
        }
    }

    override fun cellMoreClick(sectionIdx: Int, rowIdx: Int) {

        val invoiceSection: OneSection = OneSection("電子發票", INVOICE_KEY, true, invoiceOptionRows)
        val tableViewHeight: Int = rowHeight * invoiceSection.items.size
        showTableLayer(tableViewHeight)

        val panelAdapter = OneItemAdapter(this, sectionIdx, invoiceSection, this, hashMapOf())
        //val panelAdapter = PanelAdapter(this, sectionIdx, this)
        layerTableView!!.adapter = panelAdapter
    }

    override fun cellClear(sectionIdx: Int, rowIdx: Int) {
        oneSections[sectionIdx].items[rowIdx].value = ""
        oneSections[sectionIdx].items[rowIdx].show = ""
        //searchSections = updateSectionRow()
        oneSectionAdapter.setOneSection(oneSections)
        oneSectionAdapter.notifyItemChanged(sectionIdx)
    }

    fun oneRowShowToArray(row: OneRow): Array<String> {

        return row.show.toArray()
    }

    fun submitButtonPressed(view: View) {

        Loading.show(view)
        val params: HashMap<String, String> = hashMapOf()
        params["device"] = "app"
        params["do"] = "update"
        if (cartTable != null) {
            params["cart_id"] = cartTable!!.id.toString()
        }

        params[AMOUNT_KEY] = getRowValue(AMOUNT_KEY)
        params[SHIPPING_FEE_KEY] = getRowValue(SHIPPING_FEE_KEY)
        params[TAX_KEY] = getRowValue(TAX_KEY)
        params[TOTAL_KEY] = getRowValue(TOTAL_KEY)
        params[DISCOUNT_KEY] = "0"

        val discount: Int = params[DISCOUNT_KEY]!!.toInt()
        val total: Int = params[TOTAL_KEY]!!.toInt()
        grand_total = discount + total
        params[GRAND_TOTAL_KEY] = grand_total.toString()

        //是否有選擇商品屬性
        val selected_attributes: ArrayList<String> = arrayListOf()
        val attributes: ArrayList<OneRow> = getRowsFromSectionKey(ATTRIBUTE_KEY)
        if (attributes.size > 0) {
            for (attribute in attributes) {

                val value: String = attribute.value
                val alias: String = attribute.key
                val title: String = attribute.title
                if (value.length == 0) {
                    warning("請先選擇${title}")
                } else {
                    val tmp = "{name:${title},alias:${alias},value:${value}}"
                    selected_attributes.add(tmp)
                }
            }
            params["attribute"] = selected_attributes.joinToString("|")
        }

        params[GATEWAY_KEY] = getRowValue(GATEWAY_KEY)
        params[SHIPPING_KEY] = getRowValue(SHIPPING_KEY)

        //直接購買會執行的區塊
        if (productTable != null && cartsTable == null) {
            params["product_id"] = productTable!!.id.toString()
            params[QUANTITY_KEY] = getRowValue(QUANTITY_KEY)
            params[AMOUNT_KEY] = productTable!!.prices[selected_idx].price_member.toString()
        }

        val invoice_type = getRowValue(INVOICE_KEY)
        params[INVOICE_TYPE_KEY] = invoice_type
        params[INVOICE_EMAIL_KEY] = getRowValue(INVOICE_PERSONAL_KEY)
        if (invoice_type == INVOICE_PERSONAL_KEY) {
            params[INVOICE_EMAIL_KEY] = getRowValue(INVOICE_EMAIL_KEY)
        } else {
            params[INVOICE_EMAIL_KEY] = getRowValue(INVOICE_EMAIL_KEY)
            params[INVOICE_COMPANY_NAME_KEY] = getRowValue(INVOICE_COMPANY_NAME_KEY)
            params[INVOICE_COMPANY_TAX_KEY] = getRowValue(INVOICE_COMPANY_TAX_KEY)
        }

        params["member_id"] = member.id.toString()
        params["order_name"] = getRowValue(NAME_KEY)
        params["order_tel"] = getRowValue(MOBILE_KEY)
        params["order_email"] = getRowValue(EMAIL_KEY)
        params["order_address"] = getRowValue(ADDRESS_KEY)

        params[MEMO_KEY] = getRowValue(MEMO_KEY)

        //println(params)

        OrderService.update(this, params) { success ->
            runOnUiThread {
                Loading.hide(view)
            }
            if (success) {
                if (OrderService.jsonString.isNotEmpty()) {
                    //println(OrderService.jsonString)
                    try {
                        val table: OrderUpdateResTable = Gson().fromJson(
                            OrderService.jsonString,
                            OrderUpdateResTable::class.java
                        )

                        if (!table.success) {
                            runOnUiThread {
                                warning(table.msg)
                            }
                        } else {
                            val orderTable: OrderTable? = table.model
                            if (orderTable != null) {
                                orderTable.filterRow()
                                val ecpay_token: String = orderTable.ecpay_token
                                val ecpay_token_ExpireDate: String =
                                    orderTable.ecpay_token_ExpireDate
                                cartItemCount = 0
                                session.edit().putInt("cartItemCount", cartItemCount).apply()

                                var gateway_method: GATEWAY = GATEWAY.credit_card
                                if (orderTable.gateway != null) {
                                    gateway_method =
                                        GATEWAY.stringToEnum(orderTable.gateway!!.method)
                                }

                                runOnUiThread {
                                    if (gateway_method == GATEWAY.credit_card || gateway_method == GATEWAY.store_cvs) {
                                        info("訂單已經成立，是否前往付款？", "關閉", "付款") {
                                            toPayment(
                                                orderTable.token,
                                                ecpay_token,
                                                ecpay_token_ExpireDate
                                            )
                                        }
                                    } else if (gateway_method == GATEWAY.store_pay_711
                                        || gateway_method == GATEWAY.store_pay_family
                                        || gateway_method == GATEWAY.store_pay_hilife
                                        || gateway_method == GATEWAY.store_pay_ok) {
                                        info("訂單已經成立，是否前往選取超商？", "關閉", "選取超商") {
                                            toWebView(orderTable.token, this)
                                        }
                                    } else if (gateway_method == GATEWAY.coin) {
                                        member.updateMemberCoin(-1 * grand_total)
                                        info("訂單成立並已經結帳", "", "關閉") {
                                            toPayment(orderTable.token)
                                        }
                                    }
                                }
                            } else {
                                runOnUiThread {
                                    warning("無法拿到伺服器傳回值")
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        runOnUiThread {
                            warning(e.localizedMessage!!)
                        }
                    }
                }
            } else {
                runOnUiThread {
                    warning(OrderService.msg)
                }
            }
        }

    }

    fun cancelBtnPressed(view: View) {
        finishAffinity()
        toProduct()
        //prev()
    }
}

class OrderUpdateResTable {

    var success: Boolean = false
    var msg: String = ""
    var id: Int = 0
    var update: String = "INSERT"
    var model: OrderTable? = null
}



















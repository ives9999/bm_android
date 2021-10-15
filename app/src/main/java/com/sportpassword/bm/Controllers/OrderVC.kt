package com.sportpassword.bm.Controllers

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_order_vc.*
import kotlinx.android.synthetic.main.mask.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.view.*
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sportpassword.bm.Data.*
import com.sportpassword.bm.Fragments.SearchSectionViewHolder

class OrderVC : MyTableVC() {

    var product_token: String? = null
    var productTable: ProductTable? = null
    var cartsTable: CartsTable? = null
    var cartTable: CartTable? = null
    var cartitemsTable: ArrayList<CartItemTable> = arrayListOf()

    var blackViewHeight: Int = 500
    val blackViewPaddingLeft: Int = 80
    var blackView: RelativeLayout? = null
    var tableView: RecyclerView? = null

    var sub_total: Int = 0
    var shippingFee: Int = 0
    var total: Int = 0
    var selected_number: Int = 1
    var selected_price: Int = 0
    var selected_idx: Int = 0

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

    val invoicePersonalRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "EMail","key" to INVOICE_EMAIL_KEY,"value" to "${member.email}","show" to "${member.email}","cell" to "textField","keyboard" to KEYBOARD.emailAddress.toString())
    )

    val invoiceCompanyRows: ArrayList<HashMap<String, String>> = arrayListOf(
        hashMapOf("title" to "統一編號","key" to INVOICE_COMPANY_TAX_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.default.toString()),
        hashMapOf("title" to "公司行號抬頭","key" to INVOICE_COMPANY_NAME_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.default.toString()),
        hashMapOf("title" to "EMail","key" to INVOICE_EMAIL_KEY,"value" to "${member.email}","show" to "${member.email}","cell" to "textField","keyboard" to KEYBOARD.emailAddress.toString())
    )
//
//    var memberRows: ArrayList<HashMap<String, String>> = arrayListOf()
//
//    val memoRows: ArrayList<HashMap<String, String>> = arrayListOf(
//        hashMapOf("title" to "留言","key" to MEMO_KEY,"value" to "","show" to "","cell" to "memo","keyboard" to KEYBOARD.default.toString())
//    )


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_vc)

        setMyTitle("訂單")

//        mySections = arrayListOf(
//            hashMapOf("name" to "商品", "isExpanded" to true, "key" to PRODUCT_KEY),
//            hashMapOf("name" to "金額", "isExpanded" to true, "key" to AMOUNT_KEY),
//            hashMapOf("name" to "付款方式", "isExpanded" to true, "key" to GATEWAY_KEY),
//            hashMapOf("name" to "寄送方式", "isExpanded" to true, "key" to SHIPPING_KEY),
//            hashMapOf("name" to "電子發票", "isExpanded" to true, "key" to INVOICE_KEY),
//            hashMapOf("name" to "收件人資料", "isExpanded" to true, "key" to MEMBER_KEY),
//            hashMapOf("name" to "其他留言", "isExpanded" to true, "key" to MEMO_KEY)
//        )

        //initAdapter(true)
        dataService = CartService
        recyclerView = order_list

        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this)
        oneSectionAdapter.setOneSection(oneSections)
        recyclerView.adapter = oneSectionAdapter

        refreshLayout = refresh
        setRefreshListener()

        refresh()
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
        getDataStart(page, perPage, member.token)
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            //if (theFirstTime) {

            if (jsonString != null && jsonString!!.isNotEmpty()) {
                //println(dataService.jsonString)
                genericTable()
                initData()
                oneSectionAdapter.setOneSection(oneSections)
                runOnUiThread {
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
            Loading.hide(mask)
        }
        loading = false
        refreshLayout!!.isRefreshing = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    override fun genericTable() {
        cartsTable = jsonToModels<CartsTable>(CartService.jsonString)
        if (cartsTable == null) {
            warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
        } else {
            if (cartsTable!!.rows.size != 1) {
                warning("購物車中無商品，或購物車超過一個錯誤，請洽管理員")
            } else {
                var amount: Int = 0
                cartTable = cartsTable!!.rows[0]
                cartitemsTable = cartTable!!.items
//                productRows.clear()
            }
        }
    }

    fun initData() {

        var amount: Int = 0

        var rows: ArrayList<OneRow> = arrayListOf()

        //cart
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
            val row = OneRow(productTable!!.name, "", "", PRODUCT_KEY, "cart", productTable!!.featured_path, attribute_text, cartItemTable.amount_show, cartItemTable.quantity.toString())
            rows.add(row)
            //productRows.add(row)
        }

        var section = makeSectionRow("商品", PRODUCT_KEY, rows, true)
        oneSections.add(section)

        //price
//        amountRows.clear()
        rows = arrayListOf()
        val amount_show: String = amount.formattedWithSeparator()
        var row = OneRow("商品金額", amount.toString(), "NT$ ${amount_show}", "amount", "text")
        rows.add(row)
        //var row: HashMap<String, String> = hashMapOf("title" to "商品金額","key" to "amount","value" to amount.toString(),"show" to "NT$ ${amount_show}","cell" to "text")
//        amountRows.add(row)
//
        //var shipping_fee: Int = 60
        var shipping_fee: Int = 0
        if (amount > 1000) { shipping_fee = 0}
        val shipping_fee_show: String = shipping_fee.formattedWithSeparator()

        row = OneRow("運費", shipping_fee.toString(), "NT$ ${shipping_fee_show}", SHIPPING_FEE_KEY, "text")
        rows.add(row)
//        row = hashMapOf("title" to "運費","key" to SHIPPING_FEE_KEY,"value" to shipping_fee.toString(),"show" to "NT$ ${shipping_fee_show}","cell" to "text")
//        amountRows.add(row)

        //val tax: Int = (amount.toDouble() * 0.05).toInt()
        val tax: Int = 0
        val tax_show: String = tax.formattedWithSeparator()
        row = OneRow("税", tax.toString(), "NT$ ${tax_show}", TAX_KEY, "text")
        rows.add(row)
//        row = hashMapOf("title" to "稅","key" to TAX_KEY,"value" to tax.toString(),"show" to "NT$ ${tax_show}","cell" to "text")
//        amountRows.add(row)

        val total: Int = amount + shipping_fee + tax
        val total_show: String = total.formattedWithSeparator()
        row = OneRow("總金額", total.toString(), "NT$ ${total_show}", TOTAL_KEY, "text")
        rows.add(row)
//        row = hashMapOf("title" to "總金額","key" to TOTAL_KEY,"value" to total.toString(),"show" to "NT$ ${total_show}","cell" to "text")
//        amountRows.add(row)
        section = makeSectionRow("金額", AMOUNT_KEY, rows, true)
        oneSections.add(section)

        //gateway
//        gatewayRows.clear()
        rows = arrayListOf()
        var titles: Array<String> = productTable!!.gateway.toArray()
        var tmp: ArrayList<String> = arrayListOf()
        for (title in titles) {
            tmp.add(GATEWAY.stringToEnum(title).chineseName)
        }

        row = OneRow(tmp.joinToString(","), GATEWAY.credit_card.toEnglishString(), productTable!!.gateway, GATEWAY_KEY, "radio")
        rows.add(row)
        section = makeSectionRow("付款方式", GATEWAY_KEY, rows, true)
        oneSections.add(section)

        //shipping
//        shippingRows.clear()
        rows = arrayListOf()
        titles = productTable!!.gateway.toArray()
        tmp = arrayListOf()
        for (title in titles) {
            tmp.add(SHIPPING.stringToEnum(title).chineseName)
        }
        row = OneRow(tmp.joinToString(","), SHIPPING.direct.toEnglishString(), productTable!!.shipping, SHIPPING_KEY, "radio")
        rows.add(row)
        section = makeSectionRow("到貨方式", SHIPPING_KEY, rows, true)
        oneSections.add(section)

        //invoice
//        invoiceRows.clear()
//        for (invoiceFixedRow in invoiceFixedRows) {
//
//            invoiceRows.add(invoiceFixedRow)
//        }
//
//        for (invoicePersonalRow in invoicePersonalRows) {
//
//            invoiceRows.add(invoicePersonalRow)
//        }

        rows = arrayListOf()
        row = OneRow("發票(目前僅提供電子發票)", "", "", INVOICE_KEY, "more")
        rows.add(row)
        section = makeSectionRow("電子發票", INVOICE_KEY, rows, true)
        oneSections.add(section)

        //member

//        memberRows = arrayListOf(
//            hashMapOf("title" to "姓名","key" to NAME_KEY,"value" to member.name!!,"show" to member.name!!,"cell" to "textField","keyboard" to KEYBOARD.default.toString()),
//            hashMapOf("title" to "電話","key" to MOBILE_KEY,"value" to member.mobile!!,"show" to member.mobile!!,"cell" to "textField","keyboard" to KEYBOARD.numberPad.toString()),
//            hashMapOf("title" to "EMail","key" to EMAIL_KEY,"value" to member.email!!,"show" to member.email!!,"cell" to "textField","keyboard" to KEYBOARD.emailAddress.toString()),
//            hashMapOf("title" to "住址","key" to ADDRESS_KEY,"value" to member.address!!,"show" to member.address!!,"cell" to "textField","keyboard" to KEYBOARD.default.toString())
//        )
        rows = arrayListOf()
        row = OneRow("姓名", member.name!!, member.name!!, NAME_KEY, "textField")
        rows.add(row)
        row = OneRow("電話", member.mobile!!, member.mobile!!, MOBILE_KEY, "textField")
        rows.add(row)
        row = OneRow("EMail", member.email!!, member.email!!, EMAIL_KEY, "textField")
        rows.add(row)
        row = OneRow("住址", member.address!!, member.address!!, ADDRESS_KEY, "textField")
        rows.add(row)
        section = makeSectionRow("收件人資料", MEMBER_KEY, rows, true)
        oneSections.add(section)

        //memo
        rows = arrayListOf()
        row = OneRow("留言", "", "", MEMO_KEY, "textField")
        rows.add(row)
        section = makeSectionRow("其他留言", MEMO_KEY, rows, true)
        oneSections.add(section)



//        myRows = arrayListOf(
//            hashMapOf("key" to PRODUCT_KEY, "rows" to productRows),
//            hashMapOf("key" to AMOUNT_KEY, "rows" to amountRows),
//            hashMapOf("key" to GATEWAY_KEY, "rows" to gatewayRows),
//            hashMapOf("key" to SHIPPING_KEY, "rows" to shippingRows),
//            hashMapOf("key" to INVOICE_KEY, "rows" to invoiceRows),
//            hashMapOf("key" to MEMBER_KEY, "rows" to memberRows),
//            hashMapOf("key" to MEMO_KEY, "rows" to memoRows)
//        )

//        initAdapter(true)
        //        recyclerView.setHasFixedSize(true)

        //reloadData()

//        for ((idx, mySection) in mySections.withIndex()) {
//            val title: String = mySection["name"] as String
//            val groupSection = GroupSection(title)
//            val isExpanded: Boolean = mySection["isExpanded"] as Boolean
//            val expandableGroup = ExpandableGroup(groupSection, isExpanded)
//            expandableGroup.add(groupSection)
//
//            val section = Section()
//            val items = generateItems(idx)
//            section.addAll(items)
//            adapterSections.add(section)
//            //a.add(expandableGroup)
//            // adapterSections.add(expandableGroup)
//        }
//        adapter.update(adapterSections)
//        adapter.notifyDataSetChanged()

//        for ((idx, mySection) in mySections.withIndex()) {
//            val section = Section()
//            adapterSections.add(section)
//            val title: String = mySection["name"] as String
//            val isExpanded: Boolean = mySection["isExpanded"] as Boolean
//            val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)
//            val items = generateItems(idx)
//            section.addAll(items)
//            expandableGroup.add(section)
//
//            adapter.add(expandableGroup)
//        }
    }

//    override fun generateItems(section: Int): ArrayList<Item> {
//
//        if (myRows.size == 0) {
//            return arrayListOf()
//        }
//        items.clear()
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
//        if (sectionKey == GATEWAY_KEY) {
//            val item = RadioAdapter(this, sectionKey, rows, this)
//            items.add(item)
//        } else if (sectionKey == SHIPPING_KEY) {
//            val item = RadioAdapter(this, sectionKey, rows, this)
//            items.add(item)
//        } else {
//
//            //val adapterRows: ArrayList<Item> = arrayListOf()
//            for ((idx, row) in rows.withIndex()) {
//
//                var rowKey: String = ""
//                if (row.containsKey("key")) {
//                    rowKey = row["key"]!!
//                }
//                var title: String = ""
//                if (row.containsKey("title")) {
//                    title = row["title"]!!
//                }
//                var value: String = ""
//                if (row.containsKey("value")) {
//                    value = row["value"]!!
//                }
//                var show: String = ""
//                if (row.containsKey("show")) {
//                    show = row["show"]!!
//                }
//
//                val cell_type: String? = row["cell"]
//
//                //var formItemAdapter: FormItemAdapter1? = null
//                if (cell_type == "cart") {
//                    var featured_path = FEATURED_PATH
//                    if (row.containsKey("featured_path") && row["featured_path"]!!.length > 0) {
//                        featured_path = row["featured_path"]!!
//                    }
//                    var attribute = ""
//                    if (row.containsKey("attribute")) {
//                        attribute = row["attribute"]!!
//                    }
//                    var amount = ""
//                    if (row.containsKey("amount")) {
//                        amount = row["amount"]!!
//                    }
//                    var quantity = ""
//                    if (row.containsKey("quantity")) {
//                        quantity = row["quantity"]!!
//                    }
//                    val cartItemItem = CartItemItem(
//                        this,
//                        sectionKey,
//                        rowKey,
//                        title,
//                        featured_path,
//                        attribute,
//                        amount,
//                        quantity
//                    )
//                    items.add(cartItemItem)
//                } else if (cell_type == "text") {
//                    val item = PlainAdapter1(title, show)
//                    items.add(item)
//                } else if (cell_type == "textField") {
//                    val keyboard: String = row["keyboard"] ?: run { "default" }
//                    val item = TextFieldAdapter1(sectionKey, rowKey, title, value, keyboard, this)
//                    items.add(item)
//                } else if (cell_type == "more") {
//                    val item = MoreAdapter1(sectionKey, rowKey, title, value, show, this)
//                    items.add(item)
//                }
//            }
//        }
//
//        return items
//    }
//
//    fun reloadData() {
//
//        for ((idx, _) in mySections.withIndex()) {
//            val items = generateItems(idx)
//            adapterSections[idx].update(items)
//        }
//    }

    override fun cellEdit(sectionIdx: Int, rowIdx: Int) {

        val token: String = cartitemsTable[rowIdx].token
        toAddCart(null, token)
    }

    override fun cellDelete(sectionIdx: Int, rowIdx: Int) {

        val row: OneRow = oneSections[sectionIdx].items[rowIdx]
    }

    override fun cellRadioChanged(key: String, sectionIdx: Int, rowIdx: Int, idx: Int) {
        var row: OneRow = OneRow()
        if (key == GATEWAY_KEY || key == SHIPPING_KEY) {
            row = oneSections[sectionIdx].items[rowIdx]
        } else if (key == INVOICE_KEY) {
            row = invoiceOptionRows[rowIdx]
        }

        val tmp: Array<String> = row.show.toArray()
        for ((idx1, value) in tmp.withIndex()) {
            if (idx1 == idx) {
                row.value = value
                break
            }
        }
        if (key == INVOICE_KEY) {
            top.unmask()
        }
    }

    override fun cellMoreClick(sectionIdx: Int, rowIdx: Int) {
        layerMask = top.mask(this)
        layerMask!!.setOnClickListener {
            top.unmask()
        }

        val rowHeight: Int = 200
        //val tableViewHeight: Int = rowHeight * invoiceOptionRows.size
        val tableViewHeight: Int = rowHeight * 2
        val buttonViewHeight: Int = 150
        blackViewHeight = tableViewHeight + buttonViewHeight

        val statusBarHeight: Int = getStatusBarHeight()
//        val appBarHeight: Int = 64
        val frame_width = Resources.getSystem().displayMetrics.widthPixels
        val frame_height = Resources.getSystem().displayMetrics.heightPixels - statusBarHeight - 400
        val width: Int = frame_width - 2*blackViewPaddingLeft
        val topX: Int = (frame_height-blackViewHeight)/2;

        blackView = layerMask!!.blackView(
            this,
            blackViewPaddingLeft,
            topX,
            width,
            blackViewHeight)

        tableView = blackView!!.tableView(this, 0, buttonViewHeight)

        val invoiceSection: OneSection = OneSection("電子發票", INVOICE_KEY, true, invoiceOptionRows)

        val panelAdapter = OneItemAdapter(this, sectionIdx, invoiceSection, this)
        //val panelAdapter = PanelAdapter(this, sectionIdx, this)
        tableView!!.adapter = panelAdapter

        //items.clear()
//        val item = RadioAdapter(this, INVOICE_KEY, invoiceOptionRows, this)
        //items.add(item)
        //panelAdapter.addAll(items)

        layerButtonLayout = blackView!!.buttonPanel(this, buttonViewHeight)
        layerCancelBtn = layerButtonLayout.cancelButton(this) {
            top.unmask()
        }
    }
//    override fun textFieldTextChanged(sectionKey: String, rowKey: String, value: String) {
//
//        val row: HashMap<String, String> = getRowRowsFromMyRowsByKey1(rowKey)
//        row["value"] = value
//        row["show"] = value
//        replaceRowByKey(sectionKey, rowKey, row)
//    }
//
//    override fun radioDidChange(sectionKey: String, rows: ArrayList<HashMap<String, String>>) {
//
//        invoiceRows.clear()
//        if (sectionKey == INVOICE_KEY) {
//            for (invoiceFixRow in invoiceFixedRows) {
//                invoiceRows.add(invoiceFixRow)
//            }
//
//            invoiceOptionRows = rows
//
//            var selectedRow: HashMap<String, String> = hashMapOf()
//            for (row in invoiceOptionRows) {
//                if (row["value"] == "true") {
//                    selectedRow = row
//                }
//            }
//
//            val selectedKey: String = selectedRow["key"]!!
//            if (selectedKey == PERSONAL_KEY) {
//                for (invoicePresonalRow in invoicePersonalRows) {
//                    invoiceRows.add(invoicePresonalRow)
//                }
//            } else {
//                for (invoiceCompanyRow in invoiceCompanyRows) {
//                    invoiceRows.add(invoiceCompanyRow)
//                }
//            }
//
//            replaceRowsByKey(sectionKey, invoiceRows)
//            top.unmask()
//            //reloadData()
//        } else if (sectionKey == GATEWAY_KEY || sectionKey == SHIPPING_KEY){
//
//            replaceRowsByKey(sectionKey, rows)
//        }
//    }
//
//    override fun moreClick(sectionKey: String, rowKey: String) {
//        //println("more")
//        layerMask = top.mask(this)
//        layerMask!!.setOnClickListener {
//            top.unmask()
//        }
//
//        val rowHeight: Int = 200
//        val tableViewHeight: Int = rowHeight * invoiceOptionRows.size
//        val buttonViewHeight: Int = 150
//        blackViewHeight = tableViewHeight + buttonViewHeight
//
//        val statusBarHeight: Int = getStatusBarHeight()
////        val appBarHeight: Int = 64
//        val frame_width = Resources.getSystem().displayMetrics.widthPixels
//        val frame_height = Resources.getSystem().displayMetrics.heightPixels - statusBarHeight - 400
//        val width: Int = frame_width - 2*blackViewPaddingLeft
//        val topX: Int = (frame_height-blackViewHeight)/2;
//
//        blackView = layerMask!!.blackView(
//            this,
//            blackViewPaddingLeft,
//            topX,
//            width,
//            blackViewHeight)
//
//        tableView = blackView!!.tableView(this, 0, buttonViewHeight)
//        val panelAdapter = GroupAdapter<GroupieViewHolder>()
//        tableView!!.adapter = panelAdapter
//
//        //items.clear()
//        val item = RadioAdapter(this, INVOICE_KEY, invoiceOptionRows, this)
//        //items.add(item)
//        //panelAdapter.addAll(items)
//
//        layerButtonLayout = blackView!!.buttonPanel(this, buttonViewHeight)
//        layerCancelBtn = layerButtonLayout.cancelButton(this) {
//            top.unmask()
//        }
//    }

    override fun handleSectionExpanded(idx: Int) {
        //println(idx)
        val oneSection = oneSections[idx]
        var isExpanded: Boolean = oneSection.isExpanded
        isExpanded = !isExpanded
        oneSections[idx].isExpanded = isExpanded
        oneSectionAdapter.setOneSection(oneSections)
        oneSectionAdapter.notifyItemChanged(idx)
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun submitBtnPressed(view: View) {

        Loading.show(mask)
        val params: HashMap<String, String> = hashMapOf()
        params["device"] = "app"
        params["do"] = "update"
        params["cart_id"] = cartTable!!.id.toString()

        params[AMOUNT_KEY] = getRowValue(AMOUNT_KEY)
        params[SHIPPING_FEE_KEY] = getRowValue(SHIPPING_FEE_KEY)
        params[TAX_KEY] = getRowValue(TAX_KEY)
        params[TOTAL_KEY] = getRowValue(TOTAL_KEY)
        params[DISCOUNT_KEY] = "0"


        val discount: Int = params[DISCOUNT_KEY]!!.toInt()
        val total: Int = params[TOTAL_KEY]!!.toInt()
        params[GRAND_TOTAL_KEY] = (discount + total).toString()

        val gateways = getRowRowsFromMyRowsByKey(GATEWAY_KEY)
        var key: String = "credit_card"
        for (gateway in gateways) {
            if (gateway["value"] == "true") {
                key = gateway["key"]!!
            }
        }
        params[GATEWAY_KEY] = key

        val shippings = getRowRowsFromMyRowsByKey(SHIPPING_KEY)
        key = "direct"
        for (shipping in shippings) {
            if (shipping["value"] == "true") {
                key = shipping["key"]!!
            }
        }
        params[SHIPPING_KEY] = key

        //invoice
//        for (invoice_option in invoiceOptionRows) {
//            if (invoice_option["value"] == "true") {
//                key = invoice_option["key"]!!
//            }
//        }
        params[INVOICE_TYPE_KEY] = key

        val invoices = getRowRowsFromMyRowsByKey(INVOICE_KEY)
        for (invoice in invoices) {
            for ((key1, value) in invoice) {
                if (key1 == "key" && value == INVOICE_EMAIL_KEY) {
                    params[INVOICE_EMAIL_KEY] = invoice["value"]!!
                    break
                } else if (key1 == "key" && value == INVOICE_COMPANY_NAME_KEY) {
                    params[INVOICE_COMPANY_TAX_KEY] = invoice["value"]!!
                    break
                } else if (key1 == "key" && value == INVOICE_COMPANY_TAX_KEY) {
                    params[INVOICE_COMPANY_NAME_KEY] = invoice["value"]!!
                    break
                }
            }
        }

        params["member_id"] = member.id.toString()
        params["order_name"] = getRowValue(NAME_KEY)
        params["order_tel"] = getRowValue(MOBILE_KEY)
        params["order_email"] = getRowValue(EMAIL_KEY)
        params["order_address"] = getRowValue(ADDRESS_KEY)

        params[MEMO_KEY] = getRowValue(MEMO_KEY)

        //println(params)

        OrderService.update(this, params) { success ->
            Loading.hide(mask)
            if (success) {
                if (OrderService.jsonString.isNotEmpty()) {
                    try {
                        val table: OrderUpdateResTable = Gson().fromJson(
                            OrderService.jsonString,
                            OrderUpdateResTable::class.java
                        )
                        val orderTable: OrderTable? = table.model
                        if (orderTable != null) {
                            val ecpay_token: String = orderTable.ecpay_token
                            val ecpay_token_ExpireDate: String = orderTable.ecpay_token_ExpireDate
                            cartItemCount = 0
                            session.edit().putInt("cartItemCount", cartItemCount).apply()
                            info("訂單已經成立，是否前往結帳？", "關閉", "結帳") {
                                toPayment(orderTable.token, ecpay_token, ecpay_token_ExpireDate)
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        warning(e.localizedMessage!!)
                    }
                }
            } else {
                warning(OrderService.msg)
            }
        }

    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}

class OrderUpdateResTable {

    var success: Boolean = false
    var id: Int = 0
    var update: String = "INSERT"
    var model: OrderTable? = null
}



















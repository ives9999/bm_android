package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Adapters.Form.FormItemAdapter1
import com.sportpassword.bm.Adapters.Form.NumberAdapter1
import com.sportpassword.bm.Adapters.Form.PlainAdapter1
import com.sportpassword.bm.Adapters.Form.TagAdapter1
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.OrderService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_order_vc.*
import kotlinx.android.synthetic.main.mask.*
import java.util.*
import kotlin.collections.HashMap

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

        dataService = CartService

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

    override fun getDataEnd(success: Boolean) {
        if (success) {
            //if (theFirstTime) {

            if (jsonString != null && jsonString!!.isNotEmpty()) {
                //println(dataService.jsonString)
                genericTable()
                initData()
            } else {
                warning("沒有取得回傳的json字串，請洽管理員")
            }

            //}

            //notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        Loading.hide(mask)
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
                }

                //price
                val amount_show: String = amount.formattedWithSeparator()
                var row: HashMap<String, String> = hashMapOf("title" to "商品金額","key" to "amount","value" to amount.toString(),"show" to "NT$ ${amount_show}","cell" to "text")
                amountRows.add(row)

                var shipping_fee: Int = 60
                if (amount > 1000) { shipping_fee = 0}
                val shipping_fee_show: String = shipping_fee.formattedWithSeparator()

                row = ["title":"運費","key":SHIPPING_FEE_KEY,"value":String(shipping_fee),"show":"NT$ \(shipping_fee_show)","cell":"text"]
                amountRows.append(row)

                let tax: Int = Int(Double(amount) * 0.05)
                let tax_show: String = tax.formattedWithSeparator
                        row = ["title":"稅","key":TAX_KEY,"value":String(tax),"show":"NT$ \(tax_show)","cell":"text"]
                amountRows.append(row)

                let total: Int = amount + shipping_fee + tax
                let total_show: String = total.formattedWithSeparator
                        row = ["title":"總金額","key":TOTAL_KEY,"value":String(total),"show":"NT$ \(total_show)","cell":"text"]
                amountRows.append(row)
            }
        }
    }

    fun initData() {

        mySections = arrayListOf(
            hashMapOf("name" to "商品", "isExpanded" to true, "key" to PRODUCT_KEY),
            hashMapOf("name" to "金額", "isExpanded" to true, "key" to AMOUNT_KEY),
            hashMapOf("name" to "付款方式", "isExpanded" to true, "key" to GATEWAY_KEY),
            hashMapOf("name" to "寄送方式", "isExpanded" to true, "key" to SHIPPING_KEY),
            hashMapOf("name" to "電子發票", "isExpanded" to true, "key" to INVOICE_KEY),
            hashMapOf("name" to "收件人資料", "isExpanded" to true, "key" to MEMBER_KEY),
            hashMapOf("name" to "其他留言", "isExpanded" to true, "key" to MEMO_KEY)
        )

        memberRows = arrayListOf(
            hashMapOf("title" to "姓名","key" to NAME_KEY,"value" to member.name,"show" to member.name,"cell" to "textField"),
            hashMapOf("title" to "電話","key" to MOBILE_KEY,"value" to member.mobile,"show" to member.mobile,"cell" to "textField"),
            hashMapOf("title" to "EMail","key" to EMAIL_KEY,"value" to member.email,"show" to member.email,"cell" to "textField"),
            hashMapOf("title" to "住址","key" to ADDRESS_KEY,"value" to member.address,"show" to member.address,"cell" to "textField")
        )

        for (invoiceFixedRow in invoiceFixedRows) {

            invoiceRows.add(invoiceFixedRow)
        }

        for (invoicePersonalRow in invoicePersonalRows) {

            invoiceRows.add(invoicePersonalRow)
        }

        myRows = arrayListOf(
            hashMapOf("key" to PRODUCT_KEY, "rows" to productRows),
            hashMapOf("key" to AMOUNT_KEY, "rows" to amountRows),
            hashMapOf("key" to GATEWAY_KEY, "rows" to gatewayRows),
            hashMapOf("key" to SHIPPING_KEY, "rows" to shippingRows),
            hashMapOf("key" to INVOICE_KEY, "rows" to invoiceRows),
            hashMapOf("key" to MEMBER_KEY, "rows" to memberRows),
            hashMapOf("key" to MEMO_KEY, "rows" to memoRows)
        )

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

    override fun generateItems(section: Int): ArrayList<Item> {

        items.clear()
        var sectionKey: String = ""
        val mySection: HashMap<String, Any> = myRows[section]
        val tmp: String? = mySection["key"] as? String
        if (tmp != null) {
            sectionKey = tmp
        }

        if (!mySection.containsKey("rows")) {
            return arrayListOf()
        }

        @Suppress("UNCHECKED_CAST")
        val rows: ArrayList<HashMap<String, String>> = mySection["rows"] as ArrayList<HashMap<String, String>>

        //val adapterRows: ArrayList<Item> = arrayListOf()
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
                val cartItemItem = CartItemItem(this, sectionKey, rowKey, title, featured_path, attribute, amount, quantity)
                items.add(cartItemItem)
            } else if (cell_type == "text") {
                val item = PlainAdapter1(title, show)
                items.add(item)
            }
//            if (formItemAdapter != null) {
//                adapterRows.add(formItemAdapter)
//            }
        }

        return items
    }

    fun submitBtnPressed(view: View) {
        toOrder()
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}
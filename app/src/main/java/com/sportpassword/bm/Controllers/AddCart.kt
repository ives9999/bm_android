package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Data.OneSection
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Models.CartItemTable
import com.sportpassword.bm.Models.CartTable
import com.sportpassword.bm.Models.ProductTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Views.Tag
import com.sportpassword.bm.member
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_addcart_vc.*
import kotlinx.android.synthetic.main.bottom_view_general.*
import kotlinx.android.synthetic.main.cart_list_cell.view.*
import kotlinx.android.synthetic.main.formitem_barcode.*
import kotlinx.android.synthetic.main.formitem_more.view.*
import kotlinx.android.synthetic.main.formitem_more.view.promptBtn
import kotlinx.android.synthetic.main.formitem_number.view.*
import kotlinx.android.synthetic.main.formitem_plain.view.detail
import kotlinx.android.synthetic.main.formitem_plain.view.title
import kotlinx.android.synthetic.main.formitem_radio.view.*
import kotlinx.android.synthetic.main.formitem_sex.*
import kotlinx.android.synthetic.main.formitem_tag.view.*
import kotlinx.android.synthetic.main.formitem_textfield.view.*
import kotlinx.android.synthetic.main.formitem_textfield.view.clear
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tag.view.*
import org.jetbrains.anko.backgroundColor
import java.lang.IllegalArgumentException

class AddCartVC : MyTableVC() {

    var product_token: String? = null
    var cartItem_token: String? = null
    var productTable: ProductTable? = null
    var cartTable: CartTable? = null
    var cartItemTable: CartItemTable? = null

    //var section_keys: ArrayList<ArrayList<String>> = arrayListOf()

    var sub_total: Int = 0
    var shippingFee: Int = 0
    var total: Int = 0

    var selected_number: Int = 1
    var selected_price: Int = 0
    var selected_idx: Int = 0

    var update: Boolean = false
    var bottom_button_count: Int = 2
    val button_width: Int = 400

//    val productRows: ArrayList<HashMap<String, String>> =  arrayListOf(
//        hashMapOf("title" to "商品","key" to PRODUCT_KEY,"value" to "","show" to "","cell" to "text")
//    )
//
//    var attributeRows: ArrayList<HashMap<String, String>> = arrayListOf()
//
//    val amountRows: ArrayList<HashMap<String, String>> =  arrayListOf(
//        hashMapOf("title" to "數量","key" to QUANTITY_KEY,"value" to "","show" to "","cell" to "number"),
//        hashMapOf("title" to "小計","key" to SUBTOTAL_KEY,"value" to "","show" to "","cell" to "text"),
//        //hashMapOf("title" to "運費","key" to SHIPPING_FEE_KEY,"value" to "","show" to "","cell" to "text"),
//        hashMapOf("title" to "總計","key" to TOTAL_KEY,"value" to "","show" to "","cell" to "text")
//    )

//    val contactRows: ArrayList<HashMap<String, String>> =  arrayListOf(
//        hashMapOf("title" to "姓名","key" to NAME_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.default.toString()),
//        hashMapOf("title" to "行動電話","key" to MOBILE_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.numberPad.toString()),
//        hashMapOf("title" to "EMail","key" to EMAIL_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.emailAddress.toString()),
//        hashMapOf("title" to "住址","key" to ADDRESS_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.default.toString())
//    )

    override fun onCreate(savedInstanceState: Bundle?) {

//        mySections = arrayListOf(
//            hashMapOf("name" to "商品名稱", "isExpanded" to true, "key" to PRODUCT_KEY),
//            hashMapOf("name" to "商品選項", "isExpanded" to true, "key" to ATTRIBUTE_KEY),
//            hashMapOf("name" to "款項", "isExpanded" to true, "key" to AMOUNT_KEY)
//            //hashMapOf("name" to "寄件資料", "isExpanded" to true, "key" to CONTACT_KEY)
//        )
//
//        myRows = arrayListOf(
//            hashMapOf("key" to PRODUCT_KEY, "rows" to productRows),
//            hashMapOf("key" to ATTRIBUTE_KEY, "rows" to attributeRows),
//            hashMapOf("key" to AMOUNT_KEY, "rows" to amountRows)
//            //hashMapOf("key" to CONTACT_KEY, "rows" to contactRows),
//        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcart_vc)

        if (intent.hasExtra("product_token")) {
            product_token = intent.getStringExtra("product_token")
        }
        if (intent.hasExtra("cartItem_token")) {
            cartItem_token = intent.getStringExtra(("cartItem_token"))
        }

        //hidekeyboard(order_layout)

        dataService = ProductService
        recyclerView = editTableView
        oneSectionAdapter = OneSectionAdapter(this, R.layout.cell_section, this, hashMapOf())
//        addCartSections = initSectionRows1()
        oneSectionAdapter.setOneSection(oneSections)
        recyclerView.adapter = oneSectionAdapter

        refreshLayout = refresh
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

//    private fun initSectionRows1(): ArrayList<OneSection> {
//
//        val sections: ArrayList<OneSection> = arrayListOf()
//
//        sections.add(makeSection0Row1())
//        sections.add(makeSection1Row1())
//
//        return sections
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//        super.onCreateOptionsMenu(menu)
//
//        val menuView = menu!!.findItem(R.id.menu_all).actionView
//        val shoppingCartBtn = menuView.findViewById<ImageButton>(R.id.cart)
//        shoppingCartBtn.visibility = View.VISIBLE
//        return true
//    }

//    override fun initAdapter(include_section: Boolean) {
////        adapter = GroupAdapter()
//
////        adapter.setOnItemClickListener { item, view ->
////            rowClick(item, view)
////        }
//
//        // for member register and member update personal data
//        if (include_section) {
//            for ((idx, mySection) in mySections.withIndex()) {
//                val section = Section()
//                adapterSections.add(section)
//                val title: String = mySection["name"] as String
//                val isExpanded: Boolean = mySection["isExpanded"] as Boolean
//                val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)
//                val items = generateItems(idx)
//                section.addAll(items)
//                expandableGroup.add(section)
//
//                adapter.add(expandableGroup)
//            }
////            for ((idx, mySection) in mySections.withIndex()) {
////                val title: String = mySection["name"] as String
////                val isExpanded: Boolean = mySection["isExpanded"] as Boolean
////
////                val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)
////                val items = generateItems(idx)
////                adapterSections[idx].addAll(items)
////                expandableGroup.add(adapterSections[idx])
////
////                adapter.add(expandableGroup)
////            }
//        }
//
//
//        recyclerView.adapter = adapter
////        recyclerView.setHasFixedSize(true)
//        if (refreshLayout != null) {
//            setRefreshListener()
//        }
//        setRecyclerViewScrollListener()
//    }

    override fun refresh() {
        Loading.show(mask)
        if (product_token != null) {
//            adapter.clear()
//            adapterSections.clear()
//            attributeRows.clear()

            update = false
            val params: HashMap<String, String> = hashMapOf("token" to product_token!!, "member_token" to member.token!!)
            dataService.getOne(this, params) { success ->
                runOnUiThread {
                    Loading.hide(mask)
                }
                if (success) {
                    try {
                        productTable = jsonToModel<ProductTable>(dataService.jsonString)
                    } catch (e: JsonParseException) {
                        runOnUiThread {
                            warning(e.localizedMessage!!)
                        }
                        //println(e.localizedMessage)
                    }
                    if (productTable != null) {
                        //myTable = table as ProductTable
                        productTable!!.filterRow()
                        runOnUiThread {
                            initData()
                        }
                        oneSectionAdapter.setOneSection(oneSections)
                        runOnUiThread {
                            oneSectionAdapter.notifyDataSetChanged()
                        }
                    } else {
                        runOnUiThread {
                            warning("解析伺服器所傳的字串失敗，請洽管理員")
                        }
                    }
                }
            }
        }

        if (cartItem_token != null) {

            update = true
            submitBtn.text = "更新購物車"
            val params: HashMap<String, String> = hashMapOf("cart_item_token" to cartItem_token!!, "member_token" to member.token!!)
            CartService.getOne(this, params) { success ->
                runOnUiThread {
                    Loading.hide(mask)
                }
                if (success) {
                    try {
                        cartTable = jsonToModel<CartTable>(CartService.jsonString)
                    } catch (e: JsonParseException) {
                        runOnUiThread {
                            warning(e.localizedMessage!!)
                        }
                    }
                    if (cartTable != null) {
                        if (cartTable!!.items.size > 0) {
                            cartItemTable = cartTable!!.items[0]
                            cartItemTable?.filterRow()
                            productTable = cartItemTable!!.product
                            productTable?.filterRow()
                            runOnUiThread {
                                initData()
                                oneSectionAdapter.setOneSection(oneSections)
                                oneSectionAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
        closeRefresh()
    }

    private fun initData() {

//        form = OrderForm(myTable!!.type, this)
//        sections = form.getSections()
//        section_keys = form.getSectionKeys()
        if (productTable != null) {
//            initAdapter(true)
            setMyTitle(productTable!!.name)

            oneSections.clear()

            var rows: ArrayList<OneRow> = arrayListOf()
            var row = OneRow("商品", productTable!!.name, productTable!!.name, PRODUCT_KEY, "text")
            rows.add(row)

            var section = makeSectionRow("商品名稱", PRODUCT_KEY, rows, true)
            oneSections.add(section)

            rows = arrayListOf()
            for (attribute in productTable!!.attributes) {
                var tmp: String = attribute.attribute
                tmp = tmp.replace("{", "")
                tmp = tmp.replace("}", "")
                tmp = tmp.replace("\"", "")

                //show is 湖水綠,極致黑,經典白,太空灰
                //name is 顏色
                //key is color
                var value: String = ""
                val alias: String = attribute.alias
                if (cartItemTable != null) {
                    for (item_attriubtes in cartItemTable!!.attributes) {
                        for ((idx, value1) in item_attriubtes) {
                            if (value1 == alias) {
                                value = item_attriubtes["value"]!!
                                break
                            }
                        }
                    }
                }
                row = OneRow(attribute.name, value, tmp, alias, "tag")
                rows.add(row)
            }
            section = makeSectionRow("商品屬性", ATTRIBUTE_KEY, rows, true)
            oneSections.add(section)

            //print(attributeRows)
//            myRows[1]["rows"] = attributeRows

//            row = getRowRowsFromMyRowsByKey1(QUANTITY_KEY)
            rows = arrayListOf()
            val min: String = productTable!!.order_min.toString()
            val max: String = productTable!!.order_max.toString()
            var quantity: String = "1"
            if (cartItemTable != null) {
                quantity = cartItemTable!!.quantity.toString()
            }
            row = OneRow("數量", quantity, "${min},${max}", QUANTITY_KEY, "number")
            rows.add(row)

            row = OneRow("小計", "", "", SUBTOTAL_KEY, "text")
            rows.add(row)
            row = OneRow("總計", "", "", TOTAL_KEY, "text")
            rows.add(row)
            section = makeSectionRow("款項", AMOUNT_KEY, rows, true)
            oneSections.add(section)


            if (cartItemTable != null) {
                selected_number = cartItemTable!!.quantity
//                row["value"] = selected_number.toString()
            }
//            replaceRowByKey(QUANTITY_KEY, row)
//
            selected_price = productTable!!.prices[selected_idx].price_member
            updateSubTotal()
        }
    }

//    override fun generateItems(section: Int): ArrayList<Item> {
//
//        var sectionKey: String = ""
//        val section: HashMap<String, Any> = myRows[section]
//        val tmp: String? = section["key"] as? String
//        if (tmp != null) {
//            sectionKey = tmp
//        }
//
//        val clearClick = { formItem: FormItem ->
//        }
//
//        val promptClick = { formItem: FormItem ->
//        }
//
//        if (!section.containsKey("rows")) {
//            return arrayListOf()
//        }
//
//        @Suppress("UNCHECKED_CAST")
//        val rows: ArrayList<HashMap<String, String>> = section["rows"] as ArrayList<HashMap<String, String>>
//
//        val adapterRows: ArrayList<Item> = arrayListOf()
//        for (row in rows) {
//
//            val rowKey: String? = row["key"]
//            val title: String? = row["title"]
//            val value: String? = row["value"]
//            val show: String? = row["show"]
//            val cell_type: String? = row["cell"]
//
//            var formItemAdapter: FormItemAdapter1? = null
//            if (cell_type == "text") {
//                formItemAdapter = PlainAdapter1(title!!, show!!)
//            } else if (cell_type == "tag") {
//                formItemAdapter = TagAdapter1(sectionKey, rowKey!!, title!!, value!!, show!!, this)
//            } else if (cell_type == "number") {
//                val tmp1: Array<String> = show!!.split(",").toTypedArray()
//                var min: Int = 1
//                var max: Int = 1
//                if (tmp1.size == 2) {
//                    min = tmp1[0].toInt()
//                    max = tmp1[1].toInt()
//                }
//
//                formItemAdapter = NumberAdapter1(sectionKey, rowKey!!, title!!, value!!, min, max, this)
//            }
//            if (formItemAdapter != null) {
//                adapterRows.add(formItemAdapter)
//            }
//        }
//
//        return adapterRows
//    }

    fun updateSubTotal() {

        sub_total = selected_price * selected_number
        val row = getRowFromRowKey(SUBTOTAL_KEY)
//        val row = getRowRowsFromMyRowsByKey1(SUBTOTAL_KEY)
        row.value = sub_total.toString()
        row.show = "NT$ " + sub_total.toString() + "元"
//        replaceRowFromKey(SUBTOTAL_KEY, row)
        updateTotal()

//        val priceItem = getFormItemFromKey(SUBTOTAL_KEY)
//        if (priceItem != null) {
//            sub_total = selected_price * selected_number
//            priceItem.value = sub_total.toString()
//            priceItem.make()
//            //updateShippingFee()
//            updateTotal()
//        }
    }

//    fun updateShippingFee() {
//        shippingFee = myTable!!.prices[selected_idx].shipping_fee
//        val priceItem = getFormItemFromKey(SHIPPING_FEE_KEY)
//        if (priceItem != null) {
//            //shippingFee = price
//            priceItem.value = shippingFee.toString()
//            priceItem.make()
//            updateTotal()
//        }
//    }

    fun updateTotal() {

        total = sub_total + shippingFee
        val row = getRowFromRowKey(TOTAL_KEY)
//        val row = getRowRowsFromMyRowsByKey1(TOTAL_KEY)
        row.value = total.toString()
        row.show = "NT$ " + total.toString() + "元"
//        replaceRowFromKey(TOTAL_KEY, row)
        //notifyChanged(true)

//        val priceItem = getFormItemFromKey(TOTAL_KEY)
//        if (priceItem != null) {
//            total = sub_total + shippingFee
//            priceItem.value = total.toString()
//            priceItem.make()
//            //adapter.notifyDataSetChanged()
//            notifyChanged(true)
//        }
    }

//    override fun notifyChanged(include_section: Boolean) {
//        if (include_section) {
//            for ((idx, _) in mySections.withIndex()) {
//                val items = generateItems(idx)
//                adapterSections[idx].update(items)
//            }
//        } else {
//            val items = generateItems()
//            adapter.update(items)
//        }
//        adapter.notifyDataSetChanged()
//    }

//    override fun setTag(sectionKey: String, rowKey: String, attribute: String, selected: Boolean) {
//
////        println(sectionKey)
////        println(rowKey)
////        println(attribute)
////        println(selected)
//
//        val rows = getRowRowsFromMyRowsByKey(sectionKey)
//        for (row in rows) {
//            if (row.containsKey("key")) {
//                val key: String = row["key"]!!
//                if (key == rowKey) {
//                    val _row = row
//                    _row["value"] = attribute
//                    replaceRowByKey(sectionKey, rowKey, _row)
//                    //notifyChanged(true)
//                }
//            }
//        }
//    }

//    override fun stepperValueChanged(sectionKey: String, rowKey: String, number: Int) {
//
//        val rows = getRowRowsFromMyRowsByKey(sectionKey)
//        for (row in rows) {
//            if (row.containsKey("key")) {
//                val key: String = row["key"]!!
//                if (key == rowKey) {
//                    val _row = row
//                    _row["value"] = number.toString()
//                    replaceRowByKey(sectionKey, rowKey, _row)
//                    selected_number = number
//                    updateSubTotal()
//
//                    //notifyChanged(true)
//                }
//            }
//        }
//    }
//
//    override fun tagChecked(checked: Boolean, name: String, key: String, value: String) {
//        //println(value)
//        if (name == "type") {
//            val id: Int = key.toInt()
//            var idx: Int = 0
//            for (price in productTable!!.prices) {
//                if (price.id == id) {
//                    selected_price = price.price_member
//                    this.selected_idx = idx
//                    updateSubTotal()
//                    break
//                }
//                idx += 1
//            }
//        }
//    }
//
//    override fun stepperValueChanged(number: Int, name: String) {
//        selected_number = number
//        val numberFormItem: NumberFormItem = getFormItemFromKey(name) as NumberFormItem
//        numberFormItem.value = number.toString()
//        updateSubTotal()
//    }

    override fun cellSetTag(sectionIdx: Int, rowIdx: Int, value: String, isChecked: Boolean) {
        val row: OneRow = oneSections[sectionIdx].items[rowIdx]
        //replaceRowFromIdx(sectionIdx, rowIdx, row)
        row.value = value
    }

    override fun cellNumberChanged(sectionIdx: Int, rowIdx: Int, number: Int) {
        val row: OneRow = oneSections[sectionIdx].items[rowIdx]
        row.value = number.toString()
        //replaceRowFromIdx(sectionIdx, rowIdx, row)
        selected_number = number
        updateSubTotal()
        oneSectionAdapter.notifyItemChanged(sectionIdx)
    }

//    override fun textFieldTextChanged(formItem: FormItem, text: String) {}
//
//    override fun sexChanged(sex: String) {}
//
//    override fun privateChanged(checked: Boolean) {}

    fun signupButtonPressed(view: View) {

        val params: HashMap<String, String> = hashMapOf()

        params["device"] = "app"
        params["do"] = "update"
        params["member_token"] = member.token!!

        if (cartItem_token != null) {
            params["cartItem_token"] = cartItem_token!!
        }
        if (productTable != null) {
            params["product_id"] = productTable!!.id.toString()
            params["cart_token"] = productTable!!.cart_token
        }
        params["price_id"] = productTable!!.prices[selected_idx].id.toString()

        params["member_id"] = member.id.toString()
//        params["order_name"] = member.name
//        params["order_tel"] = member.mobile
//        params["order_email"] = member.email
//        params["gateway"] = "credit_card"

//        val city_name = Global.zoneIDToName(member.city)
//        val area_name = Global.zoneIDToName(member.area)
//        params["order_city"] = city_name
//        params["order_area"] = area_name
//        params["order_road"] = member.address

//        val numberFormItem = getFormItemFromKey(NUMBER_KEY)
//        params[QUANTITY_KEY] = numberFormItem!!.value!!
        params[QUANTITY_KEY] = getRowValue(QUANTITY_KEY)

//        val totalFormItem = getFormItemFromKey(TOTAL_KEY)
//        params[AMOUNT_KEY] = totalFormItem!!.value!!
        params[AMOUNT_KEY] = getRowValue(TOTAL_KEY)

        //是否有選擇商品屬性
        var isAttribute: Boolean = true

        val selected_attributes: ArrayList<String> = arrayListOf()
//        @Suppress("UNCHECKED_CAST")
        //val attributes: ArrayList<HashMap<String, String>> = myRows[1]["rows"] as ArrayList<HashMap<String, String>>
        val attributes: ArrayList<OneRow> = getRowsFromSectionKey(ATTRIBUTE_KEY)
        for (attribute in attributes) {

            val value: String = attribute.value
            val alias: String = attribute.key
            val title: String = attribute.title
//            var tmp: String? = attribute["value"]
//            if (tmp != null) {
//                value = tmp
//            }
//            tmp = attribute["key"]
//            if (tmp != null) {
//                alias = tmp
//            }
//            tmp = attribute["title"]
//            if (tmp != null) {
//                name = tmp
//            }

            if (value.length == 0) {
                isAttribute = false
                warning("請先選擇${title}")
            } else {
                val tmp = "{name:${title},alias:${alias},value:${value}}"
                selected_attributes.add(tmp)
            }
        }

//        val shippingFeeFormItem = getFormItemFromKey(SHIPPING_FEE_KEY)
//        params["shipping_fee"] = shippingFeeFormItem!!.value!!

//        var item = getFormItemFromKey(COLOR_KEY)
//        if (item != null) {
//            params["color"] = item.value!!
//        }
//
//        item = getFormItemFromKey(CLOTHES_SIZE_KEY)
//        if (item != null) {
//            params["size"] = item.value!!
//        }
//
//        item = getFormItemFromKey(WEIGHT_KEY)
//        if (item != null) {
//            params["weight"] = item.value!!
//        }
//        println(params)

        if (isAttribute) {
            Loading.show(mask)

            params["attribute"] = selected_attributes.joinToString("|")
            //println(params)

            CartService.update(this, params) { success ->
                runOnUiThread {
                    Loading.hide(mask)
                }
                var msg: String = "成功加入購物車了"
                if (success) {
                    if (cartItem_token == null) {
                        cartItemCount += 1
                        session.edit().putInt("cartItemCount", cartItemCount).apply()
                    } else {
                        msg = "已經更新購物車了"
                    }
                    runOnUiThread {
                        info(msg, "", "關閉") {
                            val intent = Intent()
                            intent.putExtra("refresh", true)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
//                    val order_token: String = CartService.order_token
//                    if (total > 0) {
//                        val ecpay_token: String = OrderService.token
//                        val tokenExpireDate: String = OrderService.tokenExpireDate
//                        info("訂單已經成立，是否前往結帳？", "取消", "結帳") {
//                            //println("aaa");
//                            toPayment(order_token, ecpay_token, tokenExpireDate)
//                        }
//                    } else {
//                        info("訂單已經成立，結帳金額為零，我們會儘速處理您的訂單", "", "關閉") {
//                            toPayment(order_token)
//                        }
//                    }
                } else {
                    runOnUiThread {
                        warning("訂單失敗，或接收失敗，請洽管理員")
                    }
                }
            }
        }
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}

//class AddCartItemViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
//
//    var title: TextView = viewHolder.findViewById(R.id.row_title)
//    var show: TextView = viewHolder.findViewById(R.id.row_detail)
//    var clear: ImageView = viewHolder.findViewById(R.id.clearBtn)
//    var greater: ImageView = viewHolder.findViewById(R.id.greater)
//    var keyword: EditText = viewHolder.findViewById(R.id.keywordTxt)
//    var switch: SwitchCompat = viewHolder.findViewById(R.id.search_switch)
//}

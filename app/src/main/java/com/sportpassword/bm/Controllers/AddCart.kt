package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
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
import kotlinx.android.synthetic.main.cart_list_cell.view.*
import kotlinx.android.synthetic.main.formitem_more.view.*
import kotlinx.android.synthetic.main.formitem_more.view.promptBtn
import kotlinx.android.synthetic.main.formitem_number.view.*
import kotlinx.android.synthetic.main.formitem_plain.view.detail
import kotlinx.android.synthetic.main.formitem_plain.view.title
import kotlinx.android.synthetic.main.formitem_radio.view.*
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

        //initData()
        refresh()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        super.onCreateOptionsMenu(menu)

        val menuView = menu!!.findItem(R.id.menu_all).actionView
        val shoppingCartBtn = menuView.findViewById<ImageButton>(R.id.cart)
        shoppingCartBtn.visibility = View.VISIBLE
        return true
    }

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
                Loading.hide(mask)
                if (success) {
                    try {
                        productTable = jsonToModel<ProductTable>(dataService.jsonString)
                    } catch (e: JsonParseException) {
                        warning(e.localizedMessage!!)
                        //println(e.localizedMessage)
                    }
                    if (productTable != null) {
                        //myTable = table as ProductTable
                        productTable!!.filterRow()
                        initData()
                        oneSectionAdapter.setOneSection(oneSections)
                        oneSectionAdapter.notifyDataSetChanged()
                    } else {
                        warning("解析伺服器所傳的字串失敗，請洽管理員")
                    }
                }
            }
        }

        if (cartItem_token != null) {

            update = true
            submitBtn.text = "更新購物車"
            val params: HashMap<String, String> = hashMapOf("cart_item_token" to cartItem_token!!, "member_token" to member.token!!)
            CartService.getOne(this, params) { success ->
                Loading.hide(mask)
                if (success) {
                    try {
                        cartTable = jsonToModel<CartTable>(CartService.jsonString)
                    } catch (e: JsonParseException) {
                        warning(e.localizedMessage!!)
                    }
                    if (cartTable != null) {
                        if (cartTable!!.items.size > 0) {
                            cartItemTable = cartTable!!.items[0]
                            cartItemTable?.filterRow()
                            productTable = cartItemTable!!.product
                            productTable?.filterRow()
                            initData()
                            oneSectionAdapter.setOneSection(oneSections)
                            oneSectionAdapter.notifyDataSetChanged()
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
        replaceRowFromKey(SUBTOTAL_KEY, row)
//        replaceRowByKey(SUBTOTAL_KEY, row)
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
        replaceRowFromKey(TOTAL_KEY, row)
//        replaceRowByKey(TOTAL_KEY, row)
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

    fun submitBtnPressed(view: View) {

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
                Loading.hide(mask)
                var msg: String = "成功加入購物車了"
                if (success) {
                    if (cartItem_token == null) {
                        cartItemCount += 1
                        session.edit().putInt("cartItemCount", cartItemCount).apply()
                    } else {
                        msg = "已經更新購物車了"
                    }
                    info(msg, "", "關閉") {
                        val intent = Intent()
                        intent.putExtra("refresh", true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
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
                    warning("訂單失敗，或接收失敗，請洽管理員")
                }
            }
        }
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}

class OneSectionAdapter(val context: Context, private val resource: Int, var delegate: List1CellDelegate, val others: HashMap<String, String>): RecyclerView.Adapter<OneSectionViewHolder>() {
    private var oneSections: ArrayList<OneSection> = arrayListOf()
    //lateinit var adapter: TeamSearchItemAdapter

    fun setOneSection(oneSections: ArrayList<OneSection>) {
        this.oneSections = oneSections
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneSectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(resource, parent, false)

        return OneSectionViewHolder(viewHolder)

    }

    override fun onBindViewHolder(holder: OneSectionViewHolder, position: Int) {

        val section: OneSection = oneSections[position]
        holder.titleLbl.text = section.title

        val adapter =
            OneItemAdapter(context, position, oneSections[position], delegate, others)
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        var iconID: Int = 0
        if (section.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)
        holder.greater.setOnClickListener {
            delegate.handleSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return oneSections.size
    }
}

class OneSectionViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView = viewHolder.findViewById(R.id.titleLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class OneItemAdapter(val context: Context, private val sectionIdx: Int, private val oneSection: OneSection, var delegate: List1CellDelegate, val others: HashMap<String, String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var oneRows: ArrayList<OneRow> = oneSection.items
    var rowIdx: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        //var viewHolder = inflater.inflate(R.layout.formitem_plain, parent, false)
        when (viewType) {
            CELL_TYPE.PLAIN.toInt() -> {
                return PlainViewHolder(inflater.inflate(R.layout.formitem_plain, parent, false))
            }
            CELL_TYPE.TEXTFIELD.toInt() -> {
                return TextFieldViewHolder(inflater.inflate(R.layout.formitem_textfield, parent, false))
            }
            CELL_TYPE.TAG.toInt() -> {
                return TagViewHolder(inflater.inflate(R.layout.formitem_tag, parent, false))
            }
            CELL_TYPE.NUMBER.toInt() -> {
                return NumberViewHolder(inflater.inflate(R.layout.formitem_number, parent, false))
            }
            CELL_TYPE.CART.toInt() -> {
                return CartViewHolder(inflater.inflate(R.layout.cart_list_cell, parent, false))
            }
            CELL_TYPE.RADIO.toInt() -> {
                return RadioViewHolder(inflater.inflate(R.layout.formitem_radio, parent, false))
            }
            CELL_TYPE.MORE.toInt() -> {
                return MoreViewHolder(inflater.inflate(R.layout.formitem_more, parent, false))
            }
            else -> {
                return PlainViewHolder(inflater.inflate(R.layout.formitem_plain, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val row: OneRow = oneRows[position]
        return when (row.cell) {
            "text" -> CELL_TYPE.PLAIN.toInt()
            "textField" -> CELL_TYPE.TEXTFIELD.toInt()
            "tag" -> CELL_TYPE.TAG.toInt()
            "number" -> CELL_TYPE.NUMBER.toInt()
            "cart" -> CELL_TYPE.CART.toInt()
            "radio" -> CELL_TYPE.RADIO.toInt()
            "more" -> CELL_TYPE.MORE.toInt()
            else -> throw IllegalArgumentException("錯誤的格式" + position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        this.rowIdx = position
        val row: OneRow = oneRows[position]

        if (holder is PlainViewHolder) {
            holder.title.text = row.title
            holder.show.text = row.show
            holder.show.backgroundColor = Color.TRANSPARENT
        } else if (holder is TextFieldViewHolder) {
            holder.title.text = row.title
            holder.prompt.visibility = View.INVISIBLE
            holder.value.setText(row.value)
            holder.value.hint = row.placeholder

            holder.value.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    delegate.cellTextChanged(sectionIdx, position, p0.toString())
                }
            })

            holder.clear.setOnClickListener {
                delegate.cellClear(sectionIdx, position)
            }
        } else if (holder is TagViewHolder) {
            holder.title.text = row.title

            val tags: ArrayList<Tag> = holder.generateTag(context, row.value, row.show)
            for (tag in tags) {
                tag.setOnClickListener {
                    holder.handleTap(tag)
                    delegate.cellSetTag(this.sectionIdx, this.rowIdx, tag.value, tag.isChecked)
                }
            }
        } else if (holder is NumberViewHolder) {
            holder.title.text = row.title

            holder.init(row.value.toInt(), row.show)
            holder.plus.setOnClickListener {
                val number = holder.plusClick()
                //delegate with number
                delegate.cellNumberChanged(sectionIdx, position, number)
            }
            holder.minus.setOnClickListener {
                val number = holder.minusClick()
                delegate.cellNumberChanged(sectionIdx, position, number)
            }
        } else if (holder is CartViewHolder) {
            holder.title.text = row.title
            if (row.feature_path.isNotEmpty()) {
                Picasso.with(context)
                    .load(row.feature_path)
                    .placeholder(R.drawable.loading_square_120)
                    .error(R.drawable.loading_square_120)
                    .into(holder.featured)
            }
            holder.attribute.text = row.attribute
            holder.amount.text = row.amount
            holder.quantity.text = "數量：${row.quantity}"

            if (others.containsKey("product_icon_view")) {
                val b: Boolean = others["product_icon_view"].toBoolean()
                if (b) {
                    holder.iconView.visibility = View.VISIBLE
                } else {
                    holder.iconView.visibility = View.GONE
                }
            }
            if (holder.iconView.visibility == View.VISIBLE) {
                holder.editIcon.setOnClickListener {
                    delegate.cellEdit(sectionIdx, rowIdx)
                }

                holder.deleteIcon.setOnClickListener {
                    delegate.cellDelete(sectionIdx, rowIdx)
                }

                holder.refreshIcon.setOnClickListener {
                    delegate.cellRefresh()
                }
            }
        } else if (holder is RadioViewHolder) {

            val group = holder.init(context, row)
            group.setOnCheckedChangeListener { radioGroup, i ->
                delegate.cellRadioChanged(row.key, sectionIdx, position, i)
            }
        } else if (holder is MoreViewHolder) {
            holder.title.text = row.title
            holder.show.text = row.show
            holder.show.visibility = View.VISIBLE
            holder.prompt.visibility = View.INVISIBLE

            holder.viewHolder.setOnClickListener {
                delegate.cellMoreClick(sectionIdx, position)
            }

            if (row.isClear) {
                holder.clear.visibility = View.VISIBLE
                holder.clear.setOnClickListener {
                    delegate.cellClear(sectionIdx, position)
                }
            } else {
                holder.clear.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        if (oneSection.isExpanded) {
            return oneRows.size
        } else {
            return 0
        }
    }
}

//abstract class FormViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
//    abstract fun bind(row: OneRow)
//}

class PlainViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.title
    val show: TextView = viewHolder.detail
}

class TextFieldViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.title
    val prompt: ImageView = viewHolder.promptBtn
    val value: EditText = viewHolder.textField

    val clear: ImageView = viewHolder.clear
}

class TagViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.title
    val tag_container: TableLayout = viewHolder.tag_container
    val tagLabels: ArrayList<Tag> = arrayListOf()

    fun generateTag(context: Context, value: String, show: String): ArrayList<Tag> {
        val columnNum: Int = 3
        var rowNum: Int = 0

        val attributes: Array<String> = show.split(",").toTypedArray()
        val count: Int = attributes.size

        val (q, r) = count.quotientAndRemainder(columnNum)
        rowNum = if (r > 0) { q + 1 } else { q }
        tag_container.removeAllViews()

        var tableRow: LinearLayout? = null
        for ((idx, attribute) in attributes.withIndex()) {
            var (_, columnCount) = idx.quotientAndRemainder(columnNum)
            columnCount++

            //如果換下一行，則new 一個新的row
            if (columnCount == 1) {
                tableRow = LinearLayout(context)
                tableRow.orientation = LinearLayout.HORIZONTAL
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180)
                tableRow.layoutParams = lp
                tag_container.addView(tableRow)
            }
            val lp_tag = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp_tag.gravity = Gravity.CENTER
            lp_tag.weight = 1F

            val tag: Tag = Tag(context)
            tag.layoutParams = lp_tag
            tableRow!!.addView(tag)
            tag.tag = idx
            tag.key = attribute
            tag.value = attribute
            tag.tag_view.text = attribute

            tagLabels.add(tag)

            if (attribute == value) {
                tag.isChecked = true
                tag.setSelectedStyle()
            }
        }

        return tagLabels
    }

    fun handleTap(view: View) {

        val tag: Tag = view as Tag
        val rowIdx: Int = tag.tag as Int
        tag.isChecked = !tag.isChecked
        tag.setSelectedStyle()
        clearOtherTagSelected(tag)
    }

    fun clearOtherTagSelected(selectedTag: Tag) {
        if (selectedTag.isChecked) {
            for (tagLabel in tagLabels) {
                if (tagLabel != selectedTag) {
                    tagLabel.isChecked = false
                    tagLabel.unSelectedStyle()
                }
            }
        }
    }
}

class NumberViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.title
    val minus: Button = viewHolder.minus
    val plus: Button = viewHolder.plus
    var numberLbl: TextView = viewHolder.numberLbl

    var value: Int = 1
    var show: String = ""
    var min: Int = 1
    var max: Int = 1
    var number: Int = value

    fun init(value: Int, show: String) {
        this.value = value
        this.show = show
        this.number = value

        val tmp1: Array<String> = show.split(",").toTypedArray()
        if (tmp1.size == 2) {
            min = tmp1[0].toInt()
            max = tmp1[1].toInt()
        }
        numberLbl.text = value.toString()
    }

    fun plusClick (): Int {

        if (!minus.isEnabled) {
            minus.isEnabled = true
        }
        number += 1
        if (number > max) {
            number = max
            plus.isEnabled = false
        }
        numberLbl.text = number.toString()

        return number
    }

    fun minusClick (): Int {

        if (!plus.isEnabled) {
            plus.isEnabled = true
        }
        number -= 1
        if (number < min) {
            number = min
            minus.isEnabled = false
        }
        numberLbl.text = number.toString()

        return number
    }
}

class CartViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    val title: TextView = viewHolder.titleLbl
    val featured: ImageView = viewHolder.listFeatured
    val attribute: TextView = viewHolder.attributeLbl
    val amount: TextView = viewHolder.amountLbl
    val quantity: TextView = viewHolder.quantityLbl

    val iconView: RelativeLayout = viewHolder.findViewById(R.id.iconView)
    val editIcon: ImageView = viewHolder.findViewById(R.id.editIcon)
    val deleteIcon: ImageView = viewHolder.findViewById(R.id.deleteIcon)
    val refreshIcon: ImageView = viewHolder.findViewById(R.id.refreshIcon)
}

class RadioViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    fun init(context: Context, row: OneRow): RadioGroup {

        val textColor: Int = ContextCompat.getColor(context, R.color.MY_WHITE)
        val checkedColor: Int = ContextCompat.getColor(context, R.color.MY_RED)

        val colorStateList: ColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled), //disabled
                intArrayOf(android.R.attr.state_enabled)   //enabled
            ), intArrayOf(
                textColor, //disabled
                textColor  //enabled
            )
        )

        viewHolder.radioContainer.removeAllViews()

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(18, 24, 0, 24)

        val group = RadioGroup(context)

        val radioButtons: ArrayList<RadioButton> = arrayListOf()

        val arr: Array<String> = row.show.split(",").toTypedArray()
        val titles: Array<String> = row.title.split(",").toTypedArray()
        for ((idx, value) in arr.withIndex()) {
            val title: String = titles[idx]
            val radioButton: RadioButton = RadioButton(context)
            radioButton.id = idx
            radioButton.text = title
            radioButton.buttonTintList = colorStateList
            radioButton.setTextColor(textColor)
            radioButton.textSize = 18F
            radioButton.layoutParams = lp

            val isChecked: Boolean = row.value == value ?: run {
                false
            }

            radioButton.isChecked = isChecked
            group.addView(radioButton)

            radioButtons.add(radioButton)
        }

        viewHolder.radioContainer.addView(group)

        return group
    }
}

class MoreViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    val title: TextView = viewHolder.title
    val show: TextView = viewHolder.detail
    val prompt: ImageView = viewHolder.promptBtn

    val clear: ImageView = viewHolder.clear
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

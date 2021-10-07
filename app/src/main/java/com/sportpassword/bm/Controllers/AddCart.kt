package com.sportpassword.bm.Controllers

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.Form.*
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Data.AddCartRow
import com.sportpassword.bm.Data.AddCartSection
import com.sportpassword.bm.Data.SearchRow
import com.sportpassword.bm.Data.SearchSection
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItem.NumberFormItem
import com.sportpassword.bm.Form.ValueChangedDelegate
import com.sportpassword.bm.Fragments.SearchItemAdapter
import com.sportpassword.bm.Models.CartItemTable
import com.sportpassword.bm.Models.CartTable
import com.sportpassword.bm.Models.ProductTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CartService
import com.sportpassword.bm.Services.ProductService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_addcart_vc.*
import kotlinx.android.synthetic.main.mask.*

class AddCartVC : MyTableVC(), ValueChangedDelegate {

    var product_token: String? = null
    var cartItem_token: String? = null
    var productTable: ProductTable? = null
    var cartTable: CartTable? = null
    var cartItemTable: CartItemTable? = null

    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()

    var sub_total: Int = 0
    var shippingFee: Int = 0
    var total: Int = 0

    var selected_number: Int = 1
    var selected_price: Int = 0
    var selected_idx: Int = 0

    val productRows: ArrayList<HashMap<String, String>> =  arrayListOf(
        hashMapOf("title" to "商品","key" to PRODUCT_KEY,"value" to "","show" to "","cell" to "text")
    )

    var attributeRows: ArrayList<HashMap<String, String>> = arrayListOf()

    val amountRows: ArrayList<HashMap<String, String>> =  arrayListOf(
        hashMapOf("title" to "數量","key" to QUANTITY_KEY,"value" to "","show" to "","cell" to "number"),
        hashMapOf("title" to "小計","key" to SUBTOTAL_KEY,"value" to "","show" to "","cell" to "text"),
        //hashMapOf("title" to "運費","key" to SHIPPING_FEE_KEY,"value" to "","show" to "","cell" to "text"),
        hashMapOf("title" to "總計","key" to TOTAL_KEY,"value" to "","show" to "","cell" to "text")
    )

//    val contactRows: ArrayList<HashMap<String, String>> =  arrayListOf(
//        hashMapOf("title" to "姓名","key" to NAME_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.default.toString()),
//        hashMapOf("title" to "行動電話","key" to MOBILE_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.numberPad.toString()),
//        hashMapOf("title" to "EMail","key" to EMAIL_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.emailAddress.toString()),
//        hashMapOf("title" to "住址","key" to ADDRESS_KEY,"value" to "","show" to "","cell" to "textField","keyboard" to KEYBOARD.default.toString())
//    )

    override fun onCreate(savedInstanceState: Bundle?) {

        mySections = arrayListOf(
            hashMapOf("name" to "商品名稱", "isExpanded" to true, "key" to PRODUCT_KEY),
            hashMapOf("name" to "商品選項", "isExpanded" to true, "key" to ATTRIBUTE_KEY),
            hashMapOf("name" to "款項", "isExpanded" to true, "key" to AMOUNT_KEY)
            //hashMapOf("name" to "寄件資料", "isExpanded" to true, "key" to CONTACT_KEY)
        )

        myRows = arrayListOf(
            hashMapOf("key" to PRODUCT_KEY, "rows" to productRows),
            hashMapOf("key" to ATTRIBUTE_KEY, "rows" to attributeRows),
            hashMapOf("key" to AMOUNT_KEY, "rows" to amountRows)
            //hashMapOf("key" to CONTACT_KEY, "rows" to contactRows),
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcart_vc)

        if (intent.hasExtra("product_token")) {
            product_token = intent.getStringExtra("product_token")
        }
        if (intent.hasExtra("cartItem_token")) {
            cartItem_token = intent.getStringExtra(("cartItem_token"))
        }

        hidekeyboard(order_layout)

        dataService = ProductService
        recyclerView = editTableView

        refreshLayout = refresh
        setRefreshListener()

        //initData()
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        super.onCreateOptionsMenu(menu)

        val menuView = menu!!.findItem(R.id.menu_all).actionView
        val shoppingCartBtn = menuView.findViewById<ImageButton>(R.id.cart)
        shoppingCartBtn.visibility = View.VISIBLE
        return true
    }

    override fun initAdapter(include_section: Boolean) {
//        adapter = GroupAdapter()

//        adapter.setOnItemClickListener { item, view ->
//            rowClick(item, view)
//        }

        // for member register and member update personal data
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
//            for ((idx, mySection) in mySections.withIndex()) {
//                val title: String = mySection["name"] as String
//                val isExpanded: Boolean = mySection["isExpanded"] as Boolean
//
//                val expandableGroup = ExpandableGroup(GroupSection(title), isExpanded)
//                val items = generateItems(idx)
//                adapterSections[idx].addAll(items)
//                expandableGroup.add(adapterSections[idx])
//
//                adapter.add(expandableGroup)
//            }
        }


        recyclerView.adapter = adapter
//        recyclerView.setHasFixedSize(true)
        if (refreshLayout != null) {
            setRefreshListener()
        }
        setRecyclerViewScrollListener()
    }

    override fun refresh() {
        Loading.show(mask)
        if (product_token != null) {
            adapter.clear()
            adapterSections.clear()
            attributeRows.clear()

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
                    } else {
                        warning("解析伺服器所傳的字串失敗，請洽管理員")
                    }
                }
            }
        }

        if (cartItem_token != null) {

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
            initAdapter(true)
            setMyTitle(productTable!!.name)

            var row = getRowRowsFromMyRowsByKey1(PRODUCT_KEY)
            row["value"] = productTable!!.name
            row["show"] = productTable!!.name
            replaceRowByKey(PRODUCT_KEY, row)

//        row = getRowRowsFromMyRowsByKey1(NAME_KEY)
//        row["value"] = member.name
//        row["show"] = member.name
//        replaceRowByKey(NAME_KEY, row)
//
//        row = getRowRowsFromMyRowsByKey1(MOBILE_KEY)
//        row["value"] = member.mobile
//        row["show"] = member.mobile
//        replaceRowByKey(MOBILE_KEY, row)
//
//        row = getRowRowsFromMyRowsByKey1(EMAIL_KEY)
//        row["value"] = member.email
//        row["show"] = member.email
//        replaceRowByKey(EMAIL_KEY, row)
//
//        row = getRowRowsFromMyRowsByKey1(ADDRESS_KEY)
//        row["value"] = member.address
//        row["show"] = member.address
//        replaceRowByKey(ADDRESS_KEY, row)

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

                //print(arr)
                row = hashMapOf(
                    "title" to attribute.name,
                    "key" to alias,
                    "value" to value,
                    "show" to tmp,
                    "cell" to "tag"
                )
                attributeRows.add(row)
            }
            //print(attributeRows)
            myRows[1]["rows"] = attributeRows

            row = getRowRowsFromMyRowsByKey1(QUANTITY_KEY)
            val min: String = productTable!!.order_min.toString()
            val max: String = productTable!!.order_max.toString()
            row["show"] = "${min},${max}"
            row["value"] = "1"

            if (cartItemTable != null) {
                selected_number = cartItemTable!!.quantity
                row["value"] = selected_number.toString()
            }
            replaceRowByKey(QUANTITY_KEY, row)

//        row = getRowRowsFromMyRowsByKey1(SHIPPING_FEE_KEY)
//        row["value"] = myTable!!.prices[selected_idx].shipping_fee.toString()
//        row["show"] = "NT$ " + myTable!!.prices[selected_idx].shipping_fee.toString() + "元"
//        replaceRowByKey(SHIPPING_FEE_KEY, row)

            selected_price = productTable!!.prices[selected_idx].price_member
            updateSubTotal()

//        val productNameItem = getFormItemFromKey("Product_Name")
//        if (productNameItem != null) {
//            productNameItem.value = superProduct!!.name
//            productNameItem.make()
//        }
//
//        val nameItem = getFormItemFromKey(NAME_KEY)
//        if (nameItem != null) {
//            nameItem.value = member.name
//            nameItem.make()
//        }
//
//        val mobileItem = getFormItemFromKey(MOBILE_KEY)
//        if (mobileItem != null) {
//            mobileItem.value = member.mobile
//            mobileItem.make()
//        }
//
//        val emailItem = getFormItemFromKey(EMAIL_KEY)
//        if (emailItem != null) {
//            emailItem.value = member.email
//            emailItem.make()
//        }
//
//        val addressItem = getFormItemFromKey(ADDRESS_KEY)
//        if (addressItem != null) {
//            addressItem.value = member.road
//            addressItem.make()
//        }
//
//        val colorItem = getFormItemFromKey(COLOR_KEY)
//        if (colorItem != null) {
//            val item = colorItem as TagFormItem
//            val res: ArrayList<HashMap<String, String>> = arrayListOf()
//            for (color in superProduct!!.colors) {
//                val dict: HashMap<String, String> = hashMapOf(color to color)
//                res.add(dict)
//            }
//            item.tags = res
//        }
//
//        val clothesSizeItem = getFormItemFromKey(CLOTHES_SIZE_KEY)
//        if (clothesSizeItem != null) {
//            val item = clothesSizeItem as TagFormItem
//            val res: ArrayList<HashMap<String, String>> = arrayListOf()
//            for (size in superProduct!!.sizes) {
//                val dict: HashMap<String, String> = hashMapOf(size to size)
//                res.add(dict)
//            }
//            item.tags = res
//        }
//
//        val weightItem = getFormItemFromKey(WEIGHT_KEY)
//        if (weightItem != null) {
//            val item = weightItem as TagFormItem
//            val res: ArrayList<HashMap<String, String>> = arrayListOf()
//            for (weight in superProduct!!.weights) {
//                val dict: HashMap<String, String> = hashMapOf(weight to weight)
//                res.add(dict)
//            }
//            item.tags = res
//        }
//
//        if (superProduct!!.type == "mejump") {
//            val typeItem = getFormItemFromKey("type")
//            if (typeItem != null) {
//                val item = typeItem as TagFormItem
//                val res: ArrayList<HashMap<String, String>> = arrayListOf()
//                for (value in superProduct!!.prices) {
//                    val type: String = value.price_title
//                    val typePrice: String = value.price_member.toString()
//                    val str: String = type + " " + typePrice
//                    val dict: HashMap<String, String> = hashMapOf(value.id.toString() to str)
//                    res.add(dict)
//                }
//                item.tags = res
//            }
//        }
//
//        val numberItem = getFormItemFromKey(NUMBER_KEY)
//        if (numberItem != null) {
//            val item = numberItem as NumberFormItem
//            item.min = superProduct!!.order_min
//            item.max = superProduct!!.order_max
//        }
//
//        if (getFormItemFromKey(SUBTOTAL_KEY) != null) {
//            selected_price = superProduct!!.prices[selected_idx].price_member
//            updateSubTotal()
//        }
//
//        if (getFormItemFromKey(SHIPPING_FEE_KEY) != null) {
//            shippingFee = superProduct!!.prices[selected_idx].shipping_fee
//            updateShippingFee()
//        }
        }
    }

    override fun generateItems(section: Int): ArrayList<Item> {

        var sectionKey: String = ""
        val section: HashMap<String, Any> = myRows[section]
        val tmp: String? = section["key"] as? String
        if (tmp != null) {
            sectionKey = tmp
        }

        val clearClick = { formItem: FormItem ->
        }

        val promptClick = { formItem: FormItem ->
        }

        if (!section.containsKey("rows")) {
            return arrayListOf()
        }

        @Suppress("UNCHECKED_CAST")
        val rows: ArrayList<HashMap<String, String>> = section["rows"] as ArrayList<HashMap<String, String>>

        val adapterRows: ArrayList<Item> = arrayListOf()
        for (row in rows) {

            val rowKey: String? = row["key"]
            val title: String? = row["title"]
            val value: String? = row["value"]
            val show: String? = row["show"]
            val cell_type: String? = row["cell"]

            var formItemAdapter: FormItemAdapter1? = null
            if (cell_type == "text") {
                formItemAdapter = PlainAdapter1(title!!, show!!)
            } else if (cell_type == "tag") {
                formItemAdapter = TagAdapter1(sectionKey, rowKey!!, title!!, value!!, show!!, this)
            } else if (cell_type == "number") {
                val tmp1: Array<String> = show!!.split(",").toTypedArray()
                var min: Int = 1
                var max: Int = 1
                if (tmp1.size == 2) {
                    min = tmp1[0].toInt()
                    max = tmp1[1].toInt()
                }

                formItemAdapter = NumberAdapter1(sectionKey, rowKey!!, title!!, value!!, min, max, this)
            }
            if (formItemAdapter != null) {
                adapterRows.add(formItemAdapter)
            }
        }

//        val arr: ArrayList<FormItem> = arrayListOf()
//        for (key in section_keys[section]) {
//            for (formItem in form.formItems) {
//                if (key == formItem.name) {
//                    arr.add(formItem)
//                    break
//                }
//            }
//        }

//        println(arr)

//        var idx: Int = 0
//        for (i in 0..(section-1)) {
//            idx += section_keys[i].size
//        }

//        for ((i,formItem) in arr.withIndex()) {
//
//            val indexPath: IndexPath = IndexPath(section, i)
//            var idx: Int = 0
//            for ((j, _forItem) in form.formItems.withIndex()) {
//                if (formItem.name == _forItem.name) {
//                    idx = j
//                    break
//                }
//            }
//
//            var formItemAdapter: FormItemAdapter? = null
//            if (formItem.uiProperties.cellType == FormItemCellType.textField) {
//                formItemAdapter = TextFieldAdapter(formItem, clearClick, promptClick)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.plain) {
//                formItemAdapter = PlainAdapter(formItem)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.tag) {
//                formItemAdapter = TagAdapter(formItem)
//            } else if (formItem.uiProperties.cellType == FormItemCellType.number) {
//                val _formItem = formItem as NumberFormItem
//                var number: Int = 1
//                if (_formItem.value != null) {
//                    number = _formItem.value!!.toInt()
//                }
//                formItemAdapter = NumberAdapter(formItem, number, _formItem.min, _formItem.max)
//            }
//
//            if (formItemAdapter != null) {
//                formItemAdapter!!.valueChangedDelegate = this
//                rows.add(formItemAdapter!!)
//            }
////            idx++
//        }

        return adapterRows
    }

    fun updateSubTotal() {

        sub_total = selected_price * selected_number
        val row = getRowRowsFromMyRowsByKey1(SUBTOTAL_KEY)
        row["value"] = sub_total.toString()
        row["show"] = "NT$ " + sub_total.toString() + "元"
        replaceRowByKey(SUBTOTAL_KEY, row)
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

        val row = getRowRowsFromMyRowsByKey1(TOTAL_KEY)
        row["value"] = total.toString()
        row["show"] = "NT$ " + total.toString() + "元"
        replaceRowByKey(TOTAL_KEY, row)
        notifyChanged(true)

//        val priceItem = getFormItemFromKey(TOTAL_KEY)
//        if (priceItem != null) {
//            total = sub_total + shippingFee
//            priceItem.value = total.toString()
//            priceItem.make()
//            //adapter.notifyDataSetChanged()
//            notifyChanged(true)
//        }
    }

    override fun notifyChanged(include_section: Boolean) {
        if (include_section) {
            for ((idx, _) in mySections.withIndex()) {
                val items = generateItems(idx)
                adapterSections[idx].update(items)
            }
        } else {
            val items = generateItems()
            adapter.update(items)
        }
        adapter.notifyDataSetChanged()
    }

    override fun setTag(sectionKey: String, rowKey: String, attribute: String, selected: Boolean) {

//        println(sectionKey)
//        println(rowKey)
//        println(attribute)
//        println(selected)

        val rows = getRowRowsFromMyRowsByKey(sectionKey)
        for (row in rows) {
            if (row.containsKey("key")) {
                val key: String = row["key"]!!
                if (key == rowKey) {
                    val _row = row
                    _row["value"] = attribute
                    replaceRowByKey(sectionKey, rowKey, _row)
                    notifyChanged(true)
                }
            }
        }
    }

    override fun stepperValueChanged(sectionKey: String, rowKey: String, number: Int) {

        val rows = getRowRowsFromMyRowsByKey(sectionKey)
        for (row in rows) {
            if (row.containsKey("key")) {
                val key: String = row["key"]!!
                if (key == rowKey) {
                    val _row = row
                    _row["value"] = number.toString()
                    replaceRowByKey(sectionKey, rowKey, _row)
                    selected_number = number
                    updateSubTotal()

                    notifyChanged(true)
                }
            }
        }
    }

    override fun tagChecked(checked: Boolean, name: String, key: String, value: String) {
        //println(value)
        if (name == "type") {
            val id: Int = key.toInt()
            var idx: Int = 0
            for (price in productTable!!.prices) {
                if (price.id == id) {
                    selected_price = price.price_member
                    this.selected_idx = idx
                    updateSubTotal()
                    break
                }
                idx += 1
            }
        }
    }

    override fun stepperValueChanged(number: Int, name: String) {
        selected_number = number
        val numberFormItem: NumberFormItem = getFormItemFromKey(name) as NumberFormItem
        numberFormItem.value = number.toString()
        updateSubTotal()
    }

    override fun textFieldTextChanged(formItem: FormItem, text: String) {}

    override fun sexChanged(sex: String) {}

    override fun privateChanged(checked: Boolean) {}

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
        @Suppress("UNCHECKED_CAST")
        val attributes: ArrayList<HashMap<String, String>> = myRows[1]["rows"] as ArrayList<HashMap<String, String>>
        for (attribute in attributes) {

            var value: String = ""
            var alias: String = ""
            var name: String = ""
            var tmp: String? = attribute["value"]
            if (tmp != null) {
                value = tmp
            }
            tmp = attribute["key"]
            if (tmp != null) {
                alias = tmp
            }
            tmp = attribute["title"]
            if (tmp != null) {
                name = tmp
            }

            if (value.length == 0) {
                isAttribute = false
                warning("請先選擇${name}")
            } else {
                value = "{name:${name},alias:${alias},value:${value}}"
                selected_attributes.add(value)
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
        //println(params)

        if (isAttribute) {
            Loading.show(mask)

            params["attribute"] = selected_attributes.joinToString("|")

            CartService.update(this, params) { success ->
                Loading.hide(mask)
                if (success) {
                    if (cartItem_token == null) {
                        info("成功加入購物車了")
                        cartItemCount += 1
                        session.edit().putInt("cartItemCount", cartItemCount).apply()
                    } else {
                        info("已經更新購物車了")
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

class AddCartSectionAdapter(val context: Context, private val resource: Int, var delegate: List1CellDelegate): RecyclerView.Adapter<AddCartSectionViewHolder>() {
    private var addCartSections: ArrayList<AddCartSection> = arrayListOf()
    //lateinit var adapter: TeamSearchItemAdapter

    fun setAddCartSection(addCartSections: ArrayList<AddCartSection>) {
        this.addCartSections = addCartSections
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCartSectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(resource, parent, false)

        return AddCartSectionViewHolder(viewHolder)

    }

    override fun onBindViewHolder(holder: AddCartSectionViewHolder, position: Int) {

        val section: AddCartSection = addCartSections[position]
        holder.titleLbl.text = section.title

        var iconID: Int = 0
        if (section.isExpanded) {
            iconID = context.resources.getIdentifier("to_down", "drawable", context.packageName)
        } else {
            iconID = context.resources.getIdentifier("to_right", "drawable", context.packageName)
        }
        holder.greater.setImageResource(iconID)

        val adapter =
            AddCartItemAdapter(context, position, addCartSections[position], delegate)
//            holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.adapter = adapter

        holder.greater.setOnClickListener {
            delegate.handleSectionExpanded(position)
        }
    }

    override fun getItemCount(): Int {
        return addCartSections.size
    }
}

class AddCartSectionViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var titleLbl: TextView = viewHolder.findViewById(R.id.titleLbl)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var recyclerView: RecyclerView = viewHolder.findViewById(R.id.recyclerView)
}

class AddCartItemAdapter(val context: Context, private val sectionIdx: Int, private val addCartSection: AddCartSection, var delegate: List1CellDelegate): RecyclerView.Adapter<AddCartItemViewHolder>() {

    var addCartRows: ArrayList<AddCartRow> = addCartSection.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCartItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = inflater.inflate(R.layout.search_row_item, parent, false)

        return AddCartItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: AddCartItemViewHolder, position: Int) {

        val row: AddCartRow = addCartRows[position]
        holder.title.text = row.title
        holder.show.text = row.show

        val cell = row.cell
        if (cell == "textField") {
            holder.show.visibility = View.INVISIBLE
            holder.greater.visibility = View.INVISIBLE
            holder.keyword.visibility = View.VISIBLE
            if (row.show.isNotEmpty()) {
                holder.keyword.setText(row.show)
            }
            holder.keyword.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    delegate.cellTextChanged(sectionIdx, position, p0.toString())
                }
            })
        } else if (cell == "switch") {
            holder.show.visibility = View.INVISIBLE
            holder.greater.visibility = View.INVISIBLE
            holder.keyword.visibility = View.INVISIBLE
            holder.switch.visibility = View.VISIBLE
            holder.clear.visibility = View.INVISIBLE

            holder.switch.isChecked = row.value == "1"
        }

        holder.viewHolder.setOnClickListener {
            delegate.cellClick(sectionIdx, position)
        }

        holder.switch.setOnCheckedChangeListener { compoundButton, b ->
            delegate.cellSwitchChanged(sectionIdx, position, b)
        }

        holder.clear.setOnClickListener {
            delegate.cellClear(sectionIdx, position)
        }
    }

    override fun getItemCount(): Int {
        if (addCartSection.isExpanded) {
            return addCartRows.size
        } else {
            return 0
        }
    }

}

class AddCartItemViewHolder(val viewHolder: View): RecyclerView.ViewHolder(viewHolder) {

    var title: TextView = viewHolder.findViewById(R.id.row_title)
    var show: TextView = viewHolder.findViewById(R.id.row_detail)
    var clear: ImageView = viewHolder.findViewById(R.id.clearBtn)
    var greater: ImageView = viewHolder.findViewById(R.id.greater)
    var keyword: EditText = viewHolder.findViewById(R.id.keywordTxt)
    var switch: SwitchCompat = viewHolder.findViewById(R.id.search_switch)
}

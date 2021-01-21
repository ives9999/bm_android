package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.sportpassword.bm.Adapters.Form.*
import com.sportpassword.bm.Form.FormItem.FormItem
import com.sportpassword.bm.Form.FormItem.NumberFormItem
import com.sportpassword.bm.Form.FormItem.TagFormItem
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Form.OrderForm
import com.sportpassword.bm.Form.RegisterForm
import com.sportpassword.bm.Form.ValueChangedDelegate
import com.sportpassword.bm.Models.SuperProduct
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_order_vc.*

class OrderVC : MyTableVC1(), ValueChangedDelegate {

    var superProduct: SuperProduct? = null
    var section_keys: ArrayList<ArrayList<String>> = arrayListOf()

    var sub_total: Int = 0
    var shippingFee: Int = 0
    var total: Int = 0

    var selected_number: Int = 1
    var selected_price: Int = 0
    var selected_idx: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_vc)

        if (intent.hasExtra("superProduct")) {
            superProduct = intent.getSerializableExtra("superProduct") as? SuperProduct
            setMyTitle(superProduct!!.name)
            hidekeyboard(order_layout)

            form = OrderForm(superProduct!!.type, this)
            sections = form.getSections()
            section_keys = form.getSectionKeys()

            initData()

            recyclerView = editTableView
            initAdapter(true)

            refreshLayout = refresh
            setRefreshListener()

        } else {
            warning("傳送商品資料錯誤，請洽管理員，或稍後再試")
        }
    }

    fun initData() {

        val productNameItem = getFormItemFromKey("Product_Name")
        if (productNameItem != null) {
            productNameItem.value = superProduct!!.name
            productNameItem.make()
        }

        val nameItem = getFormItemFromKey(NAME_KEY)
        if (nameItem != null) {
            nameItem.value = member.name
            nameItem.make()
        }

        val mobileItem = getFormItemFromKey(MOBILE_KEY)
        if (mobileItem != null) {
            mobileItem.value = member.mobile
            mobileItem.make()
        }

        val emailItem = getFormItemFromKey(EMAIL_KEY)
        if (emailItem != null) {
            emailItem.value = member.email
            emailItem.make()
        }

        val addressItem = getFormItemFromKey(ADDRESS_KEY)
        if (addressItem != null) {
            addressItem.value = member.road
            addressItem.make()
        }

        val colorItem = getFormItemFromKey(COLOR_KEY) as TagFormItem
        if (colorItem != null) {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for ((key, value) in superProduct!!.colors) {
                val dict: HashMap<String, String> = hashMapOf(key to value)
                res.add(dict)
            }
            colorItem.tags = res
        }

        val clothesSizeItem = getFormItemFromKey(CLOTHES_SIZE_KEY) as TagFormItem
        if (clothesSizeItem != null) {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (size in superProduct!!.sizes) {
                val dict: HashMap<String, String> = hashMapOf(size to size)
                res.add(dict)
            }
            clothesSizeItem.tags = res
        }

        val numberItem = getFormItemFromKey(NUMBER_KEY) as NumberFormItem
        if (numberItem != null) {
            numberItem.min = superProduct!!.order_min
            numberItem.max = superProduct!!.order_max
        }
    }

    override fun generateItems(section: Int): ArrayList<Item> {

        val rows: ArrayList<Item> = arrayListOf()

        val clearClick = { i: Int ->
        }

        val promptClick = {i: Int ->
        }

        val arr: ArrayList<FormItem> = arrayListOf()
        for (key in section_keys[section]) {
            for (formItem in form.formItems) {
                if (key == formItem.name) {
                    arr.add(formItem)
                    break
                }
            }
        }

//        println(arr)

//        var idx: Int = 0
//        for (i in 0..(section-1)) {
//            idx += section_keys[i].size
//        }

        for ((i,formItem) in arr.withIndex()) {

            val indexPath: IndexPath = IndexPath(section, i)
            var idx: Int = 0
            for ((j, _forItem) in form.formItems.withIndex()) {
                if (formItem.name == _forItem.name) {
                    idx = j
                    break
                }
            }

            var formItemAdapter: FormItemAdapter? = null
            if (formItem.uiProperties.cellType == FormItemCellType.textField) {
                formItemAdapter = TextFieldAdapter(form, idx, indexPath, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.plain) {
                formItemAdapter = PlainAdapter(form, idx, indexPath, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.tag) {
                formItemAdapter = TagAdapter(form, idx, indexPath, clearClick, promptClick)
            } else if (formItem.uiProperties.cellType == FormItemCellType.number) {

            }

            if (formItemAdapter != null) {
                formItemAdapter!!.valueChangedDelegate = this
                rows.add(formItemAdapter!!)
            }
//            idx++
        }

        return rows
    }

    fun updateSubTotal() {
        val priceItem = getFormItemFromKey(SUBTOTAL_KEY)
        if (priceItem != null) {
            sub_total = selected_price * selected_number
            priceItem.value = sub_total.toString()
            priceItem.make()
            updateShippingFee()
            //updateTotal()
        }
    }

    fun updateShippingFee() {
        shippingFee = superProduct!!.prices[selected_idx].shipping_fee
        val priceItem = getFormItemFromKey(SHIPPING_FEE_KEY)
        if (priceItem != null) {
            //shippingFee = price
            priceItem.value = shippingFee.toString()
            priceItem.make()
            updateTotal()
        }
    }

    fun updateTotal() {
        val priceItem = getFormItemFromKey(TOTAL_KEY)
        if (priceItem != null) {
            total = sub_total + shippingFee
            priceItem.value = total.toString()
            priceItem.make()
            //tableView.reloadData()
            notifyChanged(true)
        }
    }

    override fun tagChecked(checked: Boolean, name: String, key: String, value: String) {
        //println(value)
        if (name == "type") {
            val id: Int = key.toInt()
            var idx: Int = 0
            for (price in superProduct!!.prices) {
                if (price.id == id) {
                    selected_price = price.price_member
                    this.selected_idx = idx
                    updateSubTotal()
                    break
                }
                idx = idx + 1
            }
        }
    }

    override fun stepperValueChanged(number: Int, name: String) {
        selected_number = number
        updateSubTotal()
    }

    override fun textFieldTextChanged(indexPath: IndexPath, text: String) {}

    override fun sexChanged(sex: String) {}

    override fun privateChanged(checked: Boolean) {}

    fun submitBtnPressed(view: View) {
        //print("purchase")
        goOrder(superProduct!!)
    }

    fun cancelBtnPressed(view: View) {
        prev()
    }
}
package com.sportpassword.bm.Form

import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItem.*
import com.sportpassword.bm.Utilities.*

class OrderForm(type: String, delegate: BaseActivity): BaseForm(null, null, "", false, delegate) {

    var type: String = "clothes"

    init {
        this.type = type
    }

    override fun configureItems() {

        val section1 = SectionFormItem("商品名稱")
        val productItem = PlainFormItem("Product_Name", "商品")

        val section3 = SectionFormItem("款項")
        val subTotalItem = PlainFormItem(SUBTOTAL_KEY, "小計", "元", true)
        val shippingFeeItem = PlainFormItem(SHIPPING_FEE_KEY, "運費", "元", true)
        val totalItem = PlainFormItem(TOTAL_KEY, "總計", "元", true)

        val section4 = SectionFormItem("寄件資料")
        val nameItem = PlainFormItem(NAME_KEY, "姓名")
        val mobileItem = PlainFormItem(MOBILE_KEY, "行動電話")
        val emailItem = PlainFormItem(EMAIL_KEY, "EMail")
        val addressItem = PlainFormItem(ADDRESS_KEY, "住址")

        formItems = arrayListOf(
                section1,productItem,
                section3,subTotalItem,shippingFeeItem,totalItem,
                section4,nameItem,mobileItem,emailItem,addressItem
        )
    }

    fun setAttributesItem(): ArrayList<FormItem> {

        val formItem: ArrayList<FormItem> = arrayListOf()
        val section2 = SectionFormItem("商品選項")
        formItem.add(section2)

        if (this.type == "clothes") {
            //val colorItem = Color1FormItem(true)
            //formItem.add(colorItem)
        }

        return formItem
    }
}
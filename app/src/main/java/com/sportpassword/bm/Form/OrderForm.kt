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

        val section2 = SectionFormItem("個人資料")
        val nameItem = TextFieldFormItem(NAME_KEY, "姓名", "請輸入真實姓名", null, true)
        val nicknameItem = TextFieldFormItem(NICKNAME_KEY, "暱稱", "網路上使用", null, true)

        val section3 = SectionFormItem("聯絡資料")
        val mobileItem = TextFieldFormItem(MOBILE_KEY, "行動電話", "", null, true)
        val telItem = TextFieldFormItem(TEL_KEY, "市內電話", "", null, false)
        val roadItem = TextFieldFormItem(ROAD_KEY, "住址", "路街名、巷、弄、號", null, true)

        val section4 = SectionFormItem("社群資料")
        val fbItem = TextFieldFormItem(FB_KEY, "FB", "請輸入FB網址", null)
        val lineItem = TextFieldFormItem(LINE_KEY, "Line", "請輸入Line ID", null)

        formItems = arrayListOf(
                section1,productItem,
                section2,nameItem,nicknameItem,
                section3,mobileItem,telItem,roadItem,
                section4,fbItem,lineItem
        )
    }

    fun setAttributesItem(): ArrayList<FormItem> {

        var formItem: ArrayList<FormItem> = arrayListOf()
        val section2 = SectionFormItem("商品選項")
        formItem.add(section2)

        if (this.type == "clothes") {
            //val colorItem = Color1FormItem(true)
            //formItem.add(colorItem)
        }

        return formItem
    }
}
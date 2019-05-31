package com.sportpassword.bm.Form

import com.sportpassword.bm.Form.FormItem.FormItem


open class BaseForm {

    var formItems: ArrayList<FormItem> = arrayListOf()
    var title: String? = null
    var values: HashMap<String, String>? = null
    var id: Int? = null
    var isChange: Boolean = false
    var isSection: Boolean = false

    constructor(id: Int? = null, values: HashMap<String, String>? = null, title: String = "", isSection: Boolean=false) {
        this.id = id
        this.values = values
        this.title = title
        this.configureItems()
        this.fillValue()
        this.isSection = isSection
    }

    open fun isChanged(): Pair<Boolean, String?> {
        var msg: String? = null
        for (item in formItems) {
            if (!isChange) {
                isChange = item.updateCheckChange()
            }
        }
        if (!isChange) {
            msg = "沒有更改任何值，所以不用送出更新"
        }

        return Pair(isChange, msg)
    }

    open fun isValid(): Pair<Boolean, String?> {
        var isValid = true
        var msg: String? = null
        for (item in formItems) {
            item.checkValidity()
            if (!item.isValid) {
                isValid = false
                msg = item.msg
                break
            }
        }

        return Pair(isValid, msg)
    }

    open fun configureItems() {}
    open fun fillValue() {
        for (formItem in formItems) {
            if (formItem.name != null && values != null && values!![formItem.name!!] != null) {
                formItem.value = values!![formItem.name!!]
                formItem.oldValue = formItem.value
                formItem.valueToAnother()
                formItem.make()
            }
        }
    }

    open fun getSections(): ArrayList<String> {

        val sections = arrayListOf<String>()
        for (formItem in formItems) {
            if (formItem.uiProperties.cellType == FormItemCellType.section) {
                sections.add(formItem.title)
            }
        }

        return sections
    }

    open fun getSectionKeys(): ArrayList<ArrayList<String>> {

        val res: ArrayList<ArrayList<String>> = arrayListOf()

        var rows: ArrayList<String> = arrayListOf()
        var findSection: Boolean = false
        for ((idx, formItem) in formItems.withIndex()) {

            if (!findSection && formItem.uiProperties.cellType == FormItemCellType.section) {
                findSection = true
                rows = arrayListOf()
                continue
            }
            if ((findSection && formItem.uiProperties.cellType == FormItemCellType.section)) {
                res.add(rows)
                rows = arrayListOf()
                continue
            }
            if (findSection) {
                rows.add(formItem.name!!)
            }
        }
        res.add(rows)

        return res
    }
}
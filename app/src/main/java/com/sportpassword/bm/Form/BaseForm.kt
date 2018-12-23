package com.sportpassword.bm.Form

import com.sportpassword.bm.Form.FormItem.FormItem


open class BaseForm {

    var formItems: ArrayList<FormItem> = arrayListOf()
    var title: String? = null
    var values: HashMap<String, String>? = null
    var id: Int? = null
    var isChange: Boolean = false

    constructor(id: Int? = null, values: HashMap<String, String>? = null, title: String = "") {
        this.id = id
        this.values = values
        this.title = title
        this.configureItems()
        this.fillValue()
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
            if (!isChange) {
                isChange = item.updateCheckChange()
            }
        }
        if (isValid && !isChange) {
            isValid = false
            msg = "沒有更改任何值，所以不用送出更新"
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
}
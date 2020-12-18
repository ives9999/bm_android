package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItemUIProperties

open class FormItem {

    var isValid = false
    var msg: String? = null
    var isRequired = true
    var title: String = ""
    var name: String? = null
    var oldValue: String? = null
    var value: String? = null
    var placeholder = ""
    var tooltip: String? = null
    var position: HashMap<String, Int> = hashMapOf()
    //var valueCompletion: ((String?) -> Void)?
    var uiProperties = FormItemUIProperties()
    var show: String = ""
    var delegate: BaseActivity? = null

    //weekday is [Int]
    //time is ["type":SELECT_TIME_TYPE,"time":"09:00"]
    //color is [MYCOLOR]
    //status is STATUS
    //content is ["type":TEXT_INPUT_TYPE,"text":"課程說明"]
    var sender: Any? = null

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null, tooltip: String? = null, isRequired: Boolean = false, delegate: BaseActivity? = null) {
        this.title = title
        this.name = name
        if (placeholder != null) {
            this.placeholder = placeholder!!
        }
        if (tooltip != null) {
            this.tooltip = tooltip
        }
        if (delegate != null) {
            this.delegate = delegate
        }
        this.value = value
        this.isRequired = isRequired
    }

    open fun reset() {
        show = ""
        value = null
        sender = null
    }

    open fun make() {}
    open fun valueToAnother() {}

    open fun checkValidity() {
        if (isRequired) {
            if (value == null) {
                isValid = false
                msg = "${title} 沒有填寫或選擇欄位值"
                return
            }
            if (value != null && (value?.isEmpty()!!)) {
                isValid = false
                msg = "${title} 欄位值不能為空白"
                return
            }
            isValid = true
        } else {
            this.isValid = true
        }
    }

    open fun updateCheckChange(): Boolean {
        if ((value != null && oldValue != null) && (value == oldValue)) {
            isValid = false
            msg = "${title} 沒有更改欄位值"
            return false
        }
        return true
    }
}
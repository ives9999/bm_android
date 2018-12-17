package com.sportpassword.bm.Form.FormItem

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
    var position: HashMap<String, Int> = hashMapOf()
    //var valueCompletion: ((String?) -> Void)?
    var uiProperties = FormItemUIProperties()
    var show: String = ""

    //weekday is [Int]
    //time is ["type":SELECT_TIME_TYPE,"time":"09:00"]
    //color is [MYCOLOR]
    //status is STATUS
    //content is ["type":TEXT_INPUT_TYPE,"text":"課程說明"]
    var sender: Any? = null

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null) {
        this.title = title
        this.name = name
        if (placeholder != null) {
            this.placeholder = placeholder!!
        }
        this.value = value
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
//            if (value != null && oldValue != null) && (value == oldValue) {
//                isValid = false
//                msg = "\(title) 沒有更改欄位值"
//                return
//            }
            isValid = true
        } else {
            this.isValid = true
        }
    }
}
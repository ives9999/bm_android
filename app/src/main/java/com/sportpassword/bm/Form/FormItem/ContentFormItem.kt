package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.TEXT_INPUT_TYPE
import com.sportpassword.bm.Utilities.TT_CONTENT
import com.sportpassword.bm.Utilities.truncate

class ContentFormItem: FormItem {

    var contentType: TEXT_INPUT_TYPE? = null

    init {
        uiProperties.cellType = FormItemCellType.more
    }

    constructor(name: String, title: String, placeholder: String? = null, value: String? = null): super(name, title, placeholder, value) {
    }

    constructor(name: String, title: String, type: TEXT_INPUT_TYPE, isRequired: Boolean = false): super(name, title) {
        this.contentType = type
        this.isRequired = isRequired
        reset()
    }

    override fun reset() {
        super.reset()
        make()
    }

    override fun make() {
        if (value != null) {
            show = value!!
            if (show.length > 5) {
                show = show.truncate(5)
            }
            sender = value
//            sender = hashMapOf("type" to contentType!!, "text" to value!!)
        } else {
            sender = ""
        }
    }
}
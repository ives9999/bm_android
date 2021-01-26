package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.TAG_KEY

class TagFormItem(name: String = TAG_KEY, title: String = "標籤", isRequire: Boolean = false, selected_idxs: ArrayList<Int> = arrayListOf(0)): FormItem(name, title, null, null, null, isRequire) {

    var selected_idxs: ArrayList<Int> = arrayListOf(0)
    var tags: ArrayList<HashMap<String, String>> = arrayListOf()
        get() = field
        set(value) {
            field = value
            selected_idxs = arrayListOf(0)
            for ((key, value) in tags[0]) {
                this.value = value
            }
        }

    init {
        uiProperties.cellType = FormItemCellType.tag
        this.selected_idxs = selected_idxs
        reset()
    }
}
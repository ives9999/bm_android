package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.TAG_KEY

class TagFormItem(name: String = TAG_KEY, title: String = "標籤", isRequire: Boolean = false, selected_idxs: ArrayList<Int> = arrayListOf(0)): FormItem(name, title, null, null, null, isRequire) {

    var selected_idxs: ArrayList<Int> = arrayListOf(0)
    var tags: ArrayList<HashMap<String, String>> = arrayListOf()
        get() = field
        set(value) {
            field = value
            for (selected_idx in selected_idxs) {
                for ((idx, tag) in field.withIndex()) {
                    if (selected_idx == idx) {
                        for ((_, value) in tag) {
                            this.value = value
                        }
                    }
                }
            }
        }

    init {
        uiProperties.cellType = FormItemCellType.tag
        this.selected_idxs = selected_idxs
        reset()
    }
}
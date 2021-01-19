package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Utilities.TAG_KEY

class TagFormItem(name: String = TAG_KEY, title: String = "標籤", isRequire: Boolean = false, selected_idxs: ArrayList<Int> = arrayListOf(0)): FormItem(name, title, null, null, null, isRequire) {

    var tags: ArrayList<HashMap<String, String>> = arrayListOf()
    var selected_idxs: ArrayList<Int> = arrayListOf(0)

    init {
        this.selected_idxs = selected_idxs
        reset()
    }
}
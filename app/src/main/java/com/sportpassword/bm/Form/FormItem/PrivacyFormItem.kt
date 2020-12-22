package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.PRIVACY_KEY

class PrivacyFormItem(isRequired: Boolean = true, delegate: BaseActivity? = null): FormItem(PRIVACY_KEY, "隱私權", "", null, null, isRequired, delegate) {

    init {
        uiProperties.cellType = FormItemCellType.privacy
        reset()
        this.value = "1"
    }

    override fun reset() {
        super.reset()
        make()
    }

    override fun make() {
//        value = selected
//        sender = selected
    }
}
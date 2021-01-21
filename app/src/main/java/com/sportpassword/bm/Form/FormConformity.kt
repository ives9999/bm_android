package com.sportpassword.bm.Form

import com.sportpassword.bm.Utilities.IndexPath

interface ValueChangedDelegate {
    fun textFieldTextChanged(indexPath: IndexPath, text: String)
    fun sexChanged(sex: String)
    fun privateChanged(checked: Boolean)
    fun tagChecked(checked: Boolean, name: String, key: String, value: String)
    fun stepperValueChanged(number: Int, name: String)
}
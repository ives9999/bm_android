package com.sportpassword.bm.bm_new.ui.vo

data class SignUpInfo(
    val type: String,
    val titleStringRes: Int,
    var value: String,
    val isRequired: Boolean = false
)
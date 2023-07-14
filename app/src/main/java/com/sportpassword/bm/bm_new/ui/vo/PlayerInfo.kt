package com.sportpassword.bm.bm_new.ui.vo

data class PlayerInfo(
    val playerNum: Int = 0,
    val type: String,
    val titleStringRes: Int,
    var value: String,
    val isRequired: Boolean = false
)
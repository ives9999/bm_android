package com.sportpassword.bm.bm_new.ui.vo

data class PlayerInfo(
    val playerNum: Int = 0,
    val type: String,
    val titleStringRes: Int? = null,
    val giftTitle: String? = null,
    var value: String,
    val isRequired: Boolean = false
)
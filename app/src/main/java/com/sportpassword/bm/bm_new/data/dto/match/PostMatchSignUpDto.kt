package com.sportpassword.bm.bm_new.data.dto.match

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.bm_new.data.AppConfig
import com.sportpassword.bm.member

//發request用，match team get one 取得賽事報名
data class PostMatchSignUpDto(
    @SerializedName("strip_html")
    val stripHtml: Boolean = false,
    @SerializedName("device")
    val device: String = "app", // app
    @SerializedName("member_token")
    val memberToken: String? = member.token,
    @SerializedName("match_group_token")
    val matchGroupToken: String? = null,   //新增賽事報名資料，欄位名是match_group_token
    @SerializedName("token")
    val token: String? = null,   //要取得已存在的賽事報名資料時，欄位名是token
)
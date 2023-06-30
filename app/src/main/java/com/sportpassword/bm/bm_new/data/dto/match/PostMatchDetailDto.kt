package com.sportpassword.bm.bm_new.data.dto.match

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.bm_new.data.AppConfig
import com.sportpassword.bm.member

//發request用，match get one 取得賽事內容
data class PostMatchDetailDto(
    @SerializedName("strip_html")
    val stripHtml: Boolean = false,
    @SerializedName("device")
    val device: String = "app", // app
    @SerializedName("member_token")
    val memberToken: String? = member.token,
    @SerializedName("token")
    val token: String   //從MatchListDto取得的token
)
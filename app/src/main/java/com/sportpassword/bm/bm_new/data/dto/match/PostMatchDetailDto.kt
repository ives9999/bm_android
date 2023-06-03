package com.sportpassword.bm.bm_new.data.dto.match

import com.google.gson.annotations.SerializedName


data class PostMatchDetailDto(
    @SerializedName("strip_html")
    val stripHtml: Boolean = false,
    @SerializedName("device")
    val device: String = "app", // app
    @SerializedName("member_token")
    val memberToken: String = "bbeq9v41HVRBOgPNEA9pmAEH6abNZPs",
    @SerializedName("token")
    var token: String   //從MatchListDto取得的token
)
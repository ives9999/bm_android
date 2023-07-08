package com.sportpassword.bm.bm_new.data.dto.match

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.member

data class DelMatchTeamDto(
    @SerializedName("device")
    val device: String = "app",
    @SerializedName("member_token")
    val memberToken: String? = member.token,
    @SerializedName("strip_html")
    val stripHtml: String = "false",
    @SerializedName("token")
    val token: String
)
package com.sportpassword.bm.bm_new.data.dto.match

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.bm_new.data.AppConfig
import com.sportpassword.bm.member

//發request用，match team list 取得報名隊伍列表
data class PostMatchTeamListDto(
    @SerializedName("device")
    val device: String = "app", // app
    @SerializedName("member_token")
    val memberToken: String? = member.token, // bbeq9v41HVRBOgPNEA9pmAEH6abNZPs
    @SerializedName("channel")
    val channel: String = "bm", // 1
    @SerializedName("page")
    var page: String = "1", // 1
    @SerializedName("perPage")
    var perPage: String = "20" // 20
)
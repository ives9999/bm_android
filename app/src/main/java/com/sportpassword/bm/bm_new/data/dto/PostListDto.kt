package com.sportpassword.bm.bm_new.data.dto

import com.google.gson.annotations.SerializedName

//發request用，match list 取得賽事列表
data class PostListDto(
    @SerializedName("device")
    val device: String = "app", // app
    @SerializedName("member_token")
    val memberToken: String? = com.sportpassword.bm.member.token, // bbeq9v41HVRBOgPNEA9pmAEH6abNZPs
    @SerializedName("page")
    var page: String = "1", // 1
    @SerializedName("perPage")
    var perPage: String = "20" // 20
)
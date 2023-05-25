package com.sportpassword.bm.bm_new.data.dto

import com.google.gson.annotations.SerializedName


data class PostListDto(
    @SerializedName("channel")
    val channel: String = "bm", // bm
    @SerializedName("device")
    val device: String = "app", // app
    @SerializedName("member_token")
    val memberToken: String = "bbeq9v41HVRBOgPNEA9pmAEH6abNZPs", // bbeq9v41HVRBOgPNEA9pmAEH6abNZPs
    @SerializedName("page")
    var page: String = "1", // 1
    @SerializedName("perPage")
    var perPage: String = "20" // 20
)
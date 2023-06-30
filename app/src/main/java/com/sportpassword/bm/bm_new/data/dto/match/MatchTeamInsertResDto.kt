package com.sportpassword.bm.bm_new.data.dto.match
import com.google.gson.annotations.SerializedName


data class MatchTeamInsertResDto(
    @SerializedName("id")
    val id: Int, // 33
    @SerializedName("model")
    val model: Model,
    @SerializedName("success")
    val success: Boolean, // true
    @SerializedName("update")
    val update: String // INSERT
) {
    data class Model(
        @SerializedName("created_at")
        val createdAt: String, // 2023-06-30 15:38:28
        @SerializedName("id")
        val id: Int, // 33
        @SerializedName("manager_email")
        val managerEmail: String, // david@gmail.com
        @SerializedName("manager_id")
        val managerId: Int, // 1
        @SerializedName("manager_line")
        val managerLine: String, // davidline
        @SerializedName("manager_mobile")
        val managerMobile: String, // 0934254387
        @SerializedName("manager_name")
        val managerName: String, // 大明王
        @SerializedName("match_group_id")
        val matchGroupId: String, // 1
        @SerializedName("match_id")
        val matchId: String, // 6
        @SerializedName("name")
        val name: String, // 測試隊111
        @SerializedName("sort_order")
        val sortOrder: Int, // 1688110708
        @SerializedName("token")
        val token: String, // MTt5MQ9wwmTCSIreP8c5yMBW1N9DeRm
        @SerializedName("updated_at")
        val updatedAt: String // 2023-06-30 15:38:28
    )
}
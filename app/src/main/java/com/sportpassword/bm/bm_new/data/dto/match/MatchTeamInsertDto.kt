package com.sportpassword.bm.bm_new.data.dto.match

import com.google.gson.annotations.SerializedName


data class MatchTeamInsertDto(
    @SerializedName("do")
    val doX: String, // update
    @SerializedName("manager_email")
    val managerEmail: String, // david@gmail.com
    @SerializedName("manager_line")
    val managerLine: String?, // davidline
    @SerializedName("manager_mobile")
    val managerMobile: String, // 0934254387
    @SerializedName("manager_name")
    val managerName: String, // 王大明
    @SerializedName("manager_token")
    val managerToken: String, // bbeq9v41HVRBOgPNEA9pmAEH6abNZPs
    @SerializedName("match_group_id")
    val matchGroupId: String, // 1
    @SerializedName("match_id")
    val matchId: String, // 6
    @SerializedName("member_token")
    val memberToken: String, // bbeq9v41HVRBOgPNEA9pmAEH6abNZPs
    @SerializedName("name")
    val name: String, // 測試隊
    @SerializedName("players")
    val players: List<Player>,
    val token: String? = null //編輯更新時要加此token
) {
    data class Player(
        @SerializedName("age")
        val age: String?, // 35
        @SerializedName("email")
        val email: String, // david@gmail.com
        @SerializedName("gift")
        val gift: Gift?,
        @SerializedName("line")
        val line: String?, // davidline
        @SerializedName("mobile")
        val mobile: String, // 0923487384
        @SerializedName("name")
        val name: String // 人員1
    ) {
        data class Gift(
            @SerializedName("attributes")
            val attributes: String, // {name:顏色,alias:color,value:經典白}|{name:尺寸,alias:size,value:M}
            @SerializedName("match_gift_id")
            val matchGiftId: String // 1
        )
    }
}
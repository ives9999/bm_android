package com.sportpassword.bm.bm_new.data.dto.match

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class MatchListDto(
    @SerializedName("page")
    val page: Int, // 1
    @SerializedName("perPage")
    val perPage: Int, // 20
    @SerializedName("rows")
    val rows: List<Row>,
    @SerializedName("success")
    val success: Boolean, // true
    @SerializedName("totalCount")
    val totalCount: Int // 7
) {
    @Parcelize
    data class Row(
        @SerializedName("arena_id")
        val arenaId: Int, // 5
        @SerializedName("arena_name")
        val arenaName: String, // 華中羽球館
        @SerializedName("arena_city_id")
        val cityId: Int,
        @SerializedName("id")
        val id: Int, // 6
        @SerializedName("match_end")
        val matchEnd: String, // 2023-11-02 17:00:00
        @SerializedName("match_start")
        val matchStart: String, // 2023-11-01 08:00:00
        @SerializedName("name")
        val name: String, // 羽球密碼盃比賽123
        @SerializedName("signup_end")
        val signupEnd: String?, // 2023-04-23 17:30:33
        @SerializedName("signup_start")
        val signupStart: String?, // 2023-04-23 17:30:33
        @SerializedName("status")
        val status: String, // online
        @SerializedName("token")
        val token: String, // Hm0k2OvzOyZntkHvYikj1oEJR1BW5pD
        @SerializedName("priceMin")
        val priceMin: Int,
        @SerializedName("priceMax")
        val priceMax: Int
    ) : Parcelable
}
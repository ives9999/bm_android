package com.sportpassword.bm.v2.arena.show

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ShowDao(
    @SerializedName("status")
    val status: Int,

    @SerializedName("data")
    val data: Data
) {
    @Parcelize
    data class Data(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String,

        @SerializedName("zone")
        val zone: Zone,

        @SerializedName("token")
        val token: String,

        @SerializedName("pv")
        val pv: Int,

        @SerializedName("created_at")
        val created_at: String,
    ): Parcelable

    @Parcelize
    data class Zone(
        @SerializedName("city_id")
        val city_id: Int,

        @SerializedName("area_id")
        val area_id: Int,

        @SerializedName("city_name")
        val city_name: String,

        @SerializedName("area_name")
        val area_name: String
    ): Parcelable
}

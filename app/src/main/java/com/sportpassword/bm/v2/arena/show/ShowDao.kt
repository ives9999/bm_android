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

        @SerializedName("zip")
        val zip: String,

        @SerializedName("road")
        val road: String,

        @SerializedName("tel")
        val tel: String,

        @SerializedName("open_time")
        val open_time: String,

        @SerializedName("close_time")
        val close_time: String,

        @SerializedName("block")
        val block: Int,

        @SerializedName("bathroom")
        val bathroom: Int,

        @SerializedName("air_condition")
        val air_condition: Int,

        @SerializedName("parking")
        val parking: Int,

        @SerializedName("fb")
        val fb: String,

        @SerializedName("youtube")
        val youtube: String,

        @SerializedName("line")
        val line: String,

        @SerializedName("website")
        val website: String,

        @SerializedName("token")
        val token: String,

        @SerializedName("pv")
        val pv: Int,

        @SerializedName("created_at")
        val created_at: String,

        @SerializedName("images")
        val images: ArrayList<Image>,

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

    @Parcelize
    data class Image(
        @SerializedName("path")
        val path: String,
    ): Parcelable
}

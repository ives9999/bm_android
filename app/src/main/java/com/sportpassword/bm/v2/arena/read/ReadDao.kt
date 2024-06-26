package com.sportpassword.bm.v2.arena.read

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ReadDao(
    @SerializedName("status")
    val status: Int,

    @SerializedName("data")
    val data: Data
) {
    @Parcelize
    data class Data(
        @SerializedName("_meta")
        val meta: Meta,

        @SerializedName("rows")
        val rows: List<Arena>
    ): Parcelable

    @Parcelize
    data class Meta(
        @SerializedName("totalCount")
        val totalCount: Int,

        @SerializedName("totalPage")
        val totalPage: Int,

        @SerializedName("currentPage")
        val currentPage: Int,

        @SerializedName("offset")
        val offset: Int,

        @SerializedName("perPage")
        val perpage: Int,
    ): Parcelable

    @Parcelize
    data class Arena(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String,

        @SerializedName("images")
        val images: List<Image>,

        @SerializedName("zone")
        val zone: Zone,

        @SerializedName("member")
        val member: Member,

        @SerializedName("token")
        val token: String,

        @SerializedName("pv")
        val pv: Int,

        @SerializedName("created_at")
        val created_at: String,

        ): Parcelable

    @Parcelize
    data class Image(
        @SerializedName("path")
        val path: String,

        @SerializedName("upload_id")
        val upload_id: Int,

        @SerializedName("sort_order")
        val sort_order: Int,

        @SerializedName("isFeatured")
        val isFeatured: Boolean
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
    data class Member(
        @SerializedName("name")
        val name: String,

        @SerializedName("avatar")
        val avatar: String,

        @SerializedName("token")
        val token: String,
    ): Parcelable
}
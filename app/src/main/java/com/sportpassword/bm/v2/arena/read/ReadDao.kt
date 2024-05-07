package com.sportpassword.bm.v2.arena.read

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ReadDao(
    @SerialName("status")
    val status: Int,

    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("_meta")
        val meta: Meta,

        @SerialName("rows")
        val rows: List<Arena>
    )

    @Serializable
    data class Meta(
        @SerialName("totalCount")
        val totalCount: Int,

        @SerialName("totalPage")
        val totalPage: Int,

        @SerialName("currentPage")
        val currentPage: Int,

        @SerialName("offset")
        val offset: Int,

        @SerialName("perPage")
        val perpage: Int,
    )

    @Serializable
    data class Arena(
        @SerialName("id")
        val id: Int,

        @SerialName("name")
        val name: String,

        @SerialName("images")
        val images: List<Image>,

        @SerialName("zone")
        val zone: Zone,

        @SerialName("member")
        val member: Member,

        @SerialName("token")
        val token: String,

        @SerialName("pv")
        val pv: Int,

        @SerialName("created_at")
        val created_at: String,
    )

    fun Arena.toArena(currentPage: Int): PageArena {
        return PageArena(
            id = id,
            name = name,
            token = token,
            pv = pv,
            created_at = created_at,
            images = images,
            zone = zone,
            member = member,
            currentPage = currentPage
        )
    }

    @Serializable
    data class Image(
        @SerialName("path")
        val path: String,

        @SerialName("upload_id")
        val upload_id: Int,

        @SerialName("sort_order")
        val sort_order: Int,

        @SerialName("isFeatured")
        val isFeatured: Boolean
    )

    @Serializable
    data class Zone(
        @SerialName("city_id")
        val city_id: Int,

        @SerialName("area_id")
        val area_id: Int,

        @SerialName("city_name")
        val city_name: String,

        @SerialName("area_name")
        val area_name: String
    )

    @Serializable
    data class Member(
        @SerialName("name")
        val name: String,

        @SerialName("avatar")
        val avatar: String,

        @SerialName("token")
        val token: String,
    )
}

package com.sportpassword.bm.v2.arena.read

import com.sportpassword.bm.Utilities.PERPAGE

data class PageArena(
    val id: Int,
    val name: String,
    val token: String,
    val pv: Int,
    val created_at: String,

    val images: List<ReadDao.Image>,
    val zone: ReadDao.Zone,
    val member: ReadDao.Member,

    val currentPage: Int = 1,
)

//data class Image(
//    val path: String,
//    val upload_id: Int,
//    val sort_order: Int,
//    val isFeatured: Boolean
//)
//
//data class Zone(
//    val city_id: Int,
//    val area_id: Int,
//    val city_name: String,
//    val area_name: String
//)
//
//data class Member(
//    val name: String,
//    val avatar: String,
//    val token: String
//)

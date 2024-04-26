package com.sportpassword.bm.v2.base

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.v2.arena.read.ReadDao

open class BaseData(
    @SerializedName("status")
    val status: Int,

) {
}
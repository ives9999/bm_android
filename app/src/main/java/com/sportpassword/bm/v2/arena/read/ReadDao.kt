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
        @SerializedName("rows")
        val rows: List<Arena>
    ) : Parcelable

    @Parcelize
    data class Arena(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String
    ) : Parcelable
}
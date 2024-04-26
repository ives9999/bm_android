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

        @SerializedName("token")
        val token: String,

        @SerializedName("pv")
        val pv: Int,

        @SerializedName("created_at")
        val created_at: String,
    ): Parcelable
}

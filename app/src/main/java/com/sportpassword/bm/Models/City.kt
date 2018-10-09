package com.sportpassword.bm.Models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

/**
 * Created by ives on 2018/3/4.
 */
@Parcelize
data class City(val id: Int, val name: String): Parcelable
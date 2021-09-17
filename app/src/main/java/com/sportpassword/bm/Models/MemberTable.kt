package com.sportpassword.bm.Models

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.Utilities.*
import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.full.memberProperties

class MemberTable: Table() {

    var nickname: String = ""
    var dob: String = ""
    var sex: String = ""
    var email: String = ""

    var city: Int = -1
    
    @SerializedName("area_id") 
    var area: Int = -1

    var road: String = ""
    var zip: Int = 0
    var pid: String = ""
    var avatar: String = ""
    var player_id: String = ""
    var type: Int = 0
    var social: String = ""
    var fb: String = ""
    var line: String = ""
    var role: String = ""
    var validate: Int = 0
    
    var isLoggedIn: Boolean = false
    
    var area_show: String = ""

    override fun filterRow() {
        super.filterRow()

        city = city_id
        if (avatar.isNotEmpty()) {
            if (!avatar.startsWith("http://") && !avatar.startsWith("https://")) {
                avatar = BASE_URL + avatar
                //print(featured_path)
            }
        }

        if (city > 0) {
            city_show = Global.zoneIDToName(city)
        }

        if (area > 0) {
            area_show = Global.zoneIDToName(area)
        }

        if (area_show.length > 0 && city_show.length > 0) {
            address = "${city_show}${area_show}${zip}${road}"
        }
    }

    override fun printRow() {
        super.printRow()
    }

    fun toSession(context: Context) {

        val session: SharedPreferences = context.getSharedPreferences(SESSION_FILENAME, 0)
        MemberTable::class.memberProperties.forEach {
            val name = it.name
            //val type = it.returnType
            var value = it.getter.call(this)
            when (value) {
                is Int ->
                    session.edit().putInt(name, value).apply()
                is String ->
                    session.edit().putString(name, value).apply()
                is Boolean ->
                    session.edit().putBoolean(name, value).apply()
            }
        }
    }
}

















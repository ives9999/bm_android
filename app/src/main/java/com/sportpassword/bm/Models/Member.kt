
package com.sportpassword.bm.Models

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
//import kotlinx.coroutines.android.UI
import org.json.JSONObject
import java.lang.reflect.Field
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.valueParameters

/**
 * Created by ivessun on 2018/2/5.
 */
class Member(data: JSONObject): SuperModel(data){

    //val session: SharedPreferences = context.getSharedPreferences(SESSION_FILENAME, 0)

    var id: Int = -1
    var nickname: String = ""
    var uid: String = ""
    var name: String = ""
    var channel: String = "bm"
    var dob: String = ""
    var sex: String = "M"
    var tel: String = ""
    var mobile: String = ""
    var email: String = ""
    var city_id: Int = 0
    var area_id: Int = 0
    var road: String = ""
    var zip: Int = 0
    var fb: String = ""
    var line: String = ""
    var pid: String = ""
    var avatar: String = ""
    var player_id: String = ""
    var type: Int = 0
    var social: String = ""
    var role: String = "member"
    var validate: Int = 0
    var token: String = ""
    var isLoggedIn: Boolean = false
    var isTeamManager: Boolean = false
    var justGetMemberOne: Boolean = false

    fun  setMemberData(json: SharedPreferences) {
                
        id = json.getInt(ID_KEY, -1)
        if (json.has(VALIDATE_KEY)) {
            try {
                validate = json.getInt(VALIDATE_KEY, 0)
            } catch (e: Exception) {
                validate = 0
            }
        } else {
            validate = 0
        }
        if (json.has(MEMBER_TYPE_KEY)) {
            try {
                type = json.getInt(MEMBER_TYPE_KEY, 0)
            } catch (e: Exception) {
                type = 0
            }
        } else {
            type = 0
        }
        if (json.has(NICKNAME_KEY)) {
            nickname = json.getString(NICKNAME_KEY, "")!!
        } else {
            nickname = ""
        }
        if (json.has(EMAIL_KEY)) {
            email = json.getString(EMAIL_KEY, "")!!
        } else {
            email = ""
        }
        if (json.has(TOKEN_KEY)) {
            token = json.getString(TOKEN_KEY, "")!!
        } else {
            token = ""
        }
        if (json.has(UID_KEY)) {
            uid = json.getString(UID_KEY, "")!!
        } else {
            uid = ""
        }
        if (json.has(PLAYERID_KEY)) {
            player_id = json.getString(PLAYERID_KEY, "")!!
        } else {
            player_id = ""
        }
        if (json.has(NAME_KEY)) {
            name = json.getString(NAME_KEY, "")!!
        } else {
            name = ""
        }
        if (json.has(CHANNEL_KEY)) {
            channel = json.getString(CHANNEL_KEY, "bm")!!
        } else {
            channel = CHANNEL
        }
        if (json.has(TEL_KEY)) {
            tel = json.getString(TEL_KEY, "")!!
        } else {
            tel = ""
        }
        if (json.has(MOBILE_KEY)) {
            mobile = json.getString(MOBILE_KEY, "")!!
        } else {
            mobile = ""
        }
        if (json.has(CITY_ID_KEY)) {
            try {
                city_id = json.getInt(CITY_ID_KEY, 0)
            } catch (e: Exception) {
                city_id = 0
            }
        } else {
            city_id = 0
        }
        if (json.has(AREA_ID_KEY)) {
            try {
                area_id = json.getInt(AREA_ID_KEY, 0)
            } catch (e: Exception) {
                area_id = 0
            }
        } else {
            area_id = 0
        }
        if (json.has(ROAD_KEY)) {
            road = json.getString(ROAD_KEY, "")!!
        } else {
            road = ""
        }
        if (json.has(ZIP_KEY)) {
            try {
                zip = json.getInt(ZIP_KEY, 0)
            } catch (e: Exception) {
                zip = 0
            }
        } else {
            zip = 0
        }
        if (json.has(FB_KEY)) {
            fb = json.getString(FB_KEY, "")!!
        } else {
            road = ""
        }
        if (json.has(LINE_KEY)) {
            line = json.getString(LINE_KEY, "")!!
        } else {
            line = ""
        }
        if (json.has(PID_KEY)) {
            pid = json.getString(PID_KEY, "")!!
        } else {
            pid = ""
        }
        if (json.has(AVATAR_KEY)) {
            avatar = json.getString(AVATAR_KEY, "")!!
        } else {
            avatar = ""
        }
        if (json.has(DOB_KEY)) {
            dob = json.getString(DOB_KEY, "")!!
        } else {
            dob = ""
        }
        if (json.has(SEX_KEY)) {
            sex = json.getString(SEX_KEY, "M")!!
        } else {
            sex = "M"
        }
        if (json.has(SOCIAL_KEY)) {
            social = json.getString(SOCIAL_KEY, "")!!
        } else {
            social = ""
        }
        var roleString = "member"
        if (json.has(MEMBER_ROLE_KEY)) {
            roleString = json.getString(MEMBER_ROLE_KEY, "member")!!
        }
        role = roleString
        //role = MEMBER_ROLE.valueOf(roleString)

        if (json.has(ISLOGGEDIN_KEY)) {
            try {
                isLoggedIn = json.getBoolean(ISLOGGEDIN_KEY, false)
            } catch (e: Exception) {
                isLoggedIn = false
            }
        } else {
            isLoggedIn = false
        }
        val value: Int = type and TEAM_TYPE
        if (value > 0) isTeamManager = true else isTeamManager= false
    }

    fun fetch(key: String): String {
        var res = ""
        this::class.memberProperties.forEach{
            val name = it.name
            if (name == key) {
                res = it.getter.call(this).toString()
            }
        }
        return res
    }

    fun memberPrint() {
        this::class.memberProperties.forEach {
            val name = it.name
            val type = it.returnType
            val value = it.getter.call(this)
            println("${name}: ${value}")
        }
//        for (a in this::class.memberProperties) {
//            val name: String = a.name
//            val value: Any = setField(name)
//            //println("${a.name} = ${value}")
//        }
    }
    fun reset() {
        var json: JSONObject = JSONObject()
        for ((k1, v1) in MEMBER_ARRAY) {
            //println("${k1} default is ${v1["default"]}")
            val type: String = v1["type"].toString()
            val tmp = v1["default"].toString()

            if (type == "Int") {
               json.put(k1, tmp.toInt())
            } else if (type == "Boolean") {
                json.put(k1, tmp.toBoolean())
            } else {
                json.put(k1, tmp)
            }
        }
        //setMemberData(json)
        justGetMemberOne = false
    }

    fun validateShow(rawValue: Int): ArrayList<String> {
        var res = arrayListOf<String>()
        if (rawValue and 1 > 0) {
            res.add("email認證")
        }
        if (rawValue and 2 > 0) {
            res.add("手機認證")
        }
        if (rawValue and 4 > 0) {
            res.add("身分證認證")
        }
        if (res.size == 0) {
            res.add("未通過任何認證")
        }
        return res
    }

    fun typeShow(rawValue: Int) : String {
        var res: ArrayList<String> = arrayListOf()
        if (rawValue and GENERAL_TYPE >= 0) {
            res.add("一般會員")
        }
        if (rawValue and TEAM_TYPE > 0) {
            res.add("球隊隊長")
        }
        if (rawValue and ARENA_TYPE > 0) {
            res.add("球場管理員")
        }
        return res.joinToString(",")
    }
//    private fun setField(fieldName: String): Any {
//        val a: Field = javaClass.getDeclaredField(fieldName)
//        println(a.type)
//
//        return "test"
//    }

//    public fun getFieldNames(): String {
//        return javaClass.declaredFields.map { it.name }.joinToString(", ")
//    }

//    private fun getMemberRoleRawValue(value: MEMBER_ROLE): String {
//        if (value == MEMBER_ROLE.member) {
//            return "member"
//        } else if (value == MEMBER_ROLE.sale) {
//            return "sale"
//        } else if (value == MEMBER_ROLE.designer) {
//            return "designer"
//        } else if (value == MEMBER_ROLE.manager) {
//            return "manager"
//        } else if (value == MEMBER_ROLE.admin) {
//            return "admin"
//        } else {
//            return "member"
//        }
//    }
}

enum class MEMBER_ROLE(val value: String) {
    member("member"), sale("sale"), designer("designer"), manager("manager"), admin("admin");

//    companion object {
//        fun from(findValue: String): MEMBER_ROLE = MEMBER_ROLE.values().first { it.value == findValue }
//    }
}

enum class MEMBER_SEX(val value: String) {
    M("先生"), F("小姐");

    companion object {
        fun from(findValue: String): MEMBER_SEX = MEMBER_SEX.values().first { it.value == findValue }
    }
}

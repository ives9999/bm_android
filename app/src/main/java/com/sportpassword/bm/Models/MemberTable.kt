package com.sportpassword.bm.Models

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.Utilities.*
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sportpassword.bm.member
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.valueParameters

class MemberTable: Table() {

    var nickname: String = ""
    var coin: Int = 0
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
        if (avatar != null && avatar.length > 0) {
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

    fun toSession(context: Context, isLoggedIn: Boolean = false) {

        filterRow()
        this.isLoggedIn = isLoggedIn
        val session: SharedPreferences = context.getSharedPreferences(SESSION_FILENAME, 0)
        this::class.memberProperties.forEach {
            val name: String = it.name
            val value = it.getter.call(this)
            when (value) {
                is Int ->
                    session.edit().putInt(name, value).apply()
                is String ->
                    session.edit().putString(name, value).apply()
                is Boolean ->
                    session.edit().putBoolean(name, value).apply()
            }            
        }
        // val keys = session.all.map { it.key }
        // for (key in keys) {
            
        // }
    }

    fun validateShow(rawValue: Int): ArrayList<String> {
        val res = arrayListOf<String>()
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
}

/**
 * Created by ivessun on 2018/2/5.
 */
class Member(val context: Context) {

    val session: SharedPreferences = context.getSharedPreferences(SESSION_FILENAME, 0)

    var id: Int
        get() = session.getInt(ID_KEY, 0)
        set(value) {
            session.edit().putInt(ID_KEY, value).apply()
        }
    var nickname: String?
        get() = session.getString(NICKNAME_KEY, "")
        set(value) {
            session.edit().putString(NICKNAME_KEY, value).apply()
        }
    var coin: Int
        get() = session.getInt(COIN_KEY, 0)
        set(value) {
            session.edit().putInt(COIN_KEY, value).apply()
        }
    var uid: String?
        get() = session.getString(UID_KEY, "")
        set(value) {
            session.edit().putString(UID_KEY, value).apply()
        }
    var name: String?
        get() = session.getString(NAME_KEY, "")
        set(value) {
            session.edit().putString(NAME_KEY, value).apply()
        }
    var channel: String?
        get() = session.getString(CHANNEL_KEY, "bm")
        set(value) {
            session.edit().putString(CHANNEL_KEY, value).apply()
        }
    var dob: String?
        get() = session.getString(DOB_KEY, "")
        set(value) {
            session.edit().putString(DOB_KEY, value).apply()
        }
    var sex: String?
        get() = session.getString(SEX_KEY, "")
        set(value) {
            session.edit().putString(SEX_KEY, value).apply()
        }
    var tel: String?
        get() = session.getString(TEL_KEY, "")
        set(value) {
            session.edit().putString(TEL_KEY, value).apply()
        }
    var mobile: String?
        get() = session.getString(MOBILE_KEY, "")
        set(value) {
            session.edit().putString(MOBILE_KEY, value).apply()
        }
    var email: String?
        get() = session.getString(EMAIL_KEY, "")
        set(value) {
            session.edit().putString(EMAIL_KEY, value).apply()
        }
    var city: Int
        get() = session.getInt(CITY_KEY, 0)
        set(value) {
            session.edit().putInt(CITY_KEY, value).apply()
        }
    var area: Int
        get() = session.getInt(AREA_KEY, 0)
        set(value) {
            session.edit().putInt(AREA_KEY, value).apply()
        }
    var road: String?
        get() = session.getString(ROAD_KEY, "")
        set(value) {
            session.edit().putString(ROAD_KEY, value).apply()
        }
    var zip: Int
        get() = session.getInt(ZIP_KEY, 0)
        set(value) {
            session.edit().putInt(ZIP_KEY, value).apply()
        }
    var address: String?
        get() = session.getString(ADDRESS_KEY, "")
        set(value) {
            session.edit().putString(ADDRESS_KEY, value).apply()
        }
    var fb: String?
        get() = session.getString(FB_KEY, "")
        set(value) {
            session.edit().putString(FB_KEY, value).apply()
        }
    var line: String?
        get() = session.getString(LINE_KEY, "")
        set(value) {
            session.edit().putString(LINE_KEY, value).apply()
        }
    var pid: String?
        get() = session.getString(PID_KEY, "")
        set(value) {
            session.edit().putString(PID_KEY, value).apply()
        }
    var avatar: String?
        get() = session.getString(AVATAR_KEY, "")
        set(value) {
            session.edit().putString(AVATAR_KEY, value).apply()
        }
    var player_id: String?
        get() = session.getString(PLAYER_ID_KEY, "")
        set(value) {
            session.edit().putString(PLAYER_ID_KEY, value).apply()
        }
    var type: Int
        get() = session.getInt(TYPE_KEY, 0)
        set(value) {
            session.edit().putInt(TYPE_KEY, value).apply()
        }
    var social: String?
        get() = session.getString(SOCIAL_KEY, "")
        set(value) {
            session.edit().putString(SOCIAL_KEY, value).apply()
        }
    var role: String?
        get() = session.getString(ROLE_KEY, "member")
        set(value) {
            session.edit().putString(ROLE_KEY, value).apply()
        }
    var validate: Int
        get() = session.getInt(VALIDATE_KEY, 0)
        set(value) {
            session.edit().putInt(VALEDATE_KEY, value).apply()
        }
    var token: String?
        get() = session.getString(TOKEN_KEY, "")
        set(value) {
            session.edit().putString(TOKEN_KEY, value).apply()
        }
    var isLoggedIn: Boolean
        get() = session.getBoolean(ISLOGGEDIN_KEY, false)
        set(value) {
            session.edit().putBoolean(ISLOGGEDIN_KEY, value).apply()
        }
    // var isTeamManager: Boolean = false
    // var justGetMemberOne: Boolean = false

    // fun  setMemberData(json: SharedPreferences) {
                
    //     id = json.getInt(ID_KEY, -1)
    //     validate = json.getInt(VALIDATE_KEY, 0)
    //     type = json.getInt(MEMBER_TYPE_KEY, 0)
    //     nickname = json.getString(NICKNAME_KEY, "")!!
    //     email = json.getString(EMAIL_KEY, "")!!
    //     token = json.getString(TOKEN_KEY, "")!!
    //     uid = json.getString(UID_KEY, "")!!
    //     player_id = json.getString(PLAYERID_KEY, "")!!
    //     name = json.getString(NAME_KEY, "")!!
    //     channel = json.getString(CHANNEL_KEY, "bm")!!
    //     tel = json.getString(TEL_KEY, "")!!
    //     mobile = json.getString(MOBILE_KEY, "")!!
    //     city = json.getInt("city", 0)
    //     area = json.getInt("area", 0)
    //     road = json.getString(ROAD_KEY, "")!!
    //     zip = json.getInt(ZIP_KEY, 0)
    //     fb = json.getString(FB_KEY, "")!!
    //     line = json.getString(LINE_KEY, "")!!
    //     pid = json.getString(PID_KEY, "")!!
    //     avatar = json.getString(AVATAR_KEY, "")!!
    //     dob = json.getString(DOB_KEY, "")!!
    //     sex = json.getString(SEX_KEY, "M")!!
    //     social = json.getString(SOCIAL_KEY, "")!!
    //     val roleString = json.getString(MEMBER_ROLE_KEY, "member")!!
    //     role = roleString
    //     //role = MEMBER_ROLE.valueOf(roleString)

    //     val city_name = Global.zoneIDToName(member.city)
    //     val area_name = Global.zoneIDToName(member.area)
    //     address = "${city_name}${area_name}${zip}${road}"
        
    //     isLoggedIn = json.getBoolean(ISLOGGEDIN_KEY, false)
    //     val value: Int = type and TEAM_TYPE
    //     if (value > 0) isTeamManager = true else isTeamManager= false
    // }

    // fun fetch(key: String): String {
    //     var res = ""
    //     this::class.memberProperties.forEach {
    //         val name = it.name
    //         if (name == key) {
    //             res = it.getter.call(this).toString()
    //         }
    //     }
    //     return res
    // }

    // fun memberPrint() {
    //     this::class.memberProperties.forEach {
    //         val name = it.name
    //         val type = it.returnType
    //         val value = it.getter.call(this)
    //         println("${name}: ${value}")
    //     }
    // }
    fun reset() {

        MemberTable::class.memberProperties.forEach {

            val name = it.name
            val t = it.returnType
            if (t == String::class.createType()) {
                session.edit().putString(name, "").apply()
            } else if (t == Int::class.createType()) {
                session.edit().putInt(name, 0).apply()
            } else if (t == Boolean::class.createType()) {
                session.edit().putBoolean(name, false).apply()
            }
            session.edit().putString("dob", "").apply()
            session.edit().putInt("cartItemCount", 0).apply()
            // if (it.returnType == String::class.createType()) {
            //     session.edit().putString(name, "")
            // }
        }

        // var json: JSONObject = JSONObject()
        // for ((k1, v1) in MEMBER_ARRAY) {
        //     //println("${k1} default is ${v1["default"]}")
        //     val type: String = v1["type"].toString()
        //     val tmp = v1["default"].toString()

        //     if (type == "Int") {
        //        json.put(k1, tmp.toInt())
        //     } else if (type == "Boolean") {
        //         json.put(k1, tmp.toBoolean())
        //     } else {
        //         json.put(k1, tmp)
        //     }
        //}
        //不應該在這邊做設定 isLogging的設定，一個函式只做一件事
        // isLoggedIn = false
        //setMemberData(json)
        //justGetMemberOne = false
    }

    fun checkMust(): String {

        var msg: String = ""
        for (key in MEMBER_MUST_ARRAY) {

            val items = MemberTable::class.memberProperties.iterator()
            var isFilled: Boolean = false
            for (it in items) {
                val name = it.name
                if (key == name) {
                    val t = it.returnType
                    if (t == String::class.createType()) {
                        val value = session.getString(name, "")!!
                        if (value.isNotEmpty()) {
                            isFilled = true
                        }
                    } else if (t == Int::class.createType()) {
                        val value = session.getInt(name, 0)
                        if (value > 0) {
                            isFilled = true
                        }
                    }
                    break
                }
            }
            if (!isFilled) {
                msg += MEMBER_MUST_ARRAY_WARNING[key]!! + "\n"
            }
        }
        return msg
    }

    fun checkEMailValidate(): Boolean {
        
        var isValidate: Boolean = false
        
        if (member.validate and 1 > 0) {
            isValidate = true
        }
        
        return isValidate
    }

    fun checkMobileValidate(): Boolean {
        
        var isValidate: Boolean = false
        
        if (member.validate and 2 > 0) {
            isValidate = true
        }
        
        return isValidate
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

class MemberCoinTable: Table() {

    var member_id: Int = 0
    var in_out: Boolean = false
    var in_type: String = ""
    var out_type: String = ""
    var coin: Int = 0
    var able_type: String = ""
    var able_id: Int = 0
    var able_token: String = ""
    var price: Int = 0
    var balance: Int = 0
    var order_id: Int = 0
    var order_token: String = ""
    var coin_show: String = "0"
    var able_type_show: String = ""
    var type_in_enum: MEMBER_COIN_IN_TYPE = MEMBER_COIN_IN_TYPE.none
    var type_out_enum: MEMBER_COIN_OUT_TYPE = MEMBER_COIN_OUT_TYPE.none

    override fun filterRow() {
        super.filterRow()

        if (coin > 0) {
            coin_show = "NT$ " + coin.formattedWithSeparator()
        }

        if (able_type != null && able_type.length > 0 && able_id > 0) {
            able_type_show = "使用點數內容"
        } else {
            able_type_show = "購買訂單檢視"
        }

        if (in_out) {
            type_in_enum = MEMBER_COIN_IN_TYPE.enumFromString(in_type)
        } else {
            type_out_enum = MEMBER_COIN_OUT_TYPE.enumFromString(out_type)
        }
    }
}

class MemberBankTable: Table() {

    var bank: String = ""
    var branch: String = ""
    var bank_code: Int = 0
    var account: String = ""

    override fun filterRow() {
        super.filterRow()
    }
}

enum class MEMBER_ROLE(val value: String) {
    member("member"), sale("sale"), designer("designer"), manager("manager"), admin("admin");
}

enum class MEMBER_SEX(val value: String) {
    M("先生"), F("小姐");

    companion object {
        fun from(findValue: String): MEMBER_SEX = MEMBER_SEX.values().first { it.value == findValue }
    }
}
















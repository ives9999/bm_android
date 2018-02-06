package com.sportpassword.bm.Utilities

/**
 * Created by ivessun on 2018/2/2.
 */

const val CHANNEL = "bm"
var gSimulate: Boolean = false

// URL Constants
const val REMOTE_BASE_URL = "http://bm.sportpassword.com"
const val LOCALHOST_BASE_URL = "http://bm.sportpassword.localhost"
var BASE_URL = ""
var URL_HOME = ""
var URL_LIST = ""
var URL_SHOW = ""
var URL_LOGIN = ""
var URL_FB_LOGIN = ""
var URL_REGISTER = ""
var URL_MEMBER_UPDATE = ""
var URL_CITYS = ""
var URL_ARENA_BY_CITY_ID = ""
var URL_TEAM_UPDATE = ""
var URL_ONE = ""
var URL_TEAM = ""
var URL_TEAM_TEMP_PLAY = ""
var URL_TEAM_TEMP_PLAY_LIST = ""
var URL_TEAM_PLUSONE = ""

// User Defaults
val ID_KEY: String = "id"
val TOKEN_KEY: String = "token"
val EMAIL_KEY: String = "email"
val NICKNAME_KEY: String = "nickname"
val ISLOGGEDIN_KEY: String = "isLoggedIn"
val UID_KEY: String = "uid"
val NAME_KEY: String = "name"
val CHANNEL_KEY: String = "channel"
val DOB_KEY: String = "dob"
val SEX_KEY: String = "sex"
val TEL_KEY: String = "tel"
val MOBILE_KEY: String = "mobile"
val PID_KEY: String = "pid"
val AVATAR_KEY: String = "avatar"
val MEMBER_TYPE_KEY: String = "type"
val SOCIAL_KEY: String = "social"
val MEMBER_ROLE_KEY: String = "role"
val VALIDATE_KEY: String = "validate"

// member
val MEMBERS: HashMap<String, HashMap<String, String>> = hashMapOf(
        ID_KEY to hashMapOf("type" to "Int"),
        TOKEN_KEY to hashMapOf("type" to "String"),
        EMAIL_KEY to hashMapOf("type" to "String"),
        NICKNAME_KEY to hashMapOf("type" to "String"),
        ISLOGGEDIN_KEY to hashMapOf("type" to "Boolean"),
        UID_KEY to hashMapOf("type" to "String"),
        NAME_KEY to hashMapOf("type" to "String"),
        CHANNEL_KEY to hashMapOf("type" to "String"),
        DOB_KEY to hashMapOf("type" to "String"),
        SEX_KEY to hashMapOf("type" to "String"),
        TEL_KEY to hashMapOf("type" to "String"),
        MOBILE_KEY to hashMapOf("type" to "String"),
        PID_KEY to hashMapOf("type" to "String"),
        AVATAR_KEY to hashMapOf("type" to "String"),
        MEMBER_TYPE_KEY to hashMapOf("type" to "String"),
        SOCIAL_KEY to hashMapOf("type" to "String"),
        MEMBER_ROLE_KEY to hashMapOf("type" to "String"),
        VALIDATE_KEY to hashMapOf("type" to "String")
)

//val MEMBER_FIELD_STRING: Array<String> = arrayOf(TOKEN_KEY,EMAIL_KEY,NICKNAME_KEY,NAME_KEY,UID_KEY,CHANNEL_KEY,DOB_KEY,SEX_KEY,TEL_KEY,MOBILE_KEY,PID_KEY,AVATAR_KEY,MEMBER_ROLE_KEY,SOCIAL_KEY)
//val MEMBER_FIELD_INT: Array<String> = arrayOf(ID_KEY,VALIDATE_KEY,MEMBER_TYPE_KEY)
//val MEMBER_FIELD_BOOL: Array<String> = arrayOf(ISLOGGEDIN_KEY)


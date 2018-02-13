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
var URL_FORGETPASSWORD = ""
var URL_MEMBER_UPDATE = ""
var URL_CITYS = ""
var URL_ARENA_BY_CITY_ID = ""
var URL_TEAM_UPDATE = ""
var URL_ONE = ""
var URL_TEAM = ""
var URL_TEAM_TEMP_PLAY = ""
var URL_TEAM_TEMP_PLAY_LIST = ""
var URL_TEAM_PLUSONE = ""

// spinner
val LOADING: String = "努力加載中..."

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
val MEMBERS: Map<String, Map<String, String>> = mapOf(
        ID_KEY to hashMapOf("type" to "Int", "default" to "0"),
        TOKEN_KEY to hashMapOf("type" to "String", "default" to ""),
        EMAIL_KEY to hashMapOf("type" to "String", "default" to ""),
        NICKNAME_KEY to hashMapOf("type" to "String", "default" to ""),
        ISLOGGEDIN_KEY to hashMapOf("type" to "Boolean", "default" to "false"),
        UID_KEY to hashMapOf("type" to "String", "default" to ""),
        NAME_KEY to hashMapOf("type" to "String", "default" to ""),
        CHANNEL_KEY to hashMapOf("type" to "String", "default" to "bm"),
        DOB_KEY to hashMapOf("type" to "String", "default" to ""),
        SEX_KEY to hashMapOf("type" to "String", "default" to "M"),
        TEL_KEY to hashMapOf("type" to "String", "default" to ""),
        MOBILE_KEY to hashMapOf("type" to "String", "default" to ""),
        PID_KEY to hashMapOf("type" to "String", "default" to ""),
        AVATAR_KEY to hashMapOf("type" to "String", "default" to ""),
        MEMBER_TYPE_KEY to hashMapOf("type" to "Int", "default" to "0"),
        SOCIAL_KEY to hashMapOf("type" to "String", "default" to ""),
        MEMBER_ROLE_KEY to mapOf("type" to "String", "default" to "member"),
        VALIDATE_KEY to mapOf("type" to "Int", "default" to "0")
)

// Notification Constants
val NOTIF_MEMBER_DID_CHANGE = "notifMemberChanged"
val NOTIF_MEMBER_UPDATE = "notifMemberUpdate"

// Header
var HEADER = "application/json; charset=utf-8"


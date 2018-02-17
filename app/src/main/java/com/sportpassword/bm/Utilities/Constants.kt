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
var URL_CHANGE_PASSWORD = ""
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

// Team key
var TEAM_ID_KEY: String = "id"
var TEAM_CHANNEL_KEY: String = "channel"
var TEAM_NAME_KEY: String = "name"
var TEAM_LEADER_KEY: String = "leader"
var TEAM_MOBILE_KEY: String = "mobile"
var TEAM_EMAIL_KEY: String = "email"
var TEAM_WEBSITE_KEY: String = "website"
var TEAM_FB_KEY: String = "fb"
var TEAM_YOUTUBE_KEY: String = "youtube"
var TEAM_PLAY_START_KEY: String = "play_start"
var TEAM_PLAY_END_KEY: String = "play_end"
var TEAM_BALL_KEY: String = "ball"
var TEAM_DEGREE_KEY: String = "degree"
var TEAM_SLUG_KEY: String = "slug"
var TEAM_CHARGE_KEY: String = "charge"
var TEAM_CONTENT_KEY: String = "content"
var TEAM_MANAGER_ID_KEY: String = "manager_id"
var TEAM_TEMP_FEE_M_KEY: String = "temp_fee_M"
var TEAM_TEMP_FEE_F_KEY: String = "temp_fee_F"
var TEAM_TEMP_QUANTITY_KEY: String = "temp_quantity"
var TEAM_TEMP_SIGNUP_KEY: String = "temp_signup_count"
var TEAM_TEMP_CONTENT_KEY: String = "temp_content"
var TEAM_TEMP_STATUS_KEY: String = "temp_status"
var TEAM_PV_KEY: String = "pv"
var TEAM_TOKEN_KEY: String = "token"
var TEAM_CREATED_ID_KEY: String = "created_id"
var TEAM_CREATED_AT_KEY: String = "created_at"
var TEAM_UPDATED_AT_KEY: String = "updated_at"
var TEAM_THUMB_KEY: String = "thumb"
var TEAM_CITY_KEY: String = "city"
var TEAM_ARENA_KEY: String = "arena"
var TEAM_DAYS_KEY: String = "days"
var TEAM_FEATURED_KEY: String = "featured_path"
var TEAM_CAT_KEY: String = "cat_id"
var TEAM_NEAR_DATE_KEY: String = "near_date"

// Notification Constants
val NOTIF_MEMBER_DID_CHANGE = "notifMemberChanged"
val NOTIF_MEMBER_UPDATE = "notifMemberUpdate"

// Header
var HEADER = "application/json; charset=utf-8"


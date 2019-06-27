package com.sportpassword.bm.Utilities

import android.content.Intent
import android.text.InputType

/**
 * Created by ivessun on 2018/2/2.
 */

typealias CompletionHandler = (Success: Boolean) -> Unit

const val SOURCE = "app"
const val CHANNEL = "bm"
var gSimulate: Boolean = false
//var gReset: Boolean = false

const val more = "more"
const val none = "none"
const val defaultPad = InputType.TYPE_CLASS_TEXT
const val numberPad = InputType.TYPE_CLASS_NUMBER
const val phonePad = InputType.TYPE_CLASS_PHONE
const val emailPad = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS


// URL Constants
const val REMOTE_BASE_URL = "https://bm.sportpassword.com"
//const val LOCALHOST_BASE_URL = "http://bm.sportpassword.com"
//const val LOCALHOST_BASE_URL = "http://192.168.1.119"
//const val LOCALHOST_BASE_URL = "http://192.168.100.100"
const val LOCALHOST_BASE_URL = "http://192.168.2.200"
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
var URL_EMAIL_VALIDATE = ""
var URL_MOBILE_VALIDATE = ""
var URL_SEND_EMAIL_VALIDATE = ""
var URL_SEND_MOBILE_VALIDATE = ""
var URL_MEMBER_GETONE = ""
var URL_MEMBER_BLACKLIST = ""
var URL_CITYS = ""
var URL_ARENA_BY_CITY_ID = ""
var URL_ARENA_BY_CITY_IDS = ""
var URL_AREA_BY_CITY_IDS = ""
var URL_TEAM_UPDATE = ""
var URL_UPDATE = ""
var URL_DELETE = ""
var URL_ONE = ""
var URL_TT = ""
var URL_TT_UPDATE = ""
var URL_TT_DELETE = ""
var URL_TEAM = ""
var URL_TEAM_TEMP_PLAY = ""
var URL_TEAM_TEMP_PLAY_LIST = ""
var URL_TEAM_TEMP_PLAY_BLACKLIST = ""
var URL_TEAM_PLUSONE = ""
var URL_TEAM_CANCELPLUSONE = ""
var URL_TEAM_TEMP_PLAY_DATE = ""
var URL_TEAM_TEMP_PLAY_DATE_PLAYER = ""
var URL_SIGNUP = ""
var URL_CANCEL_SIGNUP = ""

var URL_COURSE_LIST = ""

// spinner
val LOADING: String = "努力加載中..."

// Team key
val TEAM_LEADER_KEY: String = "leader"
val TEAM_PLAY_START_KEY: String = "play_start"
val TEAM_PLAY_END_KEY: String = "play_end"
val TEAM_INTERVAL_KEY: String = "interval"
val TEAM_BALL_KEY: String = "ball"
val TEAM_DEGREE_KEY: String = "degree"
val TEAM_WEEKDAYS_KEY: String = "weekdays"
val TEAM_NEAR_DATE_KEY: String = "near_date"

val TEAM_TEMP_FEE_M_KEY: String = "temp_fee_M"
val TEAM_TEMP_FEE_F_KEY: String = "temp_fee_F"
val TEAM_TEMP_QUANTITY_KEY: String = "temp_quantity"
val TEAM_TEMP_SIGNUP_KEY: String = "temp_signup_count"
val TEAM_TEMP_CONTENT_KEY = "temp_content"
val TEAM_TEMP_STATUS_KEY: String = "temp_status"

// Coach key
val COACH_SENIORITY_KEY: String = "seniority"
val COACH_EXP_KEY: String = "exp"
val COACH_FEAT_KEY: String = "feat"
val COACH_LICENSE_KEY: String = "license"

// Arena key
val ARENA_OPEN_TIME_KEY: String = "open_time"
val ARENA_CLOSE_TIME_KEY: String = "close_time"
val ARENA_BLOCK_KEY: String = "block"
val ARENA_AIR_CONDITION_KEY: String = "air_condition"
val ARENA_PARKING_KEY: String = "parking"
val ARENA_BATHROOM_KEY: String = "bathroom"
val ARENA_CHARGE_KEY: String = "charge"
val ARENA_INTERVAL_KEY: String = "interval"

// Teach key
val COURSE_PROVIDER_KEY: String = "provider_url"

// General key
val ID_KEY: String = "id"
val TOKEN_KEY: String = "token"
val EMAIL_KEY: String = "email"
val MOBILE_KEY: String = "mobile"
val TEL_KEY: String = "tel"
val TITLE_KEY: String = "title"
val SLUG_KEY: String = "slug"
val CAT_KEY: String = "cat_id"
val FEATURED_KEY = "featured_path"
val THUMB_KEY = "thumb"
val WEBSITE_KEY: String = "website"
val YOUTUBE_KEY: String = "youtube"
val VIMEO_KEY: String = "vimeo"
val FB_KEY: String = "fb"
val LINE_KEY: String = "line"
val CITY_KEY: String = "city"
val CITYS_KEY: String = "citys"
val AREA_KEY: String = "area"
val ARENA_KEY: String = "arena"
val ROAD_KEY: String = "road"
val ADDRESS_KEY = "address"
val ZIP_KEY: String = "zip"
val CONTENT_KEY: String = "content"
val CHARGE_KEY = "charge"
val MANAGER_ID_KEY: String = "manager_id"
val PV_KEY: String = "pv"
val STATUS_KEY: String = "status"
val SORT_ORDER_KEY: String = "sort_order"
val COLOR_KEY: String = "color"
val CREATED_ID_KEY: String = "created_id"
val CREATED_AT_KEY: String = "created_at"
val UPDATED_AT_KEY: String = "updated_at"
val KEYWORD_KEY: String = "keyword"
val PRICE_KEY: String = "price"
val PRICE_DESC_KEY: String = "price_desc"
val PRICE_UNIT_KEY: String = "price_unit"
val WEEKDAY_KEY: String = "weekday"
val START_TIME_KEY: String = "start_time"
val END_TIME_KEY: String = "end_time"

// User Defaults
val NICKNAME_KEY: String = "nickname"
val ISLOGGEDIN_KEY: String = "isLoggedIn"
val UID_KEY: String = "uid"
val NAME_KEY: String = "name"
val CHANNEL_KEY: String = "channel"
val DOB_KEY: String = "dob"
val SEX_KEY: String = "sex"
val PID_KEY: String = "pid"
val PLAYERID_KEY = "player_id"
val AVATAR_KEY: String = "avatar"
val MEMBER_TYPE_KEY: String = "type"
val SOCIAL_KEY: String = "social"
val MEMBER_ROLE_KEY: String = "role"
val VALIDATE_KEY: String = "validate"
val EMAIL_VALIDATE: Int = 1
val MOBILE_VALIDATE: Int = 2
val PID_VALIDATE = 4
val GENERAL_TYPE = 1
val TEAM_TYPE = 2
val ARENA_TYPE = 4
val ISTEAMMANAGER_KEY = "isTeamManager"

// Timetalbe key
val TT_START_DATE: String = "start_date"
val TT_END_DATE: String = "end_date"
val TT_CHARGE: String = "charge"
val TT_LIMIT: String = "limit"
val TT_COLOR: String = "color"
val TT_STATUS: String = "status"
val TT_CONTENT: String = "content"

// Course key
val CYCLE_UNIT_KEY: String = "cycle_unit"
val COURSE_KIND_KEY: String = "kind"


// Select key
val DATE_SELECT_KEY: String = "date_select"
val COLOR_SELECT_KEY: String = "color_select"
val STATUS_SELECT_KEY: String = "status_select"

// member
val MEMBER_ARRAY: Map<String, Map<String, String>> = mapOf(
        ID_KEY to hashMapOf("type" to "Int","default" to "0"),
        TOKEN_KEY to hashMapOf("type" to "String", "default" to ""),
        EMAIL_KEY to hashMapOf("text" to "email","type" to "String", "default" to "","icon" to "email1"),
        NICKNAME_KEY to hashMapOf("text" to "暱稱","type" to "String", "default" to ""),
        ISLOGGEDIN_KEY to hashMapOf("type" to "Boolean", "default" to "false"),
        UID_KEY to hashMapOf("type" to "String", "default" to ""),
        NAME_KEY to hashMapOf("text" to "姓名","type" to "String", "default" to "","icon" to "name"),
        CHANNEL_KEY to hashMapOf("type" to "String", "default" to "bm"),
        DOB_KEY to hashMapOf("text" to "生日","type" to "String", "default" to ""),
        SEX_KEY to hashMapOf("text" to "性別","type" to "String", "default" to "M"),
        TEL_KEY to hashMapOf("text" to "電話","type" to "String", "default" to ""),
        MOBILE_KEY to hashMapOf("text" to "手機","type" to "String", "default" to "","icon" to "mobile"),
        PID_KEY to hashMapOf("text" to "身分證字號","type" to "String", "default" to ""),
        AVATAR_KEY to hashMapOf("type" to "String", "default" to ""),
        MEMBER_TYPE_KEY to hashMapOf("text" to "會員類型","type" to "Int", "default" to "0"),
        SOCIAL_KEY to hashMapOf("type" to "String", "default" to ""),
        MEMBER_ROLE_KEY to mapOf("text" to "會員身份","type" to "String", "default" to "member"),
        VALIDATE_KEY to mapOf("text" to "驗證","type" to "Int", "default" to "0")
)

// Notification Constants
val NOTIF_MEMBER_DID_CHANGE = "notifMemberChanged"
val NOTIF_MEMBER_UPDATE = "notifMemberUpdate"
val NOTIF_TEAM_UPDATE = "notifTeamUpdate"
val NOTIF_COURSE_UPDATE = "notifCourseUpdate"
val memberDidChangeIntent = Intent(NOTIF_MEMBER_DID_CHANGE)


// Header
var HEADER = "application/json; charset=utf-8"
var MULTIPART = "multipart/form-data"

val PERPAGE: Int = 8

// Prefix
val VIMEO_PREFIX: String = "/videos/"
val YOUTUBE_PREFIX: String = "https://youtu.be/"

val DAYS: Array<Map<String, Any>> = arrayOf(
        mapOf("value" to 1, "text" to "星期一", "checked" to false),
        mapOf("value" to 2, "text" to "星期二", "checked" to false),
        mapOf("value" to 3, "text" to "星期三", "checked" to false),
        mapOf("value" to 4, "text" to "星期四", "checked" to false),
        mapOf("value" to 5, "text" to "星期五", "checked" to false),
        mapOf("value" to 6, "text" to "星期六", "checked" to false),
        mapOf("value" to 7, "text" to "星期日", "checked" to false)
)

// vimeo
//val VIMEO_ID = "85a4cfd6e6ff82ea0493e269d19086c0c856936b"
//val VIMEO_SECRET = "w//PE1Vewrvaicmc9LtXyKjJB2DoFmxPenQxoZJ3vD3PkBraHahyFKpm4zmZnIIJy2EUO8NvSWuWiHkbK8mLoBvUxve1Rxm54nl4OH8FHpKHmvGtG3zm30gOa/X36oL5"
//val VIMEO_TOKEN = "6796bb60593399d57223bcdcafded37e"

val PARAMS: HashMap<String, String> = hashMapOf("source" to SOURCE, "channel" to CHANNEL)

val SESSION_FILENAME: String = "com.sportpassword.bm.session"

//request code
val GENERAL_REQUEST_CODE: Int = 100



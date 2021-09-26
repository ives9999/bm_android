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
const val LOCALHOST_BASE_URL = "http://192.168.100.120"
//const val LOCALHOST_BASE_URL = "http://192.168.3.103"
var BASE_URL = ""
var URL_HOME = ""

var URL_AREA_BY_CITY_IDS = ""
var URL_ARENA_BY_CITY_ID = ""
var URL_ARENA_BY_CITY_IDS = ""
var URL_ARENA_LIKE = ""
var URL_ARENA_LIST = ""
var URL_CANCEL_SIGNUP = ""
var URL_CART_DELETE = ""
var URL_CART_LIST = ""
var URL_CART_UPDATE = ""
var URL_CHANGE_PASSWORD = ""
var URL_CITYS = ""
var URL_COACH_LIKE = ""
var URL_COACH_LIST = ""
var URL_COURSE_LIKE = ""
var URL_COURSE_LIST = ""
var URL_DELETE = ""
var URL_EMAIL_VALIDATE = ""
var URL_FB_LOGIN = ""
var URL_FORGETPASSWORD = ""
var URL_MEMBER_LIKELIST = ""
var URL_LIST = ""
var URL_LOGIN = ""
var URL_MEMBER_BLACKLIST = ""
var URL_MEMBER_GETONE = ""
var URL_MEMBER_UPDATE = ""
var URL_MOBILE_VALIDATE = ""
var URL_ONE = ""
var URL_ORDER_LIST = ""
var URL_ORDER_UPDATE = ""
var URL_PRODUCT_LIKE = ""
var URL_PRODUCT_LIST = ""
var URL_SHOW = ""
var URL_ORDER = ""
var URL_REGISTER = ""
var URL_SEND_EMAIL_VALIDATE = ""
var URL_SEND_MOBILE_VALIDATE = ""
var URL_SIGNUP = ""
var URL_SIGNUP_DATE = ""
var URL_SIGNUP_LIST = ""
var URL_STORE_LIKE = ""
var URL_STORE_LIST = ""
var URL_TEACH_LIKE = ""
var URL_TEACH_LIST = ""
var URL_TEAM = ""
var URL_TEAM_CANCELPLUSONE = ""
var URL_TEAM_LIKE = ""
var URL_TEAM_LIST = ""
var URL_TEAM_TEMP_PLAY = ""
var URL_TEAM_TEMP_PLAY_BLACKLIST = ""
var URL_TEAM_TEMP_PLAY_LIST = ""
var URL_TEAM_PLUSONE = ""
var URL_TEAM_TEMP_PLAY_DATE = ""
var URL_TEAM_TEMP_PLAY_DATE_PLAYER = ""
var URL_TEAM_UPDATE = ""
var URL_TT = ""
var URL_TT_DELETE = ""
var URL_TT_UPDATE = ""
var URL_UPDATE = ""

val TO_ADD_CART: String = "toAddCart"
val TO_AREA: String = "toArea"
val TO_ARENA: String = "toArena"
val TO_BLACKLIST: String = "toBlacklist"
val TO_CITY: String = "toCity"
val TO_COACH: String = "toCoach"
val TO_COACH_MANAGER_FUNCTION: String = "toCoachManagerFunction"
val TO_COACH_SIGNUP: String = "toCoachSignup"
val TO_COACH_SUBMIT: String = "toCoachSubmit"
val TO_CONTENT_EDIT: String = "toContentEdit"
val TO_COURSE: String = "toCourse"
val TO_DEGREE: String = "toDegree"
val TO_DEvalE: String = "toDevale"
val TO_EDIT: String = "toEdit"
val TO_EDIT_COURSE: String = "toEditCourse"
val TO_EDIT_PROFILE: String = "toEditProfile"
val TO_EDIT_STORE: String = "toEditStore"
val TO_HOME: String = "toHome"
val TO_LIKE: String = "toLike"
val TO_LOGIN: String = "toLogin"
val TO_MANAGER: String = "toManager"
val TO_MANAGER_COURSE: String = "toManagerCourse"
val TO_MANAGER_FUNCTION: String = "toManagerFunction"
val TO_MANAGER_STORE: String = "toManagerStore"
val TO_MAP: String = "toMap"
val TO_MEMBER_CART_LIST: String = "toMemberCartList"
val TO_MEMBER_LIKE_LIST: String = "toMemberLikeList"
val TO_MEMBER_ORDER_LIST: String = "toMemberOrderList"
val TO_MULTI_SELECT: String = "toMultiSelect"
val TO_ORDER: String = "toOrder"
val TO_PASSWORD: String = "toPassword"
val TO_PRODUCT: String = "toProduct"
val TO_PN: String = "toPN"
val TO_PAYMENT: String = "toPayment"
val TO_PROFILE: String = "toProfile"
val TO_REGISTER: String = "toRegister"
val TO_REFRESH: String = "toRefresh"
val TO_SEARCH: String = "toSearch"
val TO_SELECT_AREA: String = "toSelectArea"
val TO_SELECT_AREAS: String = "toSelectAreas"
val TO_SELECT_CITY: String = "toSelectCity"
val TO_SELECT_CITYS: String = "toSelectCitys"
val TO_SELECT_DEGREE: String = "toSelectDegree"
val TO_SELECT_MANAGERS: String = "toSelectManagers"
val TO_SELECT_WEEKDAY: String = "toWeekday"
val TO_SELECT_DATE: String = "toSelectDate"
val TO_SELECT_TIME: String = "toSelectTime"
val TO_SELECT_COLOR: String = "toSelectColor"
val TO_SELECT_STATUS: String = "toSelectStatus"
val TO_SHOW: String = "toShow"
val TO_SHOW_ARENA: String = "toShowArena"
val TO_SHOW_COACH: String = "toShowCoach"
val TO_SHOW_COURSE: String = "toShowCourse"
val TO_SHOW_PRODUCT: String = "toShowProduct"
val TO_SHOW_STORE: String = "toShowStore"
val TO_SHOW_TEACH: String = "toShowTeach"
val TO_SHOW_TEAM: String = "toShowTeam"
val TO_SHOW_TIMETABLE: String = "toShowTimeTable"
val TO_SIGNUP_LIST: String = "toSignupList"
val TO_SINGLE_SELECT: String = "toSingleSelect"
val TO_STORE: String = "toStore"
val TO_TEAM_TEMP_PLAY: String = "toTeamTempPlay"//臨打編輯
val TO_TEMP_PLAY_DATE: String = "toTempPlayDate"
val TO_TEMP_PLAY_DATE_PLAYER: String = "toTempPlayDatePlayer"
val TO_TEMP_PLAY_LIST: String = "toTempplayList"
val TO_TEMP_PLAY_SHOW: String = "toTempPlayShow"
val TO_TEMP_PLAY_SIGNUP_ONE: String = "toTempPlaySignupOne"
val TO_TEXT_INPUT: String = "toTextInput"
val TO_TIMETABLE: String = "toTimeTable"
val TO_VALIDATE: String = "toValidate"
val TO_YOUTUBE_PLAYER: String = "toYoutubePlayer"

// spinner
val LOADING: String = "努力加載中..."

// Team key
val TEAM_BALL_KEY: String = "ball"
val TEAM_DEGREE_KEY: String = "degree"
val TEAM_INTERVAL_KEY: String = "interval"
val TEAM_LEADER_KEY: String = "leader"
val TEAM_NEAR_DATE_KEY: String = "near_date"
val TEAM_PLAY_END_KEY: String = "play_end"
val TEAM_PLAY_START_KEY: String = "play_start"
val TEAM_WEEKDAYS_KEY: String = "weekdays"

val TEAM_TEMP_CONTENT_KEY = "temp_content"
val TEAM_TEMP_FEE_F_KEY: String = "temp_fee_F"
val TEAM_TEMP_FEE_M_KEY: String = "temp_fee_M"
val TEAM_TEMP_QUANTITY_KEY: String = "temp_quantity"
val TEAM_TEMP_SIGNUP_KEY: String = "temp_signup_count"
val TEAM_TEMP_STATUS_KEY: String = "temp_status"

// Coach key
val COACH_EXP_KEY: String = "exp"
val COACH_FEAT_KEY: String = "feat"
val COACH_LICENSE_KEY: String = "license"
val COACH_SENIORITY_KEY: String = "seniority"

// Arena key
val ARENA_AIR_CONDITION_KEY: String = "air_condition"
val ARENA_BATHROOM_KEY: String = "bathroom"
val ARENA_BLOCK_KEY: String = "block"
val ARENA_CHARGE_KEY: String = "charge"
val ARENA_CLOSE_TIME_KEY: String = "close_time"
val ARENA_INTERVAL_KEY: String = "interval"
val ARENA_OPEN_TIME_KEY: String = "open_time"
val ARENA_PARKING_KEY: String = "parking"

// Teach key
val COURSE_PROVIDER_KEY: String = "provider_url"

// General key
val ADDRESS_KEY = "address"
val AMOUNT_KEY = "amount"
val AREA_KEY: String = "area"
val AREAS_KEY: String = "areas"
val ARENA_KEY: String = "arena"
val ATTRIBUTE_KEY: String = "attribute"
val BANK_CODE_KEY: String = "bank_code"
val BANK_ACCOUNT_KEY: String = "bank_account"
val BARCODE1_KEY: String = "barcode1"
val BARCODE2_KEY: String = "barcode2"
val BARCODE3_KEY: String = "barcode3"
val CAT_KEY: String = "cat_id"
val CELL_KEY: String = "cell"
val CHARGE_KEY = "charge"
val CITY_KEY: String = "city"
val CITYS_KEY: String = "citys"
val CLOTHES_SIZE_KEY = "clothes_size"
val COLOR_KEY: String = "color"
val COMPANY_KEY: String = "company"
val CONTACT_KEY: String = "contact"
val CONTENT_KEY: String = "content"
val CREATED_AT_KEY: String = "created_at"
val CREATED_ID_KEY: String = "created_id"
val CREDIT_CARD_KEY: String = "credit_card"
val DEGREE_KEY: String = "degree"
val DIRECT_KEY: String = "direct"
val DISCOUNT_KEY: String = "discount"
val EMAIL_KEY: String = "email"
val END_DATE_KEY: String = "end_date"
val END_TIME_KEY: String = "end_time"
val EXPIRE_AT_KEY: String = "expire_at"
val FAMILY_KEY: String = "family"
val FB_KEY: String = "fb"
val FEATURED_KEY = "featured_path"
val GATEWAY_KEY = "gateway"
val GATEWAY_AT_KEY = "gateway_at"
val GATEWAY_METHOD_KEY = "gateway_method"
val GATEWAY_PROCESS_KEY = "gateway_process"
val GRAND_TOTAL_KEY = "grand_total"
val ID_KEY: String = "id"
val INVOICE_KEY = "invoice"
val INVOICE_COMPANY_NAME_KEY = "invoice_company_name"
val INVOICE_COMPANY_TAX_KEY = "invoice_company_tax"
val INVOICE_TYPE_KEY = "invoice_type"
val INVOICE_EMAIL_KEY = "invoice_email"
val KEY_KEY: String = "key"
val KEYWORD_KEY: String = "keyword"
val LINE_KEY: String = "line"
val MANAGER_ID_KEY: String = "manager_id"
val MEMBER_KEY: String = "member"
val MEMO_KEY: String = "memo"
val MOBILE_KEY: String = "mobile"
val NUMBER_KEY: String = "number"
val OPEN_TIME_KEY = "open_time"
val ORDER_KEY = "order"
val ORDER_NO_KEY = "order_no"
val ORDER_PROCESS_KEY = "order_process"
val PAYMENT_NO_KEY: String = "payment_no_key"
val PASSWORD_KEY: String = "password"
val PAYMENT_KEY: String = "payment"
val PEOPLE_LIMIT_KEY: String = "people_limit"
val PERSONAL_KEY: String = "personal"
val PLAYER_ID_KEY: String = "player_id"
val PRICE_DESC_KEY: String = "price_desc"
val PRICE_KEY: String = "price"
val PRICE_UNIT_KEY: String = "price_unit"
val PRODUCT_KEY: String = "product"
val PV_KEY: String = "pv"
val QUANTITY_KEY: String = "quantity"
val REPASSWORD_KEY: String = "repassword"
val PRIVACY_KEY: String = "privacy"
val ROAD_KEY: String = "road"
val ROLE_KEY: String = "role"
val SEVEN_KEY = "SEVEN"
val SHIPPING_AT_KEY = "shipping_at"
val SHIPPING_BACK_AT_KEY = "shipping_back_at"
val SHIPPING_COMPLETE_AT_KEY = "shipping_complete_at"
val SHIPPING_FEE_KEY = "shipping_fee"
val SHIPPING_KEY = "shipping"
val SHIPPING_METHOD_KEY = "shipping_method"
val SHIPPING_PROCESS_KEY = "shipping_process"
val SHIPPING_STORE_AT_KEY = "shipping_store_at"
val SHOW_KEY: String = "show"
val SIZE_KEY: String = "size"
val SLUG_KEY: String = "slug"
val SORT_ORDER_KEY: String = "sort_order"
val START_DATE_KEY: String = "start_date"
val START_TIME_KEY: String = "start_time"
val STATUS_KEY: String = "status"
val SUBTOTAL_KEY: String = "sub_total"
val TAG_KEY: String = "tag"
val TAX_KEY: String = "tax"
val TEL_KEY: String = "tel"
val THUMB_KEY = "thumb"
val TITLE_KEY: String = "title"
val TOKEN_KEY: String = "token"
val TOTAL_KEY: String = "total"
val TYPE_KEY: String = "type"
val UPDATED_AT_KEY: String = "updated_at"
val VALEDATE_KEY: String = "valedate"
val VALUE_KEY: String = "value"
val VIMEO_KEY: String = "vimeo"
val WEBSITE_KEY: String = "website"
val WEIGHT_KEY: String = "weight"
val WEEKDAY_KEY: String = "weekday"
val YOUTUBE_KEY: String = "youtube"
val ZIP_KEY: String = "zip"

// User Defaults
val ARENA_TYPE = 4
val AVATAR_KEY: String = "avatar"
val CHANNEL_KEY: String = "channel"
val DOB_KEY: String = "dob"
val EMAIL_VALIDATE: Int = 1
val GENERAL_TYPE = 1
val ISLOGGEDIN_KEY: String = "isLoggedIn"
val ISTEAMMANAGER_KEY = "isTeamManager"
val MEMBER_ROLE_KEY: String = "role"
val MEMBER_TYPE_KEY: String = "type"
val NAME_KEY: String = "name"
val NICKNAME_KEY: String = "nickname"
val MOBILE_VALIDATE: Int = 2
val PID_KEY: String = "pid"
val PID_VALIDATE = 4
val PLAYERID_KEY = "player_id"
val SEX_KEY: String = "sex"
val SOCIAL_KEY: String = "social"
val TEAM_TYPE = 2
val UID_KEY: String = "uid"
val VALIDATE_KEY: String = "validate"

// Timetalbe key
val TT_CHARGE: String = "charge"
val TT_COLOR: String = "color"
val TT_CONTENT: String = "content"
val TT_END_DATE: String = "end_date"
val TT_LIMIT: String = "limit"
val TT_START_DATE: String = "start_date"
val TT_STATUS: String = "status"

// Course key
val COURSE_KIND_KEY: String = "kind"
val CYCLE_UNIT_KEY: String = "cycle_unit"


// Select key
val COLOR_SELECT_KEY: String = "color_select"
val DATE_SELECT_KEY: String = "date_select"
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
        CITY_KEY to hashMapOf("text" to "縣市","type" to "Int","default" to "0"),
        AREA_KEY to hashMapOf("text" to "區域","type" to "Int","default" to "0"),
        ROAD_KEY to hashMapOf("text" to "路名","type" to "String", "default" to ""),
        ZIP_KEY to hashMapOf("text" to "郵遞區號","type" to "Int","default" to "0"),
        FB_KEY to hashMapOf("text" to "Facebook","type" to "String", "default" to ""),
        LINE_KEY to hashMapOf("text" to "Line","type" to "String", "default" to ""),
        PID_KEY to hashMapOf("text" to "身分證字號","type" to "String", "default" to ""),
        AVATAR_KEY to hashMapOf("type" to "String", "default" to ""),
        MEMBER_TYPE_KEY to hashMapOf("text" to "會員類型","type" to "Int", "default" to "0"),
        SOCIAL_KEY to hashMapOf("type" to "String", "default" to ""),
        MEMBER_ROLE_KEY to mapOf("text" to "會員身份","type" to "String", "default" to "member"),
        VALIDATE_KEY to mapOf("text" to "驗證","type" to "Int", "default" to "0"),
        TYPE_KEY to mapOf("text" to "類型","type" to "Int", "default" to "0")
)

val MEMBER_MUST_ARRAY: ArrayList<String> = arrayListOf(
    NAME_KEY, NICKNAME_KEY, EMAIL_KEY, MOBILE_KEY, SEX_KEY, CITY_KEY, AREA_KEY, ROAD_KEY
)

val MEMBER_MUST_ARRAY_WARNING: HashMap<String, String> = hashMapOf(
    NAME_KEY to "會員資料沒有填寫姓名",
    NICKNAME_KEY to "會員資料沒有填寫暱稱",
    EMAIL_KEY to "會員資料沒有填寫email",
    MOBILE_KEY to "會員資料沒有填寫手機",
    SEX_KEY to "會員資料沒有選擇性別",
    CITY_KEY to "會員資料沒有選擇縣市",
    AREA_KEY to "會員資料沒有選擇區域",
    ROAD_KEY to "會員資料沒有填寫路名"
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

val PERPAGE: Int = 20

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

val PARAMS: HashMap<String, String> = hashMapOf("device" to SOURCE, "channel" to CHANNEL)

val SESSION_FILENAME: String = "com.sportpassword.bm.session"

//request code
val GENERAL_REQUEST_CODE: Int = 100

var FEATURED_PATH: String = "/imgs/nophoto.png"



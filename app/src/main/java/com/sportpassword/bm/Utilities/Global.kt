package com.sportpassword.bm.Utilities

import android.animation.Animator
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.makeCall
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor
import java.lang.reflect.Type
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.pow

open class MYENUM<T>() {

//    fun all(allValues: ArrayList<T>): ArrayList<HashMap<String, Any?>> {
//        val res: ArrayList<HashMap<String, Any?>> = arrayListOf()
//        for (item in allValues) {
//            res.add(hashMapOf("key" to item.toString(), "value" to item))
//        }
//        return res
//    }
//
//    fun makeSelect(allValues: ArrayList<T>): ArrayList<HashMap<String, String>> {
//        val res: ArrayList<HashMap<String, String>> = arrayListOf()
//        for (item in allValues) {
//            res.add(hashMapOf("title" to item.value, "value" to item.toString()))
//        }
//        return res
//    }
}

enum class CELL_TYPE(val value: String) {
    PLAIN("plain"),
    TEXTFIELD("textField"),
    TAG("tag"),
    NUMBER("number"),
    CART("cart"),
    RADIO("radio"),
    MORE("more"),
    BARCODE("barcode"),
    SEX("sex"),
    PASSWORD("password"),
    PRIVACY("privacy"),
    SWITCH("switch"),
    CONTENT("content");

    fun toInt(): Int {
        when (this) {
            PLAIN -> return 0
            TEXTFIELD -> return 1
            TAG -> return 2
            NUMBER -> return 3
            CART -> return 4
            RADIO -> return 5
            MORE -> return 6
            BARCODE -> return 7
            SEX -> return 8
            PASSWORD -> return 9
            PRIVACY -> return 10
            SWITCH -> return 11
            CONTENT -> return 12
        }
    }

}

enum class MEMBER_COIN_IN_TYPE(val englishName: String, val chineseName: String) {
    buy("buy", "購買"),
    gift("gift", "贈品"),
    none("none", "無");

    companion object {

        fun enumFromString(value: String): MEMBER_COIN_IN_TYPE {
            when (value) {
                "buy" -> return MEMBER_COIN_IN_TYPE.buy
                "gift" -> return MEMBER_COIN_IN_TYPE.gift
                "none" -> return MEMBER_COIN_IN_TYPE.none
            }
            return MEMBER_COIN_IN_TYPE.none
        }
    }
}

enum class MEMBER_COIN_OUT_TYPE(val englishName: String, val chineseName: String) {
    product("product", "商品"),
    course("course", "課程"),
    none("none", "無");

    companion object {

        fun enumFromString(value: String): MEMBER_COIN_OUT_TYPE {
            when (value) {
                "product" -> return product
                "course" -> return course
                "none" -> return none
            }
            return none
        }
    }
}

enum class MYCOLOR(val value: Int) {
//    danger("danger"), success("success"), primary("primary"), warning("warning"),
//    info("info"), gray("gray")
    primary(0x245580),
    warning(0xF1C40F),
    info(0x659be0),
    danger(0xc12e2a),
    success(0x419641),
    white(0xe1e1e1);


//    fun toString(): String {
//        when (this) {
//            danger -> return "danger"
//            success -> return "success"
//            primary -> return "primary"
//            warning -> return "warning"
//            info -> return "info"
//            gray -> return "gray"
//        }
//        return "success"
//    }

    fun toColor(): Int {
        val hexColor = String.format("#%06X", 0xFFFFFF and this.value)
        return Color.parseColor(hexColor)
    }

    companion object {

        val allValues: ArrayList<MYCOLOR> = arrayListOf(primary, warning, info, danger, success, white)

        fun from(value: String): MYCOLOR {
            when (value) {
                "danger" -> return danger
                "success" -> return success
                "primary" -> return primary
                "warning" -> return warning
                "info" -> return info
                "white" -> return white
            }
            return success
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item, "color" to item.toColor()))
            }
            return res
        }
    }
}

enum class STATUS(val value: String) {
    online("上線"),
    offline("下線"),
    padding("草稿"),
    trash("垃圾桶"),
    delete("刪除");

    companion object {

        val allValues: ArrayList<STATUS> = arrayListOf(online, offline, padding, trash, delete)

        fun from(value: String): STATUS {
            when (value) {
                "online" -> return online
                "offline" -> return offline
                "padding" -> return padding
                "trash" -> return trash
                "delete" -> return delete
                else -> return online
            }
            return online
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item, "ch" to item.value))
            }
            return res
        }
    }
}

enum class DEGREE(val value: String) {
    new("新手"), soso("普通"), high("高手");
    companion object {
        fun toString(value: DEGREE): String {
            when (value) {
                DEGREE.new -> return "new"
                DEGREE.soso -> return "soso"
                DEGREE.high -> return "high"
            }
        }
        fun fromChinese(findValue: String): DEGREE = DEGREE.values().first { it.value == findValue }

        fun fromEnglish(findValue: String): DEGREE {
            when (findValue) {
                "new" ->return new
                "soso" ->return soso
                "high" ->return high
            }
            return new
        }
        fun all(): Map<String, String> {
            return mapOf("new" to "新手", "soso" to "普通", "high" to "高手")
        }
    }
}

enum class SELECT_TIME_TYPE(val value: Int) {
    play_start(0), play_end(1);
}

enum class SELECT_DATE_TYPE(val value: Int) {
    start(0), end(1);
}

enum class TEXT_INPUT_TYPE(val value: String) {
    temp_play("臨打"),
    charge("收費標準"),
    content("詳細內容"),
    exp("經歷"),
    feat("比賽成績"),
    license("證照"),
    timetable_coach("課程說明")
}

enum class CYCLE_UNIT(val value: String) {
    month("月"),
    week("週");

    companion object {
        val allValues: ArrayList<CYCLE_UNIT> = arrayListOf(month, week)

        fun from(value: String): CYCLE_UNIT {
            when (value) {
                "month" -> return month
                "week" -> return week
            }
            return month
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to item.value, "value" to item.toString()))
            }
            return res
        }
    }
}

enum class COURSE_KIND(val value: String) {
    one("一次性"),
    cycle("週期性");

    companion object {

        val allValues: ArrayList<COURSE_KIND> = arrayListOf(one, cycle)

        fun from(value: String): COURSE_KIND {
            when (value) {
                "one" -> return one
                "cycle" -> return cycle
            }
            return cycle
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to item.value, "value" to item.toString()))
            }
            return res
        }
    }
}

enum class PRICE_UNIT(val value: String) {

    month("每月"),
    week("每週"),
    season("每季"),
    year("每年"),
    span("每期"),
    other("其他");

    companion object: MYENUM<PRICE_UNIT>() {

        val allValues: ArrayList<PRICE_UNIT> = arrayListOf(month, week, season, year, span, other)

        fun from(value: String): PRICE_UNIT {
            when (value) {
                "month" -> return month
                "week" -> return week
                "season" -> return season
                "year" -> return year
                "span" -> return span
                "other" -> return other
            }
            return month
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to item.value, "value" to item.toString()))
            }
            return res
        }
    }
}

enum class WEEKDAY(val value: Int) {
    mon(1),
    tue(2),
    wed(3),
    thu(4),
    fri(5),
    sat(6),
    sun(7);

    fun enumToShortString(): String {
        when (this) {
            mon -> return "一"
            tue -> return "二"
            wed -> return "三"
            thu -> return "四"
            fri -> return "五"
            sat -> return "六"
            sun -> return "日"
        }
    }

    companion object: MYENUM<WEEKDAY>() {

        val allValues: ArrayList<WEEKDAY> = arrayListOf(mon, tue, wed, thu, fri, sat, sun)

        fun from(value: Int): WEEKDAY {
            when (value) {
                1 -> return mon
                2 -> return tue
                3 -> return wed
                4 -> return thu
                5 -> return fri
                6 -> return sat
                7 -> return sun
            }
            return mon
        }

        fun intToString(value: Int): String {
            when (value) {
                1 -> return "星期一"
                2 -> return "星期二"
                3 -> return "星期三"
                4 -> return "星期四"
                5 -> return "星期五"
                6 -> return "星期六"
                7 -> return "星期日"
            }
            return "星期一"
        }

        fun enumToString(value: WEEKDAY): String {

            return WEEKDAY.intToString(value.value)
        }

        fun all(): ArrayList<HashMap<String, Any>> {
            val res: ArrayList<HashMap<String, Any>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("key" to item.toString(), "value" to item))
            }
            return res
        }

        fun makeSelect(): ArrayList<HashMap<String, String>> {
            val res: ArrayList<HashMap<String, String>> = arrayListOf()
            for (item in allValues) {
                res.add(hashMapOf("title" to WEEKDAY.enumToString(item), "value" to item.value.toString()))
            }
            return res
        }
    }
}

enum class ORDER_PROCESS(val englishName: String, val chineseName: String) {
    normal("normal", "訂單成立"),
    gateway("gateway", "完成付款"),
    shipping("shipping", "出貨中"),
    store("store", "送達超商"),
    complete("complete", "訂單完成"),
    returning("returning", "商品退回中"),
    `return`("return", "商品已退回"),
    gateway_fail("gateway_fail", "付款失敗");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<ORDER_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return ORDER_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class ALL_PROCESS(val englishName: String, val chineseName: String) {
    notexist("notexist", "沒有"),
    normal("normal", "訂單成立"),
    gateway_on("gateway_on", "付款中"),
    gateway_off("gateway_off", "完成付款，準備出貨"),
    shipping("shipping", "準備出貨"),
    logistic("logistic", "到達物流中心"),
    store("store", "到達便利商店"),
    complete("complete", "完成取貨"),
    returning("returning", "商品退回中"),
    `return`("return", "商品已退回"),
    gateway_fail("gateway_fail", "付款失敗");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<ALL_PROCESS>() {

        fun intToEnum(enumInt: Int): ALL_PROCESS {
            when (enumInt) {
                0-> return notexist
                1-> return normal
                2-> return gateway_on
                3-> return gateway_off
                4-> return shipping
                5-> return logistic
                6-> return store
                7-> return complete
                8-> return returning
                9-> return `return`
                10-> return gateway_fail
            }
            return normal
        }

        fun getRawValueFromString(value: String): String {
            return ALL_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class PAYMENT_PROCESS(val englishName: String, val chineseName: String) {
    normal("normal", "未付款"),
    code("code", "取得付款代碼"),
    complete("complete", "完成付款");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<PAYMENT_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return PAYMENT_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class GATEWAY(val englishName: String, val chineseName: String) {

    credit_card("credit_card", "信用卡"),
    store_cvs("store_cvs", "超商代碼"),
    store_barcode("store_barcode", "超商條碼"),
    store_pay_711("store_pay_711", "7-11超商取貨付款"),
    store_pay_family("store_pay_family", "全家超商取貨付款"),
    store_pay_hilife("store_pay_hilife", "萊爾富超商取貨付款"),
    store_pay_ok("store_pay_ok", "OK超商取貨付款"),
    ATM("ATM", "虛擬帳戶"),
    remit("remit", "匯款"),
    cash("cash", "現金"),
    coin("coin", "解碼點數");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    fun mapToShipping(): SHIPPING {
        return when(this) {
            store_pay_711-> SHIPPING.store_711
            store_pay_family->SHIPPING.store_family
            store_pay_hilife->SHIPPING.store_hilife
            store_pay_ok->SHIPPING.store_ok
            else->SHIPPING.direct
        }
    }

    fun enumToECPay(): String {
        return when(this) {
            store_pay_711-> "UNIMARTC2C"
            store_pay_family -> "FAMIC2C"
            store_pay_hilife -> return "HILIFEC2C"
            store_pay_ok -> return "OKMARTC2C"
            else -> "UNIMARTC2C"
        }
    }

    companion object: MYENUM<GATEWAY>() {

        fun getRawValueFromString(value: String): String {
            return GATEWAY.valueOf(value).toChineseString()
        }

        fun stringToEnum(str: String): GATEWAY {

            when(str) {

                "credit_card"-> return credit_card
                "store_cvs"-> return store_cvs
                "store_barcode"-> return store_barcode
                "store_pay_711"-> return store_pay_711
                "store_pay_family"-> return store_pay_family
                "store_pay_hilife"-> return store_pay_hilife
                "store_pay_ok"-> return store_pay_ok
                "ATM"-> return ATM
                "remit"-> return remit
                "cash"-> return cash
                "coin"-> return coin
            }
            return credit_card
        }
    }
}

enum class GATEWAY_PROCESS(val englishName: String, val chineseName: String) {

    normal("normal", "未付款"),
    code("code", "取得付款代碼"),
    complete("complete", "完成付款"),
    fail("fail", "付款失敗"),
    `return`("return", "完成退款");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<SHIPPING_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return GATEWAY_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class SHIPPING(val englishName: String, val chineseName: String) {

    direct("direct", "宅配"),
    store_711("store_711", "7-11超商取貨"),
    store_family("store_family", "全家超商取貨"),
    store_hilife("store_hilife", "萊爾富超商取貨"),
    store_ok("store_ok", "OK超商取貨"),
    cash("cash", "面交");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<SHIPPING_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return SHIPPING.valueOf(value).toChineseString()
        }

        fun stringToEnum(str: String): SHIPPING {

            when(str) {

                "direct"-> return direct
                "store_711"-> return store_711
                "store_family"-> return store_family
                "store_hilife"-> return store_hilife
                "store_ok"-> return store_ok
                "cash"-> return cash
            }
            return direct
        }
    }
}

enum class SHIPPING_PROCESS(val englishName: String, val chineseName: String) {
    normal("normal", "準備中"),
    shipping("shipping", "已經出貨"),
    logistic("logistic", "已經送到物流中心"),
    store("store", "商品已到便利商店"),
    complete("complete", "已完成取貨"),
    `return`("back", "貨物退回");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<SHIPPING_PROCESS>() {

        fun getRawValueFromString(value: String): String {
            return SHIPPING_PROCESS.valueOf(value).toChineseString()
        }
    }
}

enum class KEYBOARD(val type: String) {

    default("default"),
    emailAddress("emailAddress"),
    numberPad("numberPad"),
    URL("URL");

    override fun toString(): String {
        return type
    }

    fun toSwift(): Int {
        when(this) {
            default -> return InputType.TYPE_CLASS_TEXT
            emailAddress -> return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            numberPad -> return InputType.TYPE_CLASS_NUMBER
            URL -> return InputType.TYPE_TEXT_VARIATION_URI
        }
    }

    companion object: MYENUM<KEYBOARD>() {

        fun stringToSwift(str: String): Int {

            when(str) {

                "default"-> return InputType.TYPE_CLASS_TEXT
                "emailAddress"-> return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                "numberPad"-> return InputType.TYPE_CLASS_NUMBER
                "URL"-> return InputType.TYPE_TEXT_VARIATION_URI
            }
            return InputType.TYPE_CLASS_TEXT
        }
    }
}

enum class SIGNUP_STATUS(val type: String) {

    normal("報名"),
    standby("候補"),
    cancel("取消");

    override fun toString(): String {
        return type
    }

    companion object {
        fun toString(value: SIGNUP_STATUS): String {
            when (value) {
                normal -> return "normal"
                standby -> return "standby"
                cancel -> return "cancel"
            }
        }

        fun stringToSwift(str: String): SIGNUP_STATUS {

            when(str) {

                "normal"-> return normal
                "standby"-> return standby
                "cancel"-> return cancel
            }
            return normal
        }
    }
}

enum class MEMBER_LEVEL(val englishName: String, val chineseName: String) {

    gold("gold", "金牌"),
    silver("silver", "銀牌"),
    copper("copper", "銅牌"),
    steal("steal", "鐵牌");

    fun toChineseString(): String {
        return chineseName
    }

    fun toEnglishString(): String {
        return englishName
    }

    companion object: MYENUM<MEMBER_LEVEL>() {

        fun getRawValueFromString(value: String): String {
            return SHIPPING.valueOf(value).toChineseString()
        }

        fun stringToEnum(str: String): MEMBER_LEVEL {

            when(str) {

                "gold"-> return gold
                "silver"-> return silver
                "copper"-> return copper
                "steal"-> return steal
            }
            return steal
        }
    }
}


/**
 * Created by ives on 2018/3/8.
 */
fun String.truncate(length: Int, trailing: String = "…"): String {
    if (this.length > length) {
        val prefix = this.substring(0, length)
        return prefix + trailing
    } else {
        return this
    }
}
fun String.noYear(): String {
    val arr: List<String> = this.split("-")
    var res: String = this
    if (arr.size > 2) {
        res = "${arr[1]}-${arr[2]}"
    }

    return res
}
fun String.noSec(): String {
    val arr: List<String> = this.split(":")
    var res: String = this
    if (arr.size > 2) {
        res = "${arr[0]}:${arr[1]}"
    }
    return res
}
fun String.noTime(): String {
    var res: String = this
    if (this.isDateTime()) {
        res = res.toDateTime()!!.toMyString("yyyy-MM-dd")
    }
    return res
}
fun String.isPrimitive(): Boolean {
    val type = this.lowercase(Locale.getDefault())
    var b: Boolean = false
    when (type) {
        "boolean", "int", "float", "double", "string" -> b = true
        else -> b = false
    }
    return b
}
fun String.reMatches(pattern: String, _str: String?=null): Array<String> {
    var str = _str
    if (str == null) {
        str = this
    }
    var matches: Array<String> = arrayOf()
    val re = pattern.toRegex()
    val b = re.containsMatchIn(str)
    if (b) {
        val matchRes = re.find(str)
        if (matchRes != null) {
            val arr = matchRes.groupValues
            matches = arr.toTypedArray()
        }
        //println(res)
    }


    return matches
}
fun String.mobileShow(): String {
    var res = this
    res = res.replace(" ", "")
    res = res.replace("-", "")
    val pattern = "^(09\\d\\d)\\-?(\\d\\d\\d)\\-?(\\d\\d\\d)\$"
    val matches = reMatches(pattern, res)
    if (matches.size > 3) {
        res = matches[1] + "-" + matches[2] + "-" + matches[3]
    }

    return res
}
fun String.telShow(): String {
    var res = this
    res = res.replace(" ", "")
    res = res.replace("-", "")
    val pattern = "^(0\\d)\\-?(\\d\\d\\d\\d)\\-?(\\d\\d\\d\\d?)$"
    val matches = reMatches(pattern, res)
    if (matches.size > 3) {
        res = matches[1] + "-" + matches[2] + "-" + matches[3]
    }

    return res
}
fun String.telOrMobileShow(): String {
    if (this.startsWith("09")) {
        return this.mobileShow()
    }
    if (this.startsWith("0")) {
        return this.telShow()
    }
    return this
}
fun String.toDate(pattern: String = "yyyy-MM-dd"): Date? {
    var date: Date? = null
    if (this.isDate()) {
        //val formatter = DateTimeFormatter.ofPattern(pattern, Locale.TAIWAN)
        val formatter = SimpleDateFormat(pattern)
        date = formatter.parse(this)
    }

    return date
}

fun String.toDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    var date: Date? = null
    if (this.isDateTime(pattern) || this.isDate(pattern) || this.isTime(pattern)) {
        //val formatter = DateTimeFormatter.ofPattern(pattern, Locale.TAIWAN)
        val formatter = SimpleDateFormat(pattern)
        date = formatter.parse(this)
    }

    return date
}

fun String.isDate(format: String="yyy-MM-dd"): Boolean {
    try {
        val df = SimpleDateFormat(format)
        df.isLenient = false
        df.parse(this)
        return true
    } catch (e: ParseException) {
        return false
    }
}
fun String.isTime(format: String="HH:mm:ss"): Boolean {
    try {
        val df = SimpleDateFormat(format)
        df.isLenient = false
        df.parse(this)
        return true
    } catch (e: ParseException) {
        return false
    }
}
fun String.isDateTime(format: String="yyy-MM-dd HH:mm:ss"): Boolean {
    try {
        val df = SimpleDateFormat(format)
        df.isLenient = false
        df.parse(this)
        return true
    } catch (e: ParseException) {
        return false
    }
}

fun String.makeCall(activity: BaseActivity) {

    if (this.isNotEmpty()) {
        if (!activity.permissionExist(android.Manifest.permission.CALL_PHONE)){
            activity.requestPermission(arrayOf(android.Manifest.permission.CALL_PHONE), activity.REQUEST_PHONE_CALL)
        } else {
            activity.makeCall(this)
        }
    }
}

fun String.youtube(context: Context) {
    var id = this.getYoutubeIDFromChannel()
    var appUrl: String? = null
    var webUrl: String? = null
    if (id != null) { //channel
        appUrl = "vnd.youtube://user/channel/" + id
        webUrl = "https://youtube.com/channel/" + id
    } else {
        id = this.getYoutubeID()
        if (id != null) {
            appUrl = "vnd.youtube:" + id
            webUrl = this
        }
    }
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
    try {
        context.startActivity(appIntent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(webIntent)
    }
}

fun String.fb(context: Context) {
    var appUrl: String? = null
    val packageManager = context.packageManager
    try {
        val version = packageManager.getPackageInfo("com.facebook.katana", 0)
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            val versionCode = version.longVersionCode
            if (versionCode >= 3002850) {
                appUrl = "fb://facewebmodal/f?href=" + this
            } else {
                val pageID = this.getFBPageID()
                appUrl = "fb://page/" + pageID
            }
        } else {
            val versionCode = version.versionCode
            if (versionCode >= 3002850) {
                appUrl = "fb://facewebmodal/f?href=" + this
            } else {
                val pageID = this.getFBPageID()
                appUrl = "fb://page/" + pageID
            }
        }

    } catch (e: Exception) {

    }

    var appIntent: Intent? = null

    if (appUrl != null) {
        appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
    }
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
    if (appIntent != null) {
        try {
            context.startActivity(appIntent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    } else {
        context.startActivity(webIntent)
    }
}

fun String.line(context: Context) {
    val appUrl = "line://ti/p/@"+this
//    val appUrl = "line://ti/p/@ives9999"

    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
    try {
        context.startActivity(appIntent)
    } catch (e: ActivityNotFoundException) {
    }
}

fun String.website(context: Context) {
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
    try {
        context.startActivity(webIntent)
    } catch (e: ActivityNotFoundException) {
    }
}

fun String.email(context: Context) {
    val webUrl = "mailto:" + this
    val webIntent = Intent(Intent.ACTION_SENDTO, Uri.parse(webUrl))
    try {
        context.startActivity(webIntent)
    } catch (e: ActivityNotFoundException) {
    }
}

fun String.getYoutubeIDFromChannel(): String? {
    if (this.toLowerCase().contains("channel")) {
        val matches = this.reMatches("channel\\/(.*)", this);
//        for (match in matches) {
//            println(match)
//        }
        if (matches.size > 1) {
            return matches[1]
        }
    }
    return null
}
fun String.getYoutubeID(): String? {
    if (this.toLowerCase().contains("youtu\\.be")) {
        val matches = this.reMatches("youtu\\.be\\/(.*)", this);
//        for (match in matches) {
//            println(match)
//        }
        if (matches.size > 1) {
            return matches[1]
        }
    }
    if (this.toLowerCase().contains("watch")) {
        val matches = this.reMatches("watch\\?v=(.*)", this);
//        for (match in matches) {
//            println(match)
//        }
        if (matches.size > 1) {
            return matches[1]
        }
    }
    return null
}
fun String.getFBPageID(): String? {
    val matches = this.reMatches("facebook.com\\/(.*)", this);
    if (matches.size > 1) {
        return matches[1]
    }
    return null
}

fun String.image(context: Context, imageView: ImageView) {
    var path = this
    if (!path.startsWith("http://") && !path.startsWith("https://")) {
        path = BASE_URL + path
    }
    Picasso.with(context)
            .load(path)
            .placeholder(R.drawable.loading_square_120)
            .error(R.drawable.loading_square_120)
            .into(imageView)
}

fun String.toArray(): Array<String> {
    return this.split(",").toTypedArray()
}

fun String.isInt(): Boolean {
    if (toIntOrNull() == null) {
        return false
    }
    return true
}

fun Int.quotientAndRemainder(dividingBy: Int): Pair<Int, Int> {
    val q: Int = this / dividingBy
    val r: Int = this % dividingBy
    return Pair(q, r)
}

fun Int.formattedWithSeparator(): String {
    return NumberFormat.getNumberInstance().format(this)
}

fun Int.dpToPx(context: Context): Int {

    return (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        context.displayMetrics
    )).toInt()
}

fun Date.toMyString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(this)
}
fun Date.getY(): Int {
    val formatter = SimpleDateFormat("yyyy")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.getm(): Int {
    val formatter = SimpleDateFormat("MM")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.getd(): Int {
    val formatter = SimpleDateFormat("dd")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.getH(): Int {
    val formatter = SimpleDateFormat("HH")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.geti(): Int {
    val formatter = SimpleDateFormat("mm")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.gets(): Int {
    val formatter = SimpleDateFormat("ss")
    val res = formatter.format(this).toInt()
    return res
}

fun Date.dateToWeekday(): Int {
    val c: Calendar = Calendar.getInstance()
    //month: 0 for 1月
    c.set(this.getY(), this.getm()-1, this.getd())
    //SUNDAY is 1, MONDAY is 2
    var weekday = c.get(Calendar.DAY_OF_WEEK)-1
    if (weekday == 0) { weekday = 7}

    return weekday
}

fun Date.dateToWeekdayForChinese(): String {

    val weekday = this.dateToWeekday()
    var res: String = "一"
    if (weekday == 1) {
        res = "一"
    } else if (weekday == 2) {
        res = "二"
    } else if (weekday == 3) {
        res = "三"
    } else if (weekday == 4) {
        res = "四"
    } else if (weekday == 5) {
        res = "五"
    } else if (weekday == 6) {
        res = "六"
    } else if (weekday == 7) {
        res = "日"
    }

    return res
}

fun Date.timeIntervalSince(stop: Date): Long {
    val diffInMs = this.time - stop.time
    return TimeUnit.MILLISECONDS.toSeconds(diffInMs)
}

fun Date.getMonthDays(_y: Int?=null, _m: Int?=null): Int {
    var y = _y
    if (y == null) {
        y = this.getY()
    }
    var m = _m
    if (m == null) {
        m = this.getm()
    }
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, y)
    calendar.set(Calendar.MONTH, m)
    val d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    return d
}

fun HashMap<String, Any>.toJSONString(): String {

    var json: String = "{"
    val tmps: ArrayList<String> = arrayListOf()
    for ((key, value) in this) {
        var str: String = ""
        val type = value::class.simpleName!!
        if (type == "Array") {
            val values: Array<String> = value as Array<String>
            val str1: String = values.joinToString(",")
            str = "\"$key\":[$str1]"
        } else {
            str = "\"$key\":\"$value\""
        }
        tmps.add(str)
    }
    json += tmps.joinToString(",")
    json += "}"

    return json
}

fun <T1, T2> Map<T1, T2>.print() {
    for ((key, value) in this) {
        //println("${key} => ${value}")
    }
}

fun <T> List<T>.print() {
    for (value in this) {
        //println("${value}")
    }
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm != null && currentFocus != null) {
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0);
    }
}

fun ViewGroup.showImages(images: ArrayList<String>, context: Context) {
    images.forEachIndexed { _, s ->
        val imageView: ImageView = ImageView(context)
        this.addView(imageView)
        imageView.scaleType = ImageView.ScaleType.FIT_START
        imageView.adjustViewBounds = true
        val lp = LinearLayout.LayoutParams(this.layoutParams.width, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 0, 0, 16)
        imageView.layoutParams = lp
        s.image(context, imageView)
    }
}

fun ImageView.setImage(name: String) {
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    this.setImageResource(id)
}

fun View.getIDString(): String {
    return this.resources.getResourceName(this.id)
}

fun ViewGroup.mask(context: Context): LinearLayout {

    val mask = LinearLayout(context)
    mask.id = R.id.MyMask
    mask.layoutParams = LinearLayout.LayoutParams(this.width, this.height)
    mask.backgroundColor = Color.parseColor("#888888")
    //0是完全透明
    mask.alpha = 0.9f
    mask.setOnClickListener {
        this.unmask()
    }
    this.addView(mask)

    return mask
}

fun ViewGroup.unmask() {

    val mask = this.findViewById<ViewGroup>(R.id.MyMask)
    mask.removeAllViews()
    this.removeView(mask)
}

fun ViewGroup.blackView(context: Context, left: Int, top: Int, width: Int, height: Int): RelativeLayout {

    val blackView = RelativeLayout(context)
//    println(width)
//    println(height)
//    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height)
    lp.setMargins(left, top, 0, 0)
    blackView.layoutParams = lp
//    blackView.orientation = LinearLayout.VERTICAL
    blackView.backgroundColor = Color.BLACK
    blackView.alpha = 1f
    this.addView(blackView)

    return blackView
}

fun ViewGroup.tableView(context: Context, top: Int = 0, bottom: Int = 0): RecyclerView {

    val tableView = RecyclerView(context)
    tableView.id = R.id.SearchRecycleItem
    val lp1 = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        val lp1 = RecyclerView.LayoutParams(w - (2 * padding), 1000)
    lp1.setMargins(top, 0, 0, bottom)
    tableView.layoutParams = lp1
    tableView.layoutManager = LinearLayoutManager(context)
    tableView.backgroundColor = Color.TRANSPARENT
    this.addView(tableView)

    return tableView
}

fun ViewGroup.buttonPanel(context: Context, height: Int): LinearLayout {
    val view = LinearLayout(context)
    val lp = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    view.layoutParams = lp
    view.setPadding(20, 0, 20, 0)
    val color = ContextCompat.getColor(context, R.color.SEARCH_BACKGROUND)
    view.backgroundColor = color
    view.gravity = Gravity.CENTER
    view.orientation = LinearLayout.VERTICAL
    this.addView(view)

    return view
}

fun ViewGroup.submitButton(context: Context, height: Int, click: ()->Unit): Button {

    val a = context as BaseActivity
    val view = a.layoutInflater.inflate(R.layout.submit_button, null) as Button
    val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    //lp.weight = 1F
    lp.setMargins(32, 12, 32, 24)
    view.layoutParams = lp
    view.onClick {
        click()
    }
    this.addView(view)

    return view
}

fun ViewGroup.cancelButton(context: Context, height: Int, click: ()->Unit): Button {

    val a = context as BaseActivity
    val view = a.layoutInflater.inflate(R.layout.cancel_button, null) as Button
    val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    //lp.weight = 1F
    lp.setMargins(48, 24, 48, 12)
    view.layoutParams = lp
    view.onClick {
        click()
    }
    this.addView(view)

    return view
}

fun ViewGroup.setInfo(context: Context, info: String): TextView {

    val label: TextView = TextView(context)
    val lp: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
    lp.topMargin = 400
    label.layoutParams = lp
    label.gravity = Gravity.CENTER_HORIZONTAL
    label.textColor = ContextCompat.getColor(context, R.color.MY_WHITE)
    label.text = info

    this.addView(label)

    return label
}

fun TextView.selected() {
    val checkedColor = ContextCompat.getColor(context, R.color.MY_GREEN)
    this.setTextColor(checkedColor)
}

fun TextView.unSelected() {
    val uncheckedColor = ContextCompat.getColor(context, R.color.MY_WHITE)
    this.setTextColor(uncheckedColor)
}

fun TextView.setTextLook(size: Float, color: Int) {
    this.textSize = size
    this.setTextLookColor(color)
}

fun TextView.setTextLookColor(color: Int) {
    this.textColor = ContextCompat.getColor(context, color)
}

fun Button.setLook(backgroundColor: Int, textColor: Int) {
    this.backgroundColor = ContextCompat.getColor(context, backgroundColor)
    this.textColor = ContextCompat.getColor(context, textColor)
}


fun SharedPreferences.dump() {
    this.all.map {
        println("${it.key}=>${it.value}")
    }
}

fun SharedPreferences.has(key: String): Boolean {
    return this.contains(key)
}

fun SharedPreferences.resetMember() {
    for ((key, value) in MEMBER_ARRAY) {
        val hashMap: HashMap<String, String> = value as HashMap<String, String>
        val type: String = hashMap["type"] as String
        when (type) {
            "String" -> this.edit().putString(key, "").apply()
            "Int" -> this.edit().putInt(key, 0).apply()
            "Boolean" -> this.edit().putBoolean(key, false).apply()
        }
    }
    this.edit().putInt("id", -1).apply()
}

// return is [
//             ["id": "5", "name": "新北市"],
//             ["id": "6", "name": "台北市"]
//   ]
fun SharedPreferences.getAllCitys(): ArrayList<HashMap<String, String>> {
    val tmps: ArrayList<HashMap<String, String>> = arrayListOf()
    //this.edit().remove("citys").commit()
    val citys_string: String = this.getString("citys", "") as String
    //println(citys_string)

    if (citys_string.length > 0) {
        val jsonArray: JsonArray = JsonParser.parseString(citys_string).asJsonArray
//    println(jsonArray)
        val gson = Gson()
        for (_city in jsonArray) {
            val city: City = gson.fromJson(_city, City::class.java)
            //println("name:${city.name} => id:${city.id}")
            tmps.add(hashMapOf("id" to city.id.toString(), "name" to city.name))
        }
    }

    return tmps
}

// return is [
//             "52": [
//                     "id": "52",
//                     "name": ["新北市"],
//                     "rows": [
//                          ["id": "5", "name": "中和"],
//                          ["id": "6", "name": "永和"]
//                     ]
//                  ]
//           ]
fun SharedPreferences.getAreasFromCity(city_id: Int): ArrayList<HashMap<String, String>> {

    val allAreas = this.getAllAreas()
    for ((key, value) in allAreas) {
        if (key.toInt() == city_id) {
            val rows: ArrayList<HashMap<String, String>> = arrayListOf()
            if (value.containsKey("rows")) {
                val _rows = value["rows"] as ArrayList<*>
                for (_row in _rows) {
                    val row: HashMap<String, String> = hashMapOf()
                    val tmps = _row as LinkedTreeMap<*, *>
                    for ((key1, value1) in tmps) {
                        val _key1 = key1.toString()
                        val _value1 = value1.toString()
                        row[_key1] = _value1
                    }
                    rows.add(row)
                }
            }
            return rows
        }
    }

    val tmps: ArrayList<HashMap<String, String>> = arrayListOf()
    return tmps
}

fun SharedPreferences.getAllAreas(): HashMap<String, HashMap<String, Any>> {

    val areas_string: String = this.getString("areas", "") as String
    //println(areas_string)

    if (areas_string.length > 0) {
        val type = object : TypeToken<HashMap<String, HashMap<String, Any>>>() {}.type
        val jsonObject = Gson().fromJson<HashMap<String, HashMap<String, Any>>>(areas_string, type)
        return jsonObject
    } else {
        val tmps: HashMap<String, HashMap<String, Any>> = hashMapOf()
        return tmps
    }
}

object Global {

    var message: String = ""

    val weekdays: ArrayList<HashMap<String, Any>> = arrayListOf(
            hashMapOf("value" to 1,"text" to "星期一","simple_text" to "一","checked" to false,"title" to "星期一"),
            hashMapOf("value" to 2,"text" to "星期二","simple_text" to "二","checked" to false,"title" to "星期二"),
            hashMapOf("value" to 3,"text" to "星期三","simple_text" to "三","checked" to false,"title" to "星期三"),
            hashMapOf("value" to 4,"text" to "星期四","simple_text" to "四","checked" to false,"title" to "星期四"),
            hashMapOf("value" to 5,"text" to "星期五","simple_text" to "五","checked" to false,"title" to "星期五"),
            hashMapOf("value" to 6,"text" to "星期六","simple_text" to "六","checked" to false,"title" to "星期六"),
            hashMapOf("value" to 7,"text" to "星期日","simple_text" to "日","checked" to false,"title" to "星期七")
    )

    fun today(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
        return sdf.format(Date())
    }

    fun nowTime(): String {
        val sdf = SimpleDateFormat("hh:mm:ss", Locale.TAIWAN)
        return sdf.format(Date())
    }

    fun makeTimes(start_time: String="07:00", end_time: String="23:00", interval: Int=60): ArrayList<String> {

        val allTimes: ArrayList<String> = arrayListOf()
        var s = start_time.toDateTime("HH:mm")
        val e = end_time.toDateTime("HH:mm")
        if (s != null && e != null) {
            allTimes.add(start_time)
            while (s!!.compareTo(e) < 0) {
                val cal = Calendar.getInstance()
                cal.time = s
                cal.add(Calendar.MINUTE, interval)
                s = cal.time
                allTimes.add(s.toMyString("HH:mm"))
            }
        }

        return allTimes
    }

    fun weekdaysParse(value: Int): ArrayList<Int> {
        val arr: ArrayList<Int> = arrayListOf()
        if (value > 0 && value < 128) {
            for (i in 0..6) {
                val two = 2.toFloat().pow(i).toInt()
                if ((value and two) > 0) {
                    arr.add(i + 1)
                }
            }
        }

        return arr
    }

    fun weekdaysToDBValue(arr: ArrayList<Int>): Int {
        var res: Int = 0
        for (value in arr) {
            if (value >= 0 && value <= 6) {
                res = res or (2.toFloat().pow(value - 1).toInt())
            }
        }

        return res
    }

    fun getMonthLastDay(year: Int, month: Int): Int {

        return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun getCitys(): ArrayList<City> {

        val citys: ArrayList<City> = arrayListOf()
        for (zone in Zone.zones) {
            if (zone.containsKey("parent")) {
                val parent: Int = zone["parent"] as Int
                if (parent == 0) {
                    var id: Int? = null
                    var name: String? = null
                    if (zone.containsKey("id")) {
                        id = zone["id"] as Int
                    }
                    if (zone.containsKey("name")) {
                        name = zone["name"] as String
                    }
                    if (id != null && name != null) {
                        val city = City(id, name)
                        citys.add(city)
                    }
                }
            }
        }

        return citys
    }

    fun getAreasByCityID(city_id: Int): ArrayList<Area> {

        val areas: ArrayList<Area> = arrayListOf()
        for (zone in Zone.zones) {
            if (zone.containsKey("parent")) {
                val parent: Int = zone["parent"] as Int
                if (parent == city_id) {
                    var id: Int? = null
                    var name: String? = null
                    if (zone.containsKey("id")) {
                        id = zone["id"] as Int
                    }
                    if (zone.containsKey("name")) {
                        name = zone["name"] as String
                    }
                    if (id != null && name != null) {
                        val area = Area(id, name)
                        areas.add(area)
                    }
                }
            }
        }

        return areas
    }

    fun zoneIDToName(zone_id: Int): String {
        var value = ""
        for (zone in Zone.zones) {
            val id = zone["id"] as Int
            if (id == zone_id) {
                value = zone["name"] as String
                break
            }
        }
        return value
    }
}

class IndexPath(val section: Int, val row: Int){}

object Loading {
    val alpha = 0.8f
    val duration: Long = 100
    fun show(mask: View) {
        mask.alpha = 0f
        mask.visibility = View.VISIBLE
        mask.animate().setDuration(duration).alpha(alpha).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                mask.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }
    fun hide(mask: View) {
        mask.visibility = View.VISIBLE
        mask.animate().setDuration(duration).alpha(0f).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                mask.visibility = View.INVISIBLE
            }
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }
}

object Mask {
    val alpha = 0.8f
    val duration: Long = 100
    fun show(view: View) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate().setDuration(duration).alpha(alpha).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                view.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }
    fun hide(mask: View) {
        mask.visibility = View.VISIBLE
        mask.animate().setDuration(duration).alpha(0f).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                mask.visibility = View.INVISIBLE
            }
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }
}

object Alert {
    fun show(context: Context, title: String, msg: String): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定") { Interface, j ->

        }
        alert.show()
        return alert
    }
    fun show(context: Context, title: String, msg: String, showCloseButton:Boolean=false, buttonTitle:String, buttonAction: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        if (showCloseButton) {
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "關閉") { Interface, j ->
                Interface.cancel()
            }
        }
        alert.setButton(AlertDialog.BUTTON_POSITIVE, buttonTitle) { Interface, j ->
            buttonAction()
        }
        alert.show()
        return alert
    }
    fun show(context: Context, title: String, msg: String, closeButtonTitle:String,buttonTitle:String, buttonAction: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, closeButtonTitle) { Interface, j ->
            Interface.cancel()
        }
        alert.setButton(AlertDialog.BUTTON_POSITIVE, buttonTitle) { Interface, j ->
            buttonAction()
        }
        alert.show()
        return alert
    }
    fun show(context: Context, title: String, msg: String, ok: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定") { Interface, j ->
            ok()
        }
        alert.show()
        return alert
    }
    fun warning(context: Context, msg: String) {
        this.show(context, "警告", msg)
    }
    fun update(context: Context, action: String, back: ()->Unit): AlertDialog {
        val alert = _show(context, "成功", "新增 / 修改 成功")
        if (action == "INSERT") {
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "確定") { Interface, j ->
                Interface.cancel()
                back()
            }
        } else {
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "回上一頁") { Interface, j ->
                Interface.cancel()
                back()
            }
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "繼續修改") { Interface, j ->
                Interface.cancel()
            }
        }
        alert.show()
        return alert
    }
    fun delete(context: Context, del: ()->Unit): AlertDialog {
        val alert = _show(context, "警告", "是否真的要刪除？")
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定") { Interface, j ->
            Interface.cancel()
            del()
        }
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "取消") { Interface, j ->
            Interface.cancel()
        }
        alert.show()
        return alert
    }
    private fun _show(context: Context, title: String, msg: String): AlertDialog {
        val alert = AlertDialog.Builder(context).create()
        alert.setTitle(title)
        alert.setMessage(msg)

        return alert
    }
}

class DrawLine(context: Context, val startX: Float, val startY: Float, val stopX: Float, val stopY: Float, val color: Int = Color.BLACK, val stroke: Float = 3f) : View(context) {
    val paint: Paint = Paint()

    init {
        paint.color = color
        paint.strokeWidth = stroke
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(startX, startY, stopX, stopY, paint)
    }
}

inline fun <reified T> Any.getField(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodError) {
        println(e.localizedMessage)
        null
    }
}

inline fun <reified T: Tables> jsonToModels(jsonString: String): T? {

    var t: T? = null
    try {
        t = Gson().fromJson<T>(jsonString, T::class.java)
    } catch (e: java.lang.Exception) {
        Global.message = e.localizedMessage
    }

    return t
}

inline fun <reified T: Tables2<U>, U> jsonToModels2(jsonString: String, typeToken: Type): T? {

    var t: T? = null
    println(t)
    try {
        t = Gson().fromJson<T>(jsonString, typeToken)
    } catch (e: java.lang.Exception) {
        Global.message = e.localizedMessage
        println(e.localizedMessage)
        val e1 = e.localizedMessage
    }

    return t
}

inline fun <reified T: Table> jsonToModel(jsonString: String): T? {

    var t: T? = null
    try {
        t = Gson().fromJson<T>(jsonString, T::class.java)
    } catch (e: java.lang.Exception) {
        Global.message = e.localizedMessage
        println(e.localizedMessage)
    }

    return t
}

inline fun <reified T: Table> refreshOne(): T? {

    val t = jsonToModel<T>("aaa")

    return t
}

inline fun <reified T : Any> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        null
    }
}

infix fun <T: Any?> Boolean?.then(block: () -> T): T? = if (this == true) block() else null

infix fun Map<String, String>.mergeWith(anotherMap: Map<String, String>): Map<String, String> {
    val result = this.toMutableMap()
    anotherMap.forEach {
        var value = result[it.key]
        value = if (value == null || value == it.value) it.value else value + ", ${it.value}"
        result[it.key] = value
    }
    return result
}

//fun getFragment(activity: BaseActivity, able_type: String): TabFragment? {
//    val frags = activity.supportFragmentManager.fragments
//    var _frag: TabFragment? = null
//    for (frag in frags) {
//        if (able_type == "arena" && frag::class == ArenaFragment::class) {
//            _frag = frag as ArenaFragment
//            break
//        }
//        if (able_type == "team" && frag::class == TempPlayFragment::class) {
//            _frag = frag as TempPlayFragment
//            break
//        }
//        if (able_type == "course" && frag::class == CourseFragment::class) {
//            _frag = frag as CourseFragment
//            break
//        }
//    }
//
//    return _frag
//}


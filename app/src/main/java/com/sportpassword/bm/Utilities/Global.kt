package com.sportpassword.bm.Utilities

import android.animation.Animator
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.Fragments.CoachFragment
import com.sportpassword.bm.Fragments.CourseFragment
import com.sportpassword.bm.Fragments.TabFragment
import com.sportpassword.bm.Fragments.TeamFragment
import com.sportpassword.bm.Models.Area
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.Tables
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.DataService
import com.squareup.picasso.Picasso
import org.jetbrains.anko.makeCall
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

    companion object: MYENUM<PRICE_UNIT>() {

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
    shipping("shipping", "出貨中"),
    payment("payment", "完成付款"),
    complete("complete", "訂單完成"),
    cancel("cancel", "訂單取消");

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

enum class SHIPPING_PROCESS(val englishName: String, val chineseName: String) {
    normal("normal", "準備中"),
    shipping("shipping", "出貨中"),
    store("store", "商品已到便利商店"),
    complete("complete", "已完成取貨"),
    back("back", "貨物退回");

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
    val type = this.toLowerCase()
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

fun Int.quotientAndRemainder(dividingBy: Int): Pair<Int, Int> {
    val q: Int = this / dividingBy
    val r: Int = this % dividingBy
    return Pair(q, r)
}

fun Int.formattedWithSeparator(): String {
    return NumberFormat.getNumberInstance().format(this)
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

fun TextView.selected() {
    val checkedColor = ContextCompat.getColor(context, R.color.MY_GREEN)
    this.setTextColor(checkedColor)
}

fun TextView.unSelected() {
    val uncheckedColor = ContextCompat.getColor(context, R.color.WHITE)
    this.setTextColor(uncheckedColor)
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
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定", { Interface, j ->

        })
        alert.show()
        return alert
    }
    fun show(context: Context, title: String, msg: String, showCloseButton:Boolean=false,buttonTitle:String, buttonAction: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        if (showCloseButton) {
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "關閉", { Interface, j ->
                Interface.cancel()
            })
        }
        alert.setButton(AlertDialog.BUTTON_POSITIVE, buttonTitle, { Interface, j ->
            buttonAction()
        })
        alert.show()
        return alert
    }
    fun show(context: Context, title: String, msg: String, closeButtonTitle:String,buttonTitle:String, buttonAction: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, closeButtonTitle, { Interface, j ->
            Interface.cancel()
        })
        alert.setButton(AlertDialog.BUTTON_POSITIVE, buttonTitle, { Interface, j ->
            buttonAction()
        })
        alert.show()
        return alert
    }
    fun show(context: Context, title: String, msg: String, ok: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定", { Interface, j ->
            ok()
        })
        alert.show()
        return alert
    }
    fun warning(context: Context, msg: String) {
        this.show(context, "警告", msg)
    }
    fun update(context: Context, action: String, back: ()->Unit): AlertDialog {
        val alert = _show(context, "成功", "新增 / 修改 成功")
        if (action == "INSERT") {
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "確定", { Interface, j ->
                Interface.cancel()
                back()
            })
        } else {
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "回上一頁", { Interface, j ->
                Interface.cancel()
                back()
            })
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "繼續修改", { Interface, j ->
                Interface.cancel()
            })
        }
        alert.show()
        return alert
    }
    fun delete(context: Context, del: ()->Unit): AlertDialog {
        val alert = _show(context, "警告", "是否真的要刪除？")
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定", { Interface, j ->
            Interface.cancel()
            del()
        })
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "取消", { Interface, j ->
            Interface.cancel()
        })
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

inline fun <reified T: Table> jsonToModel(jsonString: String): T? {

    var t: T? = null
    try {
        t = Gson().fromJson<T>(jsonString, T::class.java)
    } catch (e: java.lang.Exception) {
        Global.message = e.localizedMessage
    }

    return t
}

inline fun <reified T: Table> refreshOne(): T? {

    val t = jsonToModel<T>("aaa")

    return t
}

fun getFragment(activity: BaseActivity, able_type: String): TabFragment? {
    val frags = activity.supportFragmentManager.fragments
    var _frag: TabFragment? = null
    for (frag in frags) {
        if (able_type == "coach" && frag::class == CoachFragment::class) {
            _frag = frag as CoachFragment
            break
        }
        if (able_type == "team" && frag::class == TeamFragment::class) {
            _frag = frag as TeamFragment
            break
        }
        if (able_type == "course" && frag::class == CourseFragment::class) {
            _frag = frag as CourseFragment
            break
        }
    }

    return _frag
}


package com.sportpassword.bm.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.BASE_URL
import com.squareup.picasso.Picasso
import org.jetbrains.anko.makeCall
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    Picasso.get()
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
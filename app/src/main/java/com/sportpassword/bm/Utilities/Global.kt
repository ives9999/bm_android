package com.sportpassword.bm.Utilities

import android.animation.Animator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.sportpassword.bm.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

enum class MYCOLOR(val value: Int) {
//    danger("danger"), success("success"), primary("primary"), warning("warning"),
//    info("info"), gray("gray")
    danger(0xc12e2a),
    success(0x36c6d3),
    primary(0x337ab7),
    warning(0xF1C40F),
    info(0x659be0),
    gray(0xe1e1e1);


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
        fun from(value: String): MYCOLOR {
            when (value) {
                "danger" -> return danger
                "success" -> return success
                "primary" -> return primary
                "warning" -> return warning
                "info" -> return info
                "gray" -> return gray
            }
            return success
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

enum class TEXT_INPUT_TYPE(val value: String) {
    temp_play("臨打"),
    charge("收費標準"),
    content("詳細內容"),
    exp("經歷"),
    feat("比賽成績"),
    license("證照"),
    timetable_coach("課程說明")
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
fun String.noSec(): String {
    val arr: List<String> = this.split(":")
    var res: String = this
    if (arr.size > 2) {
        res = "${arr[0]}:${arr[1]}"
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
fun String.toDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date {
    //val formatter = DateTimeFormatter.ofPattern(pattern, Locale.TAIWAN)
    val formatter = SimpleDateFormat(pattern)
    val date = formatter.parse(this)

    return date
}

fun Date.toMyString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(this)
}
fun Date.getH(): Int {
    val formatter = SimpleDateFormat("HH")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.getm(): Int {
    val formatter = SimpleDateFormat("mm")
    val res = formatter.format(this).toInt()
    return res
}

fun <T1, T2> Map<T1, T2>.print() {
    for ((key, value) in this) {
        println("${key} => ${value}")
    }
}

fun <T> List<T>.print() {
    for (value in this) {
        println("${value}")
    }
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm != null && currentFocus != null) {
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0);
    }
}
fun ImageView.setImage(name: String) {
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    this.setImageResource(id)
}

object Global {
    val weekdays: ArrayList<HashMap<String, Any>> = arrayListOf(
            hashMapOf("value" to 1,"text" to "星期一","simple_text" to "一","checked" to false),
            hashMapOf("value" to 2,"text" to "星期二","simple_text" to "二","checked" to false),
            hashMapOf("value" to 3,"text" to "星期三","simple_text" to "三","checked" to false),
            hashMapOf("value" to 4,"text" to "星期四","simple_text" to "四","checked" to false),
            hashMapOf("value" to 5,"text" to "星期五","simple_text" to "五","checked" to false),
            hashMapOf("value" to 6,"text" to "星期六","simple_text" to "六","checked" to false),
            hashMapOf("value" to 7,"text" to "星期日","simple_text" to "日","checked" to false)
    )
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
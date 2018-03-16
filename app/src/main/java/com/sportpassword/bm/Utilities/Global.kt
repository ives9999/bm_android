package com.sportpassword.bm.Utilities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R

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

object Loading {
    fun show(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val rl = RelativeLayout(context)
        val rl_lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        rl.layoutParams = rl_lp


        val loadingImg = ProgressBar(context)
        loadingImg.id = R.id.loadingImgID
        val color = ContextCompat.getColor(context, R.color.MY_GREEN)
        loadingImg.indeterminateDrawable.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)

        val loadingText: TextView = TextView(context)
        loadingText.id = R.id.loadingTextID
        loadingText.text = LOADING

        val p1 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        val p2 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        //p1.addRule(RelativeLayout.ABOVE, loadingImg.id)
        p2.addRule(RelativeLayout.BELOW, loadingImg.id)
        val _20: Int = (context.resources.getDimension(R.dimen.loadingTextMarginTop)).toInt()
        p2.setMargins(0, _20, 0, 0)

        rl.addView(loadingImg, p1)
        rl.addView(loadingText, p2)

        val p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.addContentView(rl, p)
        dialog.show()
        return dialog
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
    fun show(context: Context, title: String, msg: String, ok: ()->Unit): AlertDialog {
        val alert = _show(context, title, msg)
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "確定", { Interface, j ->
            ok()
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
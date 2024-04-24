package com.sportpassword.bm.extensions

import android.app.AlertDialog
import android.content.Context

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
    fun show(context: Context, title: String, msg: String, closeButtonTitle:String, buttonTitle:String, buttonAction: ()->Unit): AlertDialog {
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
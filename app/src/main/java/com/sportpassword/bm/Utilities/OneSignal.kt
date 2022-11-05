package com.sportpassword.bm.Utilities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.sportpassword.bm.App
import com.sportpassword.bm.Controllers.ShowPNVC
import com.sportpassword.bm.member
import org.json.JSONArray
import org.json.JSONObject

import android.util.Log;
import androidx.core.app.NotificationCompat

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler;
import com.sportpassword.bm.Controllers.BaseActivity
import org.jetbrains.anko.runOnUiThread
import java.math.BigInteger

class NotificationServiceExtension : OSRemoteNotificationReceivedHandler {
    override fun remoteNotificationReceived(
        context: Context,
        notificationReceivedEvent: OSNotificationReceivedEvent
    ) {
        val notification = notificationReceivedEvent.notification

        // Example of modifying the notification's accent color
        val mutableNotification = notification.mutableCopy()
        mutableNotification.setExtender { builder: NotificationCompat.Builder ->
            // Sets the accent color to Green on Android 5+ devices.
            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
            builder.color = BigInteger("FF00FF00", 16).toInt()
            // Sets the notification Title to Red
            val spannableTitle: Spannable = SpannableString(notification.title)
            spannableTitle.setSpan(
                ForegroundColorSpan(Color.RED),
                0,
                notification.title.length,
                0
            )
            builder.setContentTitle(spannableTitle)
            // Sets the notification Body to Blue
            val spannableBody: Spannable = SpannableString(notification.body)
            spannableBody.setSpan(
                ForegroundColorSpan(Color.BLUE),
                0,
                notification.body.length,
                0
            )
            builder.setContentText(spannableBody)
            //Force remove push from Notification Center after 30 seconds
            builder.setTimeoutAfter(30000)
            builder
        }
        val data = notification.additionalData
        Log.i("OneSignalExample", "Received Notification Data: $data")

        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        notificationReceivedEvent.complete(mutableNotification)
    }
}

class MyOneSignal {

    companion object {

        val session: SharedPreferences = App.ctx!!.getSharedPreferences(SESSION_FILENAME, 0)
        //var context: Context? = null

        fun getOneSignalHandler(activity: BaseActivity?, result: OSNotification, isForeground: Boolean = false) {
            val data = result.additionalData
            var pnID = "0"
            if (data != null) {
                pnID = getServerPNID(data)
            }

            val id = result.notificationId
            val title = result.title
            val content = result.body

            val isShow: Boolean = (isForeground) then { true } ?: false

            save(id.toString(), title, content, pnID, isShow)

            activity?.runOnUiThread {
                activity.info(content)
            }
        }

//        fun openHandler(context: Context, result: OSNotificationOpenedResult) {
//
//            //this.context = context
//            val actionType = result.action.type
//
//            val data = result.notification.additionalData
//            val id = result.notification.notificationId
//            val title = result.notification.title
//            val content = result.notification.body
//
//            var pnID = "0"
//            if (data != null) {
//                pnID = getServerPNID(data)
//            }
//
//            save(id.toString(), title, content, pnID)
//
//            toShowPNVC(context)
//        }
//
//        fun showInForegroundHandler(context: Context, notificationReceivedEvent: OSNotificationReceivedEvent) {
//
//            val notification = notificationReceivedEvent.notification
//
//            val data = notification.additionalData
//
//            val id = notification.androidNotificationId
//            val title = notification.title
//            val content = notification.body
//
//            val smallIcon = notification.smallIcon
//            val largeIcon = notification.largeIcon
//            val bigPicture = notification.bigPicture
//            val smallIconAccentColor = notification.smallIconAccentColor
//            val sound = notification.sound
//            val ledColor = notification.ledColor
//            val lockScreenVisibility = notification.lockScreenVisibility
//            val groupKey = notification.groupKey
//            val groupMessage = notification.groupMessage
//            val fromProjectNumber = notification.fromProjectNumber
//            val rawPayload = notification.rawPayload
//
//            var pnID = "0"
//            if (data != null) {
//                pnID = getServerPNID(data)
//            }
//
//            save(id.toString(), title, content, pnID)
//
////            notificationReceivedEvent.complete(notification)
////            val builder = AlertDialog.Builder(context)
////            builder.setTitle(title)
////            builder.setMessage(content)
////
////            runOnUiThread {
////                builder.show()
////            }
//            //toShowPNVC(context)
//        }

        fun toShowPNVC(context: Context) {
            //val launchUrl = result.notification.launchURL
            //if (launchUrl != null) {
            val intent = Intent(context, ShowPNVC::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
            //intent.putExtra("openURL", launchUrl)
            context.startActivity(intent)
            //}
        }

        fun save(id: String, title: String?, content: String, pnID: String, isShow: Boolean = false) {
            val pnObj = JSONObject()
            pnObj.put("id", id)
            if (title != null) {
                pnObj.put("title", title)
            }
            pnObj.put("content", content)
            pnObj.put("pnid", pnID)
            pnObj.put("sort_order", System.currentTimeMillis())
            pnObj.put("isShow", isShow)

            var pnArr: JSONArray? = null
            if (session.contains("pn")) {
                val pnStr = session.getString("pn", "")!!
                pnArr = JSONArray(pnStr)
            } else {
                pnArr = JSONArray()
            }
            if (!isExist(pnArr, id)) {
                pnArr.put(pnObj)
            }
            session.edit().putString("pn", pnArr.toString()).apply()
        }

        fun remove(id: String) {
            var pnArr: JSONArray? = null
            if (session.contains("pn")) {
                val pnStr = session.getString("pn", "")!!
                pnArr = JSONArray(pnStr)
                for (i in 0..pnArr.length()-1) {
                    val obj = pnArr[i] as JSONObject
                    val id1 = obj.getString("id")
                    if (id == id1) {
                        pnArr.remove(i)
                        break
                    }
                }
                session.edit().putString("pn", pnArr.toString()).apply()
            }
        }

        fun clear() {
            session.edit().remove("pn").apply()
        }

        fun getAllRows(): JSONArray {
            val res: JSONArray = JSONArray()
            val pnStr = session.getString("pn", "")!!
            var pnArr: JSONArray = JSONArray()
            if (pnStr.isNotEmpty()) {
                pnArr = JSONArray(pnStr)
            }

            return pnArr
        }

        fun updateAll(rows: JSONArray) {
            session.edit().putString("pn", rows.toString()).apply()
        }

        fun update(id: String, _row: JSONObject) {
            val rows: JSONArray = getAllRows()
            val rows1: JSONArray = JSONArray()
            for (i in 0 until rows.length()) {
                var row: JSONObject = rows.getJSONObject(i)
                if (id == row.getString("id")) {
                   row = _row
                }
                rows1.put(row)
            }

            updateAll(rows1)
        }

        fun getUnShowRows(): JSONArray {

            val res: JSONArray = JSONArray()
            val pnArr = getAllRows()
            for (i in 0 until pnArr.length()) {
                val row: JSONObject = pnArr.getJSONObject(i)
                if (!row.getBoolean("isShow")) {
                    res.put(row)
                }
            }

            return res
        }

        fun dump() {
            val pnStr = session.getString("pn", "")!!
            if (pnStr.isNotEmpty()) {
                val pnArr = JSONArray(pnStr)
                for (i in 0 until pnArr.length()) {
                    val row: JSONObject = pnArr.getJSONObject(i)
                    println(row)
                }
            }
        }

        fun isExist(arr: JSONArray, id: String): Boolean {
            var b: Boolean = false
            for (i in 0..arr.length()-1) {
                val obj = arr[i] as JSONObject
                val id1 = obj.getString("id")
                if (id == id1) {
                    b = true
                    break
                }
            }

            return b
        }

        fun getServerPNID(data: JSONObject): String {
            val member_id = member.id
            var id = "0"
            val keys = data.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = data.getString(key).toIntOrNull()
                if (value != null) {
                    if (value == member_id) {
                        id = key
                    }
                }
            }

            return id
        }
    }
}


















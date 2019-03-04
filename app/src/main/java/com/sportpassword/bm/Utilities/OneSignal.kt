package com.sportpassword.bm.Utilities

import android.content.Intent
import android.content.SharedPreferences
import com.onesignal.OSNotification
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.sportpassword.bm.App
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.Controllers.ShowPNVC
import org.json.JSONArray
import org.json.JSONObject


class MyNotificationOpenedHandler: OneSignal.NotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenResult) {
//        println("receive PN")
        val actionType = result.action.type
        val data = result.notification.payload.additionalData
        val id = result.notification.payload.notificationID
        val title = result.notification.payload.title
        val content = result.notification.payload.body

//        println("OpenedHandler title: $title")
//        println("OpenedHandler id: $id")

        val customKey = data?.optString("customkey", null)
        if (customKey != null) {
//            println("OpenedHandler customkey set with value: $customKey")
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
//            println("OpenedHandler Button pressed with id: ${result.action.actionID}")
        }

        MyOneSignal.save(id, title, content)
        val intent = Intent(App.instance, ShowPNVC::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.putExtra("id", id)
//        intent.putExtra("title", title)
//        intent.putExtra("content", content)
        App.instance.startActivity(intent)
    }
}

class MyNotificationReceivedHandler: OneSignal.NotificationReceivedHandler {

    override fun notificationReceived(notification: OSNotification) {
        val data = notification.payload.additionalData
        val id = notification.payload.notificationID
        val title = notification.payload.title
        val body = notification.payload.body
        val smallIcon = notification.payload.smallIcon
        val largeIcon = notification.payload.largeIcon
        val bigPicture = notification.payload.bigPicture
        val smallIconAccentColor = notification.payload.smallIconAccentColor
        val sound = notification.payload.sound
        val ledColor = notification.payload.ledColor
        val lockScreenVisibility = notification.payload.lockScreenVisibility
        val groupKey = notification.payload.groupKey
        val groupMessage = notification.payload.groupMessage
        val fromProjectNumber = notification.payload.fromProjectNumber
        val rawPayload = notification.payload.rawPayload

//        println("ReceivedHandler title: $title")
//        println("ReceivedHandler id: $id")

        var customKey: String? = null
//        println("ReceivedHandler NotificationID received: $id")
        if (data != null) {
            customKey = data.optString("customKey", null)
            if (customKey != null) {
//                println("ReceivedHandler customKey set with value: $customKey")
            }
        }
        MyOneSignal.save(id, title, body)
    }
}

class MyOneSignal {

    companion object {

        val session: SharedPreferences = App.instance.getSharedPreferences(SESSION_FILENAME, 0)

        fun save(id: String, title: String, content: String) {
            val pnObj = JSONObject()
            pnObj.put("id", id)
            pnObj.put("title", title)
            pnObj.put("content", content)
            var pnArr: JSONArray? = null
            if (session.contains("pn")) {
                val pnStr = session.getString("pn", "")!!
                pnArr = JSONArray(pnStr)
            } else {
                pnArr = JSONArray()
            }
            if (!isExist(pnArr, id)) {
                pnArr!!.put(pnObj)
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
    }
}


















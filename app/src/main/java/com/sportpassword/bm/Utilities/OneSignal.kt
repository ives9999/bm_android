package com.sportpassword.bm.Utilities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.onesignal.*
import com.sportpassword.bm.App
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.Controllers.ShowPNVC
import com.sportpassword.bm.member
import org.json.JSONArray
import org.json.JSONObject


class MyNotificationOpenedHandler: OneSignal.OSNotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenedResult) {
//        println("receive PN")
        val actionType = result.action.type
        val data = result.notification.additionalData
        val id = result.notification.notificationId
        val title = result.notification.title
        val content = result.notification.body

//        println("OpenedHandler title: $title")
//        println("OpenedHandler id: $id")

//        val customKey = data?.optString("customkey", null)
//        if (customKey != null) {
//            println("OpenedHandler customkey set with value: $customKey")
//        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
//            println("OpenedHandler Button pressed with id: ${result.action.actionID}")
        }
        var pnID = "0"
        if (data != null) {
            pnID = MyOneSignal.getServerPNID(data)
        }

        MyOneSignal.save(id, title, content, pnID)
        val intent = Intent(App.instance, ShowPNVC::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.putExtra("id", id)
//        intent.putExtra("title", title)
//        intent.putExtra("content", content)
        App.instance.startActivity(intent)
    }
}

class MyNotificationReceivedHandler: OneSignal.OSRemoteNotificationReceivedHandler {

    override fun remoteNotificationReceived(
        context: Context?,
        notificationReceivedEvent: OSNotificationReceivedEvent?
    ) {

    }
    fun remoteNotificationReceived(notification: OSNotification) {
        val data = notification.additionalData
        val id = notification.androidNotificationId
        val title = notification.title
        val body = notification.body
        val smallIcon = notification.smallIcon
        val largeIcon = notification.largeIcon
        val bigPicture = notification.bigPicture
        val smallIconAccentColor = notification.smallIconAccentColor
        val sound = notification.sound
        val ledColor = notification.ledColor
        val lockScreenVisibility = notification.lockScreenVisibility
        val groupKey = notification.groupKey
        val groupMessage = notification.groupMessage
        val fromProjectNumber = notification.fromProjectNumber
        val rawPayload = notification.rawPayload

//        println("ReceivedHandler title: $title")
//        println("ReceivedHandler id: $id")

        var customKey: String? = null
//        println("ReceivedHandler NotificationID received: $id")
        var pnID = "0"
        if (data != null) {
            pnID = MyOneSignal.getServerPNID(data)
//            customKey = data.optString("customKey", null)
//            if (customKey != null) {
//                println("ReceivedHandler customKey set with value: $customKey")
//            }
        }
        MyOneSignal.save(id.toString(), title, body, pnID)
    }
}

class MyOneSignal {

    companion object {

        val session: SharedPreferences = App.instance.getSharedPreferences(SESSION_FILENAME, 0)

        fun save(id: String, title: String?, content: String, pnID: String) {
            val pnObj = JSONObject()
            pnObj.put("id", id)
            if (title != null) {
                pnObj.put("title", title!!)
            }
            pnObj.put("content", content)
            pnObj.put("pnid", pnID)
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


















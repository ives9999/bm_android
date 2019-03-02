package com.sportpassword.bm.Utilities

import android.content.Intent
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.sportpassword.bm.App
import com.sportpassword.bm.Controllers.MainActivity


class MyNotificationOpenedHandler: OneSignal.NotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenResult) {
        println("receive PN")
        val actionType = result.action.type
        val data = result.notification.payload.additionalData
        val title = result.notification.payload.title
        val id = result.notification.payload.notificationID

        println(title)
        println(id)

        val customKey = data?.optString("customkey", null)
        if (customKey != null) {
            println("customkey set with value: $customKey")
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            println("Button pressed with id: ${result.action.actionID}")
        }

        val intent = Intent(App.instance, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.instance.startActivity(intent)
    }
}


















package com.sportpassword.bm

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sportpassword.bm.Controllers.CourseVC
import com.sportpassword.bm.Controllers.MemberOrderListVC
import com.sportpassword.bm.Controllers.SearchVC
import com.sportpassword.bm.Utilities.isPermissionGranted

const val channelId = "notification_channel"
const val channelName = "com.sportpassword.bm.fcmpushnotification"

class MyFirebaseMessagingService: FirebaseMessagingService() {

    // generate the notification
    // attach the notification created with the custom layout
    // show the notification

    //val CHANNEL_ID = "order"
    //val CHANNEL_NAME = "order"
    val NOTIF_ID = 0
    val REQUEST_NOTIFICATION = 20

    lateinit var notifManager: NotificationManagerCompat
    lateinit var notif: Notification


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.notification != null) {
            val data: Map<String, String> = remoteMessage.data

            var type: String = ""
            if (data.containsKey("type")) {
                type = data["type"]!!
            }
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!, type)
        }

        else if (remoteMessage.data != null) {
            val data: Map<String, String> = remoteMessage.data

            var type: String = ""
            if (data.containsKey("type")) {
                type = data["type"]!!
            }

            var title: String = ""
            if (data.containsKey("title")) {
                title = data["title"]!!
            }

            var body: String = ""
            if (data.containsKey("body")) {
                body = data["body"]!!
            }
            generateNotification(title, body, type)
        }
    }

    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews: RemoteViews = RemoteViews("com.sportpassword.bm", R.layout.notification)

        remoteViews.setTextViewText(R.id.titleTV, title)
        remoteViews.setTextViewText(R.id.messageTV, message)
        remoteViews.setImageViewResource(R.id.logoIV, R.drawable.ic_bell_svg)

        return remoteViews
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun generateNotification(title: String, message: String, type: String) {

        createNotifChannel(type)

        var cls: Class<*> = MemberOrderListVC::class.java
        if (type == "course") {
            cls = CourseVC::class.java
        }

        val intent: Intent = Intent(this, cls)
        intent.putExtra("source", "notification")
//        val stackBuilder = TaskStackBuilder.create(this)
//        stackBuilder.addParentStack(SearchVC::class.java)
//        stackBuilder.addNextIntent(intent)
//        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val pendingIntent = androidx.core.app.TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        }

        notifManager = NotificationManagerCompat.from(this)
        val notifBuilder = NotificationCompat.Builder(this, type)

        notif = notifBuilder
            .setSmallIcon(getNotificationIcon(notifBuilder))
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .build()

        val p: Boolean = isPermissionGranted(this, Manifest.permission.POST_NOTIFICATIONS)
        if (!p) {
            //askForPermission(this, Manifest.permission.POST_NOTIFICATIONS, REQUEST_NOTIFICATION)
        } else {
            notifManager.notify(NOTIF_ID, notif)
        }
    }

    private fun createNotifChannel(type: String): NotificationManager? {

        var manager: NotificationManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = "一般"
            when (type) {
                "order" -> name = "訂單"
                "course" -> name = "課程"
                else -> name = "一般"
            }
            val channel = NotificationChannel(type, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }

            manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        return manager
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun getNotificationIcon(notificationBuilder: NotificationCompat.Builder): Int {
        val color: Int = 0xffffff
        notificationBuilder.color = color
        return R.drawable.ic_notification
    }
}



























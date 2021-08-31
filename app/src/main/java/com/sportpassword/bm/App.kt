package com.sportpassword.bm

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import com.sportpassword.bm.Models.Member
import com.sportpassword.bm.Utilities.*
import org.json.JSONObject
import com.onesignal.OSNotificationReceivedEvent
import com.sportpassword.bm.Controllers.ShowPNVC

/**
 * Created by ives on 2018/2/6.
 */

val member: Member by lazy {
    App.member!!
}

class App: Application() {

    companion object {
        var ctx: Context? = null
        var member: Member? = null

//        private val SCOPE = "private public create edit delete interact"
//        private val IS_DEBUG_BUILD = false
//        private val ACCESS_TOKEN_PROVIDED = false

//        fun getUserAgentString(context: Context): String {
//            val packageName = context.packageName
//
//            var version = "unknown"
//            try {
//                val pInfo = context.packageManager.getPackageInfo(packageName, 0)
//                version = pInfo.versionName
//            } catch (e: PackageManager.NameNotFoundException) {
//                println("Unable to get packageInfo: " + e.message)
//            }
//
//            val deviceManufacturer = Build.MANUFACTURER
//            val deviceModel = Build.MODEL
//            val deviceBrand = Build.BRAND
//
//            val versionString = Build.VERSION.RELEASE
//            val versionSDKString = Build.VERSION.SDK_INT.toString()
//
//            return packageName + " (" + deviceManufacturer + ", " + deviceBrand + ", " + "Android " + versionString + "/" + versionSDKString + " Version " + version + ")"
//        }
    }

    override fun onCreate() {
        member = Member(JSONObject())
        super.onCreate()
        ctx = applicationContext
        val session: SharedPreferences = this.getSharedPreferences(SESSION_FILENAME, 0)
        //session.dump()
        if (session.has(ISLOGGEDIN_KEY)) {
            val isLoggedIn = session.getBoolean(ISLOGGEDIN_KEY, false)
            if (isLoggedIn) {
                member!!.setMemberData(session)
            }
        }

        // OneSignal Initialization
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("856c8fdb-79fb-418d-a397-d58b9c6b880b")

        //當app不在工作狀態時，會呼叫這個函式
        OneSignal.setNotificationOpenedHandler { result: OSNotificationOpenedResult ->
//            OneSignal.onesignalLog(
//                OneSignal.LOG_LEVEL.VERBOSE,
//                "OSNotificationOpenedResult result: $result"
//            )

            MyOneSignal.openHandler(applicationContext, result)
            //val no = NotificationServiceExtension()
            //no.remoteNotificationReceived(this, result.notification)
            //val launchUrl = result.notification.launchURL
            //if (launchUrl != null) {
                //val intent: Intent = Intent(applicationContext, ShowPNVC::class.java)
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
                //intent.putExtra("openURL", launchUrl)
                //startActivity(intent)
            //}

        }

        //當app在工作狀態時，會呼叫這個函式
        OneSignal.setNotificationWillShowInForegroundHandler { notificationReceivedEvent: OSNotificationReceivedEvent ->
//            OneSignal.onesignalLog(
//                OneSignal.LOG_LEVEL.VERBOSE, "NotificationWillShowInForegroundHandler fired!" +
//                        " with notification event: " + notificationReceivedEvent.toString()
//            )
//            val notificationServiceExtension = NotificationServiceExtension()
//            notificationServiceExtension.remoteNotificationReceived(applicationContext, notificationReceivedEvent)

            MyOneSignal.showInForegroundHandler(applicationContext, notificationReceivedEvent)

            //val no = NotificationServiceExtension()
            //no.remoteNotificationReceived(this, notificationReceivedEvent)
            //Alert.show(this, "訊息", "您有一則新訊息")
//            val alert = AlertDialog.Builder(applicationContext).create()
//            alert.setTitle("訊息")
//            alert.setMessage("您有一則新訊息")
//            alert.show()

        }

        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.pauseInAppMessages(true)
        OneSignal.setLocationShared(false)


//        OneSignal.startInit(this)
//                .setNotificationReceivedHandler(MyNotificationReceivedHandler())
//                .setNotificationOpenedHandler(MyNotificationOpenedHandler())
//                .autoPromptLocation(true)
//                .init()

//        AccountPreferenceManager.initializeInstance(this)
//        val configBuilder: Configuration.Builder
//
//        if (ACCESS_TOKEN_PROVIDED) {
//            configBuilder = accessTokenBuilder
//        } else {
//            configBuilder = clientIDAndClientSecretBuilder
//        }
//        if (IS_DEBUG_BUILD) {
//            configBuilder.enableCertPinning(false)
//            configBuilder.setLogLevel(Vimeo.LogLevel.VERBOSE)
//        }
//        configBuilder
//                .setCacheDirectory(this.cacheDir)
//                .setUserAgentString(getUserAgentString(this))
//                .setDebugLogger(NetworkingLogger())
//        VimeoClient.initialize(configBuilder.build())
    }

//    val accessTokenBuilder: Configuration.Builder
//        get() {
//            val accessToken = "PROVIDE AN ACCESS TOKEN"
//            return Configuration.Builder(accessToken)
//        }
//    val clientIDAndClientSecretBuilder: Configuration.Builder
//        get() {
//            val clientID = VIMEO_ID
//            val clientSercet = VIMEO_SECRET
//            val codeGrantRedirectUri = getString(R.string.deeplink_redirect_scheme) + "://" + getString(R.string.deeplink_redirect_host)
//            val testAccountStore = TestAccountStore(this.applicationContext)
//            val configBuilder = Configuration.Builder(clientID, clientSercet, SCOPE)
//            configBuilder.setCodeGrantRedirectUri(codeGrantRedirectUri)
//            return configBuilder
//        }
}
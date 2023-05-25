package com.sportpassword.bm

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.facebook.stetho.Stetho
import com.google.gson.Gson
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Models.Member
import com.sportpassword.bm.Utilities.MyOneSignal
import com.sportpassword.bm.bm_new.data.remote_source.NetworkModule
import com.sportpassword.bm.bm_new.data.repo.match.MatchRepo
import com.sportpassword.bm.bm_new.data.repo.match.MatchRepoImpl
import com.sportpassword.bm.bm_new.ui.match.MatchVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import timber.log.Timber

/**
 * Created by ives on 2018/2/6.
 */

val member: Member by lazy {
    App.member!!
}

class App : Application() {

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
        member = Member(applicationContext)
        super.onCreate()
        ctx = applicationContext

        /** kai */
        setupKoin()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
        /** kai */

        //registerActivityLifecycleCallbacks()
//        val session: SharedPreferences = this.getSharedPreferences(SESSION_FILENAME, 0)
        //session.dump()
//        if (session.has(ISLOGGEDIN_KEY)) {
//            val isLoggedIn = session.getBoolean(ISLOGGEDIN_KEY, false)
//            if (isLoggedIn) {
//                member!!.setMemberData(session)
//            }
//        }

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


            val activity: BaseActivity? = getActivity()
            MyOneSignal.getOneSignalHandler(activity, result.notification)
//            MyOneSignal.openHandler(applicationContext, result)


//            val builder = AlertDialog.Builder(applicationContext)
//            builder.setTitle(title)
//            builder.setMessage(content)
//            runOnUiThread {
//                builder.show()
//            }
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

            val activity = getActivity()
            MyOneSignal.getOneSignalHandler(activity, notificationReceivedEvent.notification, true)
//            runOnUiThread {
//                activity.info(content)
//            }


//            MyOneSignal.showInForegroundHandler(applicationContext, notificationReceivedEvent)

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

    fun getActivity(): BaseActivity? {

        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        val activytyField = activityThreadClass.getDeclaredField("mActivities")
        activytyField.isAccessible = true

        val activities = activytyField.get(activityThread) as Map<Object, Object>

        for (activityRecord in activities.values) {
            val activityRecordClass = activityRecord.`class`
            val pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.isAccessible = true

            if (!pausedField.getBoolean(activityRecord)) {
                val activityField = activityRecordClass.getDeclaredField("activity")
                activityField.isAccessible = true
                val activity = activityField.get(activityRecord) as BaseActivity
                return activity
            }
        }

        return null
    }

    /** kai */
    private fun setupKoin() {
        val utilModule = module {
            single<Gson> {
                Gson()
            }
        }
        val remoteModule = module {
            single { NetworkModule.provideOkHttpClient() }
            single { NetworkModule.provideRetrofit(get()) }
            single { NetworkModule.provideMatchApi(get()) }
        }

        val repoModule = module {
            single<MatchRepo> { MatchRepoImpl(get()) }
        }

        val vmModule = module {
            viewModel { (handle: SavedStateHandle) -> MatchVM(handle, get()) }

        }

        startKoin {
            androidContext(this@App)
            modules(
                utilModule,
                remoteModule,
                repoModule,
                vmModule
            )
        }
    }
    /** kai */

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


























package com.sportpassword.bm

import android.app.Application
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Controllers.SystemProperties
import com.sportpassword.bm.Models.Member
import com.sportpassword.bm.Services.MemberService

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
        member = Member(applicationContext)
        super.onCreate()
        ctx = applicationContext

        gSimulate = isEmulator()
        _setURLConstants()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("fcm token: ${task.result}")
                MemberService.deviceToken(task.result, member!!.token)
            }
        }


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

        //mark disable OneSignal ives 2022/06/09
        //OneSignal.initWithContext(this)
        //OneSignal.setAppId("856c8fdb-79fb-418d-a397-d58b9c6b880b")

        //當app不在工作狀態時，會呼叫這個函式
        //OneSignal.setNotificationOpenedHandler { result: OSNotificationOpenedResult ->

            //val activity: BaseActivity? = getActivity()
            //MyOneSignal.getOneSignalHandler(activity, result.notification)



        //}

        //當app在工作狀態時，會呼叫這個函式
//        OneSignal.setNotificationWillShowInForegroundHandler { notificationReceivedEvent: OSNotificationReceivedEvent ->
//            val activity = getActivity()
//            MyOneSignal.getOneSignalHandler(activity, notificationReceivedEvent.notification, true)
//        }
//
//        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
//        OneSignal.pauseInAppMessages(true)
//        OneSignal.setLocationShared(false)


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

    private fun _setURLConstants() {
        BASE_URL = if (gSimulate) LOCALHOST_BASE_URL else REMOTE_BASE_URL
        //println("os: " + BASE_URL)

        URL_HOME = BASE_URL + "/app/"

        URL_AREA_BY_CITY_IDS = URL_HOME + "area_by_citys"
        URL_ARENA_BY_CITY_ID = URL_HOME + "arena_by_city"
        URL_ARENA_BY_CITY_IDS = URL_HOME + "arena_by_citys"
        URL_ARENA_LIKE = URL_HOME + "arena/like/%s"
        URL_ARENA_LIST = URL_HOME + "arena/list"
        URL_CANCEL_SIGNUP = URL_HOME + "%s/cancelSignup/%d"
        URL_CART_DELETE = URL_HOME + "cart/delete"
        URL_CART_LIST = URL_HOME + "cart/list"
        URL_CART_UPDATE = "${URL_HOME}cart/update"
        URL_CHANGE_PASSWORD = "${URL_HOME}member/change_password"
        URL_CITYS = URL_HOME + "citys"
        URL_COACH_LIKE = URL_HOME + "coach/like/%s"
        URL_COACH_LIST = URL_HOME + "coach/list"
        URL_COURSE_DELETE = URL_HOME + "course/delete"
        URL_COURSE_LIKE = URL_HOME + "course/like/%s"
        URL_COURSE_LIST = URL_HOME + "course/list"
        URL_COURSE_UPDATE = URL_HOME + "course/update"
        URL_DELETE = URL_HOME + "%s/delete"
        URL_DEVICE_TOKEN = URL_HOME + "deviceToken"
        URL_ECPAY2_C2C_MAP = URL_HOME + "order/ecpay2_c2c_map"
        URL_EMAIL_VALIDATE = URL_HOME + "member/email_validate"
        URL_FB_LOGIN = URL_HOME + "member/fb"
        URL_FORGETPASSWORD = "${URL_HOME}member/forget_password"
        URL_ISNAMEEXIST = "${URL_HOME}%s/isNameExist"
        URL_LIST = "${URL_HOME}%s"
        URL_LOGIN = URL_HOME + "login"
        URL_MANAGER_SIGNUPLIST = "${URL_HOME}%s/manager_signup_list"
        URL_MEMBER_COINLIST = "${URL_HOME}member/coinlist"
        URL_MEMBER_COINRETURN = "${URL_HOME}member/coinReturn"
        URL_MEMBER_DELETE = URL_HOME + "member/delete"
        URL_MEMBER_SUBSCRIPTION_KIND = "${URL_HOME}member/subscriptionKind"
        URL_MEMBER_SUBSCRIPTION_LOG = "${URL_HOME}member/subscriptionLog"
        URL_MEMBER_LIKELIST = "${URL_HOME}member/likelist"
        URL_MEMBER_BANK = URL_HOME + "member/bank"
        URL_MEMBER_BLACKLIST = URL_HOME + "member/blacklist"
        URL_MEMBER_GETONE = URL_HOME + "member/getOne"
        URL_MEMBER_SIGNUPLIST = "${URL_HOME}member/signup_calendar"
        URL_MEMBER_SUBSCRIPTION = "${URL_HOME}member/subscription"
        URL_MEMBER_TEAM_DELETE = "${URL_HOME}member/deleteMemberTeam"
        URL_MEMBER_TEAM_LIST = "${URL_HOME}member/teamlist"
        URL_MEMBER_UPDATE = URL_HOME + "member/update"
        URL_MOBILE_VALIDATE = URL_HOME + "member/mobile_validate"
        URL_ONE = "${URL_HOME}%s/one"
        URL_ORDER = "${URL_HOME}order/payment%s"
        URL_ORDER_LIST = URL_HOME + "order/list"
        URL_ORDER_RETURN = URL_HOME + "order/ezship_return_code"
        URL_ORDER_UPDATE = "${URL_HOME}order/update"
        URL_PRODUCT_LIKE = URL_HOME + "product/like/%s"
        URL_PRODUCT_LIST = URL_HOME + "product/list"
        URL_REGISTER = URL_HOME + "register"
        URL_REQUEST_MANAGER = URL_HOME + "request_manager/update"
        URL_SEND_EMAIL_VALIDATE = URL_HOME + "member/sendEmailValidate"
        URL_SEND_MOBILE_VALIDATE = URL_HOME + "member/sendMobileValidate"
        URL_SHOW = "${URL_HOME}%s/show/%s?device=app"
        URL_SIGNUP = URL_HOME + "%s/signup/%s"
        URL_SIGNUP_DATE = "${URL_HOME}%s/signup_date/%s"
        URL_SIGNUP_LIST = "${URL_HOME}%s/signup_list"
        URL_STORE_LIKE = URL_HOME + "store/like/%s"
        URL_STORE_LIST = URL_HOME + "store/list"
        URL_TEACH_LIKE = URL_HOME + "teach/like/%s"
        URL_TEACH_LIST = URL_HOME + "teach/list"
        URL_TEAM = URL_HOME + "team/"
        URL_TEAM_DELETE = URL_HOME + "team/delete"
        URL_TEAM_LIKE = URL_HOME + "team/like/%s"
        URL_TEAM_LIST = URL_HOME + "team/list"
        URL_TEAM_MEMBER_ADD = URL_HOME + "team/addTeamMember"
        URL_TEAM_MEMBER_DELETE = URL_HOME + "team/deleteTeamMember"
        URL_TEAM_MEMBER_LEAVE = URL_HOME + "team/leave"
        URL_TEAM_MEMBER_LIST = URL_HOME + "team/teamMemberList"
        URL_TEAM_TEMP_PLAY = URL_TEAM + "tempPlay/onoff"
        URL_TEAM_TEMP_PLAY_ADD = URL_TEAM + "tempPlay/add"
        URL_TEAM_TEMP_PLAY_LIST = URL_TEAM + "tempPlay/list"
        URL_TEAM_TEMP_PLAY_BLACKLIST = URL_TEAM + "tempPlay/blacklist"
        URL_TEAM_PLUSONE = BASE_URL + "/team/tempPlay/plusOne/"
        URL_TEAM_CANCELPLUSONE = BASE_URL + "/team/tempPlay/cancelPlusOne/"
        URL_TEAM_TEMP_PLAY_DATE = URL_TEAM + "tempPlay/date"
        URL_TEAM_TEMP_PLAY_DATE_PLAYER = URL_TEAM + "tempPlay/datePlayer"
        URL_TEAM_UPDATE = URL_HOME + "team/update"
        URL_TT = URL_HOME + "%s/tt"
        URL_TT_DELETE = URL_HOME + "%s/tt/delete"
        URL_TT_UPDATE = URL_HOME + "%s/tt/update"
        URL_UPDATE = URL_HOME + "%s/update"

        FEATURED_PATH = BASE_URL + FEATURED_PATH
    }

    private fun isEmulator(): Boolean {

//        println(Build.FINGERPRINT.startsWith("google/sdk_gphone_")
//                && Build.FINGERPRINT.endsWith(":user/release-keys")
//                && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google"
//                && Build.MODEL.startsWith("sdk_gphone_"))
//
//        println("FINGERPRINT:${Build.FINGERPRINT}\n" +
//                "MODEL:${Build.MODEL}\n" +
//                "MANUFACTURER:${Build.MANUFACTURER}\n" +
//                "BRAND:${Build.BRAND}\n" +
//                "DEVICE:${Build.DEVICE}\n" +
//                "BOARD:${Build.BOARD}\n" +
//                "HOST:${Build.HOST}\n" +
//                "PRODUCT:${Build.PRODUCT}\n")

        val result = (Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                && Build.FINGERPRINT.endsWith(":user/release-keys")
                && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google"
                && Build.MODEL.startsWith("sdk_gphone_"))
                //
                || Build.FINGERPRINT.contains("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                //bluestacks
                || "QC_Reference_Phone" == Build.BOARD && !"Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true) //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")

                //Sony is true, so mark it. HOST:BuildHost
//                || Build.HOST.startsWith("Build") //MSI App Player
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk"
                // another Android SDK emulator check
                || SystemProperties.getProp("ro.kernel.qemu") == "1"
        return result

//        return (Build.FINGERPRINT.startsWith("generic")
//                || Build.FINGERPRINT.startsWith("unknown")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.MODEL.contains("Android SDK built for x86")
//                || Build.MANUFACTURER.contains("Genymotion")
//                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
//                || "google_sdk" == Build.PRODUCT)
    }

//    fun getActivity(): BaseActivity? {
//
//        val activityThreadClass = Class.forName("android.app.ActivityThread")
//        val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
//        val activytyField = activityThreadClass.getDeclaredField("mActivities")
//        activytyField.isAccessible = true
//
//        val activities = activytyField.get(activityThread) as Map<Object, Object>
//
//        for (activityRecord in activities.values) {
//            val activityRecordClass = activityRecord.`class`
//            val pausedField = activityRecordClass.getDeclaredField("paused");
//            pausedField.isAccessible = true
//
//            if (!pausedField.getBoolean(activityRecord)) {
//                val activityField = activityRecordClass.getDeclaredField("activity")
//                activityField.isAccessible = true
//                val activity = activityField.get(activityRecord) as BaseActivity
//                return activity
//            }
//        }
//
//        return null
//    }

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


























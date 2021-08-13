package com.sportpassword.bm

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
//import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.sportpassword.bm.Models.Member
import com.sportpassword.bm.Utilities.*
import org.json.JSONObject

/**
 * Created by ives on 2018/2/6.
 */

val member: Member by lazy {
    App.member!!
}

class App: Application() {

    companion object {
        lateinit var instance: Context private set
        var member: Member? = null

        private val SCOPE = "private public create edit delete interact"
        private val IS_DEBUG_BUILD = false
        private val ACCESS_TOKEN_PROVIDED = false

        fun getUserAgentString(context: Context): String {
            val packageName = context.packageName

            var version = "unknown"
            try {
                val pInfo = context.packageManager.getPackageInfo(packageName, 0)
                version = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                println("Unable to get packageInfo: " + e.message)
            }

            val deviceManufacturer = Build.MANUFACTURER
            val deviceModel = Build.MODEL
            val deviceBrand = Build.BRAND

            val versionString = Build.VERSION.RELEASE
            val versionSDKString = Build.VERSION.SDK_INT.toString()

            return packageName + " (" + deviceManufacturer + ", " + deviceBrand + ", " + "Android " + versionString + "/" + versionSDKString + " Version " + version + ")"
        }
    }

    override fun onCreate() {
        member = Member(JSONObject())
        super.onCreate()
        instance = this
        val session: SharedPreferences = this.getSharedPreferences(SESSION_FILENAME, 0)
        //session.dump()
        if (session.has(ISLOGGEDIN_KEY)) {
            val isLoggedIn = session.getBoolean(ISLOGGEDIN_KEY, false)
            if (isLoggedIn) {
                member!!.setMemberData(session)
            }
        }

        // OneSignal Initialization
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("856c8fdb-79fb-418d-a397-d58b9c6b880b")

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

/**
 * Class for persisting the the auth access token (and possibly the user)
 *
 * Created by anthonyr on 5/8/17.
 */
//object AccountPreferenceManager {
//
//    private lateinit var sharedPreferences: SharedPreferences
//
//    @Synchronized fun initializeInstance(context: Context) {
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//    }
//
//    // <editor-fold desc="Account">
//
//    val CLIENT_ACCOUNT_JSON = "CLIENT_ACCOUNT_JSON"
//    val CACHED_CLIENT_CREDENTIALS_ACCOUNT_JSON = "CACHED_CLIENT_CREDENTIALS_ACCOUNT_JSON"
//
//    fun removeClientAccount() {
//        sharedPreferences.edit().remove(CLIENT_ACCOUNT_JSON).apply()
//    }
//
//    // NOTE: This happens on the main thread, don't do this
//    // NOTE: This happens on the main thread, don't do this
//    var clientAccount: VimeoAccount?
//        get() {
//            val accountJSON = sharedPreferences.getString(CLIENT_ACCOUNT_JSON, null) ?: return null
//
//            return VimeoNetworkUtil.getGson().fromJson(accountJSON, VimeoAccount::class.java)
//        }
//        set(vimeoAccount) {
//            if (vimeoAccount != null) {
//                val accountJSON = VimeoNetworkUtil.getGson().toJson(vimeoAccount)
//                if (accountJSON == null) {
//                    removeClientAccount()
//                    return
//                }
//                sharedPreferences.edit().putString(CLIENT_ACCOUNT_JSON, accountJSON).apply()
//            }
//        }
//
//    val currentUser: User?
//        get() {
//            return AccountPreferenceManager.clientAccount?.user
//        }
//
//    fun cacheClientCredentialsAccount(vimeoAccount: VimeoAccount?) {
//        if (vimeoAccount != null) {
//            val accountJSON = VimeoNetworkUtil.getGson().toJson(vimeoAccount) ?: return
//            sharedPreferences.edit().putString(CACHED_CLIENT_CREDENTIALS_ACCOUNT_JSON, accountJSON).apply()
//        }
//    }
//
//    val cachedClientCredentialsAccount: VimeoAccount?
//        get() {
//            val accountJSON = sharedPreferences.getString(CACHED_CLIENT_CREDENTIALS_ACCOUNT_JSON, null) ?: return null
//
//            return VimeoNetworkUtil.getGson().fromJson(accountJSON, VimeoAccount::class.java)
//        }
//
//    // </editor-fold>
//
//}

/**
 * An account store that is backed by [SharedPreferences].
 *
 * Note: This class can be used with Android's [AccountManager] to tie into the Android device Accounts
 *
 * Created by anthonyr on 5/8/17.
 */
//class TestAccountStore(context: Context?) : AccountStore {
//    override fun saveAccount(vimeoAccount: VimeoAccount?, email: String?, password: String?) {
//        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    // NOTE: You can use the account manager in the below methods to hook into the Android Accounts
//    // @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
//    // val mAccountManager: AccountManager? = AccountManager.get(context!!)
//
//    override fun loadAccount(): VimeoAccount? {
//        return AccountPreferenceManager.clientAccount
//    }
//
//    override fun saveAccount(vimeoAccount: VimeoAccount, email: String) {
//        AccountPreferenceManager.clientAccount = vimeoAccount
//    }
//
//    override fun deleteAccount(vimeoAccount: VimeoAccount) {
//        AccountPreferenceManager.removeClientAccount()
//        // NOTE: You'll now need a client credentials grant (without an authenticated user)
//    }
//
//    fun updateAccount(vimeoAccount: VimeoAccount) {
//        AccountPreferenceManager.clientAccount = vimeoAccount
//        VimeoClient.getInstance().vimeoAccount = vimeoAccount
//    }
//
//}
/**
 * Logger delegate for handling logs coming from the Networking library
 *
 * Created by anthonyr on 5/8/17.
 */
//class NetworkingLogger : LogProvider {
//
//    private val TEST_APP = "~NET TEST APP~"
//
//    override fun e(error: String) {
//        Log.e(TEST_APP, error)
//    }
//
//    override fun e(error: String, exception: Exception) {
//        Log.e(TEST_APP, error, exception)
//    }
//
//    override fun d(debug: String) {
//        Log.d(TEST_APP, debug)
//    }
//
//    override fun v(verbose: String) {
//        Log.v(TEST_APP, verbose)
//    }
//
//}
package com.sportpassword.bm.Controllers

import android.content.IntentFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentPagerAdapter
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.View
import com.sportpassword.bm.Adapters.TabAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab.view.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity1 : BaseActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

    //private var mSectionPagerAdapter: SectionsPagerAdapter? = null
    //private val apiClient = VimeoClient.getInstance()

    val tabsTextArr: Array<String> = arrayOf<String>("球隊", "課程", "會員", "球館", "更多")
    val tabsIconArr: Array<String> = arrayOf<String>("team", "course", "member", "arena", "more")

//    lateinit var menuTeamListAdapter: MenuTeamListAdapter

//    private val vimeoClient = VimeoClient.getInstance()
//    private var vimeoToken: String? = null


//    fun bytesToHex(bytes: ByteArray): String {
////        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
////        val hexChars = CharArray(bytes.size * 2)
////        var v: Int
////        for (j in bytes.indices) {
////            v = bytes[j].toInt() and 0xFF
////            hexChars[j * 2] = hexArray[v.ushr(4)]
////            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
////        }
////        val res = String(hexChars)
//        val res = Base64.encodeToString(bytes, Base64.DEFAULT)
//        println(res);
//        return res
//        //return String(hexChars)
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard()

//        val signatureList: List<String>
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                // New signature
//                val sig = this.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo
//                signatureList = if (sig.hasMultipleSigners()) {
//                    // Send all with apkContentsSigners
//                    sig.apkContentsSigners.map {
//                        val digest = MessageDigest.getInstance("SHA")
//                        digest.update(it.toByteArray())
//                        bytesToHex(digest.digest())
//                    }
//                } else {
//                    // Send one with signingCertificateHistory
//                    sig.signingCertificateHistory.map {
//                        val digest = MessageDigest.getInstance("SHA")
//                        digest.update(it.toByteArray())
//                        bytesToHex(digest.digest())
//                    }
//                }
//            } else {
//                val sig = this.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
//                signatureList = sig.map {
//                    val digest = MessageDigest.getInstance("SHA")
//                    digest.update(it.toByteArray())
//                    bytesToHex(digest.digest())
//                }
//            }
//
//        } catch (e: Exception) {
//            println(e.localizedMessage)
//            // Handle error
//        }

//        ShortcutBadger.applyCount(this, 0)

//        OneSignal.clearOneSignalNotifications()

        //println("detect:" + gSimulate)
        setContentView(R.layout.activity_main)

        //App 最上面的標題列
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // TTEditAction bar 右邊的「提交」按鈕
        //val menuID = resources.getIdentifier("menu", "drawable", packageName)
        //println(menuID)
        //supportActionBar!!.setHomeAsUpIndicator(menuID)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)

        //會員側邊欄
//        val toggle = ActionBarDrawerToggle(
//                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
//        setMenuWidth()
//
//        drawer_layout.addDrawerListener(
//                object : DrawerLayout.DrawerListener {
//                    override fun onDrawerStateChanged(newState: Int) {
//
//                    }
//
//                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
//                    }
//
//                    override fun onDrawerClosed(drawerView: View) {
//                    }
//
//                    override fun onDrawerOpened(drawerView: View) {
//                        //refreshMember()
//                        _loginout()
//                        setRefreshListener()
//                    }
//                }
//        )

        //下方tab bar
        for (i in tabsTextArr.indices) {
            val text: String = tabsTextArr[i]
            val icon: Int = resources.getIdentifier(tabsIconArr[i], "drawable", packageName)
            tabs.addTab(tabs.newTab().setText(text).setIcon(icon))
        }
        val tab: TabLayout.Tab? = tabs.getTabAt(0)
        setTabIconSelected(tab!!)



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        //把fragment放到ViewPager容器內
        val w = (screenWidth.toFloat() / density).toInt()
        //val adapter = TabAdapter(supportFragmentManager, tabsTextArr, w)
        val adapter = TabAdapter(this, tabsTextArr, w)
        fragment_container.adapter = adapter

        //mSectionPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        //tab_container.adapter = mSectionPagerAdapter

//        fragment_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
//        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(fragment_container))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setTabIconSelected(tab!!)
                val position = tab.position
                fragment_container.currentItem = position
                //adapter.createFragment(position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setTabIconUnSelected(tab!!)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        //_loginout()

//        if (!member.justGetMemberOne && member.isLoggedIn) {
//            _updatePlayerIDWhenIsNull()
//        }
//
//        refreshLayout = menu_refresh

        //println("$URL_LIST".format("team"))
        //member.print()

        //Loading.show(this)
//        alert("Test alert") {
//            title = "警告"
//            positiveButton("確定") { toast("Yes") }
//            negativeButton("取消") {  }
//        }.show()
//        Alert.show(this, "警告", "姓名沒填") {
//            println("test")
//        }

//        try {
//            val info = getPackageManager().getPackageInfo("com.sportpassword.bm",PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                var md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                println("KeyHash: ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
//            }
//        } catch (e: Exception) {
//            println(e.localizedMessage)
//        }
        home_total_ad.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setTabIconSelected(tab: TabLayout.Tab) {
        val color = ContextCompat.getColor(this, R.color.MY_GREEN)
        tab.icon!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        val title = tab.text!!.toString()
        setTitle(title)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setTabIconUnSelected(tab: TabLayout.Tab) {
        val color = ContextCompat.getColor(this, R.color.MY_WHITE)
        tab.icon!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }


//    override fun onResume() {
//        super.onResume()
//        //LocalBroadcastManager.getInstance(this).registerReceiver(memberDidChange, IntentFilter(NOTIF_MEMBER_DID_CHANGE))
////        LocalBroadcastManager.getInstance(this).registerReceiver(teamUpdate, IntentFilter(NOTIF_TEAM_UPDATE))
//    }

//    override fun onDestroy() {
//        //LocalBroadcastManager.getInstance(this).unregisterReceiver(memberDidChange)
//        super.onDestroy()
//    }

    private fun setTitle(title: String) {
        //val titleView = toolbar.findViewById<View>(R.id.toolbar_title) as TextView
        //titleView.text = title
        toolbar_title.text = title
    }

//    fun search_team(view: View) {
//        toSearch("team")
//    }

//    private fun setMenuWidth() {
//        val l = drawer.layoutParams
//        val w = screenWidth * 0.85
//        l.width = w.toInt()
//        drawer.layoutParams = l
//    }

}
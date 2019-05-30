package com.sportpassword.bm.Controllers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.onesignal.OneSignal
import com.sportpassword.bm.Adapters.MemberFunctionsAdapter
import com.sportpassword.bm.Adapters.MenuTeamListAdapter
import com.sportpassword.bm.Adapters.TabAdapter
import com.sportpassword.bm.Fragments.CoachFragment
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.tab.view.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.menu_member_function.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.pure_mask.*
//import me.leolin.shortcutbadger.ShortcutBadger

import okhttp3.CacheControl
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.security.MessageDigest


class MainActivity : BaseActivity() {

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

    val tabsTextArr: Array<String> = arrayOf<String>("臨打", "教練", "球隊", "更多")
    val tabsIconArr: Array<String> = arrayOf<String>("tempplay", "coach", "team", "more")

    val fixedRows: ArrayList<Map<String, String>> = arrayListOf(
            mapOf("text" to "帳戶資料", "icon" to "account", "segue" to "account"),
            mapOf("text" to "更改密碼", "icon" to "password", "segue" to "password")
    )
    var _rows: ArrayList<Map<String, String>> = arrayListOf()

    lateinit var memberFunctionsAdapter: MemberFunctionsAdapter
//    lateinit var menuTeamListAdapter: MenuTeamListAdapter

//    private val vimeoClient = VimeoClient.getInstance()
//    private var vimeoToken: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard()

//        ShortcutBadger.applyCount(this, 0)

//        OneSignal.clearOneSignalNotifications()


//        val testAccount = TestAccountStore(this.applicationContext)
//        val configBuilder = Configuration.Builder(VIMEO_ID, VIMEO_SECRET, "private public create edit delete interact", testAccount).setCacheDirectory(this.cacheDir)
//        VimeoClient.initialize(configBuilder.build())
//        val token = VimeoClient.getInstance().vimeoAccount.accessToken
//        println(token)


//        val token = vimeoClient.vimeoAccount.accessToken
//        //println(token)
//        if (token == null) {
//            authenticateWithClientCredentials() { success ->
//                println(vimeoToken)
//                if (success) {
//                    //val uri = "/videos/265966500"
//                    val uri = "/me/videos"
//                    println(uri)
////                    vimeoClient.fetchContent(uri, CacheControl.FORCE_NETWORK, object: ModelCallback<VideoList>(VideoList::class.java) {
////                        override fun success(t: VideoList?) {
////                            println(t)
////                        }
////
////                        override fun failure(error: VimeoError?) {
////                            println(error!!.localizedMessage)
////                        }
////                    })
//                    vimeoClient.fetchNetworkContent(uri, object: ModelCallback<VideoList>(VideoList::class.java) {
//                        override fun success(t: VideoList?) {
//                            println("aaa")
//                            println(t)
//                            //val embed = t!!.embed.html
//                            //println(embed)
//                        }
//
//                        override fun failure(error: VimeoError?) {
//                            println(error!!.localizedMessage)
//                        }
//                    })
//                }
//            }
//        }

        //println("detect:" + gSimulate)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // TTEditAction bar 右邊的「提交」按鈕
        //val menuID = resources.getIdentifier("menu", "drawable", packageName)
        //println(menuID)
        //supportActionBar!!.setHomeAsUpIndicator(menuID)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)

        //會員側邊欄
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setMenuWidth()

        drawer_layout.addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerStateChanged(newState: Int) {

                    }

                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    }

                    override fun onDrawerClosed(drawerView: View) {
                    }

                    override fun onDrawerOpened(drawerView: View) {
                        //refreshMember()
                        _loginout()
                        setRefreshListener()
                    }
                }
        )

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

        val w = (screenWidth.toFloat() / density).toInt()
        val adapter = TabAdapter(supportFragmentManager, tabsTextArr, w)
        tab_container.adapter = adapter

        //mSectionPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        //tab_container.adapter = mSectionPagerAdapter



        tab_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(tab_container))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                this@MainActivity.setTabIconSelected(tab!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                this@MainActivity.setTabIconUnSelected(tab!!)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        _loginout()

        if (!member.justGetMemberOne && member.isLoggedIn) {
            _updatePlayerIDWhenIsNull()
        }

        refreshLayout = menu_refresh

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
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(memberDidChange, IntentFilter(NOTIF_MEMBER_DID_CHANGE))
//        LocalBroadcastManager.getInstance(this).registerReceiver(teamUpdate, IntentFilter(NOTIF_TEAM_UPDATE))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(memberDidChange)
        super.onDestroy()
    }

    override fun refresh() {
        if (member.isLoggedIn) {
            //initTeamList()
            refreshMember() { success ->
                closeRefresh()
                if (success) {
                    _loginout()
                }
            }
        } else {
            _logoutBlock()
        }
    }

    private fun setValidateRow() {
        _rows.clear()
        for (row in fixedRows) {
            _rows.add(row)
        }
        if (member.isLoggedIn) {
            val validate: Int = member.validate
            if (validate and EMAIL_VALIDATE <= 0) {
                val function: Map<String, String> = mapOf("text" to "email認證", "icon" to "email", "segue" to "email")
                _rows.add(function)
            }
            if (validate and MOBILE_VALIDATE <= 0) {
                val function: Map<String, String> = mapOf("text" to "手機認證", "icon" to "mobile_validate", "segue" to "mobile")
                _rows.add(function)
            }
        }
    }
    private fun setBlackListRow() {
        if (member.isTeamManager) {
            val row: Map<String, String> = mapOf("text" to "黑名單", "icon" to "blacklist", "segue" to "blacklist")
            _rows.add(row)
        }
    }

    private fun setTitle(title: String) {
        //val titleView = toolbar.findViewById<View>(R.id.toolbar_title) as TextView
        //titleView.text = title
        toolbar_title.text = title
    }

    protected fun _loginout() {
        if (member.isLoggedIn) {
            _loginBlock()
        } else {
            _logoutBlock()
        }
    }
    protected fun _loginBlock() {
        _loginAdapter()
        nicknameLbl.text = member.nickname
        loginBtn.text = "登出"
        registerBtn.visibility = View.INVISIBLE
        forgetPasswordBtn.visibility = View.INVISIBLE
        menu_member_container.visibility = View.VISIBLE
        //menu_team_container.visibility = View.VISIBLE
        refreshLayout = menu_refresh
//        initMemberFunction()
    }
    protected fun _logoutBlock() {
        nicknameLbl.text = "未登入"
        loginBtn.text = "登入"
        registerBtn.visibility = View.VISIBLE
        forgetPasswordBtn.visibility = View.VISIBLE
        menu_member_container.visibility = View.INVISIBLE
        //menu_team_container.visibility = View.INVISIBLE
    }
    protected fun _goMemberFunctions(segue: String) {
        when(segue) {
            "account" -> goEditMember()
            "password" -> goUpdatePassword()
            "email" -> goValidate("email")
            "mobile" -> goValidate("mobile")
            "blacklist" -> goBlackList()
            "refresh" -> goRefresh()
        }
    }
    protected fun _loginAdapter() {
        setValidateRow()
        setBlackListRow()
        val row: Map<String, String> = mapOf("text" to "重新整理", "icon" to "refresh", "segue" to "refresh")
        _rows.add(row)
        memberFunctionsAdapter = MemberFunctionsAdapter(this, _rows, {
            type -> _goMemberFunctions(type)
        })
        member_functions_container.adapter = memberFunctionsAdapter
        val layoutManager = LinearLayoutManager(this)
        member_functions_container.layoutManager = layoutManager
        memberFunctionsAdapter.notifyDataSetChanged()
    }

    protected fun goRefresh() {
        _getMemberOne(member.token) {
            _loginout()
        }
    }

    fun loginBtnPressed(view: View) {
        if (member.isLoggedIn) {
            if (member.uid.length > 0 && member.social == "fb") {
                FacebookSdk.sdkInitialize(getApplicationContext());
                AppEventsLogger.activateApp(this);
                LoginManager.getInstance().logOut()
            }
            MemberService.logout()
            refresh()
//            val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
        } else {
            //goLogin()
            val loginIntent: Intent = Intent(this, LoginActivity::class.java)
            //startActivity(loginIntent)
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                _loginout()
            }
            REGISTER_REQUEST_CODE -> {
                _loginout()
            }
            VALIDATE_REQUEST_CODE -> {
                hideKeyboard()
                goRefresh()
            }
        }
    }

    fun registerBtnPressed(view: View){
        //goRegister()
        val registerIntent: Intent = Intent(this, RegisterActivity::class.java)
        startActivityForResult(registerIntent, REGISTER_REQUEST_CODE)
    }

    fun forgetpasswordBtnPressed(view: View) {
        val forgetPasswordIntent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(forgetPasswordIntent)
    }

    fun search_team(view: View) {
        goSearch("team")
    }

    fun manager(view: View) {
        goManager(view.tag as String)
    }

    private fun setMenuWidth() {
        val l = drawer.layoutParams
        val w = screenWidth * 0.85
        l.width = w.toInt()
        drawer.layoutParams = l
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
        val color = ContextCompat.getColor(this, R.color.WHITE)
        tab.icon!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
//    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//
//        override fun getItem(position: Int): Fragment {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1)
//        }
//
//        override fun getCount(): Int {
//            // Show 3 total pages.
//            return tabsTextArr.size
//        }
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    class PlaceholderFragment : Fragment() {
//
//        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                                  savedInstanceState: Bundle?): View? {
//            val rootView = inflater.inflate(R.layout.tab, container, false)
//            //rootView.section_label.text = getString(R.string.section_format, arguments!!.getInt(ARG_SECTION_NUMBER))
//            return rootView
//        }
//
//        companion object {
//            /**
//             * The fragment argument representing the section number for this
//             * fragment.
//             */
//            private val ARG_SECTION_NUMBER = "section_number"
//
//            /**
//             * Returns a new instance of this fragment for the given section
//             * number.
//             */
//            fun newInstance(sectionNumber: Int): PlaceholderFragment {
//                val fragment = PlaceholderFragment()
//                val args = Bundle()
//                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
//                fragment.arguments = args
//                return fragment
//            }
//        }
//    }

    //    private val teamUpdate = object: BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            refresh()
//        }
//    }

//    private fun initMemberFunction() {
//        menu_account_container.setOnClickListener {view ->
//            val accountIntent = Intent(this, AccountActivity::class.java)
//            startActivity(accountIntent)
//        }
//        menu_updatepassword_container.setOnClickListener { view ->
//            val updatePasswordIntent = Intent(this, UpdatePasswordActivity::class.java)
//            startActivity(updatePasswordIntent)
//        }
//    }

//    private fun initTeamList() {
//        val filter1: Array<Any> = arrayOf("channel", "=", CHANNEL)
//        val filter2: Array<Any> = arrayOf("manager_id", "=", member.id)
//        val filter: Array<Array<Any>> = arrayOf(filter1, filter2)
//
//        TeamService.getList(this, "team", "name", 1, 100, filter) { success ->
//            if (success) {
//                this.menuTeamListAdapter = MenuTeamListAdapter(this, TeamService.superDataLists,
//                        { team -> goEditTeam(team.token) },
//                        { team -> goDeleteTeam(team.token) },
//                        { team -> goTeamTempPlayEdit(team.token) }
//                )
//                menu_team_list.adapter = this.menuTeamListAdapter
//
//                val layoutManager = LinearLayoutManager(this)
//                menu_team_list.layoutManager = layoutManager
//                closeRefresh()
//            }
//        }
//        menu_team_add.onClick {
//            if (member.validate < 1) {
//                Alert.show(this@MainActivity, "錯誤", "未通過EMail認證，無法新增球隊，認證完後，請先登出再登入")
//            } else {
//                goEditTeam()
//            }
//        }
//
//    }

    //    private fun authenticateWithClientCredentials(complete: CompletionHandler) {
//        vimeoClient.authorizeWithClientCredentialsGrant(object: AuthCallback {
//            override fun success() {
//                val accessToken = vimeoClient.vimeoAccount.accessToken
//                //println(accessToken)
//                vimeoToken = accessToken
//                complete(true)
//            }
//
//            override fun failure(error: VimeoError?) {
//                //println("failure")
//                complete(false)
//            }
//        })
//    }
}
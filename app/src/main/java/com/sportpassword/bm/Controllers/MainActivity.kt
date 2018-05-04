package com.sportpassword.bm.Controllers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
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
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.menu_member_function.*
import kotlinx.android.synthetic.main.menu_team_list.*
import kotlinx.android.synthetic.main.nav_header_main.*
import okhttp3.CacheControl
import org.jetbrains.anko.sdk25.coroutines.onClick
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
    var mainActivity: MainActivity? = null

    lateinit var menuTeamListAdapter: MenuTeamListAdapter

//    private val vimeoClient = VimeoClient.getInstance()
//    private var vimeoToken: String? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard()


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
        val menuID = resources.getIdentifier("menu", "drawable", packageName)
        //println(menuID)
        supportActionBar!!.setHomeAsUpIndicator(menuID)
        //toolbar.setNavigationIcon(menuID)
        //toolbar.setNavigationOnClickListener(this)

        mainActivity = this

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setMenuWidth()

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
                mainActivity!!.setTabIconSelected(tab!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                mainActivity!!.setTabIconUnSelected(tab!!)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        _loginout()


//        val configBuilder = Configuration.Builder("85a4cfd6e6ff82ea0493e269d19086c0c856936b", "w//PE1Vewrvaicmc9LtXyKjJB2DoFmxPenQxoZJ3vD3PkBraHahyFKpm4zmZnIIJy2EUO8NvSWuWiHkbK8mLoBvUxve1Rxm54nl4OH8FHpKHmvGtG3zm30gOa/X36oL5", "public")
//                .setCacheDirectory(this.cacheDir)
//        VimeoClient.initialize(configBuilder.build())
//        val accessToken = apiClient.vimeoAccount.accessToken
//        println(accessToken)


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
//        val info = getPackageManager().getPackageInfo("com.sportpassword.bm",PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//            var md = MessageDigest.getInstance("SHA")
//            md.update(signature.toByteArray())
//            println("KeyHash: ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
//        }
//    } catch (e: Exception) {
//        println(e.localizedMessage)
//    }


    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(memberDidChange, IntentFilter(NOTIF_MEMBER_DID_CHANGE))
        LocalBroadcastManager.getInstance(this).registerReceiver(teamUpdate, IntentFilter(NOTIF_TEAM_UPDATE))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(memberDidChange)
        super.onDestroy()
    }

    private fun setTitle(title: String) {
        //val titleView = toolbar.findViewById<View>(R.id.toolbar_title) as TextView
        //titleView.text = title
        toolbar_title.text = title
    }

    private val memberDidChange = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            _loginout()
        }
    }
    private val teamUpdate = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            refresh()
        }
    }

    private fun initMemberFunction() {
        menu_account_container.setOnClickListener {view ->
            val accountIntent = Intent(this, AccountActivity::class.java)
            startActivity(accountIntent)
        }
        menu_updatepassword_container.setOnClickListener { view ->
            val updatePasswordIntent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(updatePasswordIntent)
        }
    }

    private fun initTeamList() {
        val filter1: Array<Any> = arrayOf("channel", "=", CHANNEL)
        val filter2: Array<Any> = arrayOf("manager_id", "=", member.id)
        val filter: Array<Array<Any>> = arrayOf(filter1, filter2)

        TeamService.getList(this, "team", "name", 1, 100, filter) { success ->
            if (success) {
                //val teamLists: ArrayList<Team> = TeamService.dataLists
//                for (i in 0 until TeamService.dataLists.size) {
//                    println("id: ${TeamService.dataLists[i].id}, title: ${TeamService.dataLists[i].title}")
//                }
                this.menuTeamListAdapter = MenuTeamListAdapter(this, TeamService.dataLists,
                        { team -> goEditTeam(team.token) },
                        { team -> deleteTeam(team.token) },
                        { team -> goTeamTempPlayEdit(team.token) }
                )
                menu_team_list.adapter = this.menuTeamListAdapter

                val layoutManager = LinearLayoutManager(this)
                menu_team_list.layoutManager = layoutManager
                closeRefresh()
            }
        }
        menu_team_add.onClick {
            if (member.validate < 1) {
                Alert.show(this@MainActivity, "錯誤", "未通過EMail認證，無法新增球隊，認證完後，請先登出再登入")
            } else {
                goEditTeam()
            }
        }

        //menu_team_list.layoutManager = linearLayoutManager
    }

    private fun _loginout() {
        if (member.isLoggedIn) {
            _loginBlock()
        } else {
            _logoutBlock()
        }
    }
    private fun _loginBlock() {
        nicknameLbl.text = member.nickname
        loginBtn.text = "登出"
        registerBtn.visibility = View.INVISIBLE
        forgetPasswordBtn.visibility = View.INVISIBLE
        menu_member_container.visibility = View.VISIBLE
        menu_team_container.visibility = View.VISIBLE
        refreshLayout = menu_refresh
        initMemberFunction()
        refresh()
    }
    private fun _logoutBlock() {
        nicknameLbl.text = "未登入"
        loginBtn.text = "登入"
        registerBtn.visibility = View.VISIBLE
        forgetPasswordBtn.visibility = View.VISIBLE
        menu_member_container.visibility = View.INVISIBLE
        menu_team_container.visibility = View.INVISIBLE
    }

    private fun setMenuWidth() {
        val l = drawer.layoutParams
        val w = screenWidth * 0.85
        l.width = w.toInt()
        drawer.layoutParams = l
    }

    override fun refresh() {
        super.refresh()
        initTeamList()
        setRefreshListener()
    }


    fun loginBtnPressed(view: View) {
        if (member.isLoggedIn) {
            if (member.uid.length > 0 && member.social == "fb") {
                FacebookSdk.sdkInitialize(getApplicationContext());
                AppEventsLogger.activateApp(this);
                LoginManager.getInstance().logOut()
            }
            MemberService.logout()
            val memberDidChange = Intent(NOTIF_MEMBER_DID_CHANGE)
            LocalBroadcastManager.getInstance(this).sendBroadcast(memberDidChange)
        } else {
            goLogin()
        }
    }

    fun registerBtnPressed(view: View){
        goRegister()
    }

    fun forgetpasswordBtnPressed(view: View) {
        val forgetPasswordIntent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(forgetPasswordIntent)
    }

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


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setTabIconSelected(tab: TabLayout.Tab) {
        tab.icon!!.setColorFilter(getColor(R.color.MY_GREEN), PorterDuff.Mode.SRC_IN)
        val title = tab.text!!.toString()
        setTitle(title)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setTabIconUnSelected(tab: TabLayout.Tab) {
        tab.icon!!.setColorFilter(getColor(R.color.WHITE), PorterDuff.Mode.SRC_IN)
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
}
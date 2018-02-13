package com.sportpassword.bm.Controllers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.NOTIF_MEMBER_DID_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.tab.view.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.login_out.*
import kotlinx.android.synthetic.main.menu_member_function.*
import kotlinx.android.synthetic.main.nav_header_main.*
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

    private var mSectionPagerAdapter: SectionsPagerAdapter? = null

    val tabsTextArr: Array<String> = arrayOf<String>("臨打", "教練", "球隊", "更多")
    val tabsIconArr: Array<String> = arrayOf<String>("tempplay", "coach", "team", "more")
    var mainActivity: MainActivity? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        linearLayoutManager = LinearLayoutManager(this)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        //toggle.isDrawerIndicatorEnabled = false
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(memberDidChange, IntentFilter(NOTIF_MEMBER_DID_CHANGE))



//        try {
//        val info = getPackageManager().getPackageInfo(
//                "com.sportpassword.bm",
//                PackageManager.GET_SIGNATURES);
//        for (signature in info.signatures) {
//            var md = MessageDigest.getInstance("SHA");
//            md.update(signature.toByteArray());
//            println("KeyHash: ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}");
//            }
//    } catch (e: Exception) {
//
//    }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        pager.adapter = mSectionPagerAdapter

        for (i in tabsTextArr.indices) {
            val text: String = tabsTextArr[i]
            val icon: Int = resources.getIdentifier(tabsIconArr[i], "drawable", packageName)
            tabs.addTab(tabs.newTab().setText(text).setIcon(icon))
        }
        val tab: TabLayout.Tab? = tabs.getTabAt(0)
        setTabIconSelected(tab!!)


        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        //tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
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
        tab.icon!!.setColorFilter(getColor(R.color.MY_GREEN), PorterDuff.Mode.SRC_IN)
        val title = tab.text!!.toString()
        setTitle(title)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setTabIconUnSelected(tab: TabLayout.Tab) {
        tab.icon!!.setColorFilter(getColor(R.color.WHITE), PorterDuff.Mode.SRC_IN)
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

    private fun initMemberFunction() {
        menu_account_container.setOnClickListener {view ->
            println("aaa")
        }
        menu_updatepassword_container.setOnClickListener { view ->
            
        }
    }

    private fun initTeamList() {
        menu_team_list.layoutManager = linearLayoutManager
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
        initMemberFunction()
        initTeamList()
    }
    private fun _logoutBlock() {
        nicknameLbl.text = "未登入"
        loginBtn.text = "登入"
        registerBtn.visibility = View.VISIBLE
        forgetPasswordBtn.visibility = View.VISIBLE
        menu_member_container.visibility = View.INVISIBLE
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return tabsTextArr.size
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.tab, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments!!.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
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


}



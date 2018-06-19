package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.github.babedev.dexter.dsl.runtimePermission
import com.sportpassword.bm.Adapters.TempPlaySignupOneAdapter
import com.sportpassword.bm.Manifest
import com.sportpassword.bm.R
import com.sportpassword.bm.R.id.data_list
import com.sportpassword.bm.Services.MemberService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.MEMBER_ARRAY
import com.sportpassword.bm.Utilities.MOBILE_KEY
import com.sportpassword.bm.Utilities.NAME_KEY
import kotlinx.android.synthetic.main.activity_temp_play_signup_one_vc.*
import org.jetbrains.anko.makeCall
import org.jetbrains.anko.toast

class TempPlaySignupOneVC : BaseActivity() {

    var memberToken: String = ""
    var team_name: String = ""
    var team_id: Int = -1
    var near_date: String = ""

    var isTeamManager: Boolean = false
    var memberMobile: String = ""
    var memberOne: ArrayList<HashMap<String, HashMap<String, Any>>> = arrayListOf()
    var keys: ArrayList<String> = arrayListOf("team", NAME_KEY, MOBILE_KEY, "black_list")

    lateinit var tempPlaySignupOneAdapter: TempPlaySignupOneAdapter

    val REQUEST_PHONE_CALL = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_play_signup_one_vc)

        memberToken = intent.getStringExtra("token")
        team_name = intent.getStringExtra("title")
        team_id = intent.getIntExtra("id", -1)
        near_date = intent.getStringExtra("near_date")
//        println(memberToken)
//        println(team_name)
//        println(team_id)
//        println(near_date)

        refresh()
    }

    override fun refresh() {
        super.refresh()
        val loading = Loading.show(this)
        _getTeamManagerList { success ->
            for (i in 0..dataLists.size-1) {
                val list = dataLists[i]
                if (list.id == team_id) {
                    isTeamManager = true
                    break
                }
            }
        }
        _getMemberOne(memberToken) { success ->
            loading.dismiss()
            if (success) {
                val one = MemberService.one
                if (one == null) {
                    warning("無法取得該會員資訊，請稍後再試")
                } else {
                    initMemberOne()
                    for ((idx, key) in keys.withIndex()) {
                        val keys1: Iterator<String> = one.keys()
                        while (keys1.hasNext()) {
                            val key1: String = keys1.next().toString()
                            if (key == key1) {
                                memberOne[idx][key]!!["value"] = one[key].toString()
                                break
                            }
                        }
                    }
                    //println(memberOne)
                    setMyTitle(one[NAME_KEY].toString())
                    tempPlaySignupOneAdapter = TempPlaySignupOneAdapter(this, { itemKey, mobile ->
                        //println(itemKey)
                        if (itemKey == MOBILE_KEY) {
                            //println(isTeamManager)
                            if (!isTeamManager) {
                                warning("您不是此球隊管理員，所以無法檢視並撥打球友的電話")
                            } else {
                                memberMobile = mobile
                                info("球友電話是："+mobile,"取消","撥打電話", {
                                    grantCallPhonePermission()
                                })
                            }
                        }
                    })
                    tempPlaySignupOneAdapter.lists = memberOne
                    tempPlaySignupOneAdapter.keys = keys
                    data_list.adapter = tempPlaySignupOneAdapter
                    val layoutManager = LinearLayoutManager(this)
                    data_list.layoutManager = layoutManager
                }
            } else {
                warning(MemberService.msg)
            }
        }
    }

    fun grantCallPhonePermission() {
        val p = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (p != PackageManager.PERMISSION_GRANTED) {
            warning("沒有同意app撥打電話的權限，因此無法使用此功能")
        } else {
            makeCall(memberMobile)
        }
    }

    private fun initMemberOne() {
        memberOne.clear()
        memberOne.add(hashMapOf("team" to hashMapOf("title" to team_name, "value" to near_date, "more" to false)))

        for (key in keys) {
            var title: String = ""
            var icon: String = ""
            var existMember: Boolean = false // only if key exist member array add to member one

            for ((key1, value1) in MEMBER_ARRAY) {
                if (key1 == key) {
                    title = value1["text"]!!
                    icon = value1["icon"]!!
                    existMember = true
                }
            }
            if (existMember) {
                var _tmp: HashMap<String, Any> = hashMapOf("icon" to icon,"title" to title,"value" to "","more" to false)
                if (key == MOBILE_KEY) {
                    _tmp["more"] = true
                }
                val tmp:HashMap<String, HashMap<String, Any>> = hashMapOf(key to _tmp)
                memberOne.add(tmp)
            }
        }
        //memberOne.add(hashMapOf("black_list" to hashMapOf("title" to "加入黑名單", "more" to true)))
    }
}


















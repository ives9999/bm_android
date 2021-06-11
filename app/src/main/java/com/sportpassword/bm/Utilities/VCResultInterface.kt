package com.sportpassword.bm.Utilities

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sportpassword.bm.Controllers.BaseActivity

interface VCResultInterface {

    var launcherDelegate: BaseActivity

    var launcher: ActivityResultLauncher<Intent>

    fun selectCityResult(delegate: BaseActivity?=null) {
        launcher = launcherDelegate.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) {

                if (res.data != null) {
                    val i: Intent? = res.data

                    if (i != null) {
                        val key: String = CITY_KEY
                        var selected: String = ""
                        if (i.hasExtra("selected")) {
                            selected = i.getStringExtra("selected")!!
                        }

                        //activity
                        if (delegate != null) {
                            delegate.singleSelected(key, selected)
                        } else {
                            //fragment
                            var able_type = "course"
                            if (i.hasExtra("able_type")) {
                                able_type = i.getStringExtra("able_type")!!
                            }
                            val f = launcherDelegate.getFragment()
                            if (f != null) {
                                f.singleSelected(key, selected)
                            }
                        }
                    }
                }
            }
        }
    }
}
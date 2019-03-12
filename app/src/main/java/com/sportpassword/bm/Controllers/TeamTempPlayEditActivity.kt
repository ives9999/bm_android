package com.sportpassword.bm.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.Menu
import android.view.View
import android.widget.EditText
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_team_temp_play_edit.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import kotlinx.android.synthetic.main.mask.*

class TeamTempPlayEditActivity : BaseActivity() {

    var teamToken = ""
    val model: Team = Team(0, "", "", "")
    var oldQuantity: Int = 0
    var oldStatus: Boolean = false
    var isKeyboardShow: Boolean = false
    var keyboardView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_temp_play_edit)

        teamToken = intent.getStringExtra("token")
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.button, menu)
        return true
    }

    override fun refresh() {
        if (teamToken.length > 0) {
            Loading.show(mask)
            TeamService.tempPlay_onoff(this, teamToken) { success ->
                if (success) {
                    model.temp_play_data = TeamService.temp_play_data
                    //println(model.temp_play_data)
                    dataToField()
                    temp_play_quantity.setSelection(temp_play_quantity.length())
                    setEventListener()
                    val title: String = model.temp_play_data[NAME_KEY]!!["value"] as String + "臨打"
                    setMyTitle(title)
                }
                Loading.hide(mask)
            }
        }
    }

    private fun setEventListener() {
        onoff.setOnCheckedChangeListener { compoundButton, b ->
            setStatus()
        }
//        onoff.onCheckedChange { buttonView, isChecked ->
//            setStatus()
//        }
        temp_play_quantity.setOnFocusChangeListener { _v, hasFocus ->
            val v = _v as EditText
            if (hasFocus && onoff.isChecked) {
                v.setSelection(v.length())
                _showKeyboard(v)
                isKeyboardShow = true
                keyboardView = v
            } else {
                _hideKeyboard(v)
                setQuantity()
            }
        }
        clearbutton_temp_play_quantity.setOnClickListener {
            temp_play_quantity.setText("")
        }
    }

    private fun dataToField() {
        val status = model.temp_play_data[TEAM_TEMP_STATUS_KEY]!!["value"] as String
//        println(status)
        if (status == "on") {
            onoff.isChecked = true
            oldStatus = true
        } else {
            onoff.isChecked = false
            oldStatus = false
        }
        val quantity = model.temp_play_data[TEAM_TEMP_QUANTITY_KEY]!!["value"] as Int
        oldQuantity = quantity
        if (quantity > 0) {
            temp_play_quantity.setText(quantity.toString())
        }
        if (status == "off") {
            quantity_container.visibility = View.INVISIBLE
        }
    }

    private fun fieldToData() {
        setStatus()
        setQuantity()
    }

    fun submit(view: View) {
        var isPass = true
        if (onoff.isChecked) {
            val q = temp_play_quantity.text.toString()
            if (q.length == 0) {
                isPass = false
            } else {
                val q1 = q.toIntOrNull()
                if (q1 == null) {
                    isPass = false
                } else {
                    if (q1 <= 0) {
                        isPass = false
                    }
                }
            }
        }
        if (!isPass) {
            Alert.show(this, "警告", "必須填寫臨打人數或臨打人數須大於0")
        } else {
            if (isKeyboardShow && keyboardView != null) {
                _hideKeyboard(keyboardView!!)
            }
            fieldToData()
            //println(model.temp_play_data)
            val params: MutableMap<String, Any> = model.makeTempPlaySubmitArr()
            if (params.size == 0) {
                Alert.show(this, "提示", "沒有修改任何資料")
            } else {
                TeamService.update(this, "team", params, "") { success ->

                    if (success) {
                        if (TeamService.success) {
                            val id: Int = TeamService.id
                            model.data[ID_KEY]!!["value"] = id
                            model.data[ID_KEY]!!["show"] = id
                            Alert.update(this, "UPDATE", {
                                val teamUpdate = Intent(NOTIF_TEAM_UPDATE)
                                LocalBroadcastManager.getInstance(this).sendBroadcast(teamUpdate)
                                finish()
                            })
                        } else {
                            Alert.show(this, "錯誤", TeamService.msg)
                        }
                    } else {
                        Alert.show(this, "錯誤", TeamService.msg)
                    }

                }
            }
        }
    }

    private fun setStatus() {
        val isChecked = onoff.isChecked
        if (isChecked) {
            quantity_container.visibility = View.VISIBLE
            model.temp_play_data[TEAM_TEMP_STATUS_KEY]!!["value"] = "on"
        } else {
            quantity_container.visibility = View.INVISIBLE
            model.temp_play_data[TEAM_TEMP_STATUS_KEY]!!["value"] = "off"
        }
        if (isChecked != oldStatus) {
            model.temp_play_data[TEAM_TEMP_STATUS_KEY]!!["change"] = true
        }
    }
    private fun setQuantity() {
        if (temp_play_quantity.visibility == View.VISIBLE) {
            val quantity: Int = temp_play_quantity.text.toString().toInt()
            if (quantity != this.oldQuantity) {
                model.temp_play_data[TEAM_TEMP_QUANTITY_KEY]!!["value"] = quantity
                model.temp_play_data[TEAM_TEMP_QUANTITY_KEY]!!["change"] = true
            }
        }
    }
}

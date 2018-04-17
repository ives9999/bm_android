package com.sportpassword.bm.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.EditText
import com.sportpassword.bm.Models.Team
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.activity_team_temp_play_edit.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

class TeamTempPlayEditActivity : BaseActivity() {

    var teamToken = ""
    val model: Team = Team(0, "", "", "")
    var oldQuantity: Int = 0
    var oldStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_temp_play_edit)

        onoff.onCheckedChange { buttonView, isChecked ->
            //println(isChecked)
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
        temp_play_quantity.setOnFocusChangeListener { _v, hasFocus ->
            val v = _v as EditText
            if (!hasFocus) {
                _hideKeyboard(v)
                val quantity: Int = v.text.toString().toInt()
                if (quantity != this.oldQuantity) {
                    model.temp_play_data[TEAM_TEMP_QUANTITY_KEY]!!["value"] = quantity
                    model.temp_play_data[TEAM_TEMP_QUANTITY_KEY]!!["change"] = true
                }
            } else {
                v.setSelection(v.length())
                _showKeyboard(v)
            }
        }
        teamToken = intent.getStringExtra("token")
        refresh()
    }

    override fun refresh() {
        if (teamToken.length > 0) {
            val l = Loading.show(this)
            TeamService.tempPlay_onoff(this, teamToken) { success ->
                if (success) {
                    model.temp_play_data = TeamService.temp_play_data
                    //println(model.temp_play_data)
                    dataToField()
                    l.dismiss()
                }
            }
        }
    }

    fun dataToField() {
        val status = model.temp_play_data[TEAM_TEMP_STATUS_KEY]!!["value"] as String
        if (status == "on") {
            onoff.isChecked = true
            oldStatus = true
        } else {
            onoff.isChecked = false
            oldStatus = false
        }
        val quantity = model.temp_play_data[TEAM_TEMP_QUANTITY_KEY]!!["value"] as Int
        oldQuantity = quantity
        temp_play_quantity.setText(quantity.toString())
        if (status == "off") {
            quantity_container.visibility = View.INVISIBLE
        }
    }

    fun submit(view: View) {
        val params: MutableMap<String, Any> = model.makeTempPlaySubmitArr()
        if (params.size == 0) {
            Alert.show(this, "提示", "沒有修改任何資料")
        } else {
            TeamService.update(this, "team", params, "") { success ->

                if (success) {
                    if (TeamService.success) {
                        val id: Int = TeamService.id
                        model.data[TEAM_ID_KEY]!!["value"] = id
                        model.data[TEAM_ID_KEY]!!["show"] = id
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

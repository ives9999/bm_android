package com.sportpassword.bm.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.sportpassword.bm.Adapters.EditTeamItemAdapter
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.activity_edit_team_item.*

class EditTeamItemActivity : AppCompatActivity() {

    lateinit var key: String
    lateinit var editTeamItemAdapter: EditTeamItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team_item)

        key = intent.getStringExtra("key")
        //println(key)

        val daysLists: ArrayList<MutableMap<String, String>> = arrayListOf(
                mutableMapOf(
                        "value" to "1", "show" to "星期一", "checked" to "false"
                ),
                mutableMapOf(
                        "value" to "2", "show" to "星期二", "checked" to "false"
                ),
                mutableMapOf(
                        "value" to "3", "show" to "星期三", "checked" to "false"
                ),
                mutableMapOf(
                        "value" to "4", "show" to "星期四", "checked" to "false"
                ),
                mutableMapOf(
                        "value" to "5", "show" to "星期五", "checked" to "false"
                ),
                mutableMapOf(
                        "value" to "6", "show" to "星期六", "checked" to "false"
                ),
                mutableMapOf(
                        "value" to "7", "show" to "星期日", "checked" to "false"
                )
        )
        editTeamItemAdapter = EditTeamItemAdapter(this, daysLists) { position, view ->
            //println(position)
            val checked: Boolean = !(daysLists[position]["checked"]!!.toBoolean())
            daysLists[position]["checked"] = checked.toString()
            println(daysLists)
        }
        teamedititem_container.adapter = editTeamItemAdapter
        val layoutManager = LinearLayoutManager(this)
        teamedititem_container.layoutManager = layoutManager
    }
}

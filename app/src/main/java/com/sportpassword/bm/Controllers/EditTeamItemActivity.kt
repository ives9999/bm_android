package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sportpassword.bm.Adapters.EditTeamItemAdapter
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.DAYS
import com.sportpassword.bm.Utilities.TEAM_DAYS_KEY
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_edit_team_item.*

class EditTeamItemActivity() : AppCompatActivity() {

    lateinit var key: String
    lateinit var editTeamItemAdapter: EditTeamItemAdapter

    lateinit var daysLists: ArrayList<MutableMap<String, String>>
    val resDays: ArrayList<Day> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team_item)

        key = intent.getStringExtra("key")
        //println(key)
        if (key == TEAM_DAYS_KEY) {

            val days = intent.getIntArrayExtra("days")
            daysLists = arrayListOf()
            for (i in 1..7) {
                val tmp: Map<String, Any> = DAYS[i-1]
                var checked: Boolean = false
                for (j in 0..days.size-1) {
                    if (i == days.get(j)) {
                        checked = true
                        break
                    }
                }
                val m: MutableMap<String, String> = mutableMapOf("value" to i.toString(), "text" to tmp["text"]!! as String, "checked" to checked.toString())
                daysLists.add(m)
            }
        }
        editTeamItemAdapter = EditTeamItemAdapter(this, daysLists) { position ->
            //println(position)
            val checked: Boolean = !(daysLists[position]["checked"]!!.toBoolean())
            daysLists[position]["checked"] = checked.toString()
            //println(daysLists)
            setDay(position)
        }
        teamedititem_container.adapter = editTeamItemAdapter
        val layoutManager = LinearLayoutManager(this)
        teamedititem_container.layoutManager = layoutManager
    }

    fun setDay(idx: Int) {
        val checked: Boolean = daysLists[idx]["checked"]!!.toBoolean()
        val day: Int = daysLists[idx]["value"]!!.toInt()
        if (checked) {
            val text: String = daysLists[idx]["text"]!!
            val d: Day = Day(text, day)
            resDays.add(d)
        } else {
            val idx: Int = -1
            for (i in 0..resDays.size-1) {
                if (day == resDays[i].day) {
                    idx == i
                    break
                }
            }
            if (idx > 0) {
                resDays.removeAt(idx)
            }
        }
    }

    fun submit(view: View) {
        val intent = Intent()
        intent.putExtra("key", key)
        if (key == TEAM_DAYS_KEY) {
            intent.putParcelableArrayListExtra("days", resDays)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

@Parcelize
data class Day(val text: String, val day: Int) : Parcelable {

    companion object : Parceler<Day> {
        override fun Day.write(dest: Parcel, flags: Int) = with(dest) {
            writeString(text)
            writeInt(day)
        }

        override fun create(source: Parcel): Day = Day(source)
    }

    constructor(source: Parcel): this(
    source.readString(),
    source.readInt()
    )

    override fun describeContents() = 0

}




















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
import com.sportpassword.bm.Models.DEGREE
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_edit_team_item.*

class EditTeamItemActivity() : AppCompatActivity() {

    lateinit var key: String
    lateinit var editTeamItemAdapter: EditTeamItemAdapter

    var daysLists: ArrayList<MutableMap<String, String>> = arrayListOf()
    val resDays: ArrayList<Day> = arrayListOf()
    var time: String = ""
    var resDegrees: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team_item)

        key = intent.getStringExtra("key")
        //println(key)
        if (key == TEAM_DAYS_KEY) {

            val days = intent.getIntArrayExtra("value")
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
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            time = intent.getStringExtra("value")
            //println(value)
            val times: ArrayList<String> = arrayListOf("07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00",
                "12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30",
                "18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00")
            for (i in 0..times.size-1) {
                var checked: Boolean = if (times[i] == time) true else false
                val m: MutableMap<String, String> = mutableMapOf("value" to times[i], "text" to times[i], "checked" to checked.toString())
                daysLists.add(m)
            }
        } else if (key == TEAM_DEGREE_KEY) {
            val value: Array<String> = intent.getStringArrayExtra("value")
            val allDegree: Map<String, String> = DEGREE.all()
            for ((k, v) in allDegree) {
                var checked: Boolean = false
                for (i in 0..value.size-1) {
                    if (k == value.get(i)) {
                        checked = true
                        resDegrees.add(k)
                        break
                    }
                }
                val m: MutableMap<String, String> = mutableMapOf("value" to k, "text" to v, "checked" to checked.toString())
                daysLists.add(m)
            }
        }
        editTeamItemAdapter = EditTeamItemAdapter(this, key, daysLists) { position ->
            //println(position)
            val checked: Boolean = !(daysLists[position]["checked"]!!.toBoolean())
            //println(daysLists)
            if (key == TEAM_DAYS_KEY) {
                daysLists[position]["checked"] = checked.toString()
                setDay(position)
            } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
                time = daysLists[position]["value"]!!
                submit(View(this))
            } else if (key == TEAM_DEGREE_KEY) {
                daysLists[position]["checked"] = checked.toString()
                setDegree(position)
            }
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
    fun setDegree(idx: Int) {
        val checked: Boolean = daysLists[idx]["checked"]!!.toBoolean()
        val degree: String = daysLists[idx]["value"]!!
        if (checked) {
            resDegrees.add(degree)
        } else {
            resDegrees.remove(degree)
        }
    }

    fun submit(view: View) {
        val intent = Intent()
        intent.putExtra("key", key)
        if (key == TEAM_DAYS_KEY) {
            intent.putParcelableArrayListExtra("days", resDays)
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            intent.putExtra("time", time)
        } else if (key == TEAM_DEGREE_KEY) {
            val res: Array<String> = resDegrees.toTypedArray()
            intent.putExtra("degree", res)
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




















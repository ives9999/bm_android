package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import com.sportpassword.bm.Adapters.EditTeamItemAdapter
import com.sportpassword.bm.Models.Arena
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_edit_team.*
import kotlinx.android.synthetic.main.activity_edit_team_item.*
import kotlinx.android.synthetic.main.tab.*

class EditTeamItemActivity() : BaseActivity() {

    lateinit var key: String
    lateinit var editTeamItemAdapter: EditTeamItemAdapter

    var daysLists: ArrayList<MutableMap<String, String>> = arrayListOf()
    var resDays: ArrayList<Day> = arrayListOf()
    var time: String = ""
    var resDegrees: ArrayList<String> = arrayListOf()
    var oldCity: Int = 0
    var oldArena: Int = 0
    var resArena_id: Int = 0
    var resArena_name: String = ""
    lateinit var allCitys: ArrayList<City>
    lateinit var arenas: ArrayList<Arena>

    //來源的程式：目前有team的setup跟search
    var source: String = "setup"
    //縣市的類型：all所有的縣市，simple比較簡單的縣市
    var type: String = "all"
    //選擇的類型：just one單選，multi複選
    var select: String = "just one"

    var citys: ArrayList<City> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team_item)

        hidekeyboard(teamedititem_constraint)

        key = intent.getStringExtra("key")
        //println(key)
        if (intent.hasExtra("source")) {
            source = intent.getStringExtra("source")
        }
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }
        if (intent.hasExtra("select")) {
            select = intent.getStringExtra("select")
        }
        if (key == TEAM_DAYS_KEY || key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY || key == TEAM_DEGREE_KEY || key == TEAM_CITY_KEY || key == TEAM_ARENA_KEY) {
            content_container.visibility = View.INVISIBLE
            val layout = findViewById(R.id.teamedititem_constraint) as ConstraintLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(layout)
            constraintSet.connect(R.id.submit_container, ConstraintSet.TOP, R.id.teamedititem_container, ConstraintSet.BOTTOM, 32)
            constraintSet.applyTo(layout)

        }
        if (key == TEAM_DAYS_KEY) {
            setMyTitle("星期幾")
            val days = intent.getIntArrayExtra("value")
            for (i in 1..7) {
                val tmp: Map<String, Any> = DAYS[i-1]
                var checked: Boolean = false
                if (days != null) {
                    for (j in 0..days.size - 1) {
                        if (i == days.get(j)) {
                            checked = true
                            break
                        }
                    }
                }
                val m: MutableMap<String, String> = mutableMapOf("value" to i.toString(), "text" to tmp["text"]!! as String, "checked" to checked.toString())
                daysLists.add(m)
            }
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            if (key == TEAM_PLAY_START_KEY) {
                setMyTitle("開始時間")
            } else {
                setMyTitle("結束時間")
            }
            //println(value)
            time = intent.getStringExtra("value")
            val times: ArrayList<String> = arrayListOf("07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00",
                "12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30",
                "18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00")
            for (i in 0..times.size-1) {
                val checked = if (times[i] == time) true else false
                val m: MutableMap<String, String> = mutableMapOf("value" to times[i], "text" to times[i], "checked" to checked.toString())
                daysLists.add(m)
            }
        } else if (key == TEAM_DEGREE_KEY) {
            setMyTitle("球隊程度")
            val value: ArrayList<String> = intent.getStringArrayListExtra("value")
            val allDegree: Map<String, String> = DEGREE.all()
            for ((k, v) in allDegree) {
                var checked: Boolean = false
                if (value != null) {
                    for (i in 0..value.size - 1) {
                        if (k == value.get(i)) {
                            checked = true
                            resDegrees.add(k)
                            break
                        }
                    }
                }
                val m: MutableMap<String, String> = mutableMapOf("value" to k, "text" to v, "checked" to checked.toString())
                daysLists.add(m)
            }
        } else if (key == TEAM_CITY_KEY) {
            setMyTitle("縣市")
            oldCity = intent.getIntExtra("value", 0)
            citys = intent.getParcelableArrayListExtra("citys")
        } else if (key == TEAM_ARENA_KEY) {
            setMyTitle("球館")
            oldCity = intent.getIntExtra("city_id", 0)
            oldArena = intent.getIntExtra("arena_id", 0)
        } else {
            if (key == TEAM_TEMP_CONTENT_KEY) {
                setMyTitle("臨打說明")
            } else if (key == TEAM_CHARGE_KEY) {
                setMyTitle("收費說明")
            } else if (key == TEAM_CONTENT_KEY) {
                setMyTitle("球隊說明")
            }
            val value: String = intent.getStringExtra("value")
            content.setText(value)
            content.requestFocus()
            content.setSelection(content.text.length)
        }
        editTeamItemAdapter = EditTeamItemAdapter(this, key, daysLists) { position, checked ->
            //println(position)
            //val checked: Boolean = !(daysLists[position]["checked"]!!.toBoolean())
            //println(daysLists)
            if (key == TEAM_DAYS_KEY) {
                daysLists[position]["checked"] = checked.toString()
                setDay()
            } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
                time = daysLists[position]["value"]!!
                submit(View(this))
            } else if (key == TEAM_DEGREE_KEY) {
                daysLists[position]["checked"] = checked.toString()
                setDegree()
            } else if (key == TEAM_CITY_KEY) {
//                resCity_id = daysLists[position]["value"]!!.toInt()
//                resCity_name = daysLists[position]["text"]!!
                setCity(position)
                if (select == "just one") {
                    submit(View(this))
                }
            } else if (key == TEAM_ARENA_KEY) {
                resArena_id = daysLists[position]["value"]!!.toInt()
                resArena_name = daysLists[position]["text"]!!
                submit(View(this))
            }
        }
        teamedititem_container.adapter = editTeamItemAdapter
        val layoutManager = LinearLayoutManager(this)
        teamedititem_container.layoutManager = layoutManager
        if (key == TEAM_CITY_KEY) {
            TeamService.getCitys(this, type) { success ->
                allCitys = TeamService.citys
                for (i in 0..allCitys.size-1) {
                    val city = allCitys[i]
                    var checked: Boolean = false
                    for (j in 0..citys.size-1) {
                        if (city.id == citys[j].id) {
                            checked = true
                            break
                        }
                    }
                    val m: MutableMap<String, String> = mutableMapOf("value" to city.id.toString(), "text" to city.name, "checked" to checked.toString())
                    daysLists.add(m)
                }
                editTeamItemAdapter.notifyDataSetChanged()
            }
        } else if (key == TEAM_ARENA_KEY) {
            TeamService.getArenaByCityID(this, oldCity) { success ->
                arenas = TeamService.arenas
                for (i in 0..arenas.size-1) {
                    val arena = arenas[i]
                    val checked: Boolean = if (oldArena == arena.id) true else false
                    val m: MutableMap<String, String> = mutableMapOf("value" to arena.id.toString(), "text" to arena.title, "checked" to checked.toString())
                    daysLists.add(m)
                }
                editTeamItemAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.button, menu)
        return true
    }

    fun setCity(idx: Int) {
        val city = allCitys[idx]
        var exist: Boolean = false
        var i: Int = 0
        for ((idx, _city) in citys.withIndex()) {
            if (city.id == _city.id) {
                exist = true
                i = idx
                break
            }
        }
        if (exist) {
            citys.removeAt(i)
        } else {
            if (select == "just one") {
                citys.clear()
            }
            citys.add(city)
        }
    }

    fun setDay() {
        resDays = arrayListOf()
        for (i in 0..daysLists.size-1) {
            val checked: Boolean = daysLists[i]["checked"]!!.toBoolean()
            val day: Int = daysLists[i]["value"]!!.toInt()
            if (checked) {
                val text: String = daysLists[i]["text"]!!
                val d: Day = Day(text, day)
                resDays.add(d)
            } else {
                val idx: Int = -1
                for (i in 0..resDays.size - 1) {
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
    }
    fun setDegree() {
        resDegrees = arrayListOf()
        for (i in 0..daysLists.size-1) {
            val checked: Boolean = daysLists[i]["checked"]!!.toBoolean()
            val degree: String = daysLists[i]["value"]!!
            if (checked) {
                resDegrees.add(degree)
            } else {
                resDegrees.remove(degree)
            }
        }
    }

    fun submit(view: View) {
        hideKeyboard()
        val intent = Intent()
        intent.putExtra("key", key)
        if (key == TEAM_DAYS_KEY) {
            intent.putParcelableArrayListExtra("days", resDays)
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            intent.putExtra("time", time)
        } else if (key == TEAM_DEGREE_KEY) {
            val res: Array<String> = resDegrees.toTypedArray()
            intent.putExtra("degree", res)
        } else if (key == TEAM_CITY_KEY) {
            if (select == "just one" && citys.size == 0) {
                return
            }
            intent.putParcelableArrayListExtra("citys", citys)
        } else if (key == TEAM_ARENA_KEY) {
            intent.putExtra("id", resArena_id)
            intent.putExtra("name", resArena_name)
        } else {
            val res: String = content.text.toString()
            intent.putExtra("res", res)
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

@Parcelize
data class Arena(val id: Int, val name: String): Parcelable




















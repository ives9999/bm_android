package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.View
import com.sportpassword.bm.Adapters.EditTeamItemAdapter
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_edit_team_item.*
import kotlinx.android.synthetic.main.activity_edit_team_item_adapter.view.*
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_edit_team_item_adapter.*
import kotlinx.android.synthetic.main.search_section_item.*
import kotlinx.android.synthetic.main.mask.*

class EditTeamItemActivity() : BaseActivity() {

    lateinit var key: String
    lateinit var editTeamItemAdapter: EditTeamItemAdapter
    lateinit var arenaAdapter: GroupAdapter<ViewHolder>

    var resDegrees: ArrayList<String> = arrayListOf()
    var resArena_id: Int = 0
    var resArena_name: String = ""
    var allCitys: ArrayList<City> = arrayListOf()
    var citysandarenas: HashMap<Int, HashMap<String, Any>> = hashMapOf()
    var allDays = Global.days

    var dataList: ArrayList<MutableMap<String, String>> = arrayListOf()

    //來源的程式：目前有team的setup跟search
    var source: String = "setup"
    //縣市的類型：all所有的縣市，simple比較簡單的縣市
    var type: String = "all"
    //選擇的類型：just one單選，multi複選
    var select: String = "just one"

    var citys: ArrayList<City> = arrayListOf()
    var citysForArena: ArrayList<Int> = arrayListOf()
    var arenas: ArrayList<Arena> = arrayListOf()
    var selectedDays: ArrayList<Int> = arrayListOf()
    var degrees: ArrayList<DEGREE> = arrayListOf()

    //type: .play_start or .play_end, time: "09:00:00"
    var times: HashMap<String, Any> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team_item)

        hidekeyboard(teamedititem_constraint)
        Loading.show(mask)

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
            selectedDays = intent.getIntegerArrayListExtra("days")
            for (i in 0..allDays.size-1) {
                val day = allDays[i]
                var checked = false
                for (j in 0..selectedDays.size-1) {
                    if (day.get("value")!! as Int == selectedDays[j]) {
                        allDays[i]["checked"] = true
                        checked = true
                        break
                    }
                }
                val value = day.get("value")!! as Int
                val m: MutableMap<String, String> = mutableMapOf("value" to value.toString(), "text" to day.get("text")!! as String, "checked" to checked.toString())
                dataList.add(m)
            }
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            if (key == TEAM_PLAY_START_KEY) {
                setMyTitle("開始時間")
            } else {
                setMyTitle("結束時間")
            }
            //println(value)
            times = intent.getSerializableExtra("times") as HashMap<String, Any>
            val allTimes: ArrayList<String> = arrayListOf("07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00",
                "12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30",
                "18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00")
            for (i in 0..allTimes.size-1) {
                var time = ""
                if (times.containsKey("time")) {
                    time = times["time"] as String
                }
                val checked = if (allTimes[i] == time) true else false
                val m: MutableMap<String, String> = mutableMapOf("value" to allTimes[i], "text" to allTimes[i], "checked" to checked.toString())
                dataList.add(m)
            }
        } else if (key == TEAM_DEGREE_KEY) {
            setMyTitle("球隊程度")
            degrees = intent.getSerializableExtra("degrees") as ArrayList<DEGREE>
            val allDegree: Map<String, String> = DEGREE.all()
            for ((k, v) in allDegree) {
                var checked: Boolean = false
                for (i in 0..degrees.size-1) {
                    if (k == DEGREE.toString(degrees[i])) {
                        checked = true
                        break
                    }
                }
                val m: MutableMap<String, String> = mutableMapOf("value" to k, "text" to v, "checked" to checked.toString())
                dataList.add(m)
            }
        } else if (key == TEAM_CITY_KEY) {
            setMyTitle("縣市")
//            oldCity = intent.getIntExtra("value", 0)
            citys = intent.getParcelableArrayListExtra("citys")
        } else if (key == TEAM_ARENA_KEY) {
            setMyTitle("球館")
//            oldCity = intent.getIntExtra("city_id", 0)
//            oldArena = intent.getIntExtra("arena_id", 0)
            citysForArena = intent.getIntegerArrayListExtra("citys_for_arena")
            arenas = intent.getParcelableArrayListExtra("arenas")
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

        if (key == TEAM_ARENA_KEY) {
            arenaAdapter = GroupAdapter()
            arenaAdapter.setOnItemClickListener { item, view ->
                val arenaItem = item as ArenaItem
                val arena = arenaItem.arena

                var checked = false
                if (view.mark1.visibility == View.INVISIBLE) {
                    checked = true
                }
                arenaItem.toggleClick(view, checked)

                if (select == "just one") {
                    arenas.clear()
                }
                setArena(arena)
                if (select == "just one") {
                    submit(View(this))
                }


            }
            teamedititem_container.adapter = arenaAdapter
        } else {
            editTeamItemAdapter = EditTeamItemAdapter(this, key, dataList) { position, checked ->
                //println(position)
                //val checked: Boolean = !(daysLists[position]["checked"]!!.toBoolean())
                //println(daysLists)
                if (key == TEAM_DAYS_KEY) {
                    setDay(position)
                } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
                    val time = dataList[position]["value"]!!
                    setTime(time)
                    if (source == "setup" && !times.containsKey("time")) {
                    } else {
                        submit(View(this))
                    }
                } else if (key == TEAM_DEGREE_KEY) {
                    val degree = dataList[position]["value"]!!
                    setDegree(degree)
                } else if (key == TEAM_CITY_KEY) {
                    setCity(position)
                    if (select == "just one") {
                        submit(View(this))
                    }
                }
            }
            teamedititem_container.adapter = editTeamItemAdapter
            Loading.hide(mask)
        }

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
                    dataList.add(m)
                }
                editTeamItemAdapter.notifyDataSetChanged()
                Loading.hide(mask)
            }
        } else if (key == TEAM_ARENA_KEY) {
            TeamService.getArenaByCityIDs(this, citysForArena, type) { success ->
                if (success) {
                    citysandarenas = TeamService.citysandarenas
//                    println(citysandarenas)
                    val keys = citysandarenas.keys
                    keys.forEach {
                        val city_name = citysandarenas.get(it)!!.get("name") as String
                        val expandableGroup = ExpandableGroup(ArenaSection(city_name), true)
                        val rows = citysandarenas.get(it)!!.get("rows") as ArrayList<HashMap<String, Any>>
                        var _rows: ArrayList<ArenaItem> = arrayListOf()
                        for (i in 0..rows.size-1) {
                            val arena = getArena(it, i)
                            var checked = false
                            for (j in 0..arenas.size-1) {
                                if (arena.id == arenas[j].id) {
                                    checked = true
                                    break
                                }
                            }
                            val arenaItem = ArenaItem(this, arena, checked)
                            _rows.add(arenaItem)
                        }
                        expandableGroup.add(Section(_rows))
                        arenaAdapter.add(expandableGroup)
                    }

//                    arenaAdapter.notifyDataSetChanged()
                    Loading.hide(mask)
                } else {
                    println(TeamService.msg)
                }
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

    fun getArena(city_id: Int, idx: Int): Arena {
        val rows = citysandarenas.get(city_id)!!.get("rows") as ArrayList<HashMap<String, Any>>
        val row = rows[idx]

        return Arena(row.get("id")!! as Int, row.get("name")!! as String)
    }
    fun setArena(arena: Arena) {
        var isExist = false
        var idx = -1
        for (i in 0..arenas.size-1) {
            if (arenas[i].id == arena.id) {
                isExist = true
                idx = i
                break
            }
        }

        if (isExist) {
            if (idx >= 0) {
                arenas.removeAt(idx)
            }
        } else {
            arenas.add(arena)
        }
    }

    fun setDay(position: Int) {
        if (allDays[position].containsKey("checked")) {
            allDays[position]["checked"] = !(allDays[position]["checked"] as Boolean)
        } else {
            allDays[position]["checked"] = true
        }

        var day = allDays[position]
        var isExist = false
        var idx = -1
        for (i in 0..selectedDays.size-1) {
            if (day.get("value")!! as Int == selectedDays[i]) {
                isExist = true
                idx = i
                break
            }
        }
        if (isExist) {
            selectedDays.removeAt(idx)
        } else {
            selectedDays.add(day.get("value")!! as Int)
        }
    }

    fun setTime(time: String) {
        if (times.containsKey("time") && (time == times["time"]!! as String)) {
            times.remove("time")
        } else {
            times["time"] = time
        }
    }

    fun setDegree(_degree: String) {
        val degree = DEGREE.fromEnglish(_degree)
        var isExist: Boolean = false
        var idx: Int = -1
        for (i in 0..degrees.size-1) {
            if (degree == degrees[i]) {
                isExist = true
                idx = i
            }
        }
        if (isExist) {
            degrees.removeAt(idx)
        } else {
            degrees.add(degree)
        }
    }

    fun submit(view: View) {
        hideKeyboard()
        val intent = Intent()
        intent.putExtra("key", key)
        if (key == TEAM_DAYS_KEY) {
            intent.putIntegerArrayListExtra("days", selectedDays)
        } else if (key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY) {
            if (source == "setup" && !times.containsKey("time")) {
                warning("請選擇時段")
                return
            }
            intent.putExtra("times", times)
        } else if (key == TEAM_DEGREE_KEY) {
            if (source == "setup" && degrees.size == 0) {
                warning("請選擇程度")
                return
            }
            intent.putExtra("degrees", degrees)
        } else if (key == TEAM_CITY_KEY) {
            if (select == "just one" && citys.size == 0) {
                return
            }
            intent.putParcelableArrayListExtra("citys", citys)
        } else if (key == TEAM_ARENA_KEY) {
            if (select == "just one" && arenas.size == 0) {
                return
            }
            intent.putParcelableArrayListExtra("arenas", arenas)
        } else {
            val res: String = content.text.toString()
            intent.putExtra("res", res)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

class ArenaItem(val context: Context, val arena: com.sportpassword.bm.Controllers.Arena, val checked: Boolean=false): Item() {

    val checkedColor = ContextCompat.getColor(context, R.color.MY_GREEN)
    val uncheckedColor = ContextCompat.getColor(context, R.color.WHITE)

    override fun getLayout() = R.layout.activity_edit_team_item_adapter

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.item1.text = arena.name
        toggleClick(viewHolder.itemView, checked)

//        viewHolder.itemView.onClick {
//
//            checked = !checked
//            toggleClick(viewHolder, checked)
//        }
    }

    fun toggleClick(itemView: View, checked: Boolean) {
        if (checked) {
            setSelectedStyle(itemView)
        } else {
            unSetSelectedStyle(itemView)
        }
    }

    fun setSelectedStyle(itemView: View) {
        val item1View = itemView.item1
        val mark1View = itemView.mark1
        item1View.setTextColor(checkedColor)
        mark1View.visibility = View.VISIBLE
        mark1View.setColorFilter(checkedColor)
    }
    fun unSetSelectedStyle(itemView: View) {
        val item1View = itemView.item1
        val mark1View = itemView.mark1
        item1View.setTextColor(uncheckedColor)
        mark1View.visibility = View.INVISIBLE
    }
}


class ArenaSection(val title: String): Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun getLayout() = R.layout.search_section_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.section_title.text = title
        viewHolder.collapse.setImageResource(getExpandIcon())

        viewHolder.section_container.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.collapse.setImageResource(getExpandIcon())
        }
//        println(position)
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getExpandIcon() =
            if (expandableGroup.isExpanded)
                R.drawable.to_down
            else
                R.drawable.to_right

}

//@Parcelize
//data class Day(val text: String, val day: Int) : Parcelable {
//
//    companion object : Parceler<Day> {
//        override fun Day.write(dest: Parcel, flags: Int) = with(dest) {
//            writeString(text)
//            writeInt(day)
//        }
//
//        override fun create(source: Parcel): Day = Day(source)
//    }
//
//    constructor(source: Parcel): this(
//    source.readString(),
//    source.readInt()
//    )
//
//    override fun describeContents() = 0
//
//}

@Parcelize
data class Arena(val id: Int, val name: String): Parcelable




















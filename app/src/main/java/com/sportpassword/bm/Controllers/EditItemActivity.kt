package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import android.view.Menu
import android.view.View
import android.widget.RelativeLayout
import com.sportpassword.bm.Adapters.EditTeamItemAdapter
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.Area
import com.sportpassword.bm.Models.SuperData
import com.sportpassword.bm.Models.Team
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_edit_team_item.*
import kotlinx.android.synthetic.main.activity_edit_team_item_adapter.view.*
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_edit_team_item_adapter.*
import kotlinx.android.synthetic.main.mask.*
import java.util.*

class EditItemActivity() : BaseActivity() {

    lateinit var key: String
    lateinit var editTeamItemAdapter: EditTeamItemAdapter
    lateinit var arenaAdapter: GroupAdapter<ViewHolder>
    lateinit var areaAdapter: GroupAdapter<ViewHolder>

    var allCitys: ArrayList<City> = arrayListOf()
    var citysandarenas: HashMap<Int, HashMap<String, Any>> = hashMapOf()
    var citysandareas: HashMap<Int, HashMap<String, Any>> = hashMapOf()
    var allWeekdays = Global.weekdays

    var dataList: ArrayList<MutableMap<String, String>> = arrayListOf()

    //來源的程式：目前有team的setup跟search
    var source: String = "setup"
    //縣市的類型：all所有的縣市，simple比較簡單的縣市
    var type: String = "all"
    //選擇的類型：just one單選，multi複選
    var select: String = "just one"
    //來源的頁面，有coach, team, arena, course 4種
    var page: String = ""

    var start: String = "07:00"
    var end: String = "23:00"
    //minute
    var interval: Int = 30
    var allTimes: ArrayList<String> = arrayListOf()

    var citysForArena: ArrayList<Int> = arrayListOf()
    var citysForArea: ArrayList<Int> = arrayListOf()
    var selectedWeekdays: ArrayList<Int> = arrayListOf()

    var model: SuperData = Team(0, "", "", "")

    //type: .play_start or .play_end, time: "09:00:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team_item)

        hidekeyboard(teamedititem_constraint)
        Loading.show(mask)

        //get intent
        key = intent.getStringExtra("key")
        //println(key)
        if (intent.hasExtra("source")) {
            source = intent.getStringExtra("source")
        }
        if (intent.hasExtra("page")) {
            page = intent.getStringExtra("page")
        }
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }
        if (intent.hasExtra("select")) {
            select = intent.getStringExtra("select")
        }
        if (key == TEAM_WEEKDAYS_KEY || key == TEAM_PLAY_START_KEY || key == TEAM_PLAY_END_KEY || key == TEAM_DEGREE_KEY || key == CITY_KEY || key == ARENA_KEY) {
            content_container.visibility = View.INVISIBLE
//            val layout = findViewById(R.id.teamedititem_constraint) as RelativeLayout
//            val constraintSet = ConstraintSet()
//            constraintSet.clone(layout)
//            constraintSet.connect(R.id.submit_container, ConstraintSet.TOP, R.id.teamedititem_container, ConstraintSet.BOTTOM, 32)
//            constraintSet.applyTo(layout)
        }
        if (key == TEAM_WEEKDAYS_KEY) {
            setMyTitle("星期幾")
            selectedWeekdays = intent.getIntegerArrayListExtra("weekdays")
            for (i in 0..allWeekdays.size-1) {
                val weekday = allWeekdays[i]
                var checked = false
                for (j in 0..selectedWeekdays.size-1) {
                    if (weekday.get("value")!! as Int == selectedWeekdays[j]) {
                        allWeekdays[i]["checked"] = true
                        checked = true
                        break
                    }
                }
                val value = weekday.get("value")!! as Int
                val m: MutableMap<String, String> = mutableMapOf("value" to value.toString(), "text" to weekday.get("text")!! as String, "checked" to checked.toString())
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
            if (intent.hasExtra("start")) {
                start = intent.getStringExtra("start")
            }
            if (intent.hasExtra("end")) {
                start = intent.getStringExtra("end")
            }
            if (intent.hasExtra("interval")) {
                interval = intent.getIntExtra("interval", 60)
            }
            var s = start.toDateTime("HH:mm")
            val e = end.toDateTime("HH:mm")
            allTimes.add(start)
            while (s.compareTo(e) < 0) {
                val cal = Calendar.getInstance()
                cal.time = s
                cal.add(Calendar.MINUTE, interval)
                s = cal.time
                allTimes.add(s.toMyString("HH:mm"))
            }
//            println(allTimes)

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
        } else if (key == CITY_KEY) {
            setMyTitle("縣市")
//            oldCity = intent.getIntExtra("value", 0)
            citys = intent.getParcelableArrayListExtra("citys")
        } else if (key == ARENA_KEY) {
            setMyTitle("球館")
            citysForArena = intent.getIntegerArrayListExtra("citys_for_arena")
            arenas = intent.getParcelableArrayListExtra("arenas")
        } else if (key == AREA_KEY) {
            setMyTitle("區域")
            citysForArea = intent.getIntegerArrayListExtra("citys_for_area")
            areas = intent.getParcelableArrayListExtra("areas")
        } else if (key==TEAM_TEMP_CONTENT_KEY || key==CHARGE_KEY || key==CONTENT_KEY || key==COACH_EXP_KEY || key==COACH_FEAT_KEY || key==COACH_LICENSE_KEY) {
            val type = model.contentKey2Type(key)
            setMyTitle(type.value)
            val value: String = intent.getStringExtra("value")
            content.setText(value)
            content.requestFocus()
            content.setSelection(content.text.length)
        }

        //init adapter
        if (key == ARENA_KEY) {
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
        } else if (key == AREA_KEY) {
            areaAdapter = GroupAdapter()
            areaAdapter.setOnItemClickListener { item, view ->
                val areaItem = item as AreaItem
                val area = areaItem.area

                var checked = false
                if (view.mark1.visibility == View.INVISIBLE) {
                    checked = true
                }
                areaItem.toggleClick(view, checked)

                if (select == "just one") {
                    areas.clear()
                }
                setArea(area)
                if (select == "just one") {
                    submit(View(this))
                }
            }
            teamedititem_container.adapter = areaAdapter
        } else {
            editTeamItemAdapter = EditTeamItemAdapter(this, key, dataList) { position, checked ->
                //println(position)
                //val checked: Boolean = !(daysLists[position]["checked"]!!.toBoolean())
                //println(daysLists)
                if (key == TEAM_WEEKDAYS_KEY) {
                    setWeekday(position)
                    if (select == "just one") {
                        submit(View(this))
                    }
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
                } else if (key == CITY_KEY) {
                    setCity(position)
                    if (select == "just one") {
                        submit(View(this))
                    }
                }
            }
            teamedititem_container.adapter = editTeamItemAdapter
            Loading.hide(mask)
        }

        //get remote date
        if (key == CITY_KEY) {
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
        } else if (key == ARENA_KEY) {
            TeamService.getArenaByCityIDs(this, citysForArena, type) { success ->
                if (success) {
                    citysandarenas = TeamService.citysandarenas
//                    println(citysandarenas)
                    val keys = citysandarenas.keys
                    keys.forEach {
                        val city_name = citysandarenas.get(it)!!.get("name") as String
                        val expandableGroup = ExpandableGroup(GroupSection(city_name), true)
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
        } else if (key == AREA_KEY) {
            TeamService.getAreaByCityIDs(this, citysForArea, type) { success ->
                if (success) {
                    citysandareas = TeamService.citysandareas
//                    println(citysandarenas)
                    val keys = citysandareas.keys
                    keys.forEach {
                        val city_name = citysandareas.get(it)!!.get("name") as String
                        val expandableGroup = ExpandableGroup(GroupSection(city_name), true)
                        val rows = citysandareas.get(it)!!.get("rows") as ArrayList<HashMap<String, Any>>
                        var _rows: ArrayList<AreaItem> = arrayListOf()
                        for (i in 0..rows.size-1) {
                            val area = getArea(it, i)
                            var checked = false
                            for (j in 0..areas.size-1) {
                                if (area.id == areas[j].id) {
                                    checked = true
                                    break
                                }
                            }
                            val areaItem = AreaItem(this, area, checked)
                            _rows.add(areaItem)
                        }
                        expandableGroup.add(Section(_rows))
                        areaAdapter.add(expandableGroup)
                    }

//                    arenaAdapter.notifyDataSetChanged()
                    Loading.hide(mask)
                } else {
                    println(TeamService.msg)
                }
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        if (select == "multi") {
//            menuInflater.inflate(R.menu.button, menu)
//        }
//        return true
//    }

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

    fun getArea(city_id: Int, idx: Int): Area {
        val rows = citysandareas.get(city_id)!!.get("rows") as ArrayList<HashMap<String, Any>>
        val row = rows[idx]

        return Area(row.get("id")!! as Int, row.get("name")!! as String)
    }
    fun setArea(area: Area) {
        var isExist = false
        var idx = -1
        for (i in 0..areas.size-1) {
            if (areas[i].id == area.id) {
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
            areas.add(area)
        }
    }

    fun setWeekday(position: Int) {
        if (allWeekdays[position].containsKey("checked")) {
            allWeekdays[position]["checked"] = !(allWeekdays[position]["checked"] as Boolean)
        } else {
            allWeekdays[position]["checked"] = true
        }

        var weekday = allWeekdays[position]
        var isExist = false
        var idx = -1
        for (i in 0..selectedWeekdays.size-1) {
            if (weekday.get("value")!! as Int == selectedWeekdays[i]) {
                isExist = true
                idx = i
                break
            }
        }
        if (isExist) {
            selectedWeekdays.removeAt(idx)
        } else {
            if (select == "just one") {
                selectedWeekdays.clear()
            }
            selectedWeekdays.add(weekday.get("value")!! as Int)
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
        intent.putExtra("page", page)
        if (key == TEAM_WEEKDAYS_KEY) {
            intent.putIntegerArrayListExtra("weekdays", selectedWeekdays)
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
        } else if (key == CITY_KEY) {
            if (select == "just one" && citys.size == 0) {
                return
            }
            intent.putParcelableArrayListExtra("citys", citys)
        } else if (key == ARENA_KEY) {
            if (select == "just one" && arenas.size == 0) {
                return
            }
            intent.putParcelableArrayListExtra("arenas", arenas)
        } else if (key == AREA_KEY) {
            if (select == "just one" && areas.size == 0) {
                return
            }
            intent.putParcelableArrayListExtra("areas", areas)
        } else {
            val res: String = content.text.toString()
            intent.putExtra("res", res)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun cancel(view: View) {
        prev()
    }
}

class ArenaItem(val context: Context, val arena: com.sportpassword.bm.Controllers.Arena, val checked: Boolean=false): Item() {

    val checkedColor = ContextCompat.getColor(context, R.color.MY_GREEN)
    val uncheckedColor = ContextCompat.getColor(context, R.color.WHITE)

    override fun getLayout() = R.layout.activity_edit_team_item_adapter

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.item1.text = arena.name
        toggleClick(viewHolder.itemView, checked)
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

class AreaItem(val context: Context, val area: Area, val checked: Boolean=false): Item() {

    val checkedColor = ContextCompat.getColor(context, R.color.MY_GREEN)
    val uncheckedColor = ContextCompat.getColor(context, R.color.WHITE)

    override fun getLayout() = R.layout.activity_edit_team_item_adapter

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.item1.text = area.name
        toggleClick(viewHolder.itemView, checked)
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




















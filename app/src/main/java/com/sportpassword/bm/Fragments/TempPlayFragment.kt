package com.sportpassword.bm.Fragments


import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sportpassword.bm.Adapters.SearchAdapter
import com.sportpassword.bm.Adapters.inter
import com.sportpassword.bm.Controllers.Arena
import com.sportpassword.bm.Controllers.EditTeamItemActivity
import com.sportpassword.bm.Controllers.TempPlayVC
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.SELECT_TIME_TYPE
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.*
import kotlinx.android.synthetic.main.tab_tempplay_search.*

/**
 * A simple [Fragment] subclass.
 * Use the [TempPlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TempPlayFragment : TabFragment(), inter {

    protected lateinit var searchAdapter: SearchAdapter

    protected val sections: ArrayList<String> = arrayListOf("一般", "更多")
    protected val rows: ArrayList<ArrayList<HashMap<String, String>>> = arrayListOf(
            arrayListOf(
                    hashMapOf("title" to "關鍵字","detail" to "全部"),
                    hashMapOf("title" to "縣市","detail" to "全部"),
                    hashMapOf("title" to "日期","detail" to "全部"),
                    hashMapOf("title" to "時段","detail" to "全部")
            ),
            arrayListOf(
                    hashMapOf("title" to "球館","detail" to "全部"),
                    hashMapOf("title" to "程度","detail" to "全部")
            )
    )
    protected val data: ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> = arrayListOf()

    val SELECT_REQUEST_CODE = 1

    var citys: ArrayList<City> = arrayListOf()
    var days: ArrayList<Int> = arrayListOf()
    var times: HashMap<String, Any> = hashMapOf()
    var arenas: ArrayList<Arena> = arrayListOf()
    var degrees: ArrayList<DEGREE> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_tempplay_search, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.search_container)
        initAdapter()

        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        val btn = view.findViewById<Button>(R.id.submit_btn)
        btn.setOnClickListener { submit(view) }

        return view
    }

    override fun initAdapter() {
        setData()

//        println(data)

        searchAdapter = SearchAdapter()
        searchAdapter.data = data
        searchAdapter.delegate = this
        recyclerView.adapter = searchAdapter
    }

    override fun setP(section: Int, row: Int) {
//        println(section)
//        println(row)
        var intent = Intent(activity, EditTeamItemActivity::class.java)
        when (section) {
            0 -> {
                when (row) {
                    1 -> {// city
                        intent.putExtra("key", TEAM_CITY_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("type", "simple")
                        intent.putExtra("select", "multi")
                        intent.putParcelableArrayListExtra("citys", citys)
                    }
                    2 -> {// days
                        intent.putExtra("key", TEAM_DAYS_KEY)
                        intent.putExtra("source", "search")
                        intent.putIntegerArrayListExtra("days", days)
                    }
                    3 -> {// times
                        intent.putExtra("key", TEAM_PLAY_START_KEY)
                        intent.putExtra("source", "search")
//                        times["time"] = "09:00"
                        times["type"] = SELECT_TIME_TYPE.play_start
                        intent.putExtra("times", times)
                    }
                }
            }
            1 -> {
                when (row) {
                    0 -> {// arena
                        if (citys.size == 0) {
                            Alert.warning(this@TempPlayFragment.context!!, "請先選擇縣市")
                            return
                        }
                        intent.putExtra("key", TEAM_ARENA_KEY)
                        intent.putExtra("source", "search")
                        intent.putExtra("type", "simple")
                        intent.putExtra("select", "multi")

                        var citysForArena: ArrayList<Int> = arrayListOf()
                        for (city in citys) {
                            citysForArena.add(city.id)
                        }
                        intent.putIntegerArrayListExtra("citys_for_arena", citysForArena)

                        intent.putParcelableArrayListExtra("arenas", arenas)
                    }
                }
            }
        }
        startActivityForResult(intent!!, SELECT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var section = 0
        var row = 0
        var value = "全部"

        when (requestCode) {
            SELECT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val key = data!!.getStringExtra("key")

                    if (key == TEAM_CITY_KEY) { // city
                        section = 0
                        row = 1
                        citys = data!!.getParcelableArrayListExtra("citys")
                        if (citys.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            for (city in citys) {
                                arr.add(city.name)
                            }
                            value = arr.joinToString()
                        } else {
                            value = "全部"
                        }
                    } else if (key == TEAM_ARENA_KEY) { // arena
                        section = 1
                        row = 0
                        arenas = data!!.getParcelableArrayListExtra("arenas")
                        if (arenas.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            for (arena in arenas) {
                                arr.add(arena.name)
                            }
                            value = arr.joinToString()
                        } else {
                            value = "全部"
                        }
                    } else if (key == TEAM_DAYS_KEY) {
                        section = 0
                        row = 2
                        days = data!!.getIntegerArrayListExtra("days")
//                        println(days)

                        if (days.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            val gDays = Global.days
                            for (day in days) {
                                for (gDay in gDays) {
                                    if (day == gDay.get("value")!! as Int) {
                                        arr.add(gDay.get("simple_text")!! as String)
                                        break
                                    }
                                }
                            }
                            value = arr.joinToString()
                        } else {
                            value = "全部"
                        }
                    } else if (key == TEAM_PLAY_START_KEY) {
                        section = 0
                        row = 3
                        times = data!!.getSerializableExtra("times") as HashMap<String, Any>
                        if (times.containsKey("time")) {
                            value = times["time"]!! as String
                        } else {
                            value = "全部"
                        }
                    }
                }
            }
        }
        rows[section][row]["detail"] = value
        setData()
        searchAdapter.data = this.data
        searchAdapter.notifyDataSetChanged()
    }

    private fun setData() {
        data.clear()
        for ((idx, section) in sections.withIndex()) {
            val row = rows[idx]
            data.add(hashMapOf(section to row))
        }
    }

    fun submit(view: View) {
        val intent = Intent(activity, TempPlayVC::class.java)
        intent.putExtra("type", "type")
        intent.putExtra("titleField", "name")
        startActivity(intent)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "TYPE"
        private val ARG_PARAM2 = "SCREEN_WIDTH"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: Int): TabFragment {
            val fragment = TempPlayFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor

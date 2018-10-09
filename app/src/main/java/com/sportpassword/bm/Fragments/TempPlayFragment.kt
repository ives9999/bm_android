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
import com.sportpassword.bm.Controllers.Day
import com.sportpassword.bm.Controllers.EditTeamItemActivity
import com.sportpassword.bm.Controllers.TempPlayVC
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.DEGREE
import com.sportpassword.bm.Utilities.TEAM_CITY_KEY
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
    var days: ArrayList<Day> = arrayListOf()
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
        var intent: Intent? = null
        when (section) {
            0 -> {
                when (row) {
                    1 -> {
                        intent = Intent(activity, EditTeamItemActivity::class.java)
                        intent.putExtra("key", TEAM_CITY_KEY)
                        intent.putExtra("value", 218)
                        intent.putExtra("source", "search")
                        intent.putExtra("type", "simple")
                        intent.putExtra("select", "multi")
                        intent.putParcelableArrayListExtra("citys", citys)
                    }
                }
            }
        }
        startActivityForResult(intent!!, SELECT_REQUEST_CODE)
    }


    fun submit(view: View) {
        val intent = Intent(activity, TempPlayVC::class.java)
        intent.putExtra("type", "type")
        intent.putExtra("titleField", "name")
        startActivity(intent)
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

                    if (key == TEAM_CITY_KEY) {
                        section = 0
                        row = 1
                        citys = data!!.getParcelableArrayListExtra("citys")
//                        println(citys)
                        if (citys.size > 0) {
                            var arr: ArrayList<String> = arrayListOf()
                            for (city in citys) {
                                arr.add(city.name)
                            }
                            value = arr.joinToString()
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

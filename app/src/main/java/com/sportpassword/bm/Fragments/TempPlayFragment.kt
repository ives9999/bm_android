package com.sportpassword.bm.Fragments


import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Adapters.SearchAdapter
import com.sportpassword.bm.Adapters.inter
import com.sportpassword.bm.R

/**
 * A simple [Fragment] subclass.
 * Use the [TempPlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TempPlayFragment : TabFragment(), inter {
    override fun setP(section: Int, row: Int) {
        println(section)
        println(row)
    }

    protected lateinit var searchAdapter: SearchAdapter

    protected val sections: ArrayList<String> = arrayListOf("一般", "更多")
    protected val rows: ArrayList<ArrayList<String>> = arrayListOf(
            arrayListOf("縣市","日期","時段"),
            arrayListOf("球館","程度")
    )
    protected val data: ArrayList<HashMap<String, ArrayList<String>>> = arrayListOf()

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

        return view
    }

    override fun initAdapter() {

        for ((idx, section) in sections.withIndex()) {
            val row = rows[idx]
            data.add(hashMapOf(section to row))
        }
//        println(data)

        searchAdapter = SearchAdapter(data) { position ->
            println(position)
            //val intent = Intent(activity, TempPlayVC::class.java)
            //startActivity(intent)
        }
        searchAdapter.delegate = this
        recyclerView.adapter = searchAdapter
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

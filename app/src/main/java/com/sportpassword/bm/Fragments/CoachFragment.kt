package com.sportpassword.bm.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Models.Data

import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Utilities.PERPAGE


/**
 * A simple [Fragment] subclass.
 * Use the [CoachFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoachFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab, container, false)
        CoachService.getList(context!!, "coach", "name", page, PERPAGE, null) { success ->
            if (success) {
//                for (i in 0 until CoachService.dataLists.size) {
//                    val data = CoachService.dataLists[i] as Data
//                    println("${data.id}:${data.title}")
//                }
                val listAdapter = ListAdapter(context!!, CoachService.dataLists) { data ->

                }
                val recyclerView = view.findViewById<RecyclerView>(R.id.list_container)
                recyclerView.adapter = listAdapter

                val layoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = layoutManager
            }
        }
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CoachFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): CoachFragment {
            val fragment = CoachFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor

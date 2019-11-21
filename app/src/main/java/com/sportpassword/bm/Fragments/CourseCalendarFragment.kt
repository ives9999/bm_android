package com.sportpassword.bm.Fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*

class CourseCalendarFragment : TabFragment() {

    var superCourses: SuperCourses? = null
    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected val adapterSections: ArrayList<Section> = arrayListOf()
    var sections: ArrayList<String> = arrayListOf()

    override val _searchRows: ArrayList<HashMap<String, String>> = arrayListOf(
            hashMapOf("title" to "關鍵字","key" to KEYWORD_KEY,"value" to "","value_type" to "String","show" to "不限"),
            hashMapOf("title" to "縣市","key" to CITY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "日期","key" to WEEKDAY_KEY,"value" to "","value_type" to "Array","show" to "不限"),
            hashMapOf("title" to "開始時間之後","key" to START_TIME_KEY,"value" to "","value_type" to "String","show" to "不限"),
            hashMapOf("title" to "結束時間之前","key" to END_TIME_KEY,"value" to "","value_type" to "String","show" to "不限")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataService = CourseService
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.search_manager, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val memuView = menu!!.findItem(R.id.menu_search_manager).actionView

        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)

        searchBtn.tag = type
        ManagerBtn.tag = type
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_course, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask

        //initAdapter(false)
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
        //refresh()

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
            val fragment = CourseCalendarFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
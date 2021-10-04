package com.sportpassword.bm.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.ImageButton
import com.sportpassword.bm.Adapters.SearchItem
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Utilities.Loading
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_coach.*


/**
 * A simple [Fragment] subclass.
 * Use the [CoachFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoachFragment : TabFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {

        searchRows = arrayListOf(
            hashMapOf("title" to "關鍵字","detail" to "全部","key" to KEYWORD_KEY),
            hashMapOf("title" to "縣市","detail" to "全部","key" to CITY_KEY)
        )
        able_type = "coach"

        super.onCreate(savedInstanceState)
        this.dataService = CoachService
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_coach, container, false)

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
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_manager, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val memuView = menu.findItem(R.id.menu_search_manager).actionView
        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        //val ManagerBtn = memuView.findViewById<ImageButton>(R.id.manager)

        searchBtn.tag = type
        //ManagerBtn.tag = type
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //isCourseShow = isVisibleToUser
    }

//    override fun getDataStart(_page: Int, _perPage: Int) {
//        super.getDataStart(_page, _perPage)
//        Loading.show(maskView)
//        //println("page: $_page")
//        CoachService.getList(context!!, "coach", "name", mainActivity!!.params, _page, _perPage, null) { success ->
//            getDataEnd(success)
//        }
//    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "TYPE"
        private const val ARG_PARAM2 = "SCREEN_WIDTH"

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
            val fragment = CoachFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor

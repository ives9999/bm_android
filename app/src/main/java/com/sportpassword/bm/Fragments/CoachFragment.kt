package com.sportpassword.bm.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sportpassword.bm.Models.Coach
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CoachService
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Utilities.PERPAGE
import kotlinx.android.synthetic.main.tab.*


/**
 * A simple [Fragment] subclass.
 * Use the [CoachFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoachFragment : TabFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataService = CoachService
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        //println("page: $_page")
        CoachService.getList(context!!, "coach", "name", _page, _perPage, null) { success ->
            getDataEnd(success)
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): TabFragment {
            val fragment = CoachFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor

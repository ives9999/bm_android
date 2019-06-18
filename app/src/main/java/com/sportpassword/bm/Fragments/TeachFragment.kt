package com.sportpassword.bm.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sportpassword.bm.Services.TeachService


/**
 * A simple [Fragment] subclass.
 * Use the [TeachFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TeachFragment : TabFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataService = TeachService

//        val configBuilder = Configuration.Builder(VIMEO_TOKEN)
//        VimeoClient.initialize(configBuilder.build())
//        vimeoClient = VimeoClient.getInstance()
//        val token = VimeoClient.getInstance().vimeoAccount.accessToken
//        println(token)
        //val uri = "/me/videos"
        //val uri = "/videos/265966500"


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        super.getDataStart(_page, _perPage)
        //println("page: $_page")
        TeachService.getList(context!!, "teach", "title", mainActivity!!.params, _page, _perPage, null) { success ->
            getDataEnd(success)
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
            val fragment = TeachFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor

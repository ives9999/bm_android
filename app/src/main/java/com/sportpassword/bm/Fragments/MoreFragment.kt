package com.sportpassword.bm.Fragments


import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.pm.PackageInfoCompat
import com.sportpassword.bm.Adapters.ListAdapter
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.Controllers.ShowActivity
import com.sportpassword.bm.Controllers.ShowCoachVC
import com.sportpassword.bm.Controllers.ShowPNVC

import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.CITYS_KEY
import com.sportpassword.bm.Utilities.CITY_KEY


/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment : TabFragment() {

    //protected lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_more, container, false)

        val row2 = view.findViewById<ConstraintLayout>(R.id.more_coach_row)
        row2.setOnClickListener {
            (activity!! as MainActivity).goCoach()
        }

        val row3 = view.findViewById<ConstraintLayout>(R.id.more_arena_row)
        row3.setOnClickListener {
            (activity!! as MainActivity).goArena()
//            val fm = activity!!.supportFragmentManager
//            val arenaFragment = ArenaFragment.newInstance("arena", screenWidth)
//            fm.beginTransaction()
//                    .replace(R.id.more_container, arenaFragment)
//                    .addToBackStack(null)
//                    .commit()
        }

        val row4 = view.findViewById<ConstraintLayout>(R.id.more_teach_row)
        row4.setOnClickListener() {
            (activity!! as MainActivity).goTeach()

            //val mainActivity = activity as MainActivity
            //mainActivity.test()

//            val fm = activity!!.supportFragmentManager
//            val teachFragment = TeachFragment.newInstance("teach", screenWidth)
//            fm.beginTransaction()
//                    .replace(R.id.more_container, teachFragment)
//                    .addToBackStack(null)
//                    .commit()
        }
        val row5 = view.findViewById<ConstraintLayout>(R.id.more_pn_row)
        row5.setOnClickListener() {
            val intent = Intent(activity, ShowPNVC::class.java)
//            intent.putExtra("title", "報名臨打")
//            intent.putExtra("content", "孫志煌報名2019-03-09 17:00的臨打")
            startActivity(intent)

        }
        val row6 = view.findViewById<ConstraintLayout>(R.id.more_version_row)
        row6.setOnClickListener {
            val p = context!!.applicationContext.packageManager.getPackageInfo(context!!.packageName, 0)
            val v = PackageInfoCompat.getLongVersionCode(p).toInt()
            //val v = p.versionCode
            val n = p.versionName
            val builder = AlertDialog.Builder(context!!)
            builder.setMessage(n + "#" + v)
            val dialog = builder.create()
            dialog.show()
        }

        val rowStore = view.findViewById<ConstraintLayout>(R.id.more_store_row)
        rowStore.setOnClickListener() {
            (activity!! as MainActivity).goStore()

            //val mainActivity = activity as MainActivity
            //mainActivity.test()

//            val fm = activity!!.supportFragmentManager
//            val teachFragment = TeachFragment.newInstance("teach", screenWidth)
//            fm.beginTransaction()
//                    .replace(R.id.more_container, teachFragment)
//                    .addToBackStack(null)
//                    .commit()
        }

        val rowProduct = view.findViewById<ConstraintLayout>(R.id.more_product_row)
        rowProduct.setOnClickListener {
            (activity!! as MainActivity).goProduct()
        }

        return view
    }

    override fun init() {
        initAdapter()

        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()

    }

    override fun initAdapter(include_section: Boolean) {
        listAdapter = ListAdapter(context!!, type!!, screenWidth, { data ->
            var intent: Intent? = null
            if (type == "course") {
                intent = Intent(activity, ShowCoachVC::class.java)
            } else {
                intent = Intent(activity, ShowActivity::class.java)
            }
            intent.putExtra("type", type)
            intent.putExtra("token", data.token)
            intent.putExtra("title", data.title)
            startActivity(intent)
        }, { data ->
            var key = CITY_KEY
            if (type == "course") {
                key = CITYS_KEY
            }
            if (data.data.containsKey(key)) {
                if (data.data[key]!!.containsKey("value")) {
                    val city_id = data.data[CITY_KEY]!!["value"] as Int
                    mainActivity!!.cityBtnPressed(city_id, type!!)
                }
            }

        }, { data, address ->

        })
        recyclerView.adapter = listAdapter

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
            val fragment = MoreFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor

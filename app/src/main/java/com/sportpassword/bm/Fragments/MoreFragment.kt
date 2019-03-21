package com.sportpassword.bm.Fragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sportpassword.bm.Controllers.MainActivity
import com.sportpassword.bm.Controllers.ShowPNVC

import com.sportpassword.bm.R


/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment : TabFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_more, container, false)

        val row1 = view.findViewById<ConstraintLayout>(R.id.more_arena_row)
        row1.setOnClickListener { view ->
            (activity!! as MainActivity).goArena()
//            val fm = activity!!.supportFragmentManager
//            val arenaFragment = ArenaFragment.newInstance("arena", screenWidth)
//            fm.beginTransaction()
//                    .replace(R.id.more_container, arenaFragment)
//                    .addToBackStack(null)
//                    .commit()
        }

        val row2 = view.findViewById<ConstraintLayout>(R.id.more_teach_row)
        row2.setOnClickListener() { view ->
            (activity!! as MainActivity).goCourse()

            //val mainActivity = activity as MainActivity
            //mainActivity.test()

//            val fm = activity!!.supportFragmentManager
//            val teachFragment = CourseFragment.newInstance("teach", screenWidth)
//            fm.beginTransaction()
//                    .replace(R.id.more_container, teachFragment)
//                    .addToBackStack(null)
//                    .commit()
        }
        val row3 = view.findViewById<ConstraintLayout>(R.id.more_pn_row)
        row3.setOnClickListener() { view ->
            val intent = Intent(activity, ShowPNVC::class.java)
//            intent.putExtra("title", "報名臨打")
//            intent.putExtra("content", "孫志煌報名2019-03-09 17:00的臨打")
            startActivity(intent)

        }
        val row4 = view.findViewById<ConstraintLayout>(R.id.more_version_row)
        row4.setOnClickListener { view ->
            val p = context!!.applicationContext.packageManager.getPackageInfo(context!!.packageName, 0)
            val v = p.versionCode
            val n = p.versionName
            val builder = AlertDialog.Builder(context!!)
            builder.setMessage(n + "#" + v)
            val dialog = builder.create()
            dialog.show()
        }

        return view
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

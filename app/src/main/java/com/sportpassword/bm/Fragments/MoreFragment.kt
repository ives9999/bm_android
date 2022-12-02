package com.sportpassword.bm.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Adapters.MoreAdapter
import com.sportpassword.bm.Controllers.List1CellDelegate
import com.sportpassword.bm.Controllers.ShowPNVC
import com.sportpassword.bm.Data.MoreRow
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*

/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment : TabFragment() {

    //protected lateinit var listAdapter: ListAdapter
    lateinit var tableAdapter: MoreAdapter
    var moreRows: ArrayList<MoreRow> = arrayListOf()

    private fun initMoreRows(): ArrayList<MoreRow> {
        val rows: ArrayList<MoreRow> = arrayListOf()
        val r1: MoreRow = MoreRow("商品", "product", "product", R.color.MY_LIGHT_RED)
        rows.add(r1)
        val r2: MoreRow = MoreRow("教學", "teach", "teach", R.color.MY_WHITE)
        rows.add(r2)
        val r3: MoreRow = MoreRow("教練", "coach", "coach", R.color.MY_WHITE)
        rows.add(r3)
        val r4: MoreRow = MoreRow("體育用品店", "store", "store", R.color.MY_WHITE)
        rows.add(r4)
        val r5: MoreRow = MoreRow("推播訊息", "pn", "bell", R.color.MY_WHITE)
        rows.add(r5)
        val r6: MoreRow = MoreRow("版本", "version", "version", R.color.MY_WHITE)
        rows.add(r6)

        return rows
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        val memuView = menu.findItem(R.id.menu_all).actionView
//        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
//        searchBtn.visibility = View.GONE
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_course, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = list_container
        refreshLayout = tab_refresh
        maskView = mask
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()

        //recyclerView.adapter = adapter
        tableAdapter = MoreAdapter(this)
        moreRows = initMoreRows()
        tableAdapter.moreRow = moreRows
        recyclerView.adapter = tableAdapter

        refresh()
    }

    override fun cellClick(idx: Int) {
        val row: MoreRow = moreRows[idx]
        val key: String = row.key
        when (key) {
            "product"-> mainActivity!!.toProduct()
            "coach"-> mainActivity!!.toCoach()
            "teach"-> mainActivity!!.toTeach()
            "store"-> mainActivity!!.toStore()
            "pn"-> {
                val intent = Intent(activity, ShowPNVC::class.java)
                startActivity(intent)
            }
            "version"-> {
                val p = requireContext().applicationContext.packageManager.getPackageInfo(
                    requireContext().packageName,
                    0
                )
                val v = PackageInfoCompat.getLongVersionCode(p).toInt()
                val n = p.versionName
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(n + "#" + v)
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
    override fun refresh() {}

//    companion object {
//        // TODO: Rename parameter arguments, choose names that match
//        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//        private val ARG_PARAM1 = "TYPE"
//        private val ARG_PARAM2 = "SCREEN_WIDTH"
//
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment TabFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        fun newInstance(param1: String, param2: Int): TabFragment {
//            val fragment = MoreFragment()
//            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putInt(ARG_PARAM2, param2)
//            fragment.arguments = args
//            return fragment
//        }
//    }

}// Required empty public constructor

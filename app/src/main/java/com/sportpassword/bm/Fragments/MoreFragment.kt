package com.sportpassword.bm.Fragments


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sportpassword.bm.Controllers.*

import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.function_item.*
import kotlinx.android.synthetic.main.function_item.text
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*


/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment : TabFragment() {

    //protected lateinit var listAdapter: ListAdapter

    val rows: ArrayList<HashMap<String, Any>> = arrayListOf(
        hashMapOf("key" to "product", "text" to "商品","icon" to "product","color" to R.color.MY_LIGHT_RED),
        hashMapOf("key" to "teach", "text" to "教學","icon" to "teach","color" to R.color.MY_WHITE),
        hashMapOf("key" to "coach", "text" to "教練","icon" to "coach","color" to R.color.MY_WHITE),
        hashMapOf("key" to "store", "text" to "體育用品店","icon" to "store","color" to R.color.MY_WHITE),
        hashMapOf("key" to "pn", "text" to "推播訊息","icon" to "bell","color" to R.color.MY_WHITE),
        hashMapOf("key" to "version", "text" to "版本","icon" to "version","color" to R.color.MY_WHITE)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val memuView = menu.findItem(R.id.menu_all).actionView
        val searchBtn = memuView.findViewById<ImageButton>(R.id.search)
        searchBtn.visibility = View.GONE
    }

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

        recyclerView.adapter = adapter
        refresh()
    }

    override fun refresh() {
        adapter.clear()
        items.clear()
        generateItems()
        adapter.addAll(items)
    }

    override fun generateItems(): ArrayList<Item> {

        for (row in rows) {

            val myItem = MoreItem(requireContext(), row)
            myItem.list1CellDelegate = this
            items.add(myItem)
        }

        return items
    }

    override fun rowClick(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>, view: View) {

        val row = (item as MoreItem).row
        if (row.containsKey("key") && row["key"] != null) {

            val key: String = row["key"]!! as String
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

class MoreItem(val context: Context, val row: HashMap<String, Any>): Item() {

    var list1CellDelegate: List1CellDelegate? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        if (row.containsKey("icon") && row["icon"] != null) {
            viewHolder.icon.setImage(row["icon"]!! as String)
        }
        if (row.containsKey("text") && row["text"] != null) {
            viewHolder.text.text = row["text"]!! as String
        }

        if (row.containsKey("color") && row["color"] != null) {
            viewHolder.text.setTextColor(ContextCompat.getColor(context, row["color"]!! as Int))
        }
    }

    override fun getLayout() = R.layout.function_item
}

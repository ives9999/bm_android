package com.sportpassword.bm.Fragments


import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService

class ArenaFragment : TabFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataService = ArenaService
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val actionBar: ActionBar = (activity!! as AppCompatActivity).supportActionBar!!
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.prev)

        val title = "球館"
        val l: LinearLayout = LayoutInflater.from(activity!!).inflate(R.layout.title_bar, null) as LinearLayout
        val titleView: TextView = l.findViewById<TextView>(R.id.myTitle)
        titleView.setText(title)
        //println(titleView)
        val windowManager = (activity!! as AppCompatActivity).windowManager
        val display: Display = windowManager.defaultDisplay
        val size: Point = Point()
        display.getSize(size)
        val actionBarWidth: Int = size.x
        //println(actionBarWidth)

        titleView.measure(0, 0)
        val titleViewWidth = titleView.measuredWidth
        //println(titleViewWidth)


//        val dimensions: BitmapFactory.Options = BitmapFactory.Options()
//        dimensions.inJustDecodeBounds = true
//        val prev: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.prev, dimensions)
//        val prevWidth: Int = dimensions.outWidth
//        println(prevWidth)


        val params: ActionBar.LayoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
        params.leftMargin = (actionBarWidth/2) - (titleViewWidth/2) - 170

        actionBar.setCustomView(l, params)
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM)

//        inflater!!.inflate(R.menu.search_manager, menu)
//        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        super.getDataStart(_page, _perPage)
        //println("page: $_page")
        ArenaService.getList(context!!, "arena", "name", mainActivity.params, _page, _perPage, null) { success ->
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
            val fragment = ArenaFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

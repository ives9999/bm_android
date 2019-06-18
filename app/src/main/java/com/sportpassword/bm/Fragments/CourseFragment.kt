package com.sportpassword.bm.Fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import com.sportpassword.bm.Adapters.GroupSection
import com.sportpassword.bm.Models.SuperCourse
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Utilities.BASE_URL
import com.sportpassword.bm.Utilities.Loading
import com.squareup.picasso.Picasso
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.mask.*
import kotlinx.android.synthetic.main.tab_course.*
import kotlinx.android.synthetic.main.tab_list_item.*

class CourseFragment : TabFragment() {

    var superCourses: SuperCourses? = null
    protected lateinit var adapter: GroupAdapter<ViewHolder>
    protected val adapterSections: ArrayList<Section> = arrayListOf()
    var sections: ArrayList<String> = arrayListOf()


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

        initAdapter(false)
        recyclerView.setHasFixedSize(true)
        setRecyclerViewScrollListener()
        setRecyclerViewRefreshListener()
        refresh()

    }

    override fun refresh() {
        page = 1
        getDataStart(page, perPage)
    }

    fun initAdapter(include_section: Boolean=false) {
        adapter = GroupAdapter()
        adapter.setOnItemClickListener { item, view ->
            rowClick(item, view)
        }
        if (include_section) {
            for (section in sections) {
                adapterSections.add(Section())
            }
            for ((idx, title) in sections.withIndex()) {
                val expandableGroup = ExpandableGroup(GroupSection(title), true)
                val items = generateItems(idx)
                adapterSections[idx].addAll(items)
                expandableGroup.add(adapterSections[idx])
                adapter.add(expandableGroup)
            }
        } else {
            val items = generateItems()
            adapter.addAll(items)
        }
        recyclerView.adapter = adapter
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCourseShow = isVisibleToUser
    }

    override fun getDataStart(_page: Int, _perPage: Int) {
        super.getDataStart(_page, _perPage)
        Loading.show(maskView)
        //println("page: $_page")
        //println(mainActivity!!.params)
        dataService.getList(context!!, null, null, _page, _perPage) { success ->
            getDataEnd(success)
        }
    }

    override fun getDataEnd(success: Boolean) {
        if (success) {
            if (theFirstTime) {

                superCourses = dataService.superModel as SuperCourses
                if (superCourses != null) {
                    page = superCourses!!.page
                    perPage = superCourses!!.perPage
                    totalCount = superCourses!!.totalCount
                    var _totalPage: Int = totalCount / perPage
                    totalPage = if (totalCount % perPage > 0) _totalPage + 1 else _totalPage
                    theFirstTime = false

                    val items = generateItems()
                    adapter.update(items)
                    adapter.notifyDataSetChanged()
                }

            }

            //notifyDataSetChanged()
            page++
        }
//        mask?.let { mask?.dismiss() }
        Loading.hide(maskView)
        loading = false
//        println("page:$page")
//        println("perPage:$perPage")
//        println("totalCount:$totalCount")
//        println("totalPage:$totalPage")
    }

    fun generateItems(): ArrayList<Item> {
        val items: ArrayList<Item> = arrayListOf()
        if (superCourses != null) {
            for (row in superCourses!!.rows) {
                items.add(ManagerCourseItem1(context!!, row))
            }
        }

        return items
    }
    fun generateItems(section: Int): ArrayList<Item> {
        return arrayListOf()
    }

    fun rowClick(item: com.xwray.groupie.Item<ViewHolder>, view: View) {

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
            val fragment = CourseFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putInt(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    class ManagerCourseItem1(val context: Context, val superCourse: SuperCourse): Item() {
        override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {

            val citys = superCourse.coach.citys
            if (citys.size > 0) {
                viewHolder.listCityBtn.text = citys[0].name
            }
            viewHolder.title.text = superCourse.title
            Picasso.with(context)
                    .load(BASE_URL + superCourse.featured_path)
                    .placeholder(R.drawable.loading_square_120)
                    .error(R.drawable.loading_square_120)
                    .into(viewHolder.listFeatured)
            viewHolder.listArenaTxt.text = superCourse.price_text_short
            viewHolder.listDayTxt.text = superCourse.weekday_text
            viewHolder.listIntervalTxt.text = superCourse.start_time_text+"~"+superCourse.end_time_text
            viewHolder.marker.visibility = View.INVISIBLE
        }

        override fun getLayout() = R.layout.tab_list_item

    }
}

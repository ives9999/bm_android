package com.sportpassword.bm.Views

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Adapters.OneSectionAdapter
import com.sportpassword.bm.Adapters.SearchSectionAdapter
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.dpToPx
import com.sportpassword.bm.Utilities.then
import org.jetbrains.anko.backgroundColor

open class SearchPanel {

    var context: Context? = null
    var layerMask: LinearLayout? = null
    var layerBlackView: RelativeLayout? = null
    var layerButtonLayout: LinearLayout? = null
    var layerSubmitBtn: Button? = null
    var layerCancelBtn: Button? = null

    var parent: ViewGroup? = null

    var able_type: String? = null

//    lateinit var searchAdapter: GroupAdapter<GroupieViewHolder>
//    var searchRows: ArrayList<HashMap<String, String>> = arrayListOf()
    lateinit var oneSectionAdapter: OneSectionAdapter
//    var searchSections: ArrayList<SearchSection> = arrayListOf()

    var layerRightLeftPadding: Int = 40
    var layerTopPadding: Int = 200
    var layerBtnCount: Int = 2

    fun addSearchLayer(context: Context, p: ViewGroup, able_type: String, oneSectionAdapter: OneSectionAdapter) {
        parent = p
        this.context = context
        this.able_type = able_type
        this.oneSectionAdapter = oneSectionAdapter

        if (layerMask != null) {
            unmask()
        }
        mask()

        if (parent != null && layerMask != null) {
            addBlackView()
            addSearchTableView()
            layerAddButtonLayout()
//            layerBtnCount = 2
            layerAddSubmitBtn()
            layerAddCancelBtn()
        }
    }

    private fun addBlackView() {

//        val w = layerMask!!.measuredWidth
//        val h = layerMask!!.measuredHeight

        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        lp.setMargins(layerRightLeftPadding, layerTopPadding, layerRightLeftPadding, 0)
        //val lp = RelativeLayout.LayoutParams(w - (2 * layerRightLeftPadding), h - layerTopPadding)
        layerBlackView = RelativeLayout(context!!)
        layerBlackView!!.layoutParams = lp
//        layerBlackView!!.translationX = layerRightLeftPadding.toFloat()
//        layerBlackView!!.translationY = layerTopPadding.toFloat()
        layerBlackView!!.backgroundColor = Color.BLACK
        layerMask!!.addView(layerBlackView)
    }

    private fun addSearchTableView() {

//        val w = parent!!.measuredWidth
        val searchTableView = RecyclerView(context!!)
        searchTableView.id = R.id.SearchRecycleItem
//        val padding: Int = 80
        //val lp1 = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        lp.addRule(RelativeLayout.ABOVE, R.id.SearchButtonLayer)
//        val lp1 = RecyclerView.LayoutParams(w - (2 * padding), 1000)
        lp.setMargins(0, 0, 0, 150)
        searchTableView.layoutParams = lp
        searchTableView.layoutManager = LinearLayoutManager(context)
        //searchTableView.backgroundColor = Color.RED
        searchTableView.backgroundColor = Color.TRANSPARENT

//        val activity: BaseActivity = context!! as BaseActivity
        searchTableView.adapter = oneSectionAdapter

//        searchAdapter = GroupAdapter<GroupieViewHolder>()
//        searchAdapter.setOnItemClickListener { item, view ->
//            onClick(item)
//        }
//        val rows = generateSearchItems()
//        searchAdapter.addAll(rows)
//
//        searchTableView.adapter = searchAdapter
        layerBlackView!!.addView(searchTableView)
    }

    private fun layerAddButtonLayout() {

        layerButtonLayout = LinearLayout(context)
        layerButtonLayout!!.id = R.id.SearchButtonLayer

        val height: Int = (context != null) then { 50.dpToPx(context!!) } ?: 300

        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        if (layerButtonLayout != null) {
            layerButtonLayout!!.layoutParams = lp
            val color = ContextCompat.getColor(context!!, R.color.MY_BOTTOM_BACKGROUND)
            layerButtonLayout!!.backgroundColor = color
            layerButtonLayout!!.gravity = Gravity.CENTER
            layerButtonLayout!!.orientation = LinearLayout.HORIZONTAL
            layerBlackView!!.addView(layerButtonLayout)
        }
    }

    private fun layerAddSubmitBtn() {

        val activity: BaseActivity = context!! as BaseActivity
        layerSubmitBtn = activity.layoutInflater.inflate(R.layout.submit_button, null) as Button

        val width: Int = (context != null) then { 120.dpToPx(context!!) } ?: 300
        val height: Int = (context != null) then { 30.dpToPx(context!!) } ?: 60

        val lp2 = LinearLayout.LayoutParams(width, height)
        //lp2.weight = 1F

        val padding: Int = (activity.screenWidth - layerBtnCount * width - layerRightLeftPadding * 2) / (layerBtnCount + 1)
        lp2.marginStart = padding

        //lp2.setMargins(padding, 0, 60, 0)
        layerSubmitBtn!!.layoutParams = lp2
        layerSubmitBtn!!.setOnClickListener {
            layerSubmit()
        }

//        layerSubmitBtn!!.backgroundDrawable = shape
//        layerSubmitBtn!!.setTextColor(Color.WHITE)

        if (layerButtonLayout != null) {
            layerButtonLayout!!.addView(layerSubmitBtn)
        }
    }

    private fun layerAddCancelBtn() {

        val activity: BaseActivity = context!! as BaseActivity
        layerCancelBtn = activity.layoutInflater.inflate(R.layout.cancel_button, null) as Button

        val width: Int = (context != null) then { 120.dpToPx(context!!) } ?: 300
        val height: Int = (context != null) then { 30.dpToPx(context!!) } ?: 60

        val lp2 = LinearLayout.LayoutParams(width, height)
        val padding: Int = (activity.screenWidth - layerBtnCount * width - layerRightLeftPadding * 2) / (layerBtnCount + 1)
        lp2.marginStart = padding
        //lp2.weight = 1F
        //lp2.setMargins(60, 0, 60, 0)
//        lp2.setMargins(16, 0, 0, 0)
        layerCancelBtn!!.layoutParams = lp2
        layerCancelBtn!!.setOnClickListener {
            layerCancel()
        }

//        layerCancelBtn!!.backgroundDrawable = shape
//        layerCancelBtn!!.setTextColor(Color.WHITE)

        if (layerButtonLayout != null) {
            layerButtonLayout!!.addView(layerCancelBtn)
        }
    }

//    fun onClick(item: Item<GroupieViewHolder>) {
//
//        val activity: BaseActivity = context!! as BaseActivity
//        val searchItem = item as SearchItem
//        val row = searchItem.row
//        if (able_type == "course" || able_type == "team" || able_type == "arena") {
//
//            //val tag = parent.tag as String
//            var frag: TabFragment? = null
//            if (able_type == "course") {
//                frag = activity.getFragment() as CourseFragment
//            } else if (able_type == "team") {
//                frag = activity.getFragment() as TempPlayFragment
//            } else if (able_type == "arena") {
//                frag = activity.getFragment() as ArenaFragment
//            }
//            //frag?.prepare(row)
//            //prepareSearch1(row, page)
//        } else {
//            activity.prepare(row)
////                if (searchItem.switch == false) {
////                    prepareSearch(row, page)
////                }
//        }
//    }

    private fun layerSubmit() {

        unmask()
        val activity: BaseActivity = context!! as BaseActivity
        activity.prepareParams()
        activity.refresh()

//        if (able_type == "coach" || able_type == "product" || able_type == "store" || able_type == "teach") {
//            activity.prepareParams()
//            activity.refresh()
//        } else if (able_type == "team") {
//            val frag = getFragment(activity, able_type!!) as TempPlayFragment
//            frag.prepareParams()
//            frag.refresh()
//        } else if (able_type == "course") {
//            val frag = getFragment(activity, able_type!!) as CourseFragment
//            frag.prepareParams()
//            frag.refresh()
//        } else if (able_type == "arena") {
//            val frag = getFragment(activity, able_type!!) as ArenaFragment
//            frag.prepareParams()
//            frag.refresh()
//        } else {
//            //activity.warning("沒有傳送頁面類型參數，請洽管理員")
//            activity.prepareParams()
//            activity.refresh()
//        }
    }

    private fun layerCancel() {
        unmask()
    }


    fun mask() {
        if (layerMask == null) {
            layerMask = LinearLayout(context)
            layerMask!!.id = R.id.MyMask
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            lp.bottomMargin = 400
            layerMask!!.layoutParams = lp

            //mask.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            layerMask!!.backgroundColor = Color.parseColor("#888888")
//            layerMask!!.backgroundColor = Color.RED
            //0是完全透明
//            layerMask!!.alpha = 0.9f
            layerMask!!.setOnClickListener {
                //unmask()
            }
            if (parent != null) {
                parent!!.addView(layerMask)
            }
        } else {
            val v = layerMask!!.visibility
            layerMask!!.visibility = View.VISIBLE
        }
    }

    private fun unmask() {

        if (layerMask != null) {
            removeLayerChildViews()
        }
//        val duration: Long = 500
//        if (layerBlackView != null) {
//            val parent = getMyParent()
//            val h = parent.measuredHeight
//
//            val animate = TranslateAnimation(0f, 0f, layerTopPadding.toFloat(), h.toFloat())
//            animate.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationRepeat(p0: Animation?) {}
//                override fun onAnimationEnd(p0: Animation?) {
//                    removeLayerChildViews()
//                }
//
//                override fun onAnimationStart(p0: Animation?) {}
//            })
//            animate.duration = 500
//            animate.fillAfter = true
//            layerBlackView!!.startAnimation(animate)
//            layerVisibility = false
//        }
    }

    protected fun removeLayerChildViews() {

        if (layerBlackView != null) {
            layerBlackView!!.removeAllViews()
        }

        if (layerMask != null) {
            layerMask!!.removeAllViews()
        }

        layerBlackView = null
        parent!!.removeView(layerMask)
        layerMask = null
    }

//    fun generateSearchItems(): ArrayList<SearchItem> {
//
//        val activity: BaseActivity = context!! as BaseActivity
//
//        val rows: ArrayList<SearchItem> = arrayListOf()
//        for (i in 0..searchRows.size-1) {
//            val row = searchRows[i] as HashMap<String, String>
//            val title = row.get("title")!!
//            var detail: String = ""
//            if (row.containsKey("detail")) {
//                detail = row.get("detail")!!
//            } else if (row.containsKey("show")) {
//                detail = row.get("show")!!
//            }
//            var bSwitch = false
//            if (row.containsKey("switch")) {
//                bSwitch = row.get("switch")!!.toBoolean()
//            }
//            val searchItem = SearchItem(title, detail, "", bSwitch, -1, i)
//            if (able_type == "team" || able_type == "course" || able_type == "arena") {
//                //searchItem.delegate = activity.getFragment()
//            } else {
//                searchItem.delegate = activity
//            }
//            rows.add(searchItem)
//        }
//
//        return rows
//    }
}
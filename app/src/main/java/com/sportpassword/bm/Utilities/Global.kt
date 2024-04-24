package com.sportpassword.bm.Utilities

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by ives on 2018/3/8.
 */




//fun ViewGroup.mask(context: Context): LinearLayout {
//
//    val mask = LinearLayout(context)
//    mask.id = R.id.MyMask
//    mask.layoutParams = LinearLayout.LayoutParams(this.width, this.height)
//    mask.backgroundColor = Color.parseColor("#888888")
//    //0是完全透明
//    mask.alpha = 0.9f
//    mask.setOnClickListener {
//        this.unmask()
//    }
//    this.addView(mask)
//
//    return mask
//}
//
//fun ViewGroup.unmask() {
//
//    val mask = this.findViewById<ViewGroup>(R.id.MyMask)
//    mask.removeAllViews()
//    this.removeView(mask)
//}

//fun ViewGroup.setInfo(context: Context, info: String): TextView {
//
//    val label: TextView = TextView(context)
//    val lp: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
//    lp.topMargin = 400
//    label.layoutParams = lp
//    label.gravity = Gravity.CENTER_HORIZONTAL
//    label.textColor = ContextCompat.getColor(context, R.color.MY_WHITE)
//    label.text = info
//
//    this.addView(label)
//
//    return label
//}




//object Global {

//    val weekdays: ArrayList<HashMap<String, Any>> = arrayListOf(
//            hashMapOf("value" to 1,"text" to "星期一","simple_text" to "一","checked" to false,"title" to "星期一"),
//            hashMapOf("value" to 2,"text" to "星期二","simple_text" to "二","checked" to false,"title" to "星期二"),
//            hashMapOf("value" to 3,"text" to "星期三","simple_text" to "三","checked" to false,"title" to "星期三"),
//            hashMapOf("value" to 4,"text" to "星期四","simple_text" to "四","checked" to false,"title" to "星期四"),
//            hashMapOf("value" to 5,"text" to "星期五","simple_text" to "五","checked" to false,"title" to "星期五"),
//            hashMapOf("value" to 6,"text" to "星期六","simple_text" to "六","checked" to false,"title" to "星期六"),
//            hashMapOf("value" to 7,"text" to "星期日","simple_text" to "日","checked" to false,"title" to "星期七")
//    )
//}

//class IndexPath(val section: Int, val row: Int){}

//object Loading {
//    val alpha = 0.8f
//    val duration: Long = 100
//
//    fun show(view: View) {
//        view.findViewById<FrameLayout>(R.id.mask) ?. let { mask ->
//            mask.alpha = 0f
//            mask.visibility = View.VISIBLE
//            mask.animate().setDuration(duration).alpha(alpha).setListener(object : Animator.AnimatorListener {
//                override fun onAnimationEnd(p0: Animator) {
//                    mask.visibility = View.VISIBLE
//                }
//                override fun onAnimationRepeat(p0: Animator) {}
//                override fun onAnimationCancel(p0: Animator) {}
//                override fun onAnimationStart(p0: Animator) {}
//            })
//        }
//    }
//    fun hide(view: View) {
//        view.findViewById<FrameLayout>(R.id.mask) ?. let { mask ->
//            mask.visibility = View.VISIBLE
//            mask.animate().setDuration(duration).alpha(0f)
//                .setListener(object : Animator.AnimatorListener {
//                    override fun onAnimationEnd(p0: Animator) {
//                        mask.visibility = View.INVISIBLE
//                    }
//
//                    override fun onAnimationRepeat(p0: Animator) {}
//                    override fun onAnimationCancel(p0: Animator) {}
//                    override fun onAnimationStart(p0: Animator) {}
//                })
//        }
//    }
//}




//inline fun <reified T> Any.getField(propertyName: String): T? {
//    val getterName = "get" + propertyName.capitalize()
//    return try {
//        javaClass.getMethod(getterName).invoke(this) as? T
//    } catch (e: NoSuchMethodError) {
//        println(e.localizedMessage)
//        null
//    }
//}



//inline fun <reified T: Table> jsonToModel(jsonString: String): T? {
//
//    var t: T? = null
//    try {
//        t = Gson().fromJson<T>(jsonString, T::class.java)
//    } catch (e: java.lang.Exception) {
//        Global.message = e.localizedMessage
//        println(e.localizedMessage)
//    }
//
//    return t
//}

//inline fun <reified T: Table> refreshOne(): T? {
//
//    val t = jsonToModel<T>("aaa")
//
//    return t
//}

//inline fun <reified T : Any> Any.getThroughReflection(propertyName: String): T? {
//    val getterName = "get" + propertyName.capitalize()
//    return try {
//        javaClass.getMethod(getterName).invoke(this) as? T
//    } catch (e: NoSuchMethodException) {
//        null
//    }
//}

//fun getFragment(activity: BaseActivity, able_type: String): TabFragment? {
//    val frags = activity.supportFragmentManager.fragments
//    var _frag: TabFragment? = null
//    for (frag in frags) {
//        if (able_type == "arena" && frag::class == ArenaFragment::class) {
//            _frag = frag as ArenaFragment
//            break
//        }
//        if (able_type == "team" && frag::class == TempPlayFragment::class) {
//            _frag = frag as TempPlayFragment
//            break
//        }
//        if (able_type == "course" && frag::class == CourseFragment::class) {
//            _frag = frag as CourseFragment
//            break
//        }
//    }
//
//    return _frag
//}


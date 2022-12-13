package tw.com.bluemobile.hbc.extensions

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.Global
import tw.com.bluemobile.hbc.utilities.getColor

fun ViewGroup.setInfo(context: Context, info: String): TextView {

    val label: TextView = TextView(context)
    val lp: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
    lp.topMargin = 400
    label.layoutParams = lp
    label.gravity = Gravity.CENTER_HORIZONTAL
    label.setTextColor(ContextCompat.getColor(context, R.color.MY_BLACK))
    label.text = info

    this.addView(label)

    return label
}

fun ViewGroup.mask(context: Context): LinearLayout {

    val mask = LinearLayout(context)
    mask.id = R.id.MyMask
    mask.layoutParams = LinearLayout.LayoutParams(this.width, this.height)
    mask.setBackgroundColor(Color.parseColor("#888888"))
    //0是完全透明
    mask.alpha = 0.9f
    mask.setOnClickListener {
        this.unmask()
    }
    this.addView(mask)

    return mask
}

fun ViewGroup.unmask() {

    val mask = this.findViewById<ViewGroup>(R.id.MyMask)
    mask.removeAllViews()
    this.removeView(mask)
}

fun ViewGroup.blackView(context: Context, left: Int, top: Int, width: Int, height: Int): RelativeLayout {

    val blackView = RelativeLayout(context)
//    println(width)
//    println(height)
//    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height)
    lp.setMargins(left, top, 0, 0)
    blackView.layoutParams = lp
//    blackView.orientation = LinearLayout.VERTICAL
    blackView.setBackgroundColor(getColor(context, R.color.MY_BLACK))
    blackView.alpha = 1f
    this.addView(blackView)

    return blackView
}

fun ViewGroup.blackView(context: Context, widthPadding: Int, height: Int): RelativeLayout {
    val screenHeight: Int = Global.getScreenHeight(resources)
    val screenWidth: Int = Global.getScreenWidth(resources)
    val left: Int = widthPadding
    val top: Int = (screenHeight - height)/2 + 100
    val width: Int = screenWidth - 2*widthPadding

    return this.blackView(context, left, top, width, height)
}

//fun ViewGroup.bottomView(context: Context, height: Int): LinearLayout {
//    val bottomView: LinearLayout = LinearLayout(context)
//    bottomView.orientation = LinearLayout.HORIZONTAL
//    val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height)
//    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//    bottomView.layoutParams = lp
//    bottomView.setBackgroundColor(getColor(context, R.color.BOTTOM))
//    bottomView.alpha = 1f
//    this.addView(bottomView)
//
//    return bottomView
//}
//
//fun ViewGroup.bottom2ButtonView(context: Context, submit: ()->Unit, cancel: ()->Unit): LinearLayout {
//    val bottomView: LinearLayout = this.bottomView(context, 300)
//    bottomView.gravity = Gravity.CENTER_VERTICAL
//
//    val buttonSubmit: LinearLayout = View.inflate(context, R.layout.button_submit, null) as LinearLayout
//    bottomView.addView(buttonSubmit)
//    val buttonCancel: LinearLayout = View.inflate(context, R.layout.button_cancel, null) as LinearLayout
//    bottomView.addView(buttonCancel)
//
//    val bottom_button_count: Int = 2
//    val button_width: Int = 120.dpToPx()
//
//    val screenWidth: Int = Global.getScreenWidth(resources)
//    val padding: Int = (screenWidth - bottom_button_count * button_width) / (bottom_button_count + 1)
//
//    var lp = buttonSubmit.layoutParams as ViewGroup.MarginLayoutParams
//    lp.leftMargin = padding
//    buttonSubmit.layoutParams = lp
//
//    lp = buttonCancel.layoutParams as ViewGroup.MarginLayoutParams
//    lp.leftMargin = padding
//    buttonCancel.layoutParams = lp
//
//    buttonSubmit.setOnClickListener {
//        submit.invoke()
//    }
//
//    buttonCancel.setOnClickListener {
//        cancel.invoke()
//    }
//
//    return bottomView
//}

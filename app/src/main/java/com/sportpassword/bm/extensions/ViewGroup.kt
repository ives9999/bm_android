package tw.com.bluemobile.hbc.extensions

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.image
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import com.sportpassword.bm.functions.getColor

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

fun ViewGroup.tableView(context: Context, top: Int = 0, bottom: Int = 0): RecyclerView {

    val tableView = RecyclerView(context)
    tableView.id = R.id.SearchRecycleItem
    val lp1 = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        val lp1 = RecyclerView.LayoutParams(w - (2 * padding), 1000)
    lp1.setMargins(top, 0, 0, bottom)
    tableView.layoutParams = lp1
    tableView.layoutManager = LinearLayoutManager(context)
    tableView.backgroundColor = Color.TRANSPARENT
    this.addView(tableView)

    return tableView
}

fun ViewGroup.buttonPanel(context: Context, height: Int): LinearLayout {
    val view = LinearLayout(context)
    val lp = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    view.layoutParams = lp
    view.setPadding(20, 0, 20, 0)
    val color = ContextCompat.getColor(context, R.color.SEARCH_BACKGROUND)
    view.backgroundColor = color
    view.gravity = Gravity.CENTER
    view.orientation = LinearLayout.VERTICAL
    this.addView(view)

    return view
}

fun ViewGroup.submitButton(context: Context, height: Int, click: ()->Unit): Button {

    val a = context as BaseActivity
    val view = a.layoutInflater.inflate(R.layout.submit_button, null) as Button
    val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    //lp.weight = 1F
    lp.setMargins(32, 12, 32, 24)
    view.layoutParams = lp
    view.onClick {
        click()
    }
    this.addView(view)

    return view
}

fun ViewGroup.cancelButton(context: Context, height: Int, click: ()->Unit): Button {

    val a = context as BaseActivity
    val view = a.layoutInflater.inflate(R.layout.cancel_button, null) as Button
    val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    //lp.weight = 1F
    lp.setMargins(48, 24, 48, 12)
    view.layoutParams = lp
    view.onClick {
        click()
    }
    this.addView(view)

    return view
}

fun ViewGroup.showImages(images: ArrayList<String>, context: Context) {
    images.forEachIndexed { _, s ->
        val imageView: ImageView = ImageView(context)
        this.addView(imageView)
        imageView.scaleType = ImageView.ScaleType.FIT_START
        imageView.adjustViewBounds = true
        val lp = LinearLayout.LayoutParams(this.layoutParams.width, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 0, 0, 16)
        imageView.layoutParams = lp
        s.image(context, imageView)
    }
}

//fun ViewGroup.blackView(context: Context, widthPadding: Int, height: Int): RelativeLayout {
//    val screenHeight: Int = Global.getScreenHeight(resources)
//    val screenWidth: Int = Global.getScreenWidth(resources)
//    val left: Int = widthPadding
//    val top: Int = (screenHeight - height)/2 + 100
//    val width: Int = screenWidth - 2*widthPadding
//
//    return this.blackView(context, left, top, width, height)
//}

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

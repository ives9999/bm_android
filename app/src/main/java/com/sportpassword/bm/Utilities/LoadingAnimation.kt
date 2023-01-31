package com.sportpassword.bm.Utilities

import android.app.Activity
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.sportpassword.bm.R
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

// v1.0.0 created at 2023/01/29 by ives
// v1.1.0 updated at 2023/01/29 by ives 移除originalView， 增加了取得rootView的方式，原來使用ContentView的方式來顯示loading，改為將loading直接覆蓋在rootView上面

class LoadingAnimation constructor(private val context: Activity, private val animationName: String = "loading.json") {

    //現有視圖中最底層的視圖
    var rootView: ViewGroup = context.window.decorView.findViewById<ViewGroup>(android.R.id.content)

    //畫面的layout與設定layout的參數
    private var rLayout: LinearLayout = LinearLayout(context)
    private var rLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

    //載入中動畫container
    private var loadingContainer: RelativeLayout = RelativeLayout(context)

    //載入中動畫與參數
    private var lottieAnimationView: LottieAnimationView = LottieAnimationView(context)
    private var lLayoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

    //載入中文字
    private var loadingTextTV: TextView = TextView(context)

    init {

        initLayout()

        initLoadingImage()
        loadingContainer.addView(lottieAnimationView)

        initLoadingText()
        rLayout.addView(loadingTextTV)
    }

    private fun initLayout() {
        rLayout.orientation = LinearLayout.VERTICAL

        //設定背景為黑色，alpha值0.8
        rLayout.backgroundColor = Color.BLACK
        rLayout.alpha = 0.8F
    }

    private fun initLoadingImage() {

        //包住載入中的container，主要是用來做定位
        val layoutParams = lLayoutParams
        //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        layoutParams.setMargins(0, 150, 0, 0)
        loadingContainer.layoutParams = layoutParams
        rLayout.addView(loadingContainer)

        //載入中動畫主體
        lottieAnimationView.setAnimation(animationName)
        lottieAnimationView.layoutParams = lLayoutParams
    }

    private fun initLoadingText() {

        //載入中文字
        loadingTextTV.text = "努力加載中..."
        loadingTextTV.textColor = ContextCompat.getColor(context, R.color.MY_WHITE)
        loadingTextTV.gravity = Gravity.CENTER
        loadingTextTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
    }

    fun start() {
        rootView.addView(rLayout)
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE
        lottieAnimationView.playAnimation()
    }

    fun stop() {
        rootView.removeView(rLayout)
        lottieAnimationView.cancelAnimation()
    }
}
























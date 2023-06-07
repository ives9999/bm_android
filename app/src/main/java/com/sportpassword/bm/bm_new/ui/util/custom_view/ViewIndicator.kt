package com.sportpassword.bm.bm_new.ui.util.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.imageview.ShapeableImageView
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.ViewIndicatorBinding
import timber.log.Timber

class ViewIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding =
        ViewIndicatorBinding.inflate(LayoutInflater.from(context), this, true)

    private val indicators = mutableListOf(binding.indicatorOne).also {
        it.getOrNull(0)?.isSelected = true
    }

    private var preSelectedIndicators = indicators.getOrNull(0)

    fun addIndicator(num: Int) {
        binding.apply {
            //刷新時,先移除原有的indicator
            indicators.forEach {
                if (it != indicatorOne) {
                    llIndicator.removeView(it)
                }
            }
            indicators.removeAll { it != indicatorOne }
            //再重新產生indicator
            for (i in 0 until num) {
                val indicator = ShapeableImageView(context)
                indicator.setImageResource(R.drawable.sel_match_sign_up_indicator)
                indicator.layoutParams = indicatorOne.layoutParams
                llIndicator.addView(indicator)
                indicators.add(indicator)
            }
        }
    }

    fun setIndicatorSelected(position: Int) {
        preSelectedIndicators?.isSelected = false
        when (position) {
            indicators.size -> {    //這會是第一個
                indicators.getOrNull(0)?.isSelected = true
                preSelectedIndicators = indicators.getOrNull(0)
            }
            indicators.size + 1 -> {        //這會是第二個
                indicators.getOrNull(1)?.isSelected = true
                preSelectedIndicators = indicators.getOrNull(1)
            }
            in 0 until indicators.size -> {     //第二個有2種position,爲size + 1與index 0
                Timber.d("")
                indicators.getOrNull(position)?.isSelected = true
                preSelectedIndicators = indicators.getOrNull(position)
            }
        }

    }
}
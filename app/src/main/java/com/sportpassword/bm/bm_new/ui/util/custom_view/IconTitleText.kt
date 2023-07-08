package com.sportpassword.bm.bm_new.ui.util.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.ViewIconTitleTextBinding
import tw.com.bluemobile.hbc.utilities.getDrawable

class IconTitleText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding =
        ViewIconTitleTextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconTitleText)
        val drawableRes = typedArray.getResourceId(R.styleable.IconTitleText_src, 0)
        val title = typedArray.getString(R.styleable.IconTitleText_titleText) ?: ""
        val content = typedArray.getString(R.styleable.IconTitleText_contentText) ?: ""
        typedArray.recycle()

        binding.apply {
            ivIcon.setImageDrawable(getDrawable(context, drawableRes))
            tvTitle.text = title
            tvContent.text = content
        }
    }

    fun setContent(content: String) {
        binding.apply {
            tvContent.text = content
        }
    }
}
package com.sportpassword.bm.Views

import android.widget.LinearLayout
import android.view.Gravity
import android.widget.ImageButton
import android.widget.TableLayout
import android.text.InputType
import android.widget.EditText
import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.res.TypedArrayUtils.getResourceId
//import com.sportpassword.bm.Views.EditTextWithDeleteButton
import android.content.res.TypedArray
import android.util.AttributeSet
import com.sportpassword.bm.R.layout.activity_main
import android.view.LayoutInflater
import android.view.View
import com.sportpassword.bm.R


/**
 * Created by ives on 2018/2/23.
 */
//class EditTextWithDeleteButton : LinearLayout {
//    protected lateinit var editText: EditText
//    protected lateinit var clearTextButton: ImageButton
//    internal var editTextListener: TextChangedListener? = null
//
//    interface TextChangedListener : TextWatcher
//
//    fun addTextChangedListener(listener: TextChangedListener) {
//        this.editTextListener = listener
//    }
//
//    constructor(context: Context) : super(context) {
//        LayoutInflater.from(context).inflate(R.layout.activity_main, this)
//    }
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        initViews(context, attrs)
//    }
//
//    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : this(context, attrs) {
//        initViews(context, attrs)
//    }
//
//    private fun initViews(context: Context, attrs: AttributeSet) {
//        val a = context.getTheme().obtainStyledAttributes(attrs,
//                R.styleable.EditTextWithDeleteButton, 0, 0)
//        val hintText: String?
//        val deleteButtonRes: Int
//        try {
//            // get the text and colors specified using the names in attrs.xml
//            hintText = a.getString(R.styleable.EditTextWithDeleteButton_hintText)
//            deleteButtonRes = a.getResourceId(
//                    R.styleable.EditTextWithDeleteButton_deleteButtonRes,
//                    R.drawable.text_field_clear_btn)
//
//        } finally {
//            a.recycle()
//        }
//        editText = createEditText(context, hintText)
//        clearTextButton = createImageButton(context, deleteButtonRes)
//
//        this.addView(editText)
//        this.addView(clearTextButton)
//        editText.addTextChangedListener(txtEntered())
//
//
//        editText.setOnFocusChangeListener(object : View.OnFocusChangeListener {
//
//            fun onFocusChange(v: View, hasFocus: Boolean) {
//                if (hasFocus && editText.text.toString().length > 0)
//                    clearTextButton.visibility = View.VISIBLE
//                else
//                    clearTextButton.visibility = View.GONE
//
//            }
//        })
//        clearTextButton.setOnClickListener(object : View.OnClickListener {
//
//            fun onClick(v: View) {
//                editText.setText("")
//            }
//        })
//    }
//
//    fun txtEntered(): TextWatcher {
//        return object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
//                                       count: Int) {
//                if (editTextListener != null)
//                    editTextListener!!.onTextChanged(s, start, before, count)
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                if (editTextListener != null)
//                    editTextListener!!.afterTextChanged(s)
//                if (editText.text.toString().length > 0)
//                    clearTextButton.visibility = View.VISIBLE
//                else
//                    clearTextButton.visibility = View.GONE
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
//                                           after: Int) {
//                if (editTextListener != null)
//                    editTextListener!!.beforeTextChanged(s, start, count, after)
//
//            }
//
//        }
//    }
//
//    @SuppressLint("NewApi")
//    private fun createEditText(context: Context, hintText: String?): EditText {
//        editText = EditText(context)
//        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
//        editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
//        editText.layoutParams = TableLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
//        editText.setHorizontallyScrolling(false)
//        editText.isVerticalScrollBarEnabled = true
//        editText.gravity = Gravity.LEFT
//        editText.background = null
//        editText.hint = hintText
//        return editText
//    }
//
//    private fun createImageButton(context: Context, deleteButtonRes: Int): ImageButton {
//        clearTextButton = ImageButton(context)
//        val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//
//        params.gravity = Gravity.CENTER_VERTICAL
//        clearTextButton.layoutParams = params
//        clearTextButton.setBackgroundResource(deleteButtonRes)
//        clearTextButton.visibility = View.GONE
//        return clearTextButton
//    }
//
//}
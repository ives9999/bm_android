package com.sportpassword.bm.Views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.KEYBOARD
import org.jetbrains.anko.image
import tw.com.bluemobile.hbc.utilities.getDrawable
import tw.com.bluemobile.hbc.utilities.getResourceID

class MainEditText2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.main_edit_text2, this)

    private var titleTV: TextView? = null
    private var requiredTV: TextView? = null
    var contentET: EditText? = null
    private var iconIV: ImageView? = null
    private var deleteIV: ImageView? = null
    private var eyeIV: ImageView? = null

    private var isPasswordShow: Boolean = false
    private var isRequired: Boolean = false

    var text: String = ""

    init {
        view.findViewById<TextView>(R.id.titleTV) ?. let {
            titleTV = it
        }

        view.findViewById<TextView>(R.id.requiredTV) ?. let {
            requiredTV = it
        }

        view.findViewById<EditText>(R.id.contentET) ?. let {
            contentET = it
            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {

                }

                override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                    text = string.toString()
                }
            })
        }

        view.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }

        view.findViewById<ImageView>(R.id.deleteIV) ?. let {
            deleteIV = it
            it.setOnClickListener {
                contentET?.setText("")
            }
        }

        view.findViewById<ImageView>(R.id.eyeIV) ?. let {
            eyeIV = it
            it.setOnClickListener {
                if (isPasswordShow) {
                    contentET?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    contentET?.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                isPasswordShow = !isPasswordShow
            }
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MainEditText2, 0, 0)

            if (typedArray.hasValue(R.styleable.MainEditText2_MainEditText2Title)) {
                val title: String = typedArray.getString(R.styleable.MainEditText2_MainEditText2Title)!!
                titleTV?.setText(title)
            }

            if (typedArray.hasValue(R.styleable.MainEditText2_MainEditText2Icon)) {
                val icon: String = typedArray.getString(R.styleable.MainEditText2_MainEditText2Icon)!!
                setIcon(icon)
            }

            if (typedArray.hasValue(R.styleable.MainEditText2_MainEditText2Hint)) {
                val hint: String = typedArray.getString(R.styleable.MainEditText2_MainEditText2Hint)!!
                setHint(hint)
            }

            if (typedArray.hasValue(R.styleable.MainEditText2_MainEditText2Keyobard)) {
                val keyboard: String = typedArray.getString(R.styleable.MainEditText2_MainEditText2Keyobard)!!
                contentET?.inputType = KEYBOARD.stringToSwift(keyboard)
                if (keyboard == KEYBOARD.password.toString()) {
                    eyeIV?.visibility = View.VISIBLE
                } else {
                    eyeIV?.visibility = View.INVISIBLE
                }
            }

            if (typedArray.hasValue(R.styleable.MainEditText2_MainEditText2Required)) {
                val isRequired: Boolean = typedArray.getBoolean(R.styleable.MainEditText2_MainEditText2Required, false)
                if (isRequired) {
                    requiredTV?.visibility = View.VISIBLE
                } else {
                    requiredTV?.visibility = View.GONE
                }
            }
        }
    }

    fun setIcon(icon: String) {
        val resourceID1: Int = getResourceID(context, icon, "drawable")
        val drawAbleLeft: Drawable = getDrawable(context, resourceID1)!!
        iconIV?.image = drawAbleLeft

//        contentET ?. let {
//            val resourceID1: Int = getResourceID(context, icon, "drawable")
//            val drawAbleLeft: Drawable = getDrawable(context, resourceID1)!!
//
//            //val resourceID2: Int = getResourceID(context, "ic_delete_svg", "drawable")
//            //val drawAbleRight: Drawable = getDrawable(context, resourceID2)!!
//            val drawables: Array<Drawable> = it.compoundDrawables
//            it.setCompoundDrawablesWithIntrinsicBounds(
//                drawAbleLeft,
//                drawables[1],
//                drawables[2],
//                drawables[3]
//            )
//        }
    }

    fun setLabel(label: String) {
        titleTV ?. let {
            it.text = label
        }
    }

    fun setValue(value: String) {
        contentET ?. let {
            it.setText(value)
        }
    }

    private fun setHint(hint: String) {
        contentET ?. let {
            it.hint = hint
        }
    }
}
package com.sportpassword.bm.Views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.LinearLayout
import com.sportpassword.bm.R

class RoundedCorner: LinearLayout {

    //var CORNER_RADIUS: Float = 10.0f

    private var _cornerRadius: Float = 10f
    private lateinit var maskBitmap: Bitmap
    private val maskPaint: Paint = Paint()
    private lateinit var paint: Paint

    private var cornerRadius: Float = 0f

    constructor(context: Context): super(context) {
        init(context)
    }

    // 第二個被執行的區塊
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedCorner, 0, 0)
        if (typedArray.hasValue(R.styleable.RoundedCorner_radius)) {
            _cornerRadius = typedArray.getFloat(R.styleable.RoundedCorner_radius, _cornerRadius)
        }
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context)
    }

    // 第一個被執行的區塊
    init {
        val i = 3
    }

    // 第三個被執行的區塊
    private fun init(context: Context) {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _cornerRadius, metrics)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        maskPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))

        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {
        val offscreenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val offscreenCanvas = Canvas(offscreenBitmap)

        super.draw(offscreenCanvas)
        maskBitmap = createMask(width, height)

        offscreenCanvas.drawBitmap(maskBitmap!!, 0f, 0f, maskPaint)
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint)
    }

    private fun createMask(width: Int, height: Int): Bitmap {
        val mask: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        val canvas: Canvas = Canvas(mask)

        val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(Color.WHITE)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), cornerRadius, cornerRadius, paint)

        return mask
    }
}






















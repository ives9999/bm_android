package com.sportpassword.bm.extensions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class DrawLine(context: Context, val startX: Float, val startY: Float, val stopX: Float, val stopY: Float, val color: Int = Color.BLACK, val stroke: Float = 3f) : View(context) {
    val paint: Paint = Paint()

    init {
        paint.color = color
        paint.strokeWidth = stroke
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(startX, startY, stopX, stopY, paint)
    }
}
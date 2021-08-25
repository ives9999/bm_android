package com.sportpassword.bm.Adapters.Form

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.sportpassword.bm.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.formitem_barcode.*

class BarcodeAdapter(title: String, show: String): FormItemAdapter1("", "", title, "", show) {

    override fun getLayout(): Int {
        return R.layout.formitem_barcode
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        super.bind(viewHolder, position)

        viewHolder.barcode.setImageBitmap(
            createBarcodeBitmap(
                value = show,
                barcodeColor = Color.BLACK,
                backgroundColor = Color.WHITE,
                width = 500,
                height = 150
            )
        )
    }

    private fun createBarcodeBitmap(value: String, @ColorInt barcodeColor: Int, @ColorInt backgroundColor: Int, width: Int, height: Int): Bitmap {

        val bitMatrix = Code128Writer().encode(
            value, BarcodeFormat.CODE_128,width,height
        )
        val pixels = IntArray(bitMatrix.width * bitMatrix.height)

        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )

        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )

        return bitmap
    }
}



































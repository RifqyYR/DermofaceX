package com.makassar.dermofacex.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.os.Build
import android.util.AttributeSet
import android.view.View

class OvalView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private val ovalRect = RectF()
    private val ovalPath = Path()

    init {
        paint.apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 6f
            // Tambahkan efek garis putus-putus
            pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
        }
    }

    fun getOvalRect() = ovalRect
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2
        val centerY = height / 2.3
        val radiusX = width / 2.1
        val radiusY = height / 3.1

        ovalRect.set(
            (centerX - radiusX).toFloat(),
            (centerY - radiusY).toFloat(),
            (centerX + radiusX).toFloat(),
            (centerY + radiusY).toFloat()
        )

        // Menggambar oval dengan garis putus-putus
        ovalPath.reset()  // Pastikan path di-reset sebelum digunakan
        ovalPath.addOval(ovalRect, Path.Direction.CCW)
        canvas.drawPath(ovalPath, paint)
    }
}
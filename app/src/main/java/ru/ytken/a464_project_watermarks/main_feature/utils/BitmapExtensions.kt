package ru.ytken.a464_project_watermarks.main_feature.utils

import android.graphics.*


object BitmapExtensions {
    internal fun Bitmap.rotateBitmap(angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    fun Bitmap.toGrayscale(): Bitmap? {
        val height: Int = this.height
        val width: Int = this.width
        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(this, 0f, 0f, paint)
        return bmpGrayscale
    }
}



package ru.ytken.a464_project_watermarks.main_feature.utils

import android.graphics.*
import org.opencv.android.Utils
import org.opencv.core.Core.addWeighted
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc.*


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

    fun makeImageSharpGaussian(srcBitmap: Bitmap): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(srcBitmap, src)
        val dest = Mat(src.rows(), src.cols(), src.type())
        GaussianBlur(src, dest, Size(0.0,0.0), 10.0)
        addWeighted(src, 1.5, dest, -0.5, 0.0, dest)
        val sharpBitmap = Bitmap.createBitmap(dest.cols(), dest.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dest, sharpBitmap)
        return sharpBitmap
//        val sigma = 1.0
//        val amount = 1.0
//        val blurry = Mat()
//        val sharp = Mat()
//        Utils.bitmapToMat(srcBitmap, sharp)
//        val srcMat = Mat()
//        Utils.bitmapToMat(srcBitmap, srcMat)
//        GaussianBlur(srcMat, blurry, Size(), sigma)
//        addWeighted(srcMat, 1 + amount, blurry, -amount, 0.0, sharp)
//        val sharpBitmap = Bitmap.createBitmap(sharp.cols(), sharp.rows(), Bitmap.Config.ARGB_8888)
//        Utils.matToBitmap(sharp, sharpBitmap)
//        return sharpBitmap
//          val sharpBitmap: Bitmap
//          val srcMat = Mat()
//          Utils.bitmapToMat(srcBitmap, srcMat)
//          val destMat = Mat()
//          GaussianBlur(srcMat, destMat, Size(3.00,3.00), sigmaX)
//          sharpBitmap = Bitmap.createBitmap(destMat.cols(), destMat.rows(), Bitmap.Config.ARGB_8888)
//          Utils.matToBitmap(destMat, sharpBitmap)
//          return sharpBitmap
    }
}

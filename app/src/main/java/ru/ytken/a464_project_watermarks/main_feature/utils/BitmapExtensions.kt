package ru.ytken.a464_project_watermarks.main_feature.utils

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicConvolve3x3
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc.filter2D


object BitmapExtensions {
    internal fun Bitmap.rotateBitmap(angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
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

    fun makeImageSharpGaussian(srcBitmap: Bitmap, context: Context): Bitmap {
        val sharp = floatArrayOf(
            -0.60f, -0.60f, -0.60f,
            -0.60f, 5.81f, -0.60f,
            -0.60f, -0.60f, -0.60f
        )

        val sharp2 = floatArrayOf(
            0f, -1f, 0f,
            -1f, 5f, -1f,
            0f, -1f, 0f
        )

        val sharp3 = floatArrayOf(
            -0.15f, -0.15f, -0.15f,
            -0.15f, 2.2f, -0.15f,
            -0.15f, -0.15f, -0.15f
        )

        val sharp4 = floatArrayOf(
            -0.1f, -0.1f, -0.1f,
            -0.1f, 2f, -0.1f,
            -0.1f, -0.1f, -0.1f
        )

        val sharp5 = floatArrayOf(
            -0.1f, 0.1f, -0.1f,
            0.1f, 0.5f, 0.1f,
            -0.1f, 0.1f, -0.1f
        )

        val sharp6 = floatArrayOf(
            0.0f, -1.0f, 0.0f,
            -1.0f, 5.0f, -1.0f,
            0.0f, -1.0f, 0.0f
        )

//        val bit = srcBitmap.applySharpen(sharp4, context)
//        return bit
        val src = Mat()
        Utils.bitmapToMat(srcBitmap, src)
        val dest = Mat(src.rows(), src.cols(), src.type())
        val convMat = Mat(3, 3, CvType.CV_32F)

        convMat.put(0,0, -0.1)
        convMat.put(0,1, -0.1)
        convMat.put(0,2, -0.1)
        convMat.put(1,0, -0.1)
        convMat.put(1,1, 2.0)
        convMat.put(1,2, -0.1)
        convMat.put(2,0, -0.1)
        convMat.put(2,1, -0.1)
        convMat.put(2,2, -0.1)
        val point = org.opencv.core.Point(-1.0,-1.0)
        filter2D(src, dest, -1, convMat, point)
        //filter2D(src, dest, -1, convMat)
//        GaussianBlur(src, dest, Size(0.0,0.0), 10.0)
//        addWeighted(src, 1.5, dest, -0.5, 0.0, dest)
        val sharpBitmap = Bitmap.createBitmap(dest.cols(), dest.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dest, sharpBitmap)
        return sharpBitmap
    }

    fun Bitmap.applySharpen(
        radius: FloatArray, context: Context,
    ): Bitmap {
        val bitmap = copy(config,true)
        RenderScript.create(context).apply {
            val input = Allocation.createFromBitmap(this,this@applySharpen)
            val output = Allocation.createFromBitmap(this, this@applySharpen)

            ScriptIntrinsicConvolve3x3.create(
                this, Element.U8_4(this)).apply {
                setInput(input)
                setCoefficients(radius)
                forEach(output)
            }
            output.copyTo(bitmap)
            destroy()
        }
        return bitmap
    }
}

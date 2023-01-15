//package ru.ytken.a464_project_watermarks
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//import ru.ytken.a464_project_watermarks.databinding.ActivityImageCropBinding
//
//class ImageCropActivity : AppCompatActivity() {
//
//    companion object {
//        private const val FILE_DIR = "FileDir"
//        fun newIntent(context: Context, selectedFilePath: String) =
//            Intent(context, ImageCropActivity::class.java).putExtra(FILE_DIR, selectedFilePath)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding: ActivityImageCropBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_image_crop)
//        val bitmap = assetToBitmap(intent.extras?.getString(FILE_DIR)!!)
//        binding.documentScanner.setOnLoadListener { loading ->
//            binding.progressBar.isVisible = loading
//        }
//        binding.documentScanner.setImage(bitmap)
//        binding.btnImageCrop.setOnClickListener {
//            lifecycleScope.launch {
//                binding.progressBar.isVisible = true
//                val image = binding.documentScanner.getCroppedImage()
//                binding.progressBar.isVisible = false
//                binding.resultImage.isVisible = true
//                binding.resultImage.setImageBitmap(image)
//            }
//        }
//    }
//
//    private fun assetToBitmap(file: String): Bitmap =
//        contentResolver.openInputStream(Uri.parse(file)).run {
//            BitmapFactory.decodeStream(this)
//        }
//}
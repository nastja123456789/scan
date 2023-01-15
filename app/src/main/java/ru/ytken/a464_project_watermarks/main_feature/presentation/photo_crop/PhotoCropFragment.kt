package ru.ytken.a464_project_watermarks.main_feature.presentation.photo_crop
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_photo_crop.*
import kotlinx.coroutines.launch
import ru.ytken.a464_project_watermarks.R
import java.io.ByteArrayOutputStream
import java.util.*

internal class PhotoCropFragment : Fragment(R.layout.fragment_photo_crop) {

//    private lateinit var imageProcessor: ImageProcessor
//    private lateinit var contourDetector: ContourDetector
//
//    private var selectedImage: Bitmap? = null
//    private lateinit var cropping: CroppingConfiguration
//    private lateinit var pageFileStorage: PageFileStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("fromButtonToCrop") {
                _ , bun ->
            val str = bun.getString("uri")
            val bitmap = assetToBitmap(str!!)
            document_scanner.setOnLoadListener { loading ->
                progressBar.isVisible = loading
            }
            document_scanner.setImage(bitmap)
            btnImageCrop.setOnClickListener {
                lifecycleScope.launch {
                    progressBar.isVisible = true
                    val image = document_scanner.getCroppedImage()
                    progressBar.isVisible = false
                    result_image.isVisible = true
                    result_image.setImageBitmap(image)
                }
            }
//            val uri = Uri.parse(
//                str
//            )
//            selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
//            resultImageView.setImageBitmap(selectedImage)
//            imageButtonCloseCrop.visibility = View.INVISIBLE
//            if (selectedImage == null) {
//                Toast.makeText(context, "Загрузите файл!", Toast.LENGTH_SHORT).show()
//                cropButton.visibility = View.INVISIBLE
//                saveButton.visibility = View.INVISIBLE
//                imageButtonCloseCrop.visibility = View.VISIBLE
//                resultImageView.setImageBitmap(null)
//            }
//            else {
//                createSDK()
//                resultImageView.visibility = View.VISIBLE
//                resultImageView.setImageBitmap(selectedImage)
//            }
//            cropButton.setOnClickListener {
//                crop()
//            }
//            saveButton.setOnClickListener {
////                if (selectedImage!=null) {
////                    findNavController().navigate(R.id.action_photoCropFragment_to_imageResultFragment)
////                } else {
////                    findNavController().popBackStack()
////                }
//            }
//
//            imageButtonCloseCrop.setOnClickListener {
//                ActivityCompat.finishAffinity(requireActivity())
//            }
        }

    }

    private fun assetToBitmap(file: String): Bitmap =
        context!!.contentResolver.openInputStream(Uri.parse(file)).run {
            BitmapFactory.decodeStream(this)
        }

    private fun createSDK() {
//        val scanbot = ScanbotSDK(context!!)
//        pageFileStorage = scanbot.createPageFileStorage()
//        contourDetector = scanbot.createContourDetector()
//        imageProcessor = scanbot.imageProcessor()
    }

    private fun crop() {
//        if (selectedImage!=null) {
//            val pageId = pageFileStorage.add(selectedImage!!)
//
//            val page = Page(pageId, emptyList(), DetectionStatus.OK, ImageFilterType.NONE)
//
//            try {
//                cropping = CroppingConfiguration(page)
//                resultLauncher.launch(cropping)
//            } catch (e: RuntimeException) {
//                findNavController().popBackStack()
//                Toast.makeText(context, "sorry, session has been done", Toast.LENGTH_SHORT).show()
//            }
//        }
//        else {
//            Toast.makeText(context, "Загрузите другой файл!", Toast.LENGTH_SHORT).show()
//            cropButton.visibility = View.INVISIBLE
//            saveButton.visibility = View.INVISIBLE
//        }
    }

//    private var resultLauncher = registerForActivityResultOk(CroppingActivity.ResultContract()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            if (result.resultOk) {
//                val page: String = result.result!!.pageId
//                val image: Bitmap? = pageFileStorage.getImage(page, PageFileStorage.PageFileType.DOCUMENT, BitmapFactory.Options())
//                val bytes = ByteArrayOutputStream()
//                image!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//                val path: String = MediaStore.Images.Media.insertImage(
//                    requireActivity().contentResolver,
//                    image,
//                    "IMG_" + Calendar.getInstance().time,
//                    null
//                )
//                val uri = Uri.parse(path)
//                setFragmentResult("fromCropToImage", bundleOf("uri" to uri.toString()))
//            }
//            findNavController().navigate(R.id.action_photoCropFragment_to_imageResultFragment)
//        }
//    }
    companion object {
    private const val FILE_DIR = "FileDir"
    }
}
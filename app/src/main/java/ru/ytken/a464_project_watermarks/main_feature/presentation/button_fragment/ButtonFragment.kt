package ru.ytken.a464_project_watermarks.main_feature.presentation.button_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_button.*
import ru.ytken.a464_project_watermarks.R
import ru.ytken.a464_project_watermarks.main_feature.presentation.button_fragment.util.ImportImageContract
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ButtonFragment : Fragment(
    R.layout.fragment_button,
) {
    private var photoPath: String?=null
//    private var scanBot: ScanbotSDK? = null
//    private var pageFileStorage: PageFileStorage? = null
//    private var pageProcess: PageProcessor? = null
//    private var pageId: String? = null


    override fun onDestroyView() {
        super.onDestroyView()
//        stopSDK()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        stopSDK()

//        galleryImageLauncher = registerForActivityResult(ImportImageContract()){ resultEntity ->
//            if (resultEntity!=null) {
//                setFragmentResult(
//                    "fromButtonToCrop",
//                    bundleOf("uri" to resultEntity.toString())
//                )
//                findNavController().navigate(R.id.action_buttonFragment_to_photoCropFragment)
//            } else {
//                reLoadFragment()
//            }
//        }

        buttonChoosePhotoFromStorage.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(context!!.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.i("Main", "IOException")
                }
                if (photoFile != null) {
                    val builder = StrictMode.VmPolicy.Builder()
                    StrictMode.setVmPolicy(builder.build())
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    startActivityForResult(cameraIntent, 1231)
                }
            }
//            createSDK()
//
//            galleryImageLauncher.launch(Unit)
//            buttonChoosePhotoFromStorage.visibility = View.INVISIBLE
//            buttonTakePhoto.visibility = View.INVISIBLE
//            progressBarWaitForResult.visibility = View.VISIBLE
        }

//        val resultLauncher = registerForActivityResultOk(DocumentScannerActivity.ResultContract()) { result ->
//
//            if (result.resultOk) {
//
//                val snappedPages: List<Page>? = result.result
//                pageId = snappedPages?.get(0)?.pageId
//                val image = pageFileStorage!!.getImage(pageId!!, PageFileStorage.PageFileType.DOCUMENT, BitmapFactory.Options())
//                val bytes = ByteArrayOutputStream()
//                image!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//
//                val path: String = MediaStore.Images.Media.insertImage(
//                    requireActivity().contentResolver,
//                    image,
//                    "IMG_" + Calendar.getInstance().time,
//                    null)
//
//                val uri = Uri.parse(path)
//                setFragmentResult(
//                    "fromButtonToImage",
//                    bundleOf("uri" to uri.toString())
//                )
//                pageFileStorage!!.remove(pageId!!)
//            }
//            findNavController().navigate(R.id.action_mainFragment_to_imageResultFragment)
//        }

        buttonTakePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1111)
//            createSDK()
//            val cameraConfiguration = DocumentScannerConfiguration()
//            resultLauncher.launch(cameraConfiguration)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            setFragmentResult(
                    "fromButtonToCrop",
                    bundleOf("uri" to data.data.toString())
                )
            findNavController().navigate(R.id.action_mainFragment_to_imageResultFragment)
//            startActivity(ImageCropActivity.newIntent(this, data.data.toString()))
        } else if (requestCode == 1231 && resultCode == Activity.RESULT_OK) {
//            startActivity(ImageCropActivity.newIntent(this, photoPath))
            setFragmentResult(
                "fromButtonToCrop",
                bundleOf("uri" to photoPath)
            )
            findNavController().navigate(R.id.action_mainFragment_to_imageResultFragment)
        }
    }
    private fun createSDK() {
//        scanBot = ScanbotSDK(context!!)
//        pageFileStorage = scanBot!!.createPageFileStorage()
//        pageProcess = scanBot!!.createPageProcessor()
    }

    private fun  stopSDK(){
//        scanBot = null
//        pageFileStorage = null
//        pageProcess = null
    }

//    private fun reLoadFragment(){
//        val id = findNavController().currentDestination?.id
//        findNavController().popBackStack(id!!,true)
//        findNavController().navigate(id)
//    }
//
//    companion object {
//        lateinit var galleryImageLauncher: ActivityResultLauncher<Unit>
//    }
@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
private fun createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    )
    val image = File.createTempFile(
        imageFileName, // prefix
        ".jpg", // suffix
        storageDir      // directory
    )

    // Save a file: path for use with ACTION_VIEW intents
    photoPath = "file:" + image.absolutePath
    return image
}
}
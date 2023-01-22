package ru.ytken.a464_project_watermarks.main_feature.presentation.button_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_button.*
import ru.ytken.a464_project_watermarks.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ButtonFragment : Fragment(
    R.layout.fragment_button,
) {
    private var photoPath: String?=null
    var fileUri: Uri ?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonChoosePhotoFromStorage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryImageLauncher.launch(intent)
        }

        buttonTakePhoto.setOnClickListener {
            val fileName = System.currentTimeMillis().toString() + ".jpg"
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, fileName)
            fileUri =
                requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            pickPhotoLauncher.launch(intent)
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            if (cameraIntent.resolveActivity(context!!.packageManager) != null) {
//                Log.d("cameraIntent","cameraIntent")
//                var photoFile: File? = null
//                try {
//                    photoFile = createImageFile()
//                    Log.d("photoFile","photoFile")
//                } catch (ex: IOException) {
//                    Log.i("Main", "IOException")
//                }
//                if (photoFile != null) {
//                    Log.d("notNull","notNull")
//                    val builder = StrictMode.VmPolicy.Builder()
//                    StrictMode.setVmPolicy(builder.build())
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
//                    pickPhotoLauncher.launch(cameraIntent)
//                }
//            }
        }
        galleryImageLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                setFragmentResult(
                    "fromButtonToCrop",
                    bundleOf("uri" to data!!.data.toString())
                )
                findNavController().navigate(R.id.action_buttonFragment_to_photoCropFragment)
            } else {
                reload()
            }
        }
        pickPhotoLauncher = registerForActivityResult(StartActivityForResult()) { result->
            Log.d("${result.resultCode}","resultCode")
            if (result.resultCode == Activity.RESULT_OK) {
                photoPath = fileUri?.let { getPath(it) }
                photoPath = Uri.fromFile(File(photoPath)).toString()
                setFragmentResult(
                    "fromButtonToCrop",
                    bundleOf("uri" to photoPath)
                )
                findNavController().navigate(R.id.action_buttonFragment_to_photoCropFragment)
            } else {
                reload()
            }
        }
    }


    private fun getPath(selectedImaeUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireActivity().contentResolver.query(selectedImaeUri, projection, null, null,
            null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return cursor.getString(columnIndex)
        }
        return selectedImaeUri.path
    }

    private fun reload() {
        val id = findNavController().currentDestination?.id
        findNavController().popBackStack(id!!,true)
        findNavController().navigate(id)
    }

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
private fun createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    )
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )
    photoPath = "file:" + image.absolutePath
    return image
}
    companion object {
        lateinit var galleryImageLauncher: ActivityResultLauncher<Intent>
        lateinit var pickPhotoLauncher: ActivityResultLauncher<Intent>
    }
}
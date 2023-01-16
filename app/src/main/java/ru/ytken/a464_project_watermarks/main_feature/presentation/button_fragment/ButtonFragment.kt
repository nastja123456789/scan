package ru.ytken.a464_project_watermarks.main_feature.presentation.button_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonChoosePhotoFromStorage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryImageLauncher.launch(intent)
        }

        buttonTakePhoto.setOnClickListener {
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
                    pickPhotoLauncher.launch(cameraIntent)
                }
            }
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
            if (result.resultCode == Activity.RESULT_OK) {
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

    private fun reload() {
        val id = findNavController().currentDestination?.id
        findNavController().popBackStack(id!!,true)
        findNavController().navigate(id)
    }

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
private fun createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(Date())
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
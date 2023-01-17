package ru.ytken.a464_project_watermarks.main_feature.presentation.photo_crop
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("fromButtonToCrop") {
                _ , bun ->
            val str = bun.getString("uri")
            val bitmap = assetToBitmap(str!!)
            document_scanner.setOnLoadListener { loading ->
                progressBar.isVisible = loading
            }
            document_scanner.post {
                document_scanner.setImage(bitmap)
            }

            btnImageCrop.setOnClickListener {
                lifecycleScope.launch {
                    progressBar.isVisible = true
                    val image = document_scanner.getCroppedImage()
                    progressBar.isVisible = false
                    result_image.isVisible = true
                    result_image.setImageBitmap(image)
                    val bytes = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(
                        requireActivity().contentResolver,
                        image,
                    "IMG_" + Calendar.getInstance().time,
                    null
                    )
                    val uri = Uri.parse(path)
                    setFragmentResult("fromCropToImage", bundleOf("uri" to uri.toString()))
                    findNavController().navigate(R.id.action_photoCropFragment_to_imageResultFragment)
                }
            }
        }
    }

    private fun assetToBitmap(file: String): Bitmap =
        context!!.contentResolver.openInputStream(Uri.parse(file)).run {
            BitmapFactory.decodeStream(this)
        }
}
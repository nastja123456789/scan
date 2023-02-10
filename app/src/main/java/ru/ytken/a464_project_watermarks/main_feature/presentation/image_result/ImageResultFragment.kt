package ru.ytken.a464_project_watermarks.main_feature.presentation.image_result

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_image_result.*
import kotlinx.android.synthetic.main.fragment_scan_result.*
import ru.ytken.a464_project_watermarks.R
import ru.ytken.a464_project_watermarks.main_feature.domain.use_cases.SavedImageFactoryUseCase
import ru.ytken.a464_project_watermarks.main_feature.utils.BitmapExtensions.makeImageSharpGaussian

import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class ImageResultFragment: Fragment(R.layout.fragment_image_result) {
    private val vm: ImageResultFragmentViewModel by viewModels {
        SavedImageFactoryUseCase()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("fromCropToImage") {
                _, bun ->
            val str = bun.getString("uri")
            val uri = Uri.parse(
                str
            )
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
//            val bitmapp = makeImageSharpGaussian(bitmap, 1.0)
            val bitmapp = makeImageSharpGaussian(bitmap)
            vm.findTextInBitmap(bitmapp)
            val fdelete = vm.getFilePath(uri, requireContext())?.let { File(it) }
            if (fdelete!!.exists()) {
                if (fdelete.delete()) {
                } else {
                }
            }
        }
        buttonSeeSkan.setOnClickListener {
            vm.setScanImageToInit()
            val bit = vm.scanImage.value
            val bytes = ByteArrayOutputStream()
            bit!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bit,
                "IMG_" + Calendar.getInstance().time,
                null
            )
            val uri = Uri.parse(path)
            setFragmentResult(
                "fromImageToSeeScan",
                bundleOf("uri" to uri.toString())
            )
            val arr = vm.lineBounds
//            for (i in arr) {
//                vm.intervalLineBounds.add(i)
//            }
//            for (i in 0 until vm.intervalLineBounds.size-1){
//                Log.d("${vm.intervalLineBounds[i+1] - vm.intervalLineBounds[i]}","nonono")
//            }
            setFragmentResult(
                "arrayList",
                bundleOf( "array" to arr.toString())
            )
            findNavController().navigate(R.id.action_imageResultFragment_to_seeScanFragment)
        }
        progressBarWaitForImage.visibility = View.VISIBLE
        imageViewResultImage.visibility = View.INVISIBLE
        vm.highlightedImage.observe(viewLifecycleOwner) {
            imageViewResultImage.setImageBitmap(it)
            imageViewResultImage.visibility = View.VISIBLE
            progressBarWaitForImage.visibility = View.INVISIBLE
            if (vm.hasText.value == false) {
                imageButtonClose.visibility = View.VISIBLE
                Toast.makeText(activity, getString(R.string.text_not_found), Toast.LENGTH_SHORT).show()
            } else {
                buttonSeeSkan.visibility = View.VISIBLE
            }
        }
        imageButtonClose.setOnClickListener {
            findNavController().popBackStack(R.id.buttonFragment, true)
            findNavController().navigate(R.id.buttonFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageViewResultImage.setImageBitmap(null)
    }
}
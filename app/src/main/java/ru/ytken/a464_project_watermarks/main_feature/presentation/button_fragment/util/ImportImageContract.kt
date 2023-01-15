package ru.ytken.a464_project_watermarks.main_feature.presentation.button_fragment.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class ImportImageContract : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val imageIntent = Intent()
        imageIntent.type = "image/*"
        imageIntent.action = Intent.ACTION_GET_CONTENT
        imageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, false)
        imageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)

        return Intent.createChooser(imageIntent, "Select picture")
    }

    private fun processGalleryResult(data: Intent): Uri? {
        return data.data
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            return processGalleryResult(intent)
        } else {
            null
        }
    }
}
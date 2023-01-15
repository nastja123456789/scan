package ru.ytken.a464_project_watermarks.main_feature.presentation.see_scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SeeScanFragmentViewModel: ViewModel() {
    private val liveScanLettersText = MutableLiveData<String>()

    var lineBounds: ArrayList<Int> = ArrayList()

    fun setLetterText(text: String) {
        liveScanLettersText.value = text
    }
}

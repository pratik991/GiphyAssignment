package com.example.giphyassignment

import android.view.HapticFeedbackConstants
import android.view.View


fun vibrate(view: View) {
    view.performHapticFeedback(
        HapticFeedbackConstants.VIRTUAL_KEY,
        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
    )
}
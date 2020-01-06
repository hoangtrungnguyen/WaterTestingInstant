package com.hackathon.watertestinginstant.ui.util

import android.app.Activity
import android.util.Log

internal fun Activity.showError(exception: Exception) =
    getErrorMessage(exception).let { errorMessage ->
        Log.e("Scanning", errorMessage, exception)
        this.showSnackbarShort(errorMessage ?: "No exception message")
    }

internal fun Activity.getErrorMessage(exception: Exception): String? {
    return exception.message
}
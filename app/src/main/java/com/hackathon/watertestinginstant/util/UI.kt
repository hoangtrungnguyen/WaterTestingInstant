@file:Suppress("DEPRECATION")

package com.hackathon.watertestinginstant.util

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothClass
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar


internal fun Activity.showSnackbarShort(text: CharSequence) {
    Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show()
}


internal fun Activity.showSnackbarShort(@StringRes text: Int) {
    Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show()
}

internal fun Activity.hideKeyBoard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun Context.setBkg(view: View, @ColorRes res: Int) {
    view.setBackgroundColor(this.resources.getColor(res))
}

val s = BluetoothClass.Service.INFORMATION


fun Context.showDailog(data: HashMap<String, String>?) {
    AlertDialog.Builder(this)
        .setTitle("You are in danger water range at ${data?.get("time")}")
        .setMessage("There is an user at district ${data?.get("address")}")

        .setPositiveButton(
            android.R.string.yes
        ) { dialog, which ->

        }

        // A null listener allows the button to dismiss the dialog and take no further action.
        .setNegativeButton(android.R.string.no) { dialog, which ->
            {

            }
        }
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show()
}

package com.hackathon.watertestinginstant.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.ActivityCompat

private const val REQUEST_INTERNET_CODE = 102


internal fun Activity.requestInternetPermission() =
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.INTERNET),
        REQUEST_INTERNET_CODE
    )

internal fun isInternetPermissionGranted(requestCode: Int, grantResults: IntArray) {
    requestCode == REQUEST_INTERNET_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
}

internal fun Context.isInternetConnection(): Boolean {
    val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            //It will check for both wifi and cellular network
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    } else {
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}
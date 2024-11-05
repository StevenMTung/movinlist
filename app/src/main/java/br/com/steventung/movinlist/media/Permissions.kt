package br.com.steventung.movinlist.media

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat

fun Context.persistUriPermission(uri: Uri) {
    val contentResolver = contentResolver
    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
    contentResolver.takePersistableUriPermission(uri, takeFlags)
}

fun Context.verifyPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) != PackageManager.PERMISSION_GRANTED

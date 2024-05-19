package com.test.landmarkremark.utils

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.gson.Gson
import com.permissionx.guolindev.PermissionX
import retrofit2.HttpException
import java.net.SocketTimeoutException

fun Throwable.is500InternalError() = this is HttpException && code() == 500
fun Throwable.isTimeoutError() = this is SocketTimeoutException
fun <A> A.toJson(): String? {
    return Gson().toJson(this)
}
fun Context.isPermissionGranted(permission: String) = PermissionX.isGranted(this, permission)

@Composable
fun Modifier.debouncedClickable(
    debounceTime: Long = 1000L,
    onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableStateOf(0L) }

    return this.clickable {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            onClick()
        }
    }
}
package com.test.landmarkremark.utils

import android.content.Context
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
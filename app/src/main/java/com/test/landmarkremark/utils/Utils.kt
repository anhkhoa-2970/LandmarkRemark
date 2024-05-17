package com.test.landmarkremark.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.test.landmarkremark.application
import com.test.landmarkremark.presentation.activities.MainActivity
import com.test.landmarkremark.presentation.activities.base.baseActivity
import com.test.landmarkremark.presentation.activities.base.localContext

object Utils {

    fun isNetworkAvailable(): Boolean {
        (localContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    fun getString(resId: Int) = localContext?.resources?.getString(resId) ?: ""

    fun updateLocale(context: Context, localString: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(localString)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localString))
        }
    }

    fun validateEmail(email: String?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun isPasswordFieldValid(password: String): Boolean {
        if (password.length < 8) return false
        val letterPattern = Regex("[a-zA-Z]")
        val digitPattern = Regex("\\d")
        val specialCharPattern = Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\",.<>?/\\\\|]")

        val containsLetter = letterPattern.containsMatchIn(password)
        val containsDigit = digitPattern.containsMatchIn(password)
        val containsSpecialChar = specialCharPattern.containsMatchIn(password)

        // Ensure that the string contains a combination of letters, numbers, and special characters
        return containsLetter && containsDigit && containsSpecialChar
    }

    fun navigateToActivity(isLogin: Boolean, activity: Class<*>){
        SavedStore.saveBoolean(Constants.PREFS_LOGIN, isLogin)
        baseActivity?.startActivity(Intent(baseActivity, activity))
        baseActivity?.finish()
    }

    fun bitmapDescriptor(context: Context, vectorResId: Int): BitmapDescriptor? {
        // retrieve the actual drawable
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // draw it onto the bitmap
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    fun Modifier.shadow(
        color: Color = Color(android.graphics.Color.parseColor("#33000000")),
        borderRadius: Dp = 0.dp,
        blurRadius: Dp = 0.dp,
        offsetY: Dp = 0.dp,
        offsetX: Dp = 0.dp,
        spread: Dp = 0f.dp,
        modifier: Modifier = Modifier
    ) = this.then(
        modifier.drawBehind {
            this.drawIntoCanvas {
                val paint = Paint()
                val frameworkPaint = paint.asFrameworkPaint()
                val spreadPixel = spread.toPx()
                val leftPixel = (0f - spreadPixel) + offsetX.toPx()
                val topPixel = (0f - spreadPixel) + offsetY.toPx()
                val rightPixel = (this.size.width + spreadPixel)
                val bottomPixel = (this.size.height + spreadPixel)

                if (blurRadius != 0.dp) {
                    frameworkPaint.maskFilter =
                        (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
                }

                frameworkPaint.color = color.toArgb()
                it.drawRoundRect(
                    left = leftPixel,
                    top = topPixel,
                    right = rightPixel,
                    bottom = bottomPixel,
                    radiusX = borderRadius.toPx(),
                    radiusY = borderRadius.toPx(),
                    paint
                )
            }
        }
    )
}
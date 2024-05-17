package com.test.landmarkremark.presentation.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.test.landmarkremark.R
import com.test.landmarkremark.presentation.activities.base.BaseActivity
import com.test.landmarkremark.presentation.activities.base.localContext
import com.test.landmarkremark.presentation.composes.auth.AuthScreen
import com.test.landmarkremark.presentation.ui.theme.BaseAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseAppComposeTheme {
                localContext = LocalContext.current
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.white)) {
                    AuthScreen()
                }
            }
        }
    }
}

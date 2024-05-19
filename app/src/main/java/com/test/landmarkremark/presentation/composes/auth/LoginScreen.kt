package com.test.landmarkremark.presentation.composes.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.test.landmarkremark.R
import com.test.landmarkremark.presentation.activities.MainActivity
import com.test.landmarkremark.presentation.composes.common.CommonButton
import com.test.landmarkremark.presentation.composes.common.CustomInputView
import com.test.landmarkremark.presentation.composes.common.CustomToast
import com.test.landmarkremark.presentation.composes.common.ShowProgressDialog
import com.test.landmarkremark.presentation.ui.states.ProgressState
import com.test.landmarkremark.presentation.viewmodels.auth.AuthViewModel
import com.test.landmarkremark.utils.Utils.navigateToActivity

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    // holding state register or login: true - login || false - register
    var isHaveAccount by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var onError by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
					keyboardController?.hide()
					focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isHaveAccount) stringResource(id = R.string.login) else stringResource(id = R.string.register),
            style = TextStyle(
                color = Color(0xFF181725),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        CustomInputView(title = stringResource(id = R.string.email),
            isRequiredLabel = !isHaveAccount,
            textValue = email,
            onTextValueChanged = { email = it })

        Box(modifier = Modifier.height(6.dp))

        CustomInputView(
            title = stringResource(id = R.string.password),
            isRequiredLabel = !isHaveAccount,
            textValue = password,
            onTextValueChanged = { password = it },
//			visualTransformation = PasswordVisualTransformation()
        )

        if (!isHaveAccount) {
            Box(modifier = Modifier.height(6.dp))

            CustomInputView(title = stringResource(id = R.string.user_name),
                isRequiredLabel = true,
                textValue = userName,
                onTextValueChanged = { userName = it })
        }
        Box(modifier = Modifier.height(12.dp))

        CommonButton(
            text = if (isHaveAccount) stringResource(id = R.string.login) else stringResource(
                id = R.string.register
            ), onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                if (isHaveAccount) {
                    viewModel.login(
                        email = email.trim(),
                        password = password,
                        onSuccess = { navigateToActivity(true, MainActivity::class.java) }) {
                        onError = it
                    }
                } else {
                    viewModel.register(
                        email = email.trim(),
                        password = password,
                        userName = userName.trim(),
                        onSuccess = { navigateToActivity(true, MainActivity::class.java) }) {
                        onError = it
                    }
                }
            })

        TextButton(onClick = {
            keyboardController?.hide()
            focusManager.clearFocus()
            isHaveAccount = !isHaveAccount
        }) {
            Text(
                if (isHaveAccount) stringResource(id = R.string.not_have_account) else stringResource(
                    id = R.string.have_account
                )
            )
        }
    }

    if (onError.isNotBlank()) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(horizontal = 16.dp)
        ) {
            CustomToast(onError) {
                onError = ""
            }
        }
    }
    viewModel.let {
        val uiState by it.uiState.collectAsState()
        when (uiState.progressState) {
            ProgressState.Loading -> ShowProgressDialog()
            else -> {}
        }
    }

}
package com.test.landmarkremark.presentation.viewmodels.auth

import androidx.lifecycle.viewModelScope
import com.test.landmarkremark.data.base.handleNetworkResult
import com.test.landmarkremark.domain.usecases.AuthUseCase
import com.test.landmarkremark.presentation.ui.states.ProgressState
import com.test.landmarkremark.presentation.viewmodels.base.BaseViewModel
import com.test.landmarkremark.utils.Utils
import com.test.landmarkremark.utils.Utils.validateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val useCase: AuthUseCase // UseCase for handling authentication
) : BaseViewModel() {

	// Function to register a new user
	fun register(
		email: String?,
		password: String?,
		userName: String?,
		onSuccess: () -> Unit,
		onFail: (String) -> Unit
	) {
		// Validate email and password
		val errorMessage = validateCredentials(email, password)
		if (errorMessage != null) {
			onFail(errorMessage)
			return
		}
		updateProgressState(ProgressState.Loading)
		viewModelScope.launch {
			// Asynchronously registers the user
			useCase.register(email!!, password!!, userName = userName!!).collectLatest { networkResult ->
				networkResult.handleNetworkResult(
					success = {
						onSuccess()
					},
					fail = {
						onFail(it.message ?: "Unknown error")
					},
					loading = {
						if (it) {
							updateProgressState(ProgressState.Loading)
						} else {
							updateProgressState(ProgressState.NoLoading)
						}
					}
				)
			}
		}
	}

	// Function to login a user
	fun login(
		email: String?,
		password: String?,
		onSuccess: () -> Unit,
		onFail: (String) -> Unit
	) {
		// Validate email and password
		val errorMessage = validateCredentials(email, password)
		if (errorMessage != null) {
			onFail(errorMessage)
			return
		}

		updateProgressState(ProgressState.Loading)
		viewModelScope.launch {
			// Asynchronously logs in the user
			useCase.login(email!!, password!!).collectLatest { networkResult ->
				networkResult.handleNetworkResult(
					success = {
						onSuccess()
					},
					fail = {
						onFail(it.message ?: "Unknown error")
					},
					loading = {
						if (it) {
							updateProgressState(ProgressState.Loading)
						} else {
							updateProgressState(ProgressState.NoLoading)
						}
					}
				)
			}
		}
	}

	// Function to validate email and password
	private fun validateCredentials(email: String?, password: String?): String? {
		return when {
			email.isNullOrBlank() -> "Email is empty"
			!validateEmail(email) -> "Email is invalid"
			password.isNullOrBlank() -> "Password is empty"
			!Utils.isPasswordFieldValid(password) -> "Invalid password"
			else -> null
		}
	}
}



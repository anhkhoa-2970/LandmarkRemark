package com.test.landmarkremark.domain.usecases

import com.test.landmarkremark.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) {
	suspend fun login(email: String, password: String) = authRepository.login(email, password)
	suspend fun register(email: String, password: String, userName: String) = authRepository.register(email, password, userName)

	suspend fun logout() = authRepository.logout()
}
package com.test.landmarkremark.domain.repositories

import com.google.firebase.auth.FirebaseUser
import com.test.landmarkremark.data.base.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
	suspend fun register(email: String, password: String, userName: String): Flow<NetworkResult<FirebaseUser>>
	suspend fun login(email: String, password: String): Flow<NetworkResult<FirebaseUser>>

	suspend fun logout(): Flow<NetworkResult<Any>>
}
package com.test.landmarkremark.data.repositories

import android.provider.ContactsContract.CommonDataKinds.Note
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.repositories.AuthRepository
import com.test.landmarkremark.presentation.ui.states.ProgressState
import com.test.landmarkremark.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val auth: FirebaseAuth,
	private val firestore: FirebaseFirestore
) : AuthRepository {
	override suspend fun register(email: String, password: String, userName: String): Flow<NetworkResult<FirebaseUser>> = flow {
		try {
			val authResult = auth.createUserWithEmailAndPassword(email, password).await()
			val user = authResult.user
			if (user != null) {
				val userMap = UserInfoModel(
					uid = user.uid,
					username = userName,
					email = email,
					notes = emptyList()
				)
				// add info of user to fireStore
				firestore.collection("users").document(user.uid).set(userMap).await()
				emit(NetworkResult.Success("User data saved successfully", user))
			} else {
				emit(NetworkResult.Failure("User registration failed", Constants.CODE_UNKNOWN))
			}
		} catch (e: Exception) {
			emit(NetworkResult.Failure(e.message ?: "Failed to save user data", Constants.CODE_UNKNOWN))
		}
	}

	override suspend fun login(email: String, password: String): Flow<NetworkResult<FirebaseUser>> = flow {
		try {
			val authResult = auth.signInWithEmailAndPassword(email, password).await()
			emit(NetworkResult.Success("", authResult.user))
		} catch (e: Exception) {
			emit(NetworkResult.Failure(e.message ?: "Login failed", Constants.CODE_UNKNOWN))
		}
	}

	override suspend fun logout(): Flow<NetworkResult<Any>> = flow {
		try {
			auth.signOut()
			emit(NetworkResult.Success("Successfully logged out", Unit))
		} catch (e: Exception) {
			emit(NetworkResult.Failure(e.message ?: "Logout failed", Constants.CODE_UNKNOWN))
		}
	}

}

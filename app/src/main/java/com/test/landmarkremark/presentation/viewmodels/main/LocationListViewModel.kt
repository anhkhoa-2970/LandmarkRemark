package com.test.landmarkremark.presentation.viewmodels.main

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.test.landmarkremark.data.base.handleNetworkResult
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.usecases.AuthUseCase
import com.test.landmarkremark.domain.usecases.NoteUseCase
import com.test.landmarkremark.presentation.ui.states.ProgressState
import com.test.landmarkremark.presentation.viewmodels.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
	private val noteUseCase: NoteUseCase, // Dependency for fetching user notes
	private val authUseCase: AuthUseCase, // Dependency for fetching user notes
	auth: FirebaseAuth // Firebase authentication dependency
) : BaseViewModel() {
	// State indicating if the view is refreshing
	private val _isRefreshing = MutableStateFlow(false)
	val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

	// StateFlow holding the list of user notes
	private val _notes = MutableStateFlow<List<UserInfoModel>>(emptyList())
	val notes: StateFlow<List<UserInfoModel>> get() = _notes

	// StateFlow holding the current authenticated user
	private var _currentUser = MutableStateFlow<FirebaseUser?>(null)
	val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

	init {
		_currentUser.value = auth.currentUser // Initializes _currentUser with the current authenticated user
	}

	// Function to load user notes
	fun loadUserNotes(onError: (String) -> Unit) {
		if (_currentUser.value != null) { // Fetches user notes only if there's a current user authenticated
			updateProgressState(ProgressState.Loading) // Updates progress state to indicate loading
			viewModelScope.launch {
				// Asynchronously fetches user notes and collects the latest result
				noteUseCase.getNotes().collectLatest { networkResult ->
					networkResult.handleNetworkResult(
						success = {
							_notes.value = it.data ?: emptyList()
						},
						fail = {
							onError(it.message ?: "Unknown error!")
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
	}

	fun logout(onSuccess: ()-> Unit,onError: (String) -> Unit) {
		if (_currentUser.value != null) { // Fetches user notes only if there's a current user authenticated
			updateProgressState(ProgressState.Loading) // Updates progress state to indicate loading
			viewModelScope.launch {
				// Asynchronously fetches user notes and collects the latest result
				authUseCase.logout().collectLatest { networkResult ->
					networkResult.handleNetworkResult(
						success = {
							onSuccess()
						},
						fail = {
							onError(it.message ?: "Unknown error!")
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
	}
}


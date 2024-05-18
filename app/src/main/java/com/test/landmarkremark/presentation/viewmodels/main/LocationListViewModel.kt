package com.test.landmarkremark.presentation.viewmodels.main

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.test.landmarkremark.data.base.handleNetworkResult
import com.test.landmarkremark.domain.models.NoteModel
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
import org.joda.time.DateTime
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
			updateProgressState(ProgressState.Loading)
			viewModelScope.launch {
				// Asynchronously fetches user notes and collects the latest result
				noteUseCase.getNotes().collectLatest { networkResult ->
					networkResult.handleNetworkResult(
						success = {
							_notes.value =it.data?.let {listItem->  moveItemToTop(listItem, _currentUser.value!!.uid) } ?: emptyList()
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

	// move item note of current user to top
	private fun moveItemToTop(userList: List<UserInfoModel>, targetUid: String): List<UserInfoModel> {
		val mutableList = userList.toMutableList()
		val targetIndex = mutableList.indexOfFirst { it.uid == targetUid }
		if (targetIndex != -1 && targetIndex != 0) {
			val targetItem = mutableList.removeAt(targetIndex)
			mutableList.add(0, targetItem)
		}

		return mutableList.toList()
	}

	fun editMyNote(userId: String, note: NoteModel, newNote: String,onEditSuccess:()->Unit, onEditError: (String) -> Unit){
		if (_currentUser.value != null) { // Fetches user notes only if there's a current user authenticated
			updateProgressState(ProgressState.Loading)
			viewModelScope.launch {
				// Asynchronously edit note of current user and collects the latest list notes
				noteUseCase.editMyNote(userId,note.id, NoteModel(id = DateTime.now().toString(), text =  newNote, latitude = note.latitude, longitude = note.longitude)).collectLatest { networkResult ->
					networkResult.handleNetworkResult(
						success = {
							onEditSuccess()
							loadUserNotes{errorMsg -> onEditError(errorMsg) }
						},
						fail = {
							onEditError(it.message ?: "Unknown error!")
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

	fun updateUserName(userId: String, newUserName: String,onEditSuccess:()->Unit, onEditError: (String) -> Unit){
		if (_currentUser.value != null) { // Fetches user notes only if there's a current user authenticated
			updateProgressState(ProgressState.Loading)
			viewModelScope.launch {
				// Asynchronously edit note of current user and collects the latest list notes
				noteUseCase.updateUserName(userId, newUserName).collectLatest { networkResult ->
					networkResult.handleNetworkResult(
						success = {
							onEditSuccess()
							loadUserNotes{errorMsg -> onEditError(errorMsg) }
						},
						fail = {
							onEditError(it.message ?: "Unknown error!")
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

	fun deleteMyNote(userId: String, noteId: String,onDeleteError: (String) -> Unit){
		if (_currentUser.value != null) { // Fetches user notes only if there's a current user authenticated
			updateProgressState(ProgressState.Loading)
			viewModelScope.launch {
				// Asynchronously delete note of current user and collects the latest list notes
				noteUseCase.deleteNote(userId,noteId).collectLatest { networkResult ->
					networkResult.handleNetworkResult(
						success = {
							loadUserNotes{errorMsg -> onDeleteError(errorMsg) }
						},
						fail = {
							onDeleteError(it.message ?: "Unknown error!")
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


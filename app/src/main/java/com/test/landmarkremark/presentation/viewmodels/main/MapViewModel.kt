package com.test.landmarkremark.presentation.viewmodels.main

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.test.landmarkremark.data.base.handleNetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.usecases.NoteUseCase
import com.test.landmarkremark.presentation.composes.common.ToastMode
import com.test.landmarkremark.presentation.ui.states.ProgressState
import com.test.landmarkremark.presentation.viewmodels.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
	private val auth: FirebaseAuth, // Firebase authentication dependency
	private val noteUseCase: NoteUseCase // UseCase for handling notes
) : BaseViewModel() {

	// StateFlow holding the list of user info
	private var _listUserInfo = MutableStateFlow<List<UserInfoModel>?>(null)
	val listUserInfo: StateFlow<List<UserInfoModel>?> get() = _listUserInfo

	// Function to update the list of user info
	fun updateListUser(data: List<UserInfoModel>?) {
		_listUserInfo.value = data
	}

	// Function to add a note to a user
	private fun addNoteToUser(uid: String, newNote: NoteModel?) {
		val currentList = _listUserInfo.value?.toMutableList() ?: mutableListOf()

		val userIndex = currentList.indexOfFirst { it.uid == uid }
		if (userIndex != -1) {
			val userInfo = currentList[userIndex]
			val updatedUserInfo = userInfo.copy(
				notes = userInfo.notes?.toMutableList()?.apply {
					if (newNote != null) {
						add(newNote)
					}
				}
			)
			currentList[userIndex] = updatedUserInfo
			_listUserInfo.value = currentList
		}
	}

	// Function to save a note
	fun saveNote(
		text: String,
		latLng: LatLng,
		onFinished: (ToastMode, String) -> Unit
	) {
		val currentUser = auth.currentUser
		if (currentUser != null) {
			val userId = currentUser.uid
			val note = NoteModel(id= DateTime.now().toString(),text = text, latitude = latLng.latitude, longitude = latLng.longitude)
			viewModelScope.launch {
				noteUseCase.saveNote(userId, note).collectLatest { networkResult ->
					networkResult.handleNetworkResult(
						success = {
							addNoteToUser(currentUser.uid, it.data) // Adds note to user on success
							onFinished(ToastMode.SUCCESS,"Add note successfully!")
						},
						fail = {
							onFinished(ToastMode.ERROR,it.message ?: "Unknown error!")
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
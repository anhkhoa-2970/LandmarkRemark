package com.test.landmarkremark.domain.repositories

import kotlinx.coroutines.flow.Flow
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel

interface NoteRepository {
	fun saveNoteForUser(userId: String, note: NoteModel): Flow<NetworkResult<NoteModel>>
	fun getAllUsersWithNotes(): Flow<NetworkResult<List<UserInfoModel>>>
	fun editMyNote(userId: String, noteId: String, updatedNote: NoteModel): Flow<NetworkResult<Any>>

	fun deleteMyNote(userId: String, noteId: String): Flow<NetworkResult<String>>
}
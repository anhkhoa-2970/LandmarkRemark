package com.test.landmarkremark.domain.repositories

import kotlinx.coroutines.flow.Flow
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel

interface NoteRepository {
	suspend fun saveNoteForUser(userId: String, note: NoteModel): Flow<NetworkResult<NoteModel>>
	suspend fun getAllUsersWithNotes(): Flow<NetworkResult<List<UserInfoModel>>>
}
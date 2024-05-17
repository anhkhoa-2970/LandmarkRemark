package com.test.landmarkremark.domain.usecases

import kotlinx.coroutines.flow.Flow
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.repositories.NoteRepository
import javax.inject.Inject

class NoteUseCase @Inject constructor(private val courseRepository: NoteRepository) {
    suspend fun getNotes(): Flow<NetworkResult<List<UserInfoModel>>> {
        return courseRepository.getAllUsersWithNotes()
    }

    suspend fun saveNote(
        userId: String,
        note: NoteModel
    ): Flow<NetworkResult<NoteModel>>
    {
        return courseRepository.saveNoteForUser(userId, note)
    }
}
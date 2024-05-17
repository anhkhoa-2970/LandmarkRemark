package com.test.landmarkremark.domain.usecases

import kotlinx.coroutines.flow.Flow
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.repositories.NoteRepository
import javax.inject.Inject

class NoteUseCase @Inject constructor(private val courseRepository: NoteRepository) {
    fun getNotes(): Flow<NetworkResult<List<UserInfoModel>>> =
        courseRepository.getAllUsersWithNotes()

    fun saveNote(
        userId: String,
        note: NoteModel
    ): Flow<NetworkResult<NoteModel>> =
        courseRepository.saveNoteForUser(userId, note)
    fun editMyNote(userId: String, noteId: String, updatedNote: NoteModel): Flow<NetworkResult<Any>> =
        courseRepository.editMyNote(userId, noteId, updatedNote)

    fun deleteNote(userId: String, noteId: String): Flow<NetworkResult<String>> =
        courseRepository.deleteMyNote(userId, noteId)
}
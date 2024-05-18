package com.test.landmarkremark.domain.usecases

import kotlinx.coroutines.flow.Flow
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.repositories.NoteRepository
import javax.inject.Inject

class NoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    fun getNotes(): Flow<NetworkResult<List<UserInfoModel>>> =
        noteRepository.getAllUsersWithNotes()

    fun saveNote(
        userId: String,
        note: NoteModel
    ): Flow<NetworkResult<NoteModel>> =
        noteRepository.saveNoteForUser(userId, note)

    fun editMyNote(
        userId: String,
        noteId: String,
        updatedNote: NoteModel
    ): Flow<NetworkResult<Any>> =
        noteRepository.editMyNote(userId, noteId, updatedNote)

    fun updateUserName(userId: String, newUserName: String): Flow<NetworkResult<Any>> =
        noteRepository.updateUserName(userId, newUserName)

    fun deleteNote(userId: String, noteId: String): Flow<NetworkResult<String>> =
        noteRepository.deleteMyNote(userId, noteId)
}
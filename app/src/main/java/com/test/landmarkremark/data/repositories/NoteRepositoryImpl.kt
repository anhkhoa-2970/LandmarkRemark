package com.test.landmarkremark.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.repositories.NoteRepository
import com.test.landmarkremark.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {
    override fun saveNoteForUser(userId: String, note: NoteModel): Flow<NetworkResult<NoteModel>> = flow {
        try {
            // get info of user by userId and update new note for it
            val userRef = firestore.collection("users").document(userId)
            val userSnapshot = userRef.get().await()
            if (userSnapshot.exists()) {
                val userNotes = userSnapshot.toObject(UserInfoModel::class.java)?.notes?.toMutableList() ?: mutableListOf()

                userNotes.add(note)
                userRef.update("notes", userNotes).await()
                emit(NetworkResult.Success("Note saved successfully", note))
            } else {
                emit(NetworkResult.Failure("User not found", 404))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Failure(e.toString(), Constants.CODE_UNKNOWN))
        }
    }

    override fun editMyNote(userId: String, noteId: String, updatedNote: NoteModel): Flow<NetworkResult<Any>> = flow {
        try {
            // Get the user document reference
            val userRef = firestore.collection("users").document(userId)

            // Fetch the user snapshot
            val userSnapshot = userRef.get().await()

            if (userSnapshot.exists()) {
                // Get the user's notes
                val userInfo = userSnapshot.toObject(UserInfoModel::class.java)
                val userNotes = userInfo?.notes?.toMutableList() ?: mutableListOf()

                // Find the index of the note to be updated
                val noteIndex = userNotes.indexOfFirst { it.id == noteId }

                if (noteIndex != -1) {
                    // Update the note
                    userNotes[noteIndex] = updatedNote

                    // Update the notes field in Firestore
                    userRef.update("notes", userNotes).await()

                    emit(NetworkResult.Success("Note updated successfully", updatedNote))
                } else {
                    emit(NetworkResult.Failure("Note not found", 404))
                }
            } else {
                emit(NetworkResult.Failure("User not found", 404))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Failure(e.toString(), Constants.CODE_UNKNOWN))
        }
    }

    override fun updateUserName(userId: String, newUserName: String): Flow<NetworkResult<Unit>> = flow {
        try {
            val userRef = firestore.collection("users").document(userId)
            userRef.update("username", newUserName).await()
            emit(NetworkResult.Success("Username updated successfully", null))
        } catch (e: Exception) {
            emit(NetworkResult.Failure(e.message ?: "Failed to update username", Constants.CODE_UNKNOWN))
        }
    }

    override fun deleteMyNote(userId: String, noteId: String): Flow<NetworkResult<String>> = flow {
        try {
            // Get the user document reference
            val userRef = firestore.collection("users").document(userId)

            // Fetch the user snapshot
            val userSnapshot = userRef.get().await()

            if (userSnapshot.exists()) {
                // Get the user's notes
                val userInfo = userSnapshot.toObject(UserInfoModel::class.java)
                val userNotes = userInfo?.notes?.toMutableList() ?: mutableListOf()

                // Find the index of the note to be deleted
                val noteIndex = userNotes.indexOfFirst { it.id == noteId }

                if (noteIndex != -1) {
                    // Remove the note
                    userNotes.removeAt(noteIndex)

                    // Update the notes field in Firestore
                    userRef.update("notes", userNotes).await()

                    emit(NetworkResult.Success("Note deleted successfully", noteId))
                } else {
                    emit(NetworkResult.Failure("Note not found", 404))
                }
            } else {
                emit(NetworkResult.Failure("User not found", 404))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Failure(e.toString(), Constants.CODE_UNKNOWN))
        }
    }

    //get all user from firestore
    override fun getAllUsersWithNotes(): Flow<NetworkResult<List<UserInfoModel>>> = flow {
        try {
            val usersSnapshot = firestore.collection("users").get().await()
            val usersWithNotes = mutableListOf<UserInfoModel>()
            for (userDoc in usersSnapshot.documents) {
                val user = userDoc.toObject(UserInfoModel::class.java)
                usersWithNotes.add(user ?: continue)
            }
            emit(NetworkResult.Success("Retrieved all users with notes successfully", usersWithNotes))
        } catch (e: Exception) {
            emit(NetworkResult.Failure(e.toString(), Constants.CODE_UNKNOWN))
        }
    }
}

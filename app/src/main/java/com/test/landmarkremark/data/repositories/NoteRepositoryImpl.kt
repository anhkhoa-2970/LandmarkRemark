package com.test.landmarkremark.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.test.landmarkremark.data.base.NetworkResult
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {


    override suspend fun saveNoteForUser(userId: String, note: NoteModel): Flow<NetworkResult<NoteModel>> = flow {
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
            emit(NetworkResult.Failure(e.toString(), 1000))
        }
    }

    //get all user from firestore
    override suspend fun getAllUsersWithNotes(): Flow<NetworkResult<List<UserInfoModel>>> = flow {
        try {
            val usersSnapshot = firestore.collection("users").get().await()
            val usersWithNotes = mutableListOf<UserInfoModel>()
            for (userDoc in usersSnapshot.documents) {
                val user = userDoc.toObject(UserInfoModel::class.java)
                usersWithNotes.add(user ?: continue)
            }
            emit(NetworkResult.Success("Retrieved all users with notes successfully", usersWithNotes))
        } catch (e: Exception) {
            emit(NetworkResult.Failure(e.toString(), 1000))
        }
    }
}

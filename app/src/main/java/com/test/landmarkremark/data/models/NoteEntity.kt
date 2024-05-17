package com.test.landmarkremark.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class CourseEntity(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
)
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String, // Unique identifier for the note
    val text: String,
    val latitude: Double,
    val longitude: Double,
    val userName: String
)
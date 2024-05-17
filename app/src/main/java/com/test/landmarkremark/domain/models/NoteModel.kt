package com.test.landmarkremark.domain.models


data class UserInfoModel(
	var uid: String? = null,
	var username: String? = null,
	var email: String? = null,
	var notes: List<NoteModel>? = null
)

data class NoteModel(
	val text: String = "",
	val latitude: Double = 0.0,
	val longitude: Double = 0.0
)


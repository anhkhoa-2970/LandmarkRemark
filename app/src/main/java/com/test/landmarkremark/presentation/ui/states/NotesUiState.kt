package com.test.landmarkremark.presentation.ui.states

import com.test.landmarkremark.domain.models.NoteModel

data class NotesUiState(var notes: List<NoteModel> = emptyList())
package com.test.landmarkremark.presentation.ui.states

import com.test.landmarkremark.utils.Constants

data class UiState(
    var progressState: ProgressState = ProgressState.NoLoading,
    var messageState: MessageState = MessageState(),
)

enum class ProgressState {
    Loading,
    NoLoading
}

data class MessageState(
    var msg: String = "",
    var type: Constants.MessageType = Constants.MessageType.SUCCESS
)
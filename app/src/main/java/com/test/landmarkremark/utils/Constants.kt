package com.test.landmarkremark.utils

import androidx.annotation.StringRes
import com.test.landmarkremark.R


object Constants {
	//region HTTP Code
	const val CODE_ERROR_INTERNAL_SERVER = 500
	const val CODE_EXPIRED_TOKEN = 401
	const val CODE_EXPIRED_ACCOUNT = 419
	const val CODE_UNKNOWN = 1000
	const val HTTP_RESULT = "ok"
	const val HTTP_HEADER_TOKEN = "token"
	const val MESSAGE = "message"
	//endregion

	//region SaveStore Keys
	const val PREFS_LANGUAGE_MODEL = "PREFS_LANGUAGE_MODEL"
	const val PREFS_LOGIN = "PREFS_LOGIN"
	//endregion

	enum class Screen(@StringRes val title: Int) {
		LocationListScreen(title = R.string.location_list_screen),
		MapScreen(title = R.string.map_screen),
	}

	enum class MessageType {
		ERROR, SUCCESS
	}
	enum class EventType {
		CLEAR_DATA_GO_TO_LOGIN
	}

	class MessageEvent(val eventType: EventType) {
		var data: Any? = null
	}

	//state to handle only show map or show map with dialog
	enum class ActionToMap(val action: String) {
		SeeMap("SeeMap"),
		AddNote("AddNote")
	}
}
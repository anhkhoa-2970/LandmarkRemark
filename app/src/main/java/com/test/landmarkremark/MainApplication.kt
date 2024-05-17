package com.test.landmarkremark

import android.app.Application
import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

var application: MainApplication? = null

@HiltAndroidApp
class MainApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		application = this
		Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.MAPS_API_KEY)
		FirebaseApp.initializeApp(this)
		Firebase.appCheck.installAppCheckProviderFactory(
			PlayIntegrityAppCheckProviderFactory.getInstance(),
		)

	}

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
	}
}

package com.test.landmarkremark.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.permissionx.guolindev.PermissionX

class LocationRequestUpdate(private val context: Context) {

	private val TAG = LocationRequestUpdate::class.java.simpleName
	private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
		.setWaitForAccurateLocation(false)
		.setMinUpdateIntervalMillis(2000)
		.setMaxUpdateDelayMillis(100)
		.build()
	private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

	@SuppressLint("MissingPermission")
	fun requestLocationUpdates(callback: (LatLng) -> Unit) {
		if (PermissionX.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
			val locationCallback = object : LocationCallback() {
				override fun onLocationResult(locationResult: LocationResult) {
					locationResult.lastLocation?.let {
						val latitude = it.latitude
						val longitude = it.longitude
						callback.invoke(LatLng(latitude, longitude))
						Log.d(TAG, "requestLocationUpdates() called $latitude, $longitude")
					}
				}
			}
			fusedLocationClient.requestLocationUpdates(
				locationRequest,
				locationCallback,
				null
			)
		}
	}

	@SuppressLint("MissingPermission")
	fun getCurrentLocation(successCallback: (LatLng) -> Unit, failureCallback: (Exception) -> Unit) {
		fun getRefreshLocation() {
			val cancellationTokenSource = CancellationTokenSource()
			fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancellationTokenSource.token).addOnSuccessListener {
				if (it != null) {
					successCallback.invoke(LatLng(it.latitude, it.longitude))
					Log.d(TAG, "getCurrentLocation() called ${it.latitude}, ${it.longitude}")
				} else {
					failureCallback.invoke(Exception(""))
				}
			}.addOnFailureListener {
				failureCallback.invoke(it)
			}
		}

		if (PermissionX.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
			fusedLocationClient.lastLocation.addOnSuccessListener {
				if (it != null) {
					successCallback.invoke(LatLng(it.latitude, it.longitude))
				} else {
					getRefreshLocation()
				}
			}.addOnFailureListener {
				getRefreshLocation()
			}
		} else {
			failureCallback.invoke(Exception(""))
		}
	}
}
package com.test.landmarkremark.presentation.composes.main

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState
import com.test.landmarkremark.R
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.presentation.composes.common.AddNoteDialog
import com.test.landmarkremark.presentation.composes.common.CustomToast
import com.test.landmarkremark.presentation.composes.common.FABAddNote
import com.test.landmarkremark.presentation.composes.common.ShowProgressDialog
import com.test.landmarkremark.presentation.ui.states.ProgressState
import com.test.landmarkremark.presentation.viewmodels.main.MapViewModel
import com.test.landmarkremark.utils.Constants
import com.test.landmarkremark.utils.LocationRequestUpdate
import com.test.landmarkremark.utils.Utils.bitmapDescriptor
import com.test.landmarkremark.utils.isPermissionGranted

@Composable
fun MapScreen(
    listUserInfo: List<UserInfoModel>?,
    keyAction: String?,
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var onError by remember { mutableStateOf("") }
    val latLng: LatLng? by remember { mutableStateOf(null) }

    // holding state to show add dialog
    var isShowAddDialog by remember {
        mutableStateOf(false)
    }
    var currentLocation: LatLng? by remember {
        mutableStateOf(null)
    }

    // request camera to a point at the intersection of the equator and the prime meridian
    var cameraPositionState by remember {
        mutableStateOf(
            CameraPositionState(
                position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 5f)
            )
        )
    }

    // move camera to this latLng
    fun requestLocation(latLng: LatLng) {
        cameraPositionState =
            CameraPositionState(position = CameraPosition.fromLatLngZoom(latLng, 14f))
    }

    // get current location of user and move camera to it
    fun requestCurrentLocation() {
        LocationRequestUpdate(context).getCurrentLocation(successCallback = {
            currentLocation = it
            requestLocation(it)
        }, failureCallback = {
        })
    }

    //sets up a permission launcher to request the ACCESS_FINE_LOCATION permission. When the result is available, it checks if the permission was granted.
    // If it was granted, it either requests a specific location (latLng?.let { requestLocation(it) }) or requests the current location (requestCurrentLocation())
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                latLng?.let {
                    requestLocation(it)
                } ?: run {
                    requestCurrentLocation()
                }
            }
        }

    LaunchedEffect(Unit) {
        viewModel.updateListUser(listUserInfo)

        if (keyAction == Constants.ActionToMap.AddNote.action) {
            isShowAddDialog = true
        }
        if (context.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestCurrentLocation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
    Scaffold(
        floatingActionButton = {
            Column {
                FABAddNote(icon = Icons.Filled.Edit) {
                    isShowAddDialog = true
                }
                FABAddNote(icon = Icons.Filled.LocationOn) {
                    requestCurrentLocation()
                }
            }
        }
    ) {
        CreateMap(context, currentLocation, cameraPositionState,
            Modifier
                .fillMaxSize()
                .padding(it), viewModel, onError = { msg -> onError = msg })
    }

    if (onError.isNotBlank()) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(horizontal = 16.dp)
        ) {
            CustomToast(onError) {
                onError = ""
            }
        }
    }

    if (isShowAddDialog) {
        AddNoteDialog(
            onDismiss = {
                isShowAddDialog = false
            },
            onSave = {
                viewModel.saveNote(it, currentLocation ?: LatLng(0.0, 0.0)) { error ->
                    onError = error
                }
            },
            onError = { onError = it }
        )
    }

    viewModel.let {
        val uiState by it.uiState.collectAsState()
        when (uiState.progressState) {
            ProgressState.Loading -> ShowProgressDialog()
            else -> {}
        }
    }
}

@Composable
private fun CreateMap(
    context: Context,
    currentLocation: LatLng?,
    cameraPositionState: CameraPositionState,
    modifier: Modifier, vm: MapViewModel,
    onError: (String) -> Unit
) {
    val allPlace by vm.listUserInfo.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))

    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            // add current marker
            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "",
                    icon = bitmapDescriptor(context, R.drawable.ic_map_pin_large)
                )
            }

            // add all marker if them exist
            allPlace?.let { listUser ->
                listUser.forEach { user ->
                    user.notes?.forEach { note ->
                        CustomMarker(
                            context =context,
                            position = LatLng(note.latitude, note.longitude),
                            title = user.username ?: "",
                            snippet = note.text,
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun CustomMarker(context: Context,position: LatLng, title: String, snippet: String?) {
    val markerState = rememberMarkerState(null, position)
    Marker(
        state = markerState,
        title = title,
        snippet = snippet,
        icon = bitmapDescriptor(context, R.drawable.ic_map_pin_large_color)
    )
    markerState.showInfoWindow()
}
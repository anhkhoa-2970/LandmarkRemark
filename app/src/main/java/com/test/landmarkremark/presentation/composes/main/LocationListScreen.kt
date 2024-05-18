package com.test.landmarkremark.presentation.composes.main

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.test.landmarkremark.R
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.presentation.activities.LoginActivity
import com.test.landmarkremark.presentation.activities.base.baseActivity
import com.test.landmarkremark.presentation.composes.common.ChooseOptionsBottomSheet
import com.test.landmarkremark.presentation.composes.common.CommonButton
import com.test.landmarkremark.presentation.composes.common.CustomToast
import com.test.landmarkremark.presentation.composes.common.EnterTextBottomSheet
import com.test.landmarkremark.presentation.composes.common.FABAddNote
import com.test.landmarkremark.presentation.composes.common.SearchView
import com.test.landmarkremark.presentation.composes.common.ShowNoteDialog
import com.test.landmarkremark.presentation.composes.common.ShowProgressDialog
import com.test.landmarkremark.presentation.ui.states.ProgressState
import com.test.landmarkremark.presentation.viewmodels.main.LocationListViewModel
import com.test.landmarkremark.utils.Constants
import com.test.landmarkremark.utils.SavedStore
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationListScreen(
    navController: NavHostController,
    viewModel: LocationListViewModel = hiltViewModel()
) {
    // collect data from model to show in UI
    val notesList by viewModel.notes.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // hold state to click to close the keyboard and remove focus
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // hold state of search value
    val inputSearch = remember { mutableStateOf(TextFieldValue("")) }
    var onError by remember { mutableStateOf("") }

    // holding state if user click to note will show dialog
    var isShowFullNote: Pair<String?, NoteModel?> by remember { mutableStateOf(Pair(null, null)) }

    // holding state if user long click to current user note will show bottom sheet dialog to choose delete or edit
    var isLongClickNote: NoteModel? by remember { mutableStateOf(null) }

    // holding state if user long click to current user note will show bottom sheet dialog to edit name
    var isLongClickName: UserInfoModel? by remember { mutableStateOf(null) }

    // holding state show edit note bottom sheet dialog
    var isShowEditBottomSheet: NoteModel? by remember { mutableStateOf(null) }

    // holding state show edit note bottom sheet dialog
    var isLogout by remember { mutableStateOf(false) }

    // state pull to refresh
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.loadUserNotes {
            onError = it
        }
    })
    LaunchedEffect(Unit) {
        viewModel.loadUserNotes {
            onError = it
        }
    }
    // pass argument when navigate to map screen
    fun actionNavigate(action: String) {
        val userInfoJson =
            URLEncoder.encode(Gson().toJson(notesList), StandardCharsets.UTF_8.toString())
        navController.navigate("${Constants.Screen.MapScreen}/$userInfoJson?keyActions=${action}")
    }

    Scaffold(
        modifier = Modifier.background(colorResource(id = R.color.background_color)),
        topBar = {
            LandmarkAppBar(onLogoutClick = {
                isLogout = true
            })
        },
        floatingActionButton = {
            FABAddNote(
                modifier = Modifier.padding(bottom = 50.dp),
                icon = Icons.Filled.Edit
            ) {
                // navigate to map with AddNote action and show dialog add note
                actionNavigate(Constants.ActionToMap.AddNote.action)
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background_color))
                    .padding(it)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                    }
                    .pullRefresh(pullRefreshState)
            ) {
                SearchView(state = inputSearch, modifier = Modifier)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp)
                ) {
                    // logic to show list User by keyword
                    val userListVisible = notesList.filter { userModel ->
                        val hasNotes = userModel.notes?.isNotEmpty() == true
                        val matchesUsername = userModel.username?.contains(
                            inputSearch.value.text,
                            ignoreCase = true
                        ) == true
                        val matchesNoteText = userModel.notes?.any { note ->
                            note.text.contains(inputSearch.value.text, ignoreCase = true)
                        } == true

                        hasNotes && (matchesUsername || matchesNoteText)
                    }
                    items(userListVisible) { user ->
                        UserItem(
                            userInfoModel = user,
                            currentUser?.uid,
                            inputSearch.value.text,
                            onItemNoteClicked = { note ->
                                isShowFullNote = Pair(user.username, note)
                                focusManager.clearFocus()
                            }, onLongClickMyNote = { note ->
                                isLongClickNote = note
                            }, onLongClickMyName = { userInfoModel ->
                                isLongClickName = userInfoModel
                            })
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // navigate to map with SeeMap action and don't show dialog add note
                CommonButton(
                    text = stringResource(id = R.string.view_map),
                    paddingHorizontal = 16.dp,
                    onClick = { actionNavigate(Constants.ActionToMap.SeeMap.action) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )

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

    if (isShowFullNote.first != null && isShowFullNote.second != null) {
        ShowNoteDialog(
            userName = isShowFullNote.first,
            note = isShowFullNote.second!!,
            onClose = {
                isShowFullNote = Pair(null, null)
            }
        )
    }

    if (isLongClickNote != null) {
        currentUser?.uid?.let {
            ChooseOptionsBottomSheet(
                textPositive = "Edit",
                onPositive = {
//                    viewModel.editMyNote(
//                        it,
//                        isLongClick!!,
//                        onEditError = { msg -> onError = msg })
                    isShowEditBottomSheet = isLongClickNote
                    isLongClickNote = null
                },
                textNegative = "Delete",
                onNegative = {
                    viewModel.deleteMyNote(
                        it,
                        isLongClickNote!!.id,
                        onDeleteError = { msg -> onError = msg })
                    isLongClickNote = null
                },
                onDismiss = {
                    isLongClickNote = null
                }
            )
        } ?: run {
            onError = "Not found user!!!"
        }
    }

    if (isLongClickName != null) {
        currentUser?.uid?.let {
            EnterTextBottomSheet(
                textNegative = stringResource(id = R.string.cancel),
                initValue = isLongClickName!!.username,
                title = stringResource(id = R.string.change_name),
                label = stringResource(id = R.string.enter_name),
                onNegative = {
                    isLongClickName = null
                },
                textPositive = stringResource(id = R.string.save),
                onPositive = { newUserName ->
                    viewModel.updateUserName(
                        userId = it,
                        newUserName = newUserName,
                        onEditSuccess = {
                            isLongClickName = null
                        },
                        onEditError = { msg -> onError = msg })
                },
                onDismiss = {
                    isLongClickName = null
                },
                onEnterNoteError = {
                    isLongClickName = null
                    onError = it
                },
                errorMsg = stringResource(id = R.string.please_enter_name)
            )
        } ?: run {
            onError = "Not found user!!!"
        }
    }

    if (isShowEditBottomSheet != null) {
        currentUser?.uid?.let {
            EnterTextBottomSheet(
                textNegative = stringResource(id = R.string.cancel),
                initValue = isShowEditBottomSheet!!.text,
                title = stringResource(id = R.string.change_note),
                label = stringResource(id = R.string.enter_note),
                onNegative = {
                    isShowEditBottomSheet = null
                },
                textPositive = stringResource(id = R.string.save),
                onPositive = { newNote ->
                    viewModel.editMyNote(
                        userId = it,
                        note = isShowEditBottomSheet!!,
                        newNote = newNote,
                        onEditSuccess = {
                            isShowEditBottomSheet = null
                        },
                        onEditError = { msg -> onError = msg })

                },
                onDismiss = {
                    isShowEditBottomSheet = null
                },
                onEnterNoteError = {
                    isShowEditBottomSheet = null
                    onError = it
                },
                errorMsg = stringResource(id = R.string.please_enter_note)
            )
        } ?: run {
            onError = "Not found user!!!"
        }
    }

    if (isLogout) {
        ChooseOptionsBottomSheet(
            title = stringResource(id = R.string.logout_warning),
            textNegative = stringResource(id = R.string.cancel),
            onNegative = { isLogout = false },
            iconNegative = Icons.Filled.Close,
            onPositive = {
                isLogout = false
                viewModel.logout(onSuccess = {
                    // set flags PREFS_LOGIN to false and go to login screen
                    SavedStore.saveBoolean(Constants.PREFS_LOGIN, false)
                    baseActivity?.startActivity(Intent(baseActivity, LoginActivity::class.java))
                    baseActivity?.finish()
                }, onError = { onError = it })
            },
            textPositive = stringResource(id = R.string.logout),
            iconPositive = Icons.AutoMirrored.Outlined.ExitToApp,

            onDismiss = { isLogout = false }
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

// User with notes
@Composable
fun UserItem(
    userInfoModel: UserInfoModel,
    userId: String?,
    inputSearch: String?,
    onItemNoteClicked: (NoteModel) -> Unit,
    onLongClickMyNote: (NoteModel) -> Unit,
    onLongClickMyName: (UserInfoModel) -> Unit,
) {
    var visibleListNotes = emptyList<NoteModel>()
    // logic to show list Note by keyword
    userInfoModel.notes?.let {
        visibleListNotes = if (userInfoModel.username?.contains(
                inputSearch.toString(),
                ignoreCase = true
            ) == true
        ) it else it.filter { userModel ->
            userModel.text.contains(
                inputSearch.toString(),
                ignoreCase = true
            )
        }
    }
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(Modifier.wrapContentWidth()) {
            Text(
                text = userInfoModel.username ?: "",
                Modifier
                    .weight(1f)
                    .padding(start = 12.dp, top = 12.dp)
                    .wrapContentHeight(),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (userInfoModel.uid == userId) colorResource(id = R.color.color_main) else Color.Black
            )
            if (userId == userInfoModel.uid) {
                IconButton(onClick = { onLongClickMyName(userInfoModel) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.user_name)
                    )
                }
            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                // calculate height of column by list size - 106.dp is height of NoteItem and 1.dp is a height of divider
                .height(
                    106.dp * (visibleListNotes.size) + 1.dp * ((visibleListNotes.size) - 1)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            userInfoModel.notes?.let {
                items(visibleListNotes) { user ->
                    Column(Modifier.background(Color.White)) {
                        NoteItem(
                            isMyNote = userInfoModel.uid == userId,
                            note = user,
                            onClick = { onItemNoteClicked(user) },
                            onLongClick = { onLongClickMyNote(user) }
                        )

                        if (visibleListNotes.last() != user) {
                            Spacer(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .background(colorResource(id = R.color.background_color))
                            )
                        }
                    }
                }
            }

        }
    }

}

//Note content item
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    isMyNote: Boolean = false,
    note: NoteModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .height(106.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { if (isMyNote) onLongClick() }
            )
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = note.text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "${stringResource(id = R.string.latitude)} ${note.latitude}",
            style = MaterialTheme.typography.bodySmall, color = Color(0xFF7F8283)
        )
        Text(
            text = "${stringResource(id = R.string.longitude)} ${note.longitude}",
            style = MaterialTheme.typography.bodySmall, color = Color(0xFF7F8283)
        )
    }
}

// create top bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandmarkAppBar(onLogoutClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier.background(colorResource(id = R.color.background_color)),
        colors = TopAppBarColors(
            containerColor = colorResource(id = R.color.background_color),
            scrolledContainerColor = colorResource(id = R.color.background_color),
            navigationIconContentColor = colorResource(id = R.color.black),
            titleContentColor = colorResource(id = R.color.black),
            actionIconContentColor = colorResource(id = R.color.black)
        ),
        title = {
            Text(
                text = stringResource(id = R.string.landmark_notes),
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { onLogoutClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(id = R.string.logout)
                )
            }
        }
    )
}



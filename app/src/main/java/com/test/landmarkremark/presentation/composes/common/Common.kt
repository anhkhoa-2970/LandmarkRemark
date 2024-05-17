package com.test.landmarkremark.presentation.composes.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.test.landmarkremark.R
import com.test.landmarkremark.domain.models.NoteModel
import com.test.landmarkremark.presentation.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun CustomInputView(
    modifier: Modifier = Modifier,
    title: String = "",
    isRequiredLabel: Boolean? = false,
    titleFontWeight: FontWeight = FontWeight.Bold,
    textValue: String,
    onTextValueChanged: (String) -> Unit,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    maxLength: Int = Int.MAX_VALUE,
    isEditable: Boolean = true,
    onClick: (() -> Unit)? = null,
    onFocus: ((Boolean) -> Unit)? = null,
    colorBorder: Color = colorResource(id = R.color.color_border_and_line),
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Row(modifier.padding(start = 4.dp, bottom = 4.dp)) {
            if (title.isNotEmpty()) {
                Text(
                    modifier = modifier.wrapContentSize(),
                    text = title,
                    style = Typography.bodySmall.copy(
                        color = Color.Black,
                        fontWeight = titleFontWeight
                    ),
                )
            }

            if (isRequiredLabel == true) {
                Text(
                    modifier = modifier.wrapContentSize(),
                    text = " *",
                    style = Typography.bodySmall.copy(
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Box(modifier.wrapContentSize()) {
            CustomTextField(
                textValue = textValue,
                onTextValueChanged = onTextValueChanged,
                placeholder = placeholder,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                maxLength = maxLength,
                isEditable = isEditable,
                trailingIcon = trailingIcon,
                colorBorder = colorBorder,
                onFocus = onFocus
            )
            if (trailingIcon != null) {
                Box(
                    modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Transparent, shape = RoundedCornerShape(10.dp))
                        .clickable { onClick?.invoke() })
            }
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textValue: String,
    onTextValueChanged: (String) -> Unit,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    singleLine: Boolean = false,
    isEditable: Boolean = true,
    onClick: (() -> Unit)? = null,
    onFocus: ((Boolean) -> Unit)? = null,
    colorBorder: Color = colorResource(id = R.color.color_border_and_line),
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val clippedTextValue = if (textValue.length > maxLength) {
        textValue.substring(0, maxLength)
    } else {
        textValue
    }
    Row(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = colorBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(start = 16.dp, end = if (trailingIcon != null) 8.dp else 16.dp)
            .clickable { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = clippedTextValue,
            onValueChange = {
                if (isEditable && it.length <= maxLength) {
                    onTextValueChanged(it)
                }
            },
            modifier = modifier
                .weight(1f)
                .padding(end = if (trailingIcon != null) 6.dp else 0.dp)
                .onFocusChanged { focusState ->
                    onFocus?.invoke(focusState.isFocused)
                },
            textStyle = Typography.bodyMedium,
            maxLines = maxLines,
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (clippedTextValue.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            color = colorResource(id = R.color.color_text_hint),
                            modifier = modifier
                                .wrapContentHeight()
                                .align(Alignment.CenterStart),
                            style = Typography.bodySmall.copy(color = colorResource(id = R.color.color_text_hint))
                        )
                    }
                    innerTextField()
                }
            },
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            readOnly = !isEditable
        )

        // Add the trailing icon if provided
        trailingIcon?.invoke()
    }
}

enum class ToastMode {
    ERROR, SUCCESS
}

@Composable
fun CustomToast(
    message: String,
    modifier: Modifier = Modifier,
    toastMode: ToastMode = ToastMode.ERROR,
    onHide: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(visible) {
        delay(3000)
        visible = false
        onHide()
    }

    if (visible) {
        Surface(
            modifier = modifier.wrapContentHeight(),
            color = if (toastMode == ToastMode.ERROR) {
                colorResource(id = R.color.color_red)
            } else {
                colorResource(id = R.color.color_toast_success)
            },
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (toastMode == ToastMode.SUCCESS) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_toast_success),
                        contentDescription = null
                    )
                    Spacer(modifier = modifier.width(8.dp))
                }

                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.wrapContentWidth()
                )
            }
        }
    }
}

@Composable
fun ShowProgressDialog() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) {},
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = colorResource(id = R.color.color_main))
    }
}

@Composable
fun FABAddNote(modifier: Modifier = Modifier, icon: ImageVector, onClick: () -> Unit) {
    SmallFloatingActionButton(
        modifier = modifier,
        onClick = { onClick() },
        shape = CircleShape,
    ) {
        Icon(icon, stringResource(id = R.string.add_note))
    }
}

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onError: (String) -> Unit
) {
    var noteText by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.add_note)) },
        text = {
            Column {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text(stringResource(id = R.string.enter_note)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (noteText.isNotBlank()) {
                        onSave(noteText)
                    } else {
                        onError("Please enter note!")
                    }
                    onDismiss()
                },
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.color_main),
                    contentColor = colorResource(id = R.color.white),
                    disabledContainerColor = colorResource(id = R.color.color_main),
                    disabledContentColor = colorResource(id = R.color.white)
                )
            ) {
                Text(stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }, colors = ButtonColors(
                    containerColor  = Color.Red,
                    contentColor = colorResource(id = R.color.white),
                    disabledContainerColor = Color.Red,
                    disabledContentColor = colorResource(id = R.color.white)
                )
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
fun ShowNoteDialog(
    modifier: Modifier = Modifier,
    userName: String?,
    note: NoteModel,
    onClose: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Column(
                modifier = modifier
                    .padding(18.dp)
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "$userName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState(), true)
                ) {

                    Text(
                        text = note.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
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
                Spacer(modifier = Modifier.height(10.dp))
                CommonButton(text = stringResource(id = R.string.cancel), onClick = { onClose() })
            }
        },
    )
}

@Composable
fun CommonButton(
    text: String? = "",
    enabled: Boolean = true,
    height: Dp? = null,
    color: Color? = null,
    paddingHorizontal: Dp? = null,
    paddingVertical: Dp? = null,
    imageVector: ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick.invoke() },
        modifier = Modifier.run {
            fillMaxWidth()
                .height(height ?: 44.dp)
                .padding(horizontal = paddingHorizontal ?: 0.dp, vertical = paddingVertical ?: 0.dp)
        },
        shape = RoundedCornerShape(10.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color ?: colorResource(id = R.color.color_main),
            disabledContainerColor = Color(0xFFCBCBCB)
        )
    ) {
        Text(
            text = "$text",
            modifier = Modifier.padding(horizontal = 10.dp),
            style = Typography.bodyMedium.copy(
                color = colorResource(id = R.color.white),
            )
        )
        imageVector?.let {
            Icon(imageVector = it, contentDescription = "Click")
        }
    }
}

@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    modifier: Modifier,
    hint: String = "Search..."
) {
    BasicTextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFF13A1BE), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp),
        textStyle = Typography.bodyMedium,
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Search, "Search...")
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        if (state.value.text.isEmpty()) {
                            Text(
                                text = hint,
                                style = Typography.bodyMedium.copy(color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        visualTransformation = VisualTransformation.None
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterNoteBottomSheet(
    modifier: Modifier = Modifier,
    initValue: String? = "",
    textPositive: String,
    onPositive: (String) -> Unit,
    textNegative: String,
    onNegative: () -> Unit,
    onEnterNoteError: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    var noteText by remember { mutableStateOf(initValue) }
    ModalBottomSheet(
        containerColor = colorResource(id = R.color.white),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {

        OutlinedTextField(
            value = "$noteText",
            onValueChange = { noteText = it },
            label = { Text(stringResource(id = R.string.enter_note)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = modifier.height(16.dp))
        Row(
            modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
        ) {
            Box(modifier = modifier.weight(1f)) {
                CommonButton(
                    text = textNegative,
                    imageVector = Icons.Filled.Delete,
                    color = Color.Red
                ) {
                    onNegative()
                }
            }
            Spacer(
                modifier = modifier
                    .width(16.dp)
            )
            Box(modifier = modifier.weight(1f)) {
                CommonButton(text = textPositive, imageVector = Icons.Filled.Edit) {
                    if (!noteText.isNullOrBlank())
                        onPositive(noteText ?: "")
                    else
                        onEnterNoteError("Please enter note!")
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseOptionsBottomSheet(
    modifier: Modifier = Modifier,
    textPositive: String,
    onPositive: () -> Unit,
    iconPositive: ImageVector?  = Icons.Filled.Edit,
    textNegative: String,
    onNegative: () -> Unit,
    iconNegative: ImageVector?  = Icons.Filled.Delete,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Row(
            modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
        ) {
            Box(modifier = modifier.weight(1f)) {
                CommonButton(
                    text = textNegative,
                    imageVector = iconNegative,
                    color = Color.Red
                ) {
                    onNegative()
                }
            }
            Spacer(modifier = modifier.width(16.dp))
            Box(modifier = modifier.weight(1f)) {
                CommonButton(text = textPositive, imageVector = iconPositive) {
                    onPositive()
                }
            }

        }
    }
}


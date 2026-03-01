@file:OptIn(ExperimentalMaterial3Api::class)

package com.popov.dev.notes.presentation.screens.editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.popov.dev.notes.presentation.screens.editing.EditNoteCommand.InputContent
import com.popov.dev.notes.presentation.screens.editing.EditNoteCommand.InputTitle
import com.popov.dev.notes.presentation.utils.DateFormater

@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    notId: Int,
    viewModel: EditNoteViewModel = viewModel { EditNoteViewModel(noteId = notId) },
    onFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    when (val currentState = state.value) {
        is EditNoteState.Editing -> {
            with(MaterialTheme.colorScheme) {
                Scaffold(
                    modifier = modifier, topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Edit Note",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onBackground,
                                )
                            },
                            navigationIcon = {
                                Icon(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 8.dp)
                                        .clickable {
                                            viewModel.processCommand(command = EditNoteCommand.Back)
                                        },
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back button"
                                )
                            },
                            actions = {
                                Icon(
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .clickable {
                                            viewModel.processCommand(command = EditNoteCommand.Delete)
                                        },
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete note button"
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                navigationIconContentColor = onSurface,
                                actionIconContentColor = onSurface
                            )
                        )
                    }) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        TextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            value = currentState.note.title,
                            onValueChange = {
                                viewModel.processCommand(
                                    command = InputTitle(
                                        title = it
                                    )
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = surface,
                                unfocusedContainerColor = surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            placeholder = {
                                Text(
                                    text = "Title",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface.copy(alpha = .2f)
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 24.sp, fontWeight = FontWeight.Bold, color = onSurface
                            ),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = DateFormater.formatDateToString(currentState.note.updatedAt),
                            fontSize = 12.sp,
                            color = onSurfaceVariant
                        )
                        TextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .weight(1f),
                            value = currentState.note.content,
                            onValueChange = {
                                viewModel.processCommand(
                                    command = InputContent(
                                        content = it
                                    )
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = surface,
                                unfocusedContainerColor = surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            placeholder = {
                                Text(
                                    text = "Note something down...",
                                    fontSize = 16.sp,
                                    color = onSurface.copy(alpha = .2f)
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp, color = onSurface
                            ),
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            onClick = {
                                viewModel.processCommand(
                                    command = EditNoteCommand.Save
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            enabled = currentState.isSaveEnabled,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primary,
                                disabledContentColor = primary.copy(alpha = .1f),
                                contentColor = onPrimary,
                                disabledContainerColor = onPrimary,
                            )
                        ) {
                            Text(text = "SaveNote")
                        }

                    }
                }
            }
        }


        EditNoteState.Finished -> {
            LaunchedEffect(key1 = Unit) {
                onFinished()
            }
        }

        EditNoteState.Initial -> {}
        EditNoteState.Delete -> TODO()

    }
}
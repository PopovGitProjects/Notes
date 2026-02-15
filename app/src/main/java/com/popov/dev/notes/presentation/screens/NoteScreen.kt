@file:OptIn(ExperimentalFoundationApi::class)

package com.popov.dev.notes.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.presentation.ui.theme.Green
import com.popov.dev.notes.presentation.ui.theme.Yellow200

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier, viewModel: NotesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        //TODO Убрать после
        modifier = modifier.padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Title(text = "All Notes")
        }
        item {
            SearchBar(
                query = state.query, onQueryChange = {
                    viewModel.processCommand(NotesCommand.InputSearchQuery(it))
                })
        }
        item {
            Subtitle(text = "Pinned")
        }
        item {
            LazyRow(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.pinedNotes, key = { note -> note.id }) { note ->
                    NoteCard(
                        note = note, onNoteClick = {
                            viewModel.processCommand(NotesCommand.EditNotes(note))
                        },
                        onLongClick = {
                            viewModel.processCommand(NotesCommand.SwitchPinedStatus(note.id))
                        },
                        onDoubleClick = {
                            viewModel.processCommand(NotesCommand.DeleteNote(note.id))
                        },
                        backgroundColor = Yellow200
                    )
                }
            }
        }
        item { Subtitle(text = "Others") }
        items(
            items = state.otherNotes,
            key = { note -> note.id })
        { note ->
            NoteCard(
                modifier = modifier
                    .fillMaxWidth(),
                note = note,
                onNoteClick = {
                    viewModel.processCommand(NotesCommand.EditNotes(note))
                },
                onLongClick = {
                    viewModel.processCommand(NotesCommand.SwitchPinedStatus(note.id))
                },
                onDoubleClick = {
                    viewModel.processCommand(NotesCommand.DeleteNote(note.id))
                },
                backgroundColor = Green
            )
        }
    }
}

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    backgroundColor: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    onDoubleClick: (Note) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = backgroundColor)
            .combinedClickable(
                onClick = { onNoteClick(note) },
                onLongClick = { onLongClick(note) },
                onDoubleClick = { onDoubleClick(note) }),
    ) {
        Text(
            text = note.title, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = note.updatedAt.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = note.content,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }

}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun Subtitle(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text, fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,

        )
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search"
            )
        },
        shape = RoundedCornerShape(10.dp)
    )
}
@file:OptIn(ExperimentalFoundationApi::class)

package com.popov.dev.notes.presentation.screens.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.popov.dev.notes.R
import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.presentation.ui.theme.OtherNotesColors
import com.popov.dev.notes.presentation.ui.theme.PinnedNotesColors
import com.popov.dev.notes.presentation.utils.DateFormater

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(),
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                onClick = onAddNoteClick,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_note),
                    contentDescription = "Button add note"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Title(
                    modifier = modifier.padding(horizontal = 24.dp),
                    text = stringResource(R.string.all_notes)
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                SearchBar(
                    modifier = modifier.padding(horizontal = 24.dp),
                    query = state.query, onQueryChange = {
                        viewModel.processCommand(NotesCommand.InputSearchQuery(it))
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            if (state.pinedNotes.isNotEmpty()) {
                item {
                    Subtitle(
                        modifier = modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.pinned)
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                LazyRow(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    itemsIndexed(
                        items = state.pinedNotes,
                        key = { _, note -> note.id },
                    )
                    { index, note ->
                        NoteCard(
                            modifier = Modifier.widthIn(max = 160.dp),
                            note = note,
                            onNoteClick = onNoteClick,
                            onLongClick = {
                                viewModel.processCommand(NotesCommand.SwitchPinedStatus(note.id))
                            },
                            backgroundColor = PinnedNotesColors[index % PinnedNotesColors.size]
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Subtitle(
                    modifier = modifier.padding(start = 24.dp, end = 24.dp),
                    text = stringResource(R.string.others)
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            itemsIndexed(
                items = state.otherNotes,
                key = { _, note -> note.id })
            { index, note ->
                if (state.otherNotes.isNotEmpty()) {
                    NoteCard(

                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp),
                        note = note,
                        onNoteClick = onNoteClick,
                        onLongClick = {
                            viewModel.processCommand(NotesCommand.SwitchPinedStatus(note.id))
                        },
                        backgroundColor = OtherNotesColors[index % OtherNotesColors.size]
                    )
                } else {
                    Box(
                        modifier = Modifier.size(100.dp).background(Color.Red),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add_note),
                            contentDescription = "Button add note"
                        )
                    }
                    Text(
                        text = "Add your first note...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                        )

                }
                Spacer(modifier = Modifier.height(8.dp))
            }
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
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = backgroundColor)
            .combinedClickable(
                onClick = { onNoteClick(note) },
                onLongClick = { onLongClick(note) },
            )
            .padding(16.dp),
    ) {
        with(MaterialTheme.colorScheme) {
            Text(
                text = note.title,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = DateFormater.formatDateToString(note.updatedAt),
                fontSize = 12.sp,
                color = onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = note.content,
                fontSize = 16.sp,
                color = onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
            )
        }

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
    with(MaterialTheme.colorScheme) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .background(surface)
                .border(
                    width = 1.dp,
                    color = onSurfaceVariant,
                    shape = RoundedCornerShape(10.dp)
                ),
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "Search...",
                    fontSize = 14.sp,
                    color = onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = onSurface,
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = surface,
                unfocusedTextColor = surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }

}
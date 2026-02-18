package com.popov.dev.notes.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popov.dev.notes.data.TestNoteRepositoryImpl
import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.domain.usecases.AddNoteUseCase
import com.popov.dev.notes.domain.usecases.DeleteNoteUseCase
import com.popov.dev.notes.domain.usecases.EditNoteUseCase
import com.popov.dev.notes.domain.usecases.GetAllNotesUseCase
import com.popov.dev.notes.domain.usecases.GetNoteUseCase
import com.popov.dev.notes.domain.usecases.SearchNoteUseCase
import com.popov.dev.notes.domain.usecases.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel : ViewModel() {
    private val repository = TestNoteRepositoryImpl
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val searchNoteUseCase = SearchNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)

    private val _state = MutableStateFlow(NoteScreenState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        addSomeNotes()
        query
            .onEach { input -> _state.update { it.copy(query = input) } }
            .flatMapLatest { input ->
                if (input.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNoteUseCase(input)
                }
            }
            .onEach { notes ->
                val pinedNotes = notes.filter { it.isPinned }
                val otherNotes = notes.filter { !it.isPinned }
                _state.update { it.copy(pinedNotes = pinedNotes, otherNotes = otherNotes) }
            }
            .launchIn(viewModelScope)
    }

    // TODO: Удалить после проведения проверки добавления заметок
    private fun addSomeNotes() {
        viewModelScope.launch {
            repeat(10_000) {
                addNoteUseCase(title = "Title#$it", content = "Content#$it")
            }
        }
    }

    fun processCommand(command: NotesCommand) {
        viewModelScope.launch {
            when (command) {
                is NotesCommand.DeleteNote -> {
                    deleteNoteUseCase(command.noteId)
                }

                is NotesCommand.EditNotes -> {
                    val note = getNoteUseCase(command.note.id)
                    val title = command.note.title
                    editNoteUseCase(note.copy(title = "$title + edited"))
                }

                is NotesCommand.SwitchPinedStatus -> {
                    switchPinnedStatusUseCase(command.noteId)
                }

                is NotesCommand.InputSearchQuery -> {
                    query.update { command.query.trim() }
                }
            }
        }
    }
}

sealed interface NotesCommand {
    data class InputSearchQuery(val query: String) : NotesCommand
    data class SwitchPinedStatus(val noteId: Int) : NotesCommand

    //Temp

    data class DeleteNote(val noteId: Int) : NotesCommand
    data class EditNotes(val note: Note) : NotesCommand
}

data class NoteScreenState(
    val query: String = "",
    val pinedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf(),
)
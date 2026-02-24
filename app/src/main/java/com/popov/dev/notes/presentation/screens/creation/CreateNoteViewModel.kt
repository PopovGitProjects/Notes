package com.popov.dev.notes.presentation.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popov.dev.notes.data.TestNoteRepositoryImpl
import com.popov.dev.notes.domain.usecases.AddNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateNoteViewModel : ViewModel() {
    private val repository = TestNoteRepositoryImpl
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val _state = MutableStateFlow<CreateNoteState>(CreateNoteState.Creation())
    val state = _state.asStateFlow()

    fun processCommand(command: CreateNoteCommand) {
        when (command) {
            is CreateNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy(
                            title = command.title,
                            isSaveEnabled = previousState.title.isNotBlank() && previousState.content.isNotBlank()
                        )
                    } else {
                        CreateNoteState.Creation(title = command.title)
                    }
                }
            }
            is CreateNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy(
                            content = command.content,
                            isSaveEnabled = previousState.title.isNotBlank() && command.content.isNotBlank()
                        )
                    } else {
                        CreateNoteState.Creation(content = command.content)
                    }
                }
            }

            is CreateNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is CreateNoteState.Creation) {
                            val title = previousState.title
                            val content = previousState.content
                            addNoteUseCase(title = title, content = content)
                            CreateNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }

            is CreateNoteCommand.Back -> {
                _state.update { CreateNoteState.Finished }
            }
        }
    }
}

sealed interface CreateNoteCommand {
    data class InputTitle(
        val title: String
    ) : CreateNoteCommand

    data class InputContent(
        val content: String
    ) : CreateNoteCommand

    data object Save : CreateNoteCommand
    data object Back : CreateNoteCommand
}

sealed interface CreateNoteState {
    data class Creation(
        val title: String = "",
        val content: String = "",
        val isSaveEnabled: Boolean = false,
    ) : CreateNoteState

    data object Finished : CreateNoteState
}


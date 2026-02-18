package com.popov.dev.notes.domain.usecases

import com.popov.dev.notes.domain.repository.NoteRepository

class SwitchPinnedStatusUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(noteId: Int) {
        return repository.switchPinnedStatus(noteId)
    }
}
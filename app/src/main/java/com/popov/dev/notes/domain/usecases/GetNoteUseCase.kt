package com.popov.dev.notes.domain.usecases

import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.domain.repository.NoteRepository

class GetNoteUseCase(private val repository: NoteRepository) {
    operator fun invoke(noteInt: Int): Note {
        return repository.getNotes(noteInt)
    }
}
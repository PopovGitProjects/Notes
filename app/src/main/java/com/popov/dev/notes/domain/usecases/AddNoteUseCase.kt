package com.popov.dev.notes.domain.usecases

import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.domain.repository.NoteRepository

class AddNoteUseCase(private val repository: NoteRepository) {
    operator fun invoke(
        title: String,
        content: String
    ) {
        repository.addNote(title = title, content = content)
    }
}
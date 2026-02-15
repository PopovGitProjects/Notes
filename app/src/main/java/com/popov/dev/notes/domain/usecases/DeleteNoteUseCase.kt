package com.popov.dev.notes.domain.usecases

import com.popov.dev.notes.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}
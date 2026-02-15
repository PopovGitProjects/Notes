package com.popov.dev.notes.domain.usecases

import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.domain.repository.NoteRepository

class EditNoteUseCase(private val repository: NoteRepository) {
    operator fun invoke(note: Note){
        repository.editNote(note)
    }
}
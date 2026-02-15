package com.popov.dev.notes.domain.usecases

import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}
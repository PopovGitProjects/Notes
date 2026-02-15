package com.popov.dev.notes.domain.repository

import com.popov.dev.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun addNote(title: String, content: String)
    fun deleteNote(noteId: Int)
    fun editNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
    fun getNotes(noteId: Int): Note
    fun searchNote(query: String): Flow<List<Note>>
    fun switchPinnedStatus(noteId: Int)
}
package com.popov.dev.notes.domain.repository

import com.popov.dev.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(title: String, content: String, isPinned: Boolean, updateAt: Long)
    suspend fun deleteNote(noteId: Int)
    suspend fun editNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNotes(noteId: Int): Note
    fun searchNote(query: String): Flow<List<Note>>
    suspend fun switchPinnedStatus(noteId: Int)
}
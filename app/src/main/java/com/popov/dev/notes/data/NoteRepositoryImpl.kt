package com.popov.dev.notes.data

import android.content.Context
import com.popov.dev.notes.domain.model.Note
import com.popov.dev.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl private constructor(context: Context) : NoteRepository {
    private val notesDatabase = NotesDataBase.getInstance(context)
    private val notesDao = notesDatabase.notesDao()
    override suspend fun addNote(
        title: String,
        content: String,
        isPinned: Boolean,
        updateAt: Long
    ) {
        val noteDbModel = NoteDbModel(
            id = 0,
            title = title,
            content = content,
            isPinned = isPinned,
            updatedAt = updateAt
        )
        notesDao.addNote(noteDbModel)
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toDbModel())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {
            it.toEntities()
        }
    }

    override suspend fun getNotes(noteId: Int): Note {
        return notesDao.getNotes(noteId).toEntity()
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map {
            it.toEntities()
        }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    companion object {
        private val LOCK = Any()
        private var instance: NoteRepositoryImpl? = null

        fun getInstance(context: Context): NoteRepositoryImpl {
            synchronized(LOCK) {
                instance?.let { return it }
                return NoteRepositoryImpl(context).also { instance = it }
            }

        }
    }
}
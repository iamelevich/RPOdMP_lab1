package by.iamelevich.notes.db.repository

import androidx.lifecycle.LiveData
import by.iamelevich.notes.db.dao.NoteDao
import by.iamelevich.notes.db.entity.Note

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NoteRepository(private val noteDao: NoteDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Note>> = noteDao.getLast()

    fun searchNotes(search: String): LiveData<List<Note>> {
        return noteDao.getByText(search)
    }

    fun getById(id: Int): Note? {
        return noteDao.getById(id)
    }

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }
}
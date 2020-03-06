package by.iamelevich.notes.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import by.iamelevich.notes.db.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getLast(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE text LIKE '%' || :search || '%' ORDER BY updatedAt DESC")
    fun getByText(search: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg note: Note)

    @Update
    suspend fun update(vararg note: Note)

    @Delete
    suspend fun delete(vararg note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}
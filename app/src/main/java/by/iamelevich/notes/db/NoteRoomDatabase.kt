package by.iamelevich.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.iamelevich.notes.db.dao.NoteDao
import by.iamelevich.notes.db.entity.Note
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = true)
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time. 
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): NoteRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "Note_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
package by.iamelevich.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import by.iamelevich.notes.db.converters.DateConverter
import by.iamelevich.notes.db.dao.NoteDao
import by.iamelevich.notes.db.entity.Note
import java.util.*

@Database(entities = arrayOf(Note::class), version = 3, exportSchema = true)
@TypeConverters(value = arrayOf(DateConverter::class))
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time. 
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(
            context: Context
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE notes ADD COLUMN createdAt INTEGER")
        database.execSQL("ALTER TABLE notes ADD COLUMN updatedAt INTEGER")
        val now = Date().time
        database.execSQL("UPDATE notes SET createdAt = ?, updatedAt = ? WHERE createdAt IS NULL OR updatedAt IS NULL", arrayOf(now, now))
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE notes ADD COLUMN title VARCHAR(255)")
    }
}
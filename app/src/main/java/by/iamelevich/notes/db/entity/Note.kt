package by.iamelevich.notes.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
class Note(@ColumnInfo(name = "title") var title: String?, @ColumnInfo(name = "text") var text: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "createdAt")
    var createdAt: Date? = Date()
    @ColumnInfo(name = "updatedAt")
    var updatedAt: Date? = Date()
}
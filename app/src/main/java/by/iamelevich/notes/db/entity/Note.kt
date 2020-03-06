package by.iamelevich.notes.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class Note(@ColumnInfo(name = "text") var text: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
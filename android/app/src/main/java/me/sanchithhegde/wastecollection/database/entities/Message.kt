package me.sanchithhegde.wastecollection.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @ColumnInfo
    val timestamp: Long,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val body: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

package me.sanchithhegde.wastecollection.data

import android.content.Context
import android.text.format.DateUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @ColumnInfo
    val timestamp: Long,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val body: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun toMessage(context: Context): Message {
        return Message(
            id,
            DateUtils.formatDateTime(
                context,
                timestamp,
                DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE or
                    DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_WEEKDAY
            ),
            title,
            body
        )
    }
}

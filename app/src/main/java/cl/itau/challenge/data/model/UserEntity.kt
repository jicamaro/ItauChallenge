package cl.itau.challenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = arrayOf("name", "last_name", "email"))])
data class UserEntity(
    @ColumnInfo("id") val id: String,
    @PrimaryKey @ColumnInfo("email") val email: String = "",
    @ColumnInfo("name") val name: String = "",
    @ColumnInfo("last_name") val lastName: String = "",
    @ColumnInfo("password") val password: String = ""
)

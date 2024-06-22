package cl.itau.challenge.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.itau.challenge.data.model.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE email LIKE :email")
    suspend fun getUser(email: String): UserEntity?

    @Query("SELECT COUNT(*) from users")
    fun getCount(): Int

    @Query("DELETE FROM users")
    fun deleteAll()
}
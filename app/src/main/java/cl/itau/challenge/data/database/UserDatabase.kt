package cl.itau.challenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.itau.challenge.data.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}
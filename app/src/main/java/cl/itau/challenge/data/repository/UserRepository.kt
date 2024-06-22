package cl.itau.challenge.data.repository

import cl.itau.challenge.data.datasource.UserLocalDataSource
import cl.itau.challenge.data.model.UserEntity

class UserRepository(private val userLocalDataSource: UserLocalDataSource) {

    suspend fun signUp(userEntity: UserEntity): Unit =
        userLocalDataSource.signUp(userEntity)

    suspend fun signIn(email: String, password: String): Boolean =
        userLocalDataSource.signIn(email = email, password = password)
}
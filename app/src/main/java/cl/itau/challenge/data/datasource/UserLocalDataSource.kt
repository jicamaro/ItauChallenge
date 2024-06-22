package cl.itau.challenge.data.datasource

import cl.itau.challenge.data.database.UserDao
import cl.itau.challenge.data.model.UserEntity
import cl.itau.challenge.domain.model.InvalidPasswordException

class UserLocalDataSource(private val userDao: UserDao) {

    suspend fun signUp(user: UserEntity) {
        return userDao.insert(user)
    }

    suspend fun signIn(email: String, password: String): Boolean {
        val userEntity = userDao.getUser(email)

        check(userEntity != null) { throw InvalidPasswordException() }
        check(userEntity.email == email && userEntity.password == password) { throw InvalidPasswordException() }

        return true
    }
}
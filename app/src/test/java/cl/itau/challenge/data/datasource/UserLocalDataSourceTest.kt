package cl.itau.challenge.data.datasource

import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.data.database.UserDao
import cl.itau.challenge.data.model.UserEntity
import cl.itau.challenge.domain.model.InvalidPasswordException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.test.mock.declareMock
import java.util.UUID

class UserLocalDataSourceTest: BaseUnitTest() {

    private lateinit var userDao: UserDao
    private lateinit var userLocalDataSource: UserLocalDataSource

    override fun setUp() {
        super.setUp()

        userDao = declareMock()
        userLocalDataSource = UserLocalDataSource(userDao)
    }

    @Test
    fun ShouldSignUpUser_When_DataIsCorrect() = runTest {
        val id = UUID.randomUUID().toString()
        val name = "Felipe"
        val lastName = "Gomez"
        val email = "f.gomez@gmail.com"
        val password = "a1s0A.018s"

        val userEntity = UserEntity(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            password = password
        )

        coEvery {
            userDao.insert(userEntity)
        } just Runs

        userLocalDataSource.signUp(userEntity)

        coVerify {
            userDao.insert(userEntity)
        }
    }

    @Test
    fun ShouldSignInUser_When_DataIsCorrect() = runTest {
        val id = UUID.randomUUID().toString()
        val name = "Felipe"
        val lastName = "Gomez"
        val email = "f.gomez@gmail.com"
        val password = "a1s0A.018s"

        val userEntity = UserEntity(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            password = password
        )

        coEvery {
            userDao.getUser(email = email)
        } returns userEntity

        val response = userLocalDataSource.signIn(email, password)

        assert(response)
    }

    @Test(expected = InvalidPasswordException::class)
    fun ShouldThrowException_When_PasswordIsIncorrect() = runTest {
        val id = UUID.randomUUID().toString()
        val name = "Felipe"
        val lastName = "Gomez"
        val email = "f.gomez@gmail.com"
        val password = "a1s0A"

        val userEntity = UserEntity(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            password = "10aksi29sA."
        )

        coEvery {
            userDao.getUser(email = email)
        } returns userEntity

        userLocalDataSource.signIn(email, password)
    }

    @Test(expected = InvalidPasswordException::class)
    fun ShouldThrowException_When_EmailDoesNotMatch() = runTest {
        val id = UUID.randomUUID().toString()
        val name = "Felipe"
        val lastName = "Gomez"
        val email = "f.gomez@gmail.com"
        val password = "10aksi29sA."

        val userEntity = UserEntity(
            id = id,
            name = name,
            lastName = lastName,
            email = "f.gomez1@gmail.com",
            password = password
        )

        coEvery {
            userDao.getUser(email = email)
        } returns userEntity

        userLocalDataSource.signIn(email, password)
    }

    @Test(expected = InvalidPasswordException::class)
    fun ShouldThrowException_When_UserDoesNotExists() = runTest {
        val email = "f.gomez@gmail.com"
        val password = "10aksi29sA."

        coEvery {
            userDao.getUser(email = email)
        } returns null

        userLocalDataSource.signIn(email, password)
    }
}
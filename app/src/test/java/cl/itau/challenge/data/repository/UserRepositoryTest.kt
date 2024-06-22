package cl.itau.challenge.data.repository

import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.data.datasource.UserLocalDataSource
import cl.itau.challenge.data.model.UserEntity
import cl.itau.challenge.domain.model.InvalidPasswordException
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.test.mock.declareMock
import java.util.UUID

class UserRepositoryTest: BaseUnitTest() {

    private lateinit var userLocalDataSource: UserLocalDataSource
    private lateinit var userRepository: UserRepository

    override fun setUp() {
        super.setUp()
        userLocalDataSource = declareMock()
        userRepository = UserRepository(userLocalDataSource)
    }

    @Test
    fun ShouldSignUpUser_When_DataIsCorrect() = runTest {

        val userEntity = UserEntity(
            id = UUID.randomUUID().toString(),
            name = "Felipe",
            lastName = "Gómez",
            email = "f.gomez@gmail.com",
            password = "Password1."
        )

        coEvery { userLocalDataSource.signUp(userEntity) } returns Unit

        userRepository.signUp(
            userEntity
        )

        coVerify { userLocalDataSource.signUp(userEntity) }
    }

    @Test
    fun ShouldSignInUser_When_DataIsCorrect() = runTest {

        val email = "f.gomez@gmail.com"
        val password = "Password1."

        coEvery {
            userLocalDataSource.signIn(
                email = email,
                password = password
            )
        } returns true

        val response = userLocalDataSource.signIn(email, password)

        coVerify { userLocalDataSource.signIn(email, password) }
        assert(response)
    }

    @Test(expected = InvalidPasswordException::class)
    fun ShouldThrowException_When_PasswordIsIncorrect() = runTest {

        val email = "f.gomez@gmail.com"
        val password = "Pord1."

        coEvery {
            userLocalDataSource.signIn(
                email = email,
                password = password
            )
        } throws InvalidPasswordException()

        userLocalDataSource.signIn(email, password)

        coVerify { userLocalDataSource.signIn(email, password) }
    }
}
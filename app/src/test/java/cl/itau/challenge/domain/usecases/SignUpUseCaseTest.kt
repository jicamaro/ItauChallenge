package cl.itau.challenge.domain.usecases

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.data.model.UserEntity
import cl.itau.challenge.data.repository.UserRepository
import cl.itau.challenge.domain.model.InvalidEmailException
import cl.itau.challenge.domain.model.InvalidLastNameException
import cl.itau.challenge.domain.model.InvalidNameException
import cl.itau.challenge.domain.model.InvalidPasswordException
import cl.itau.challenge.domain.model.SignUpRequest
import cl.itau.challenge.domain.model.User
import cl.itau.challenge.domain.utilities.EmailValidator
import cl.itau.challenge.domain.utilities.LastNameValidator
import cl.itau.challenge.domain.utilities.NameValidator
import cl.itau.challenge.domain.utilities.PasswordValidator
import cl.itau.challenge.utilities.DispatcherFactory
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.mock.declareMock
import java.util.UUID

class SignUpUseCaseTest: BaseUnitTest() {

    private lateinit var nameValidator: NameValidator
    private lateinit var lastNameValidator: LastNameValidator
    private lateinit var emailValidator: EmailValidator
    private lateinit var passwordValidator: PasswordValidator
    private lateinit var userRepository: UserRepository
    private lateinit var signUpUseCase: SignUpUseCase

    @Before
    override fun setUp() {
        nameValidator = declareMock<NameValidator>()
        lastNameValidator = declareMock<LastNameValidator>()
        emailValidator = declareMock<EmailValidator>()
        passwordValidator = declareMock<PasswordValidator>()
        userRepository = declareMock<UserRepository>()

        val dispatcherFactory = get<DispatcherFactory>()

        signUpUseCase = SignUpUseCase(
            nameValidator = nameValidator,
            lastNameValidator = lastNameValidator,
            emailValidator = emailValidator,
            passwordValidator = passwordValidator,
            userRepository = userRepository,
            dispatcherFactory = dispatcherFactory
        )
    }

    @Test
    fun ShouldSuccess_When_DataIsCorrect() = runTest {
        turbineScope {

            val id = UUID.randomUUID().toString()
            val name = "Felipe"
            val lastName = "Gomez"
            val email = "f.gomez@gmail.com"
            val password = "a1s0A.018s"

            every {
                nameValidator.validate(name)
            } returns true

            every {
                lastNameValidator.validate(lastName)
            } returns true

            every {
                emailValidator.validate(email)
            } returns true

            every {
                passwordValidator.validate(password)
            } returns true

            coEvery {
                userRepository.signUp(
                    UserEntity(
                        id = id,
                        name = name,
                        lastName = lastName,
                        email = email,
                        password = password
                    )
                )
            } returns Unit

            signUpUseCase.execute(
                SignUpRequest(
                    id = id,
                    name = name,
                    lastName = lastName,
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isSuccess)

                val user = response.getOrNull()
                assert(user != null)
                assertThat(user, instanceOf(User::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun ShouldFail_When_PasswordIsWrong() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val email = "asd@asd.com"
            val password = "a1s0"

            every {
                emailValidator.validate(email)
            } returns true

            every {
                passwordValidator.validate(password)
            } returns false

            signUpUseCase.execute(
                SignUpRequest(
                    id = id,
                    name = "",
                    lastName = "",
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isFailure)

                val throwable = response.exceptionOrNull() ?: NullPointerException()
                assertThat(throwable, instanceOf(InvalidPasswordException::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun ShouldFail_When_EmailIsWrong() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val email = "ásd@asd.com"
            val password = "a1s0apsP.sac"

            every {
                emailValidator.validate(email)
            } returns false

            every {
                passwordValidator.validate(password)
            } returns true

            signUpUseCase.execute(
                SignUpRequest(
                    id = id,
                    name = "",
                    lastName = "",
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isFailure)

                val throwable = response.exceptionOrNull() ?: NullPointerException()
                assertThat(throwable, instanceOf(InvalidEmailException::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun ShouldFail_When_NameIsWrong() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val name = "Felipe Benjamin"
            val email = "f.gomez@gmail.com"
            val password = "a1s0apsP.sac"

            every {
                emailValidator.validate(email)
            } returns true

            every {
                passwordValidator.validate(password)
            } returns true

            every {
                nameValidator.validate(name)
            } returns false

            signUpUseCase.execute(
                SignUpRequest(
                    id = id,
                    name = name,
                    lastName = "",
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isFailure)

                val throwable = response.exceptionOrNull() ?: NullPointerException()
                assertThat(throwable, instanceOf(InvalidNameException::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun ShouldFail_When_LastNameIsWrong() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val name = "Felipe"
            val lastName = "Gomez"
            val email = "f.gomez@gmail.com"
            val password = "a1s0apsP.sac"

            every {
                emailValidator.validate(email)
            } returns true

            every {
                passwordValidator.validate(password)
            } returns true

            every {
                nameValidator.validate(name)
            } returns true

            every {
                lastNameValidator.validate(lastName)
            } returns false

            signUpUseCase.execute(
                SignUpRequest(
                    id = id,
                    name = name,
                    lastName = lastName,
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isFailure)

                val throwable = response.exceptionOrNull() ?: NullPointerException()
                assertThat(throwable, instanceOf(InvalidLastNameException::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
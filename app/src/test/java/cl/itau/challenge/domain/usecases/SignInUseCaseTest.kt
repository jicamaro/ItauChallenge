package cl.itau.challenge.domain.usecases

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.data.repository.UserRepository
import cl.itau.challenge.domain.model.InvalidEmailException
import cl.itau.challenge.domain.model.InvalidPasswordException
import cl.itau.challenge.domain.model.SignInRequest
import cl.itau.challenge.domain.utilities.EmailValidator
import cl.itau.challenge.domain.utilities.LastNameValidator
import cl.itau.challenge.domain.utilities.NameValidator
import cl.itau.challenge.domain.utilities.PasswordValidator
import cl.itau.challenge.utilities.DispatcherFactory
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.mock.declareMock

class SignInUseCaseTest: BaseUnitTest() {

    private lateinit var nameValidator: NameValidator
    private lateinit var lastNameValidator: LastNameValidator
    private lateinit var emailValidator: EmailValidator
    private lateinit var passwordValidator: PasswordValidator
    private lateinit var userRepository: UserRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    override fun setUp() {
        nameValidator = declareMock<NameValidator>()
        lastNameValidator = declareMock<LastNameValidator>()
        emailValidator = declareMock<EmailValidator>()
        passwordValidator = declareMock<PasswordValidator>()
        userRepository = declareMock<UserRepository>()

        val dispatcherFactory = get<DispatcherFactory>()

        signInUseCase = SignInUseCase(
            emailValidator = emailValidator,
            passwordValidator = passwordValidator,
            userRepository = userRepository,
            dispatcherFactory = dispatcherFactory
        )
    }

    @Test
    fun ShouldSuccess_When_RequestIsCorrect() = runTest {
        turbineScope {
            val email = "asd@asd.com"
            val password = "a1s0P0sak."

            every {
                emailValidator.validate(email)
            } returns true

            every {
                passwordValidator.validate(password)
            } returns true

            coEvery {
                userRepository.signIn(email, password)
            } returns true

            signInUseCase.execute(
                SignInRequest(
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isSuccess)

                val isSuccessful = response.getOrNull() ?: false
                assert(isSuccessful)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun ShouldFail_When_PasswordIsWrong() = runTest {
        turbineScope {
            val email = "asd@asd.com"
            val password = "a1s0"

            every {
                emailValidator.validate(email)
            } returns true

            every {
                passwordValidator.validate(password)
            } returns false

            signInUseCase.execute(
                SignInRequest(
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isFailure)

                val throwable = response.exceptionOrNull() ?: NullPointerException()
                assertThat(throwable, CoreMatchers.instanceOf(InvalidPasswordException::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun ShouldFail_When_EmailIsWrong() = runTest {
        turbineScope {
            val email = "ásd@asd.com"
            val password = "a1s0Pa012."

            every {
                emailValidator.validate(email)
            } returns false

            every {
                passwordValidator.validate(password)
            } returns true

            signInUseCase.execute(
                SignInRequest(
                    email = email,
                    password = password
                )
            ).test {
                val response = awaitItem()
                assert(response.isFailure)

                val throwable = response.exceptionOrNull() ?: NullPointerException()
                assertThat(throwable, CoreMatchers.instanceOf(InvalidEmailException::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
package cl.itau.challenge.presentation

import app.cash.turbine.turbineScope
import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.domain.model.Earthquake
import cl.itau.challenge.domain.model.EarthquakeProperty
import cl.itau.challenge.domain.model.Geometry
import cl.itau.challenge.domain.model.GetEarthquakesRequest
import cl.itau.challenge.domain.model.InvalidEmailException
import cl.itau.challenge.domain.model.InvalidLastNameException
import cl.itau.challenge.domain.model.InvalidNameException
import cl.itau.challenge.domain.model.InvalidPasswordException
import cl.itau.challenge.domain.model.SignInRequest
import cl.itau.challenge.domain.model.SignUpRequest
import cl.itau.challenge.domain.model.User
import cl.itau.challenge.domain.usecases.GetEarthquakesUseCase
import cl.itau.challenge.domain.usecases.SignInUseCase
import cl.itau.challenge.domain.usecases.SignUpUseCase
import cl.itau.challenge.presentation.model.ListItem
import cl.itau.challenge.presentation.model.UiState
import cl.itau.challenge.presentation.utilities.YEAR_FIRST_FORMAT
import cl.itau.challenge.presentation.utilities.toFormattedString
import cl.itau.challenge.presentation.viewmodel.MainViewModel
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.koin.test.mock.declareMock
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest: BaseUnitTest() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var getEarthquakesUseCase: GetEarthquakesUseCase
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var signUpUseCase: SignUpUseCase

    @Before
    override fun setUp() {
        getEarthquakesUseCase = declareMock<GetEarthquakesUseCase>()
        signInUseCase = declareMock<SignInUseCase>()
        signUpUseCase = declareMock<SignUpUseCase>()

        mainViewModel = MainViewModel(
            getEarthquakesUseCase = getEarthquakesUseCase,
            signInUseCase = signInUseCase,
            signUpUseCase = signUpUseCase
        )
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun SignUpShouldSuccess_When_DataIsCorrect() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val name = "Felipe"
            val lastName = "Gomez"
            val email = "f.gomez@gmail.com"
            val password = "12A4!93js3"

            coEvery { signUpUseCase.execute(SignUpRequest(
                id = id,
                name = name,
                lastName = lastName,
                email = email,
                password = password
            )) } returns flowOf(Result.success(
                User(
                    id = id,
                    email = email,
                    name = name,
                    lastName = lastName,
                    password = password
                )
            ))

            mainViewModel.signUp(
                id,
                name,
                lastName,
                email,
                password
            )

            val turbine = mainViewModel.signUpUiState.testIn(
                this
            )
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignUpSuccessful::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun SignUpShouldFail_When_PasswordIsTooShort() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val name = "Felipe"
            val lastName = "Gomez"
            val email = "f.gomez@gmail.com"
            val password = "12A4"

            coEvery { signUpUseCase.execute(SignUpRequest(
                id = id,
                name = name,
                lastName = lastName,
                email = email,
                password = password
            )) } returns flowOf(Result.failure(InvalidPasswordException()))

            mainViewModel.signUp(
                id,
                name,
                lastName,
                email,
                password
            )
            val turbine = mainViewModel.signUpUiState.testIn(this)
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignUpError::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun SignUpShouldFail_When_EmailIsIncorrect() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val name = "Felipe"
            val lastName = "Gomez"
            val email = "f.gómez@gmail.com"
            val password = "Password1."

            coEvery { signUpUseCase.execute(SignUpRequest(
                id = id,
                name = name,
                lastName = lastName,
                email = email,
                password = password
            )) } returns flowOf(Result.failure(InvalidEmailException()))

            mainViewModel.signUp(
                id,
                name,
                lastName,
                email,
                password
            )
            val turbine = mainViewModel.signUpUiState.testIn(this)
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignUpError::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun SignUpShouldFail_When_NameIsIncorrect() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val name = "Felipe Benjamin"
            val lastName = "Gomez"
            val email = "f.gomez@gmail.com"
            val password = "Password1."

            coEvery { signUpUseCase.execute(SignUpRequest(
                id = id,
                name = name,
                lastName = lastName,
                email = email,
                password = password
            )) } returns flowOf(Result.failure(InvalidNameException()))

            mainViewModel.signUp(
                id,
                name,
                lastName,
                email,
                password
            )
            val turbine = mainViewModel.signUpUiState.testIn(this)
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignUpError::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun SignUpShouldFail_When_LastNameIsIncorrect() = runTest {
        turbineScope {
            val id = UUID.randomUUID().toString()
            val name = "Felipe"
            val lastName = "Gomez Milla"
            val email = "f.gomez@gmail.com"
            val password = "Password1."

            coEvery { signUpUseCase.execute(SignUpRequest(
                id = id,
                name = name,
                lastName = lastName,
                email = email,
                password = password
            )) } returns flowOf(Result.failure(InvalidLastNameException()))

            mainViewModel.signUp(
                id,
                name,
                lastName,
                email,
                password
            )
            val turbine = mainViewModel.signUpUiState.testIn(this)
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignUpError::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun SignInShouldSuccess_When_DataIsCorrect() = runTest {
        turbineScope {
            val email = "f.gomez@gmail.com"
            val password = "12A4!93js3"

            coEvery { signInUseCase.execute(
                SignInRequest(
                    email = email,
                    password = password
                )
            ) } returns flowOf(Result.success(true))

            mainViewModel.signIn(
                email,
                password
            )

            val turbine = mainViewModel.signInUiState.testIn(
                this
            )
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignInSuccessful::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun SignInShouldFail_When_EmailIsIncorrect() = runTest {
        turbineScope {
            val email = "f.gómez@gmail.com"
            val password = "Password1."

            coEvery { signInUseCase.execute(SignInRequest(
                email = email,
                password = password
            )) } returns flowOf(Result.failure(InvalidEmailException()))

            mainViewModel.signIn(
                email,
                password
            )
            val turbine = mainViewModel.signInUiState.testIn(this)
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignInError::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun SignInShouldFail_When_PasswordIsIncorrect() = runTest {
        turbineScope {
            val email = "f.gomez@gmail.com"
            val password = "Password1"

            coEvery { signInUseCase.execute(SignInRequest(
                email = email,
                password = password
            )) } returns flowOf(Result.failure(InvalidPasswordException()))

            mainViewModel.signIn(
                email,
                password
            )
            val turbine = mainViewModel.signInUiState.testIn(this)
            turbine.run {
                skipItems(2)
                assertThat(awaitItem(), instanceOf(UiState.SignInError::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun GetEarthquakesShouldSuccess_When_DataIsCorrect() = runTest {
        turbineScope {
            val format = "geojson"
            val startTime = LocalDate.now()
            val endTime = LocalDate.now().minusDays(1)
            val limit = 10
            val orderBy = "time-asc"

            coEvery { getEarthquakesUseCase.execute(
                GetEarthquakesRequest(
                    format = format,
                    startTime = startTime.toFormattedString(YEAR_FIRST_FORMAT),
                    endTime = endTime.toFormattedString(YEAR_FIRST_FORMAT),
                    limit = limit,
                    orderBy = orderBy
                )
            ) } returns flowOf(Result.success(
                listOf(dummy())
            ))

            mainViewModel.getEarthquakes(
                startTime = startTime,
                endTime = endTime,
                limit = limit
            )

            val turbine = mainViewModel.dashboardUiState.testIn(
                this
            )
            turbine.run {
                skipItems(1)

                val response = awaitItem()
                assertThat(response, instanceOf(UiState.PrivateDashboard::class.java))
                assert((response as UiState.PrivateDashboard).listItems.isNotEmpty())
                assert(response.listItems.any {
                    (it as ListItem.Item).earthquakeModel.properties.title == "Terremoto"
                })

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun GetEarthquakesShouldFail_When_ExceptionIsThrown() = runTest {
        turbineScope {
            val format = "geojson"
            val startTime = LocalDate.now()
            val endTime = LocalDate.now().minusDays(1)
            val limit = 10
            val orderBy = "time-asc"

            coEvery { getEarthquakesUseCase.execute(
                GetEarthquakesRequest(
                    format = format,
                    startTime = startTime.toFormattedString(YEAR_FIRST_FORMAT),
                    endTime = endTime.toFormattedString(YEAR_FIRST_FORMAT),
                    limit = limit,
                    orderBy = orderBy
                )
            ) } returns flowOf(Result.failure(NoSuchElementException()))

            mainViewModel.getEarthquakes(
                startTime = startTime,
                endTime = endTime,
                limit = limit
            )

            val turbine = mainViewModel.dashboardUiState.testIn(
                this
            )
            turbine.run {
                skipItems(1)

                val response = awaitItem()
                assertThat(response, instanceOf(UiState.PrivateDashboardError::class.java))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
    }
}

fun dummy() = Earthquake(
    "",
    "",
    EarthquakeProperty(
        0.5,
        "",
        100L,
        100L,
        null,
        "",
        "",
        null,
        null,
        null,
        null,
        "",
        100L,
        100L,
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        0.5,
        null,
        "",
        "",
        "Terremoto"
    ),
    Geometry(
        "",
        listOf()
    )
)
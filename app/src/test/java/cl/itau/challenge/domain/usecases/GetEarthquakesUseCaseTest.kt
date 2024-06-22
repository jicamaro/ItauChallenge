package cl.itau.challenge.domain.usecases

import app.cash.turbine.test
import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.data.repository.EarthquakeRepository
import cl.itau.challenge.domain.model.Earthquake
import cl.itau.challenge.domain.model.EarthquakeProperty
import cl.itau.challenge.domain.model.Geometry
import cl.itau.challenge.domain.model.GetEarthquakesRequest
import cl.itau.challenge.utilities.DispatcherFactory
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.koin.test.inject
import org.koin.test.mock.declareMock

class GetEarthquakesUseCaseTest: BaseUnitTest() {

    private lateinit var earthquakeRepository: EarthquakeRepository
    private lateinit var getEarthquakesUseCase: GetEarthquakesUseCase
    private val dispatcherFactory by inject<DispatcherFactory>()

    override fun setUp() {
        super.setUp()
        earthquakeRepository = declareMock()
        getEarthquakesUseCase = GetEarthquakesUseCase(earthquakeRepository, dispatcherFactory)
    }

    @Test
    fun ShouldGetEarthquakes_When_DataIsCorrect() = runTest {
        val format = "geojson"
        val startTime = "2020-01-01"
        val endTime = "2020-01-02"
        val limit = 10
        val orderBy = "time-asc"

        coEvery {
            earthquakeRepository.getEarthquakes(
                format = format,
                startTime = startTime,
                endTime = endTime,
                limit = limit,
                orderBy = orderBy
            )
        } returns listOf(dummy())

        getEarthquakesUseCase.execute(
            GetEarthquakesRequest(
                format = format,
                startTime = startTime,
                endTime = endTime,
                limit = limit,
                orderBy = orderBy
            )
        ).test {
            coVerify { earthquakeRepository.getEarthquakes(
                format = format,
                startTime = startTime,
                endTime = endTime,
                limit = limit,
                orderBy = orderBy
            ) }

            val response = awaitItem()
            assert(response.isSuccess)

            val items = response.getOrNull()
            assertThat(items,
                everyItem(instanceOf(Earthquake::class.java))
            )
            assert(items?.any { it.properties.title == "Terremoto" } ?: false)
            cancelAndIgnoreRemainingEvents()
        }
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
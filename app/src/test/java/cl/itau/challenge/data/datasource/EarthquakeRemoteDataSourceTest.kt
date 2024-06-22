package cl.itau.challenge.data.datasource

import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.data.model.EarthquakeResponse
import cl.itau.challenge.data.model.MetadataEntity
import cl.itau.challenge.data.source.EarthquakeAPI
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.koin.test.mock.declareMock

class EarthquakeRemoteDataSourceTest: BaseUnitTest() {

    private lateinit var webService: EarthquakeAPI
    private lateinit var earthquakeRemoteDataSource: EarthquakeRemoteDataSource

    override fun setUp() {
        super.setUp()
        webService = declareMock<EarthquakeAPI>()
        earthquakeRemoteDataSource = EarthquakeRemoteDataSource(webService)
    }

    @Test
    fun ShouldGetEarthquakes_When_DataIsCorrect() = runTest {
        val format = "geojson"
        val startTime = "2020-01-01"
        val endTime = "2020-01-02"
        val limit = 10
        val orderBy = "time-asc"

        coEvery {
            webService.getEarthquakes(
                format = format,
                startTime = startTime,
                endTime = endTime,
                limit = limit,
                orderBy = orderBy
            )
        } returns dummy()

        val response = earthquakeRemoteDataSource.getEarthquakes(
            format = format,
            startTime = startTime,
            endTime = endTime,
            limit = limit,
            orderBy = orderBy
        )

        coVerify {
            webService.getEarthquakes(
                format = format,
                startTime = startTime,
                endTime = endTime,
                limit = limit,
                orderBy = orderBy
            )
        }

        assertThat(response, instanceOf(EarthquakeResponse::class.java))
    }
}

fun dummy() = EarthquakeResponse(
    type = "",
    metadataEntity = MetadataEntity(
        10L,
    "",
        "",
        10L,
        "",
        10L
    ),
    bbox = listOf(),
    features = listOf()
)
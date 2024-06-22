package cl.itau.challenge.data.repository

import cl.itau.challenge.BaseUnitTest
import cl.itau.challenge.data.datasource.EarthquakeRemoteDataSource
import cl.itau.challenge.data.model.EarthquakeEntity
import cl.itau.challenge.data.model.EarthquakePropertyEntity
import cl.itau.challenge.data.model.EarthquakeResponse
import cl.itau.challenge.data.model.GeometryEntity
import cl.itau.challenge.data.model.MetadataEntity
import cl.itau.challenge.domain.model.Earthquake
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.koin.test.mock.declareMock

class EarthquakeRepositoryTest: BaseUnitTest() {

    private lateinit var earthquakeRemoteDataSource: EarthquakeRemoteDataSource
    private lateinit var earthquakeRepository: EarthquakeRepository

    override fun setUp() {
        super.setUp()
        earthquakeRemoteDataSource = declareMock()
        earthquakeRepository = EarthquakeRepository(earthquakeRemoteDataSource)
    }

    @Test
    fun ShouldGetEarthquakes_When_DataIsCorrect() = runTest {
        val format = "geojson"
        val startTime = "2020-01-01"
        val endTime = "2020-01-02"
        val limit = 10
        val orderBy = "time-asc"

        coEvery {
            earthquakeRemoteDataSource.getEarthquakes(
                startTime = startTime,
                endTime = endTime,
                format = format,
                limit = limit,
                orderBy = orderBy
            )
        } returns dummy()

        val response = earthquakeRepository.getEarthquakes(
            startTime = startTime,
            endTime = endTime,
            format = format,
            limit = limit,
            orderBy = orderBy
        )

        coVerify {
            earthquakeRemoteDataSource.getEarthquakes(
                startTime = startTime,
                endTime = endTime,
                format = format,
                limit = limit,
                orderBy = orderBy
            )
        }

        assertThat(response, everyItem(instanceOf(Earthquake::class.java)))
        assert(response.any { it.properties.title == "Terremoto" })
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
    features = listOf(
        EarthquakeEntity(
            "",
            "",
            EarthquakePropertyEntity(
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
            GeometryEntity(
                "",
                listOf()
            )
        )
    )
)
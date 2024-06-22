package cl.itau.challenge.data.repository

import cl.itau.challenge.data.datasource.EarthquakeRemoteDataSource
import cl.itau.challenge.domain.utilities.toDomain

class EarthquakeRepository(
    private val earthquakeRemoteDataSource: EarthquakeRemoteDataSource
) {

    suspend fun getEarthquakes(startTime: String, endTime: String, format: String, limit: Int, orderBy: String) =
        earthquakeRemoteDataSource.getEarthquakes(
            startTime, endTime, format, limit, orderBy
        ).features.map {
            it.toDomain()
        }
}
package cl.itau.challenge.data.datasource

import cl.itau.challenge.data.source.EarthquakeAPI

class EarthquakeRemoteDataSource(
    private val webService: EarthquakeAPI
) {

    suspend fun getEarthquakes(startTime: String, endTime: String, format: String, limit: Int, orderBy: String) =
        webService.getEarthquakes(
            startTime = startTime,
            endTime = endTime,
            format = format,
            limit = limit,
            orderBy = orderBy
        )
}
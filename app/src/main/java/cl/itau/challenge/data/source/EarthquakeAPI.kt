package cl.itau.challenge.data.source

import cl.itau.challenge.data.model.EarthquakeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface EarthquakeAPI {

    @GET("fdsnws/event/1/query")
    suspend fun getEarthquakes(
        @Query("format") format: String,
        @Query("starttime") startTime: String,
        @Query("endtime") endTime: String,
        @Query("limit") limit: Int,
        @Query("orderby") orderBy: String
    ): EarthquakeResponse
}
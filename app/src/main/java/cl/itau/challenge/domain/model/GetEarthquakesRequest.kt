package cl.itau.challenge.domain.model

data class GetEarthquakesRequest(
    val format: String = "geojson",
    val startTime: String = "2020-01-01",
    val endTime: String = "2020-01-02",
    val limit: Int = 10,
    val orderBy: String = "time-asc"
)
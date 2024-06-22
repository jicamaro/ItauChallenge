package cl.itau.challenge.domain.model

data class Earthquake(
    val id: String,
    val type: String,
    val properties: EarthquakeProperty,
    val geometry: Geometry
)
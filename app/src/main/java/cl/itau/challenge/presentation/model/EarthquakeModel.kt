package cl.itau.challenge.presentation.model

data class EarthquakeModel(
    val id: String,
    val type: String,
    val properties: EarthquakePropertyModel,
    val geometryModel: GeometryModel
)
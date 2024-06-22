package cl.itau.challenge.data.model

import com.google.gson.annotations.SerializedName

data class EarthquakeEntity(
    val id: String,
    val type: String,
    val properties: EarthquakePropertyEntity,
    @SerializedName("geometry") val geometryEntity: GeometryEntity
)
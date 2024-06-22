package cl.itau.challenge.data.model

import com.google.gson.annotations.SerializedName

data class EarthquakeResponse(
    private val type: String,
    @SerializedName("metadata") private val metadataEntity: MetadataEntity,
    val features: List<EarthquakeEntity>,
    private val bbox: List<Double>
)

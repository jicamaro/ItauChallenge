package cl.itau.challenge.data.model

data class MetadataEntity(
    private val generated: Long,
    private val url: String,
    private val title: String,
    private val status: Long,
    private val api: String,
    private val count: Long
)

package cl.itau.challenge.domain.model

data class SignUpRequest(
    val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val password: String
)
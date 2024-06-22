package cl.itau.challenge.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val lastName: String,
    val password: String
)

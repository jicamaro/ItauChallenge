package cl.itau.challenge.domain.model

data class SignInRequest(
    val email: String,
    val password: String
)
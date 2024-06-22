package cl.itau.challenge.domain.utilities

import java.util.regex.Pattern

class PasswordValidator: Validator<String> {

    override fun validate(input: String): Boolean = Pattern.matches(
        "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$",
        input
    )
}
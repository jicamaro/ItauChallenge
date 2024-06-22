package cl.itau.challenge.domain.utilities

import java.util.regex.Pattern

class EmailValidator: Validator<String> {

    override fun validate(input: String): Boolean = Pattern.matches(
        "^[A-za-z0-9-.]+@([A-za-z0-9-]+\\.)+[A-za-z-]{2,4}",
        input
    )
}
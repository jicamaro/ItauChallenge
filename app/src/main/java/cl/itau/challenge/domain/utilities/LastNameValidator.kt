package cl.itau.challenge.domain.utilities

import java.util.regex.Pattern

class LastNameValidator: Validator<String> {

    override fun validate(input: String): Boolean = Pattern.matches(
        "^([a-zA-ZÁÉÍÓÚáéíóú]{0,4})?( *[a-zA-ZÁÉÍÓÚáéíóú]+)\$",
        input
    )
}
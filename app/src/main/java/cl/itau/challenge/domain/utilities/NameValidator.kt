package cl.itau.challenge.domain.utilities

import java.util.regex.Pattern

class NameValidator: Validator<String> {

    override fun validate(input: String): Boolean = Pattern.matches(
        "^[A-za-zÁÉÍÓÚáéíóú]+\$",
        input
    )
}
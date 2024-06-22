package cl.itau.challenge.domain.utilities

interface Validator<T> {

    fun validate(input: T): Boolean
}
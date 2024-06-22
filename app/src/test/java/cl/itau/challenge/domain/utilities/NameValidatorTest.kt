package cl.itau.challenge.domain.utilities

import cl.itau.challenge.BaseUnitTest
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

class NameValidatorTest: BaseUnitTest() {

    private val nameValidator by inject<NameValidator>()

    @Test
    fun ShouldSuccess_When_NameDoesHaveOnlyAlphabetCharacters() {
        assert(nameValidator.validate("Felipe"))
    }

    @Test
    fun ShouldSuccess_When_NameDoesHaveAlphabetCharactersAndAccentMark() {
        assert(nameValidator.validate("Pía"))
    }

    @Test
    fun ShouldFail_When_NameDoesDigitsAsCharacters() {
        assertEquals(false, nameValidator.validate("F3lip3"))
    }

    @Test
    fun ShouldFail_When_NameDoesHaveSpacesIncluded() {
        assertEquals(false, nameValidator.validate("Felipe Segundo"))
    }

    @Test
    fun ShouldFail_When_NameDoesHaveSpacesIncluded2() {
        assertEquals(false, nameValidator.validate("Felipe "))
    }
}
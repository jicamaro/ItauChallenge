package cl.itau.challenge.domain.utilities

import cl.itau.challenge.BaseUnitTest
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

class LastNameValidatorTest: BaseUnitTest() {

    private val lastNameValidator by inject<LastNameValidator>()

    @Test
    fun ShouldSuccess_When_NameDoesHaveOnlyAlphabetCharacters() {
        assert(lastNameValidator.validate("Campano"))
    }

    @Test
    fun ShouldSuccess_When_NameDoesHaveAlphabetCharactersAndAccentMark() {
        assert(lastNameValidator.validate("Martín"))
    }

    @Test
    fun ShouldSuccess_When_NameDoesHaveAlphabetCharactersAndSpaceBetweenWords() {
        assert(lastNameValidator.validate("San Martín"))
    }

    @Test
    fun ShouldFail_When_NameDoesDigitsAsCharacters() {
        assertEquals(false, lastNameValidator.validate("C4mPan0!"))
    }

    @Test
    fun ShouldFail_When_NameDoesHaveSpacesIncludedAtEnd() {
        assertEquals(false, lastNameValidator.validate("Campano "))
    }
}
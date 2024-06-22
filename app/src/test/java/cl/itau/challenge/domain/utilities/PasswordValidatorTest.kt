package cl.itau.challenge.domain.utilities

import cl.itau.challenge.BaseUnitTest
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

class PasswordValidatorTest: BaseUnitTest() {

    private val passwordValidator by inject<PasswordValidator>()

    @Test
    fun ShouldSuccess_When_PasswordComply() {
        assert(passwordValidator.validate("a1@20sJ18s"))
    }

    @Test
    fun ShouldFail_When_NoUppercaseCharacters() {
        assertEquals(false, passwordValidator.validate("a1@20s18s"))
    }

    @Test
    fun ShouldFail_When_NoAlphabetCharacters() {
        assertEquals(false, passwordValidator.validate("1@2018393283"))
    }

    @Test
    fun ShouldFail_When_NoLowercaseCharacters() {
        assertEquals(false, passwordValidator.validate("A1@20S18S"))
    }

    @Test
    fun ShouldFail_When_OnlyDigits() {
        assertEquals(false, passwordValidator.validate("2018393283"))
    }

    @Test
    fun ShouldFail_When_OnlyCharacters() {
        assertEquals(false, passwordValidator.validate("@/()¨*[¡?[]**"))
    }

    @Test
    fun ShouldFail_When_PasswordIsTooShort() {
        assertEquals(false, passwordValidator.validate("@1sS"))
    }
}
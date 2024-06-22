package cl.itau.challenge.domain.utilities

import cl.itau.challenge.BaseUnitTest
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

class EmailValidatorTest: BaseUnitTest() {

    private val emailValidator by inject<EmailValidator>()

    @Test
    fun ShouldSuccess_When_EmailComply() {
        assert(emailValidator.validate("asd@asd.com"))
    }

    @Test
    fun ShouldSuccess_When_EmailComplyWithADot() {
        assert(emailValidator.validate("asd.ad@asd.com"))
    }

    @Test
    fun ShouldSuccess_When_EmailComplyWithADomainDot() {
        assert(emailValidator.validate("asdad@asd.asdd.com"))
    }

    @Test
    fun ShouldFail_When_EmailDomainIsADigit() {
        assert(emailValidator.validate("asd@1.com"))
    }

    @Test
    fun ShouldFail_When_EmailDoesNotHaveDomain() {
        assertEquals(false, emailValidator.validate("asd@"))
    }

    @Test
    fun ShouldFail_When_EmailDoesNotHaveAtCharacter() {
        assertEquals(false, emailValidator.validate("asdasd.com"))
    }

    @Test
    fun ShouldFail_When_EmailHaveAInvalidCharacter() {
        assertEquals(false, emailValidator.validate("asd,asd@asd.com"))
    }

    @Test
    fun ShouldFail_When_EmailHaveAAccentMarkCharacter() {
        assertEquals(false, emailValidator.validate("ádasd@asd.com"))
    }
}
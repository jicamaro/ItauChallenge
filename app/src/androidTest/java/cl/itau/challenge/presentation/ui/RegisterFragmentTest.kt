package cl.itau.challenge.presentation.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import cl.itau.challenge.R
import cl.itau.challenge.data.database.UserDao
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class RegisterFragmentTest: KoinTest {

    private val userDao by inject<UserDao>()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        userDao.deleteAll()
    }

    @Test
    fun ShouldRegister_When_DataInsertedIsCorrect() {
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )
        Espresso.onView(withId(R.id.name_edittext)).perform(
            replaceText("Felipe")
        )
        Espresso.onView(withId(R.id.last_name_edittext)).perform(
            replaceText("Gomez")
        )
        Espresso.onView(withId(R.id.email_edittext)).perform(
            replaceText("f.gomez@gmail.com")
        )
        Espresso.onView(withId(R.id.password_edittext)).perform(
            replaceText("Password1.")
        )
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )

        Espresso.onView(withText(R.string.signin)).check(
            matches(
                isDisplayed()
            )
        )

        assert(userDao.getCount() == 1)
    }

    @Test
    fun ShouldFail_When_NameHasComposite() {
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )
        Espresso.onView(withId(R.id.name_edittext)).perform(
            replaceText("Felipe Benjamin")
        )
        Espresso.onView(withId(R.id.last_name_edittext)).perform(
            replaceText("Gomez")
        )
        Espresso.onView(withId(R.id.email_edittext)).perform(
            replaceText("f.gomez@gmail.com")
        )
        Espresso.onView(withId(R.id.password_edittext)).perform(
            replaceText("Password1.")
        )
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )

        Espresso.onView(withText("El nombre introducido no es válido")).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun ShouldFail_When_LastNameHasComposite() {
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )
        Espresso.onView(withId(R.id.name_edittext)).perform(
            replaceText("Felipe")
        )
        Espresso.onView(withId(R.id.last_name_edittext)).perform(
            replaceText("Gomez Milla")
        )
        Espresso.onView(withId(R.id.email_edittext)).perform(
            replaceText("f.gomez@gmail.com")
        )
        Espresso.onView(withId(R.id.password_edittext)).perform(
            replaceText("Password1.")
        )
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )

        Espresso.onView(withText("El apellido introducido no es válido")).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun ShouldFail_When_EmailPatternDoesNotMatch() {
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )
        Espresso.onView(withId(R.id.name_edittext)).perform(
            replaceText("Felipe")
        )
        Espresso.onView(withId(R.id.last_name_edittext)).perform(
            replaceText("Gomez")
        )
        Espresso.onView(withId(R.id.email_edittext)).perform(
            replaceText("f.gómez@gmail.com")
        )
        Espresso.onView(withId(R.id.password_edittext)).perform(
            replaceText("Password1.")
        )
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )

        Espresso.onView(withText("El correo electrónico introducido no es válido")).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun ShouldFail_When_PasswordPatternDoesNotMatch() {
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )
        Espresso.onView(withId(R.id.name_edittext)).perform(
            replaceText("Felipe")
        )
        Espresso.onView(withId(R.id.last_name_edittext)).perform(
            replaceText("Gomez")
        )
        Espresso.onView(withId(R.id.email_edittext)).perform(
            replaceText("f.gomez@gmail.com")
        )
        Espresso.onView(withId(R.id.password_edittext)).perform(
            replaceText("password1.")
        )
        Espresso.onView(withId(R.id.button_register)).perform(
            click()
        )

        Espresso.onView(withText("La contraseña introducido no es válida")).check(
            matches(
                isDisplayed()
            )
        )
    }
}
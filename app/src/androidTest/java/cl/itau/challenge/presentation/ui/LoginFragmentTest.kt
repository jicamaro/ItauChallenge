package cl.itau.challenge.presentation.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import cl.itau.challenge.R
import cl.itau.challenge.data.database.UserDao
import cl.itau.challenge.data.model.UserEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.UUID

class LoginFragmentTest: KoinTest {

    private val userDao by inject<UserDao>()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        runBlocking {
            activityScenarioRule.scenario.moveToState(Lifecycle.State.CREATED)
            activityScenarioRule.scenario.onActivity {
                it.lifecycleScope.launch {
                    userDao.insert(UserEntity(
                        id = UUID.randomUUID().toString(),
                        name = "Felipe",
                        lastName = "Gómez",
                        email = "f.gomez@gmail.com",
                        password = "Password1."
                    ))
                }
            }
            activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
        }
    }

    @Test
    fun ShouldLogin_When_DataIsCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.email_edittext)).perform(
            replaceText("f.gomez@gmail.com")
        )
        Espresso.onView(ViewMatchers.withId(R.id.password_edittext)).perform(
            replaceText("Password1.")
        )
        Espresso.onView(ViewMatchers.withId(R.id.button_login)).perform(
            click()
        )
        Espresso.onView(ViewMatchers.withText(R.string.dashboard_fragment_label)).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}
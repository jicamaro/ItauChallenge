package cl.itau.challenge.presentation.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest

class MainActivityTest: KoinTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun ShouldShowSnackbar_When_MessageIsSent(){

        val message = "Error"

        activityScenarioRule.scenario.onActivity {
            it.show(message)
        }

        Espresso.onView(withText(message)).check(
            matches(isDisplayed())
        )
    }
}
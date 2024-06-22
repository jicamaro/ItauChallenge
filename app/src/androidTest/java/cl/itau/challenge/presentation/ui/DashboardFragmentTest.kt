package cl.itau.challenge.presentation.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import cl.itau.challenge.R
import cl.itau.challenge.presentation.model.EarthquakeModel
import cl.itau.challenge.presentation.ui.adapter.EarthquakeViewHolder
import cl.itau.challenge.presentation.utilities.RecyclerViewItemCountAssertion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardFragmentTest {

    @Test
    fun ShouldShowRecycler_When_Started() = runTest {

        val scenario = launchFragmentInContainer<DashboardFragment>(themeResId = R.style.AppTheme)
        scenario.recreate()

        Espresso.onView(withId(R.id.recycler)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun ShouldHaveTenItemsPlusHeader_When_Started() = runTest {

        val scenario = launchFragmentInContainer<DashboardFragment>(themeResId = R.style.AppTheme)
        scenario.recreate()

        runBlocking {
            advanceUntilIdle()
            Espresso.onView(withId(R.id.recycler)).check(
                RecyclerViewItemCountAssertion.withItemCount(11)
            )
        }
    }
}
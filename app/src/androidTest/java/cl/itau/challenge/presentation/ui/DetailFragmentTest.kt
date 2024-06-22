package cl.itau.challenge.presentation.ui

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.itau.challenge.R
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class DetailFragmentTest: KoinTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val title = "M 1.3 - 38 km SE of Tanana, Alaska"
    private val place = "38 km SE of Tanana, Alaska"
    private val magnitude = 1.3
    private val coordinates = doubleArrayOf(-151.5952, 64.8922, 2.5)

    private val bundle by lazy {
        Bundle().apply {
            putString(DETAIL_TITLE, title)
            putString(DETAIL_PLACE, place)
            putDouble(DETAIL_MAGNITUDE, magnitude)
            putDoubleArray(DETAIL_COORDINATES, coordinates)
        }
    }

    private val bundleWithoutCoordinates by lazy {
        Bundle().apply {
            putString(DETAIL_TITLE, title)
            putString(DETAIL_PLACE, place)
            putDouble(DETAIL_MAGNITUDE, magnitude)
        }
    }

    @Test
    fun ShouldSuccess_When_BundleHasData() = runTest {
        val scenario = launchFragmentInContainer<DetailFragment>(bundle, themeResId = R.style.AppTheme)
        scenario.recreate()

        Espresso.onView(withId(R.id.earthquake_item_magnitude)).check(
            matches(withText(containsString("$magnitude")))
        )

        Espresso.onView(withId(R.id.earthquake_item_depth)).check(
            matches(withText(containsString("${coordinates.last()}")))
        )

        Espresso.onView(withText(title)).check(matches(isDisplayed()))

        Espresso.onView(withText(place)).check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.map)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun ShouldHideMap_When_BundleHasNotCoordinateData() = runTest {
        val scenario = launchFragmentInContainer<DetailFragment>(bundleWithoutCoordinates, themeResId = R.style.AppTheme)
        scenario.recreate()

        Espresso.onView(withId(R.id.earthquake_item_magnitude)).check(
            matches(withText(containsString("$magnitude")))
        )

        Espresso.onView(withId(R.id.earthquake_item_depth)).check(
            matches(withText(containsString("${0.0}")))
        )

        Espresso.onView(withText(title)).check(matches(isDisplayed()))

        Espresso.onView(withText(place)).check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.map)).check(
            matches(
                not(isDisplayed())
            )
        )
    }

    @Test(expected = IllegalStateException::class)
    fun ShouldThrowException_When_BundleIsNotAdded() = runTest {
        val scenario = launchFragmentInContainer<DetailFragment>()
        scenario.recreate()
    }
}
package com.uniandes.abcjobs.ui

import android.os.SystemClock
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.abcjobs.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class CreatePerformanceEvaluationTest {
    @get:Rule
    var activityTestScenarioRule = ActivityScenarioRule(CreateEvaluationPerformanceActivity::class.java)

    @get:Rule
    var intentTestScenarioRule = IntentsTestRule(CreateEvaluationPerformanceActivity::class.java)

    @Before
    /*fun useAppContext() {
        // Context of the app under test.
        val appContext: Context = InstrumentationRegistry.getTargetContext()
        assertEquals("com.uniandes.abcjobs", appContext.getPackageName())
        SystemClock.sleep(1500);
    }*/

    @Test
    fun OnDisplayActivity() {
        onView(ViewMatchers.withText(R.string.LabelProyecto))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        onView(ViewMatchers.withText(R.string.LabelMiembro))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )

        onView(withText(R.string.LabelCalificacion)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withText(R.string.LabelObservacion)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        SystemClock.sleep(1500);
    }
}
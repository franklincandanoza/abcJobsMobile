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
class SearCandidateActivityTest {
    @get:Rule var activityTestScenarioRule = ActivityScenarioRule(SearchCandidatesActivity::class.java)

    @get:Rule var intentTestScenarioRule = IntentsTestRule(SearchCandidatesActivity::class.java)

    @Before
    /*fun useAppContext() {
        // Context of the app under test.
        val appContext: Context = InstrumentationRegistry.getTargetContext()
        assertEquals("com.uniandes.abcjobs", appContext.getPackageName())
        SystemClock.sleep(1500);
    }*/

    @Test
    fun OnDisplayActivity(){
        onView(ViewMatchers.withText(R.string.LabelProyecto))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        onView(ViewMatchers.withText(R.string.LabelPerfil))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )

        onView(withText(R.string.LabelTecnologia)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withText(R.string.LabelHabilidad)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withText(R.string.LabelFiltroRol)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.Scroll1)).perform(ViewActions.swipeUp())

        onView(withText(R.string.LabelBuscarRol)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withText(R.string.LabelExperienciaRol)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        SystemClock.sleep(1500);

        /*onView(withText(R.string.LabelFiltroTitulo)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


        onView(withText(R.string.LabelBuscarTitulo)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withText(R.string.LabelExperienciaTitulo)).perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        */

    }

}
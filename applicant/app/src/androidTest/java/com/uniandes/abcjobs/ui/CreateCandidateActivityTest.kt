package com.uniandes.abcjobs.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.abcjobs.R
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateCandidateActivityTest {
    @get:Rule
    var activityTestScenarioRule = ActivityScenarioRule(CreateCandidateActivity::class.java)

    @get:Rule
    var intentTestScenarioRule = IntentsTestRule(CreateCandidateActivity::class.java)
    @Test
    fun onCreateTest(){

        onView(ViewMatchers.withText(R.string.LabelCorreo))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        onView(ViewMatchers.withText(R.string.LabelPassword))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        onView(ViewMatchers.withText(R.string.LabelTipoDoc))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        onView(ViewMatchers.withText(R.string.LabelDocumento))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        onView(ViewMatchers.withText(R.string.LabelNombre))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        onView(ViewMatchers.withText(R.string.LabelApellido))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )

    }
}
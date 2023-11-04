package com.uniandes.abcjobs.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.abcjobs.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateCandidateTechnicalRolesInfoActivityTest {

    @get:Rule
    var activityTestScenarioRule = ActivityScenarioRule(CreateCandidateTechnicalRolesInfoActivity::class.java)

    @get:Rule
    var intentTestScenarioRule = IntentsTestRule(CreateCandidateTechnicalRolesInfoActivity::class.java)

    @Test
    fun createWithEmptyNameTest(){
        Espresso.onView(ViewMatchers.withId(R.id.name))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createCandidateTechnicalRoleButton)).perform(
            ViewActions.click())

        Thread.sleep(2000)

        var nameEditTest = Espresso.onView(ViewMatchers.withId(R.id.name))

        nameEditTest.check(ViewAssertions.matches(hasErrorText("Enter a correct name")))

    }

    @Test
    fun createWithEmptyDescriptionTest(){

        Espresso.onView(ViewMatchers.withId(R.id.name))
            .perform(ViewActions.typeText("Support"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.experience))
            .perform(ViewActions.typeText("1"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.description))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createCandidateTechnicalRoleButton)).perform(
            ViewActions.click())

        Thread.sleep(2000)

        var descriptionEditTest = Espresso.onView(ViewMatchers.withId(R.id.description))

        descriptionEditTest.check(ViewAssertions.matches(hasErrorText("Enter a description of maximum 500 characters")))

    }

    @Test
    fun createWithZeroExperienceTest(){

        Espresso.onView(ViewMatchers.withId(R.id.name))
            .perform(ViewActions.typeText("Support"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.experience))
            .perform(ViewActions.typeText("0"), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createCandidateTechnicalRoleButton)).perform(
            ViewActions.click())

        Thread.sleep(2000)

        var descriptionEditTest = Espresso.onView(ViewMatchers.withId(R.id.experience))

        descriptionEditTest.check(ViewAssertions.matches(hasErrorText("Enter a value greater than 0")))

    }
}
package com.uniandes.abcjobs.ui

import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.abcjobs.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateCandidateWorkingInfoActivityTest {

    @get:Rule
    var activityTestScenarioRule = ActivityScenarioRule(CreateCandidateWorkingInfoActivity::class.java)

    @get:Rule
    var intentTestScenarioRule = IntentsTestRule(CreateCandidateWorkingInfoActivity::class.java)

    @Test
    fun createWithEmptyPosition(){
        Espresso.onView(ViewMatchers.withId(R.id.workingInfoPosition))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createWorkingInfoAcceptButton)).perform(ViewActions.click())

        Thread.sleep(2000)

        var titleEditTest = Espresso.onView(ViewMatchers.withId(R.id.workingInfoPosition))

        titleEditTest.check(ViewAssertions.matches(hasErrorText("Enter a charge of up to 60 characters")))

    }

    @Test
    fun createWithEmptyCompany(){
        Espresso.onView(ViewMatchers.withId(R.id.workingInfoPosition))
            .perform(ViewActions.typeText("position"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.workingInfoCompany))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createWorkingInfoAcceptButton)).perform(ViewActions.click())

        Thread.sleep(2000)

        var titleEditTest = Espresso.onView(ViewMatchers.withId(R.id.workingInfoCompany))

        titleEditTest.check(ViewAssertions.matches(hasErrorText("Enter the company name of maximum 40 characters")))

    }

    @Test
    fun createWithEmptyAddress(){
        Espresso.onView(ViewMatchers.withId(R.id.workingInfoPosition))
            .perform(ViewActions.typeText("position"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.workingInfoCompany))
            .perform(ViewActions.typeText("company"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.workingInfoAddress))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())


        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createWorkingInfoAcceptButton)).perform(ViewActions.click())

        Thread.sleep(2000)

        var titleEditTest = Espresso.onView(ViewMatchers.withId(R.id.workingInfoAddress))

        titleEditTest.check(ViewAssertions.matches(hasErrorText("Enter the company address of maximum 60 characters")))

    }

    @Test
    fun createWithEmptyTelephone(){
        Espresso.onView(ViewMatchers.withId(R.id.workingInfoPosition))
            .perform(ViewActions.typeText("position"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.workingInfoCompany))
            .perform(ViewActions.typeText("company"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.workingInfoAddress))
            .perform(ViewActions.typeText("address"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.workingInfoTelephone))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())


        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createWorkingInfoAcceptButton)).perform(ViewActions.click())

        Thread.sleep(2000)

        var titleEditTest = Espresso.onView(ViewMatchers.withId(R.id.workingInfoTelephone))

        titleEditTest.check(ViewAssertions.matches(hasErrorText("Enter the company phone number")))

    }

}
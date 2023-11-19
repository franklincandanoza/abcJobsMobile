package com.uniandes.abcjobs.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
class RegisterTestResultActivityTest{

    @get:Rule var activityTestScenarioRule = ActivityScenarioRule(RegisterTestResultActivity::class.java)

    @get:Rule var intentTestScenarioRule = IntentsTestRule(RegisterTestResultActivity::class.java)


    @Test
    fun registerResultTest(){
        onView(withId(R.id.document)).perform(typeText("140523496"), closeSoftKeyboard())

        onView(withId(R.id.editTextPoints)).perform(typeText("50"), closeSoftKeyboard())

        onView(withId(R.id.editObservation)).perform(typeText("Observation"), closeSoftKeyboard())

        Thread.sleep(2000)
        onView(withId(R.id.BotonAceptar)).perform(click())

        Thread.sleep(2000)
        onView(withId(R.id.registerTestLabel)).check(matches(isDisplayed()));
    }

    @Test
    fun registerResultInvalidPointsTest(){
        onView(withId(R.id.document)).perform(typeText("140523496"), closeSoftKeyboard())

        onView(withId(R.id.editTextPoints)).perform(typeText("150"), closeSoftKeyboard())

        onView(withId(R.id.editObservation)).perform(typeText("Observation"), closeSoftKeyboard())

        Thread.sleep(2000)
        onView(withId(R.id.BotonAceptar)).perform(click())

        Thread.sleep(1000)
        onView(withId(R.id.editTextPoints)).check(matches(isDisplayed()));


    }


    @Test
    fun fieldTests(){

        onView(withText(R.string.LabelTest))
            .check(
                matches(
                    isDisplayed()
                )
            )
        onView(withText(R.string.LabelTechnology))
            .check(
                matches(
                    isDisplayed()
                )
            )

        onView(withText(R.string.LabelTestDate))
            .check(
                matches(
                    isDisplayed()
                )
            )

    }

    @Test
    fun cancelRegisterActivity(){

        onView(withId(R.id.BotonCancelar)).perform(click())

        Thread.sleep(2000)

        onView(withText(R.string.yes)).perform(click())
        Thread.sleep(2000)
        intended(hasComponent(CompanyOptionsActivity::class.java.name))

    }

    @Test
    fun cancelRegisterAvoidActivity(){

        onView(withId(R.id.BotonCancelar)).perform(click())

        Thread.sleep(2000)

        onView(withText(R.string.no)).perform(click())
        Thread.sleep(1000)


        onView(withId(R.id.editTextPoints))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }
}
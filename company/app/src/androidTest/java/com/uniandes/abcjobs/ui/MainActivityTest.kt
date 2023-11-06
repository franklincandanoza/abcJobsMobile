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
class MainActivityTest{

    @get:Rule var activityTestScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule var intentTestScenarioRule = IntentsTestRule(MainActivity::class.java)


    @Test
    fun loginEmptyUserTest(){
        onView(withId(R.id.editTextUser)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password"), closeSoftKeyboard())

        Thread.sleep(2000)
        onView(withId(R.id.BotonAceptar)).perform(click())

        Thread.sleep(2000)
        onView(withId(R.id.editTextUser)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()));
    }

    @Test
    fun loginEmptyPassword(){
        onView(withId(R.id.editTextUser)).perform(typeText("user"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText(""), closeSoftKeyboard())

        Thread.sleep(2000)
        onView(withId(R.id.BotonAceptar)).perform(click())

        Thread.sleep(2000)
        onView(withId(R.id.editTextUser)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()));
    }

    @Test
    fun loginFields(){

        onView(withText(R.string.LabelCorreo))
            .check(
                matches(
                    isDisplayed()
                )
            )
        onView(withText(R.string.LabelPassword))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }

    @Test
    fun loginOpenRegisterActivity(){

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(2000)

        intended(hasComponent(CreateCandidateActivity::class.java.name))

    }
}
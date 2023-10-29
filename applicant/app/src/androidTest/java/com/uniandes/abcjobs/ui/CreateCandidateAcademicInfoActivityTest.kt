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
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.hamcrest.Description
import org.junit.runner.RunWith
import org.hamcrest.Matcher


@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateCandidateAcademicInfoActivityTest {

    @get:Rule
    var activityTestScenarioRule = ActivityScenarioRule(CreateCandidateAcademicInfoActivity::class.java)

    @get:Rule
    var intentTestScenarioRule = IntentsTestRule(CreateCandidateAcademicInfoActivity::class.java)

    @Test
    fun createWithEmptyTitleTest(){
        Espresso.onView(ViewMatchers.withId(R.id.academicInfoTitle))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createAcademicInfoAcceptButton)).perform(ViewActions.click())

        Thread.sleep(2000)

        var titleEditTest = Espresso.onView(ViewMatchers.withId(R.id.academicInfoTitle))

        titleEditTest.check(ViewAssertions.matches(hasErrorText("Enter a title of maximum 60 characters")))

    }

    @Test
    fun createWithEmptyInstitutionTest(){
        Espresso.onView(ViewMatchers.withId(R.id.academicInfoTitle))
            .perform(ViewActions.typeText("title"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.academicInfoInstitution))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createAcademicInfoAcceptButton)).perform(ViewActions.click())

        Thread.sleep(2000)

        var titleEditTest = Espresso.onView(ViewMatchers.withId(R.id.academicInfoInstitution))

        titleEditTest.check(ViewAssertions.matches(hasErrorText("Enter an institution of maximum 60 characters")))

    }

    @Test
    fun createWithEmptyDescriptionTest(){
        Espresso.onView(ViewMatchers.withId(R.id.academicInfoTitle))
            .perform(ViewActions.typeText("title"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.academicInfoInstitution))
            .perform(ViewActions.typeText("institution"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.description))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.createAcademicInfoAcceptButton)).perform(ViewActions.click())

        Thread.sleep(2000)

        var titleEditTest = Espresso.onView(ViewMatchers.withId(R.id.description))

        titleEditTest.check(ViewAssertions.matches(hasErrorText("Enter an description of maximum 500 characters")))

    }

    @Test
    fun createWithAllFieldsTest(){

        Espresso.onView(ViewMatchers.withId(R.id.academicInfoTitle))
            .perform(ViewActions.typeText("title"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.academicInfoInstitution))
            .perform(ViewActions.typeText("institution"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.description))
            .perform(ViewActions.typeText("Description"), ViewActions.closeSoftKeyboard())

        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.createAcademicInfoAcceptButton)).perform(ViewActions.click())

    }

}

fun hasErrorText(expectedText: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View): Boolean {
            if (view is EditText) {
                val error = view.error
                return error != null && error.toString() == expectedText
            }
            return false
        }

        override fun describeTo(description: Description) {
            description.appendText("has error text: $expectedText")
        }
    }
}
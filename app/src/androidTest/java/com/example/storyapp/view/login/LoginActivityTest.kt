package com.example.storyapp.view.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.storyapp.R
import com.example.storyapp.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testIfLoginSuccess() {
        Espresso.onView(withId(R.id.ed_login_email))
            .perform(ViewActions.typeText("bravo@gmail.com"))
        Espresso.onView(withId(R.id.ed_login_password))
            .perform(ViewActions.typeText("bravo0123"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btn_login_page))
            .perform(ViewActions.click())
        Espresso.onView(withText(R.string.continue_dialog))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.action_logout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
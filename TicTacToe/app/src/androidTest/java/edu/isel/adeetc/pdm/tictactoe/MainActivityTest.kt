package edu.isel.adeetc.pdm.tictactoe


import android.app.UiAutomation.ROTATION_FREEZE_90
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import edu.isel.adeetc.pdm.tictactoe.game.MainActivity
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun whenGameIsStarted_buttonsEnabledStateIsCorrect() {

        onView(
            allOf(withId(R.id.startButton), withText("Start"))
        ).perform(click())

        assertThat(activityTestRule.activity.game.state, `is`(Game.State.STARTED))

        onView(withId(R.id.startButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.forfeitButton)).check(matches(isEnabled()))
    }

    @Test
    fun whenGameIsForfeited_buttonsEnabledStateIsCorrect() {

        onView(
            allOf(withId(R.id.startButton), withText("Start"))
        ).perform(click())

        onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.board),
                    0
                ),
                0
            )
        ).perform(click())

        onView(withId(R.id.forfeitButton)).perform(click())

        assertThat(activityTestRule.activity.game.state, `is`(Game.State.FINISHED))
        assertThat(activityTestRule.activity.game.getMoveAt(0,0), notNullValue())

        onView(withId(R.id.startButton)).check(matches(isEnabled()))
        onView(withId(R.id.forfeitButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun whenScreenIsRotated_uiStateIsCorrect() {

        onView(
            allOf(withId(R.id.startButton), withText("Start"))
        ).perform(click())

        onView(withId(R.id.startButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.forfeitButton)).check(matches(isEnabled()))

        onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.board),
                    0
                ),
                0
            )
        ).perform(click())

        InstrumentationRegistry.getInstrumentation().uiAutomation.setRotation(ROTATION_FREEZE_90)

        assertThat(activityTestRule.activity.game.state, `is`(Game.State.STARTED))
        assertThat(activityTestRule.activity.game.getMoveAt(0,0), notNullValue())

        onView(withId(R.id.startButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.forfeitButton)).check(matches(isEnabled()))


    }

}

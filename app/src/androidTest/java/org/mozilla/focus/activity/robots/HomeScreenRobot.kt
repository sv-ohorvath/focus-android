package org.mozilla.focus.activity.robots

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.mozilla.focus.R
import org.mozilla.focus.helpers.TestHelper
import org.mozilla.focus.helpers.TestHelper.appName
import org.mozilla.focus.helpers.TestHelper.mDevice
import org.mozilla.focus.helpers.TestHelper.waitingTime

class HomeScreenRobot {

    class Transition {
        fun openMainMenu(interact: ThreeDotMainMenuRobot.() -> Unit): ThreeDotMainMenuRobot.Transition {
            searchBar.waitForExists(waitingTime)
            mainMenu
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click())

            ThreeDotMainMenuRobot().interact()
            return ThreeDotMainMenuRobot.Transition()
        }
    }
}

fun homeScreen(interact: HomeScreenRobot.() -> Unit): HomeScreenRobot.Transition {
    HomeScreenRobot().interact()
    return HomeScreenRobot.Transition()
}

private val searchBar = mDevice.findObject(UiSelector().resourceId("$appName:id/urlView"))

private val mainMenu = onView(ViewMatchers.withId(R.id.menuView))
package org.mozilla.focus.activity.robots

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.mozilla.focus.R
import org.mozilla.focus.helpers.TestHelper
import org.mozilla.focus.helpers.TestHelper.appName
import org.mozilla.focus.helpers.TestHelper.waitingTime

class SettingsRobot {

    class Transition {
        fun openSearchSettingsMenu(interact: SearchSettingsRobot.() -> Unit): SearchSettingsRobot.Transition {
            searchSettingsMenu.waitForExists(waitingTime)
            searchSettingsMenu.click()

            SearchSettingsRobot().interact()
            return SearchSettingsRobot.Transition()
        }
    }
}

private val settingsMenuList = UiScrollable(UiSelector().resourceId("$appName:id/recycler_view"))

private val searchSettingsMenu = settingsMenuList.getChild(
    UiSelector()
        .resourceId("android:id/title")
        .text("Search")
        .enabled(true)
)
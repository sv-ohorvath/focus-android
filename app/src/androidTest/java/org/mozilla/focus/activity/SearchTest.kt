/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.focus.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.mozilla.focus.R
import org.mozilla.focus.activity.robots.browserScreen
import org.mozilla.focus.activity.robots.homeScreen
import org.mozilla.focus.activity.robots.searchScreen
import org.mozilla.focus.helpers.EspressoHelper.childAtPosition
import org.mozilla.focus.helpers.MainActivityFirstrunTestRule
import org.mozilla.focus.helpers.TestHelper.pressBackKey
import org.mozilla.focus.helpers.TestHelper.pressEnterKey
import org.mozilla.focus.helpers.TestHelper.webPageLoadwaitingTime

// This test checks the search engine can be changed and that search suggestions appear
class SearchTest {
    private val enginesList = listOf("Google", "DuckDuckGo", "Amazon.com", "Wikipedia")

    @get: Rule
    var mActivityTestRule = MainActivityFirstrunTestRule(showFirstRun = false)

    @Test
    fun changeSearchEngineTest() {

        for (searchEngine in enginesList) {
            // Open [settings menu] and select Search engine
            homeScreen {
            }.openMainMenu {
            }.openSettings {
            }.openSearchSettingsMenu {
                openSearchEngineSubMenu()
                selectSearchEngine(searchEngine)
            }
            // press back 3 times to exit to HomeScreen
            pressBackKey()
            pressBackKey()
            pressBackKey()

            searchScreen {
                typeInSearchBar("mozilla ")
                pressEnterKey()
            }

            browserScreen {
                verifyPageURL(searchEngine)
            }.clearBrowsingData { }
        }
    }


    @Test
    fun enableSearchSuggestionOnFirstRunTest() {
        val searchString = "mozilla "

        searchScreen {
            // Search on blank spaces should not do anything
            typeInSearchBar(" ")
            pressEnterKey()
            verifySearchBarIsDisplayed()
            typeInSearchBar(searchString)
            allowEnableSearchSuggestions()
            verifyHintForSearch(searchString)
            verifySearchSuggestionsAreShown()
        }
        // we expect min=1, max=5 suggestions
        var count = 0
        val maxCount = 3
        while (count < maxCount) {
            Espresso.onView(
                Matchers.allOf(
                    ViewMatchers.withText(Matchers.containsString(searchString)),
                    ViewMatchers.withId(R.id.suggestion),
                    ViewMatchers.isDescendantOfA(
                        childAtPosition(
                            ViewMatchers.withId(R.id.suggestionList),
                            count
                        )
                    )
                )
            )
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            count++
        }
    }

    @Test
    fun testSearchBarShowsSearchTermOnEdit() {
        val searchString = "mozilla focus"

        searchScreen {
            typeInSearchBar(searchString)
            pressEnterKey()
        }
        browserScreen {
            verifyPageContent(searchString)
            progressBar.waitUntilGone(webPageLoadwaitingTime)
        }.openSearchBar {
            // Tap URL bar, check it displays search term (instead of URL)
            verifySearchEditBarContainsTest(searchString)
        }
    }

    @Test
    fun disableSearchSuggestionsTest() {
        val searchString = "mozilla "

        searchScreen {
            // Search on blank spaces should not do anything
            verifySearchBarIsDisplayed()
            typeInSearchBar(searchString)
            clearSearchBar()
        }
        homeScreen {
        }.openMainMenu {
        }.openSettings {
        }.openSearchSettingsMenu {
            clickSearchSuggestionsSwitch()
        }
        pressBackKey()
        pressBackKey()
        searchScreen {
            typeInSearchBar(searchString)
            verifySearchSuggestionsAreNotShown()
        }
    }
}
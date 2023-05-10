package com.assignment.githubusersearch.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.assignment.githubusersearch.R
import com.assignment.githubusersearch.SearchActivity
import com.assignment.githubusersearch.repository.GithubRepository
import com.assignment.githubusersearch.ui.theme.GithubUserSearchTheme
import com.assignment.githubusersearch.ui.view.MainScreen
import com.assignment.githubusersearch.viewmodel.RepoListViewModel
import com.assignment.githubusersearch.viewmodel.UserViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainScreenTest {
    @get:Rule(order = 0)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<SearchActivity>()

    @Inject
    lateinit var repository: GithubRepository

    @Before
    fun setUp() {
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            GithubUserSearchTheme {
                MainScreen(
                    userViewModel = UserViewModel(repository = repository),
                    repoListViewModel = RepoListViewModel(repository = repository)
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testInitialState() = runTest {
        // Assert that the initial state is correct
        composeTestRule.onNodeWithText("Enter a github user id")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Search")
            .assertIsDisplayed()
    }

    @Test
    fun testSearchButtonClicked_WithEmptyUserId() {
        // Assert that a Toast message is shown when the search button is clicked with an empty user id
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val toastMessage = context.getString(R.string.please_enter_a_github_user_id)

        composeTestRule.onNodeWithText("Search")
            .performClick()
        composeTestRule.onNodeWithContentDescription(toastMessage)
            .assertIsDisplayed()
    }

    @Test
    fun testSearchButtonClicked_WithValidUserId() {
        val userViewModel = mock(UserViewModel::class.java)
        val repoListViewModel = mock(RepoListViewModel::class.java)

        val userId = "testuser"
        val searchButton =
            composeTestRule.onNodeWithText("Search")
        val userIdTextField =
            composeTestRule.onNodeWithText("Enter a github user id")

        userIdTextField.performTextInput(userId)
        searchButton.performClick()

        verify(userViewModel).getUser(userId)
        verify(repoListViewModel).getRepoList(userId)
    }
}


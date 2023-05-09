package com.assignment.githubusersearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.assignment.githubusersearch.models.User
import com.assignment.githubusersearch.network.GithubApi
import com.assignment.githubusersearch.repository.GithubRepository
import com.assignment.githubusersearch.util.MessageType
import com.assignment.githubusersearch.viewmodel.UserUiState
import com.assignment.githubusersearch.viewmodel.UserViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var api: GithubApi
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi
    private lateinit var userViewModel: UserViewModel
    private lateinit var githubRepository: GithubRepository

    @Mock
    private lateinit var observer: Observer<UserUiState>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()

        api = retrofit.create(GithubApi::class.java)
        githubRepository = GithubRepository(api)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        userViewModel = UserViewModel(githubRepository)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getUser with valid user id should update userUiState with success state`() {
        // Create a CountDownLatch with a count of 1
        val latch = CountDownLatch(1)
        // Mock response
        val user = User("John", "https://avatars.githubusercontent.com/u/296614?v=4")
        val mockResponse = MockResponse().setBody(moshi.adapter(User::class.java).toJson(user))
        mockWebServer.enqueue(mockResponse)

        // Set up the observer to wait for the success state
        userViewModel.uiState.observeForever(observer)
        `when`(observer.onChanged(UserUiState.Success(user))).thenAnswer { latch.countDown() }

        // Call getUser with a valid user id
        userViewModel.getUser("johnsmith")

        // Wait for the observer to be triggered or time out after 5 seconds
        assertThat(latch.await(5, TimeUnit.SECONDS), equalTo(true))

        // Clean up the observer
        userViewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `getUser with invalid user id should update userUiState with error state`() {
        // Create a CountDownLatch with a count of 1
        val latch = CountDownLatch(1)
        // Mock response
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        // Set up the observer to wait for the error state
        userViewModel.uiState.observeForever(observer)
        doAnswer {
            val state = it.arguments[0] as UserUiState
            if (state is UserUiState.Error && state.message == MessageType.NoUserFound) {
                latch.countDown()
            }
            null
        }.`when`(observer).onChanged(any(UserUiState::class.java))

        // Call getUser with an invalid user id
        userViewModel.getUser("invaliduser234738e47")

        // Wait for the observer to be triggered or time out after 5 seconds
        assertThat(latch.await(5, TimeUnit.SECONDS), equalTo(true))

        // Clean up the observer
        userViewModel.uiState.removeObserver(observer)
    }
}


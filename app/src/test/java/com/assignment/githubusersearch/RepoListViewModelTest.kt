package com.assignment.githubusersearch
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.assignment.githubusersearch.models.Repository
import com.assignment.githubusersearch.network.GithubApi
import com.assignment.githubusersearch.repository.GithubRepository
import com.assignment.githubusersearch.viewmodel.RepoListViewModel
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
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class RepoListViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi
    private lateinit var viewModel: RepoListViewModel
    private lateinit var githubRepository: GithubRepository
    private lateinit var api: GithubApi

    @Mock
    private lateinit var observerRepoList: Observer<List<Repository>>

    @Mock
    private lateinit var observerError: Observer<String>

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
        viewModel = RepoListViewModel(githubRepository)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getRepoList with valid user id should update repoList with success state`() {
        // Create a CountDownLatch with a count of 1
        val latch = CountDownLatch(1)
        // Mock response
        val repoList = listOf(Repository("Repo1", "https://github.com/owner/repo1", "Description1"),
            Repository("Repo2", "https://github.com/owner/repo2", "Description2"))

        val mockResponse = MockResponse().setBody(moshi.adapter(List::class.java).toJson(repoList))
        mockWebServer.enqueue(mockResponse)

        // Set up the observers to wait for the success state
        viewModel.repoList.observeForever(observerRepoList)
        viewModel.error.observeForever(observerError)

        `when`(observerRepoList.onChanged(repoList)).thenAnswer { latch.countDown() }

        // Call getRepoList with a valid user id
        viewModel.getRepoList("johnsmith")

        // Wait for the observer to be triggered or time out after 5 seconds
        assertThat(latch.await(5, TimeUnit.SECONDS), equalTo(true))

        // Clean up the observers
        viewModel.repoList.removeObserver(observerRepoList)
        viewModel.error.removeObserver(observerError)
    }

    @Test
    fun `getRepoList with invalid user id should update error with error state`() {
        // Create a CountDownLatch with a count of 1
        val latch = CountDownLatch(1)
        // Mock response
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        // Set up the observers to wait for the error state
        viewModel.repoList.observeForever(observerRepoList)
        viewModel.error.observeForever(observerError)

        `when`(observerError.onChanged("No results found!")).thenAnswer { latch.countDown() }

        // Call getRepoList with an invalid user id
        viewModel.getRepoList("invaliduser234738e47")

        // Wait for the observer to be triggered or time out after 5 seconds
        assertThat(latch.await(5, TimeUnit.SECONDS), equalTo(true))

        // Clean up the observers
        viewModel.repoList.removeObserver(observerRepoList)
        viewModel.error.removeObserver(observerError)
    }

}
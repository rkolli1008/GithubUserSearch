package com.assignment.githubusersearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.assignment.githubusersearch.ui.theme.GithubUserSearchTheme
import com.assignment.githubusersearch.ui.view.MainScreen
import com.assignment.githubusersearch.viewmodel.RepoListViewModel
import com.assignment.githubusersearch.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val repoListViewModel: RepoListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubUserSearchTheme {
                MainScreen(userViewModel, repoListViewModel)
            }
        }
    }
}


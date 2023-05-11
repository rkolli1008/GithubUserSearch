package com.assignment.githubusersearch.repository

import androidx.lifecycle.MutableLiveData
import com.assignment.githubusersearch.models.Repository
import com.assignment.githubusersearch.network.GithubApi
import com.assignment.githubusersearch.util.MessageType
import com.assignment.githubusersearch.viewmodel.UserState
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApi: GithubApi) {
    suspend fun getUser(userId: String): MutableLiveData<UserState> {
        val data = MutableLiveData<UserState>()
        githubApi.getUser(userId).body()?.let {
            data.value = UserState.Success(it)
        } ?: run {
            data.value = UserState.Error(MessageType.NoUserFound)
        }
        return data
    }

    suspend fun getUserRepositories(userId: String): MutableLiveData<List<Repository>?> {
        val data = MutableLiveData<List<Repository>?>()
        val response = githubApi.getUserRepositories(userId).body()
        if (response?.isNotEmpty() == true) {
            data.value = response
        } else {
            data.value = null
        }
        return data
    }
}

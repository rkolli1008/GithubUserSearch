package com.assignment.githubusersearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.githubusersearch.models.Repository
import com.assignment.githubusersearch.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val repository: GithubRepository) :
    ViewModel() {
    private val _repoList = MutableLiveData<List<Repository>>()
    val repoList: LiveData<List<Repository>>
        get() = _repoList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getRepoList(userId: String) = viewModelScope.launch {
        try {
            val data = repository.getUserRepositories(userId)
            _repoList.value = data.value
            _repoList.postValue(data.value)
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}
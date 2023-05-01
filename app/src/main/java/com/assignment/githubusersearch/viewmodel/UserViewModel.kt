package com.assignment.githubusersearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.githubusersearch.models.User
import com.assignment.githubusersearch.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
    private val _uiState = MutableLiveData<UserUiState>()
    val uiState: LiveData<UserUiState>
        get() = _uiState

    fun getUser(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.postValue(UserUiState.Loading)
                repository.getUser(userId).observeForever {
                    if (it != null) {
                        _uiState.postValue(UserUiState.Success(it))
                    } else {
                        _uiState.postValue(UserUiState.Error("User not found."))
                    }
                }
            } catch (e: Exception) {
                _uiState.postValue(UserUiState.Error(e.message ?: "Unknown error occurred."))
            }
        }
    }
}
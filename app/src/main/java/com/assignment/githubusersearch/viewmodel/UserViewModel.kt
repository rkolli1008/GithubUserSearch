package com.assignment.githubusersearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.githubusersearch.models.User
import com.assignment.githubusersearch.repository.GithubRepository
import com.assignment.githubusersearch.util.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: MessageType) : UserUiState()
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
                    _uiState.postValue(it)
                }
            } catch (e: Exception) {
                _uiState.postValue(UserUiState.Error(MessageType.ErrorGettingData))
            }
        }
    }
}
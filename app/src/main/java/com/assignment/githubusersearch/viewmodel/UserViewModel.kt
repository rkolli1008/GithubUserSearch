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

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: MessageType) : UserState()
}

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
    private val _uiState = MutableLiveData<UserState>()
    val uiState: LiveData<UserState>
        get() = _uiState

    fun getUser(userId: String) = viewModelScope.launch {
        try {
            _uiState.postValue(UserState.Loading)
            val data = repository.getUser(userId)
            _uiState.postValue(data.value)
        } catch (e: Exception) {
            _uiState.postValue(UserState.Error(MessageType.ErrorGettingData))
        }
    }
}
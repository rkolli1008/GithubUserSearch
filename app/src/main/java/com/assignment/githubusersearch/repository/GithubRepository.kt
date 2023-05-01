package com.assignment.githubusersearch.repository

import androidx.lifecycle.MutableLiveData
import com.assignment.githubusersearch.models.Repository
import com.assignment.githubusersearch.models.User
import com.assignment.githubusersearch.network.GithubApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApi: GithubApi) {
    fun getUser(userId: String): MutableLiveData<User?> {
        val data = MutableLiveData<User?>()
        githubApi.getUser(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    data.value = null
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle error
                data.value = null
            }
        })
        return data
    }

    fun getUserRepositories(userId: String): MutableLiveData<List<Repository>?> {
        val data = MutableLiveData<List<Repository>?>()
        githubApi.getUserRepositories(userId).enqueue(object : Callback<List<Repository>> {
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    data.value = null
                }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                // Handle error
                data.value = null
            }
        })
        return data
    }
}

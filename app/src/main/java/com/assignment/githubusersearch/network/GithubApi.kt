package com.assignment.githubusersearch.network

import com.assignment.githubusersearch.models.Repository
import com.assignment.githubusersearch.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface GithubApi {
    @GET("users/{userId}")
    fun getUser(@Path("userId") userId: String): Call<User>

    @GET("users/{userId}/repos")
    fun getUserRepositories(@Path("userId") userId: String): Call<List<Repository>>
}



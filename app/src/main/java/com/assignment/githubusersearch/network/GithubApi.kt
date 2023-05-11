package com.assignment.githubusersearch.network

import com.assignment.githubusersearch.models.Repository
import com.assignment.githubusersearch.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface GithubApi {
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<User>

    @GET("users/{userId}/repos")
    suspend fun getUserRepositories(@Path("userId") userId: String): Response<List<Repository>>
}



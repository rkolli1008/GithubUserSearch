package com.assignment.githubusersearch.models

import com.squareup.moshi.Json

data class User(
    val name: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)

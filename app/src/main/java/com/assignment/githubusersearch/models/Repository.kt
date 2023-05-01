package com.assignment.githubusersearch.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    val name: String? = "",
    val description: String? = "",
    @Json(name = "updated_at")
    val updatedAt: String? = "",
    @Json(name = "stargazers_count")
    val stargazersCount: Int? = 0,
    val forks: Int? = 0
): Parcelable

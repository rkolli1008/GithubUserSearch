package com.assignment.githubusersearch.util

enum class MessageType(val message: String) {
    DataNotAvailable("Data not available"),
    GitUserIdEmpty("User ID field is empty"),
    NoUserFound("No user found"),
    ErrorGettingData("Error getting data"),
    NoReposFound("No repositories found"),
    ForbiddenError("API rate limit exceeded")
}
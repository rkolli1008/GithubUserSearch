package com.assignment.githubusersearch.util

enum class MessageType(val message: String) {
    DataNotAvailable("Data not available"),
    GitUserIdEmpty("Please enter a Github User Id"),
    NoUserFound("No user found"),
    ErrorGettingData("Error getting data"),
    NoReposFound("No repositories found"),
    ForbiddenError("API rate limit exceeded")
}
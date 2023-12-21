package com.atwa.androidgpstask.core.network

sealed class NetworkError(
    val title: String,
    val messageBody: String,
    val isRetryEnabled: Boolean = false,
    override val message:String = title,
    val code: Int = -1,
) : Throwable() {

    data object NoConnection : NetworkError(
        "No network connection",
        "check your mobile data or WI-FI",
        true
    )

    data object NoResults : NetworkError(
        "No results found",
        "Please try again later",
        true
    )

    data object Cancellation : NetworkError(
       "Something went wrong",
        "Error Occurred"
    )

    data object UnExpected : NetworkError(
        "Something went wrong",
        "Unexpected Error Happened"
    )

    data object TimeOut : NetworkError(
        "Server cannot be reached",
        "Please try again later"
    )

    data object Parsing : NetworkError(
        "Server Error",
        "Unexpected Error Happened"
    )

    data object UnAuthorized : NetworkError(
        "UnAuthorized Access",
        "UnAuthorized Access"
    )

    data class ServerError(val errorMsg: String, val errorCode: Int = -1) :
        NetworkError("Server Error", errorMsg, code = errorCode)
}
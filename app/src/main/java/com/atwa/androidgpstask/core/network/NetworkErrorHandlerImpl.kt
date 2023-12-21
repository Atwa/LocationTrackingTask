package com.atwa.androidgpstask.core.network

import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlin.coroutines.cancellation.CancellationException

class NetworkErrorHandlerImpl : NetworkErrorHandler {

    override fun <T : Any> resolveApiError(response: Response<T>?): Result<T> {
        var errorCode: Int? = -1
        val message = try {
            errorCode = response?.code()
            response?.errorBody().toString()
        } catch (e: Exception) {
            e.message
        }
        val errorMessage = if (message.isNullOrEmpty()) "UnKnown error occurred !" else message
        return Result.failure(NetworkError.ServerError(errorMessage, errorCode ?: -1))
    }

    override fun <T : Any> resolveNetworkError(e: Throwable): Result<T> {
        val message = when (e) {
            is CancellationException -> NetworkError.Cancellation
            is SocketTimeoutException -> NetworkError.TimeOut
            is NoConnectionException, is UnknownHostException, is ConnectException, is SSLException -> NetworkError.NoConnection
            is IOException -> NetworkError.UnExpected
            else -> NetworkError.UnExpected
        }
        return Result.failure(message)
    }

    override fun <T : Any> resolveHttpError(errorBody: ResponseBody?, code: Int): Result<T> {
        return Result.failure(NetworkError.ServerError(errorBody.toString()))
    }
}
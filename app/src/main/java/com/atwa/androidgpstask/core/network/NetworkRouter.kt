package com.atwa.androidgpstask.core.network

import retrofit2.HttpException

object NetworkRouter {

    private val networkErrorHandler: NetworkErrorHandler by lazy { NetworkErrorHandlerImpl() }

    suspend fun <R : Any> invokeCall(call: suspend () -> R): Result<R> {
        return try {
            val response = call.invoke()
            if (isSuccessResponse(response)) Result.success(response)
            else Result.failure(NetworkError.UnExpected)
        } catch (exception: HttpException) {
            networkErrorHandler.resolveHttpError(
                exception.response()?.errorBody(),
                exception.code()
            )
        } catch (exception: Throwable) {
            networkErrorHandler.resolveNetworkError(exception)
        }
    }

    private fun <R> isSuccessResponse(response: R?): Boolean {
        return response != null
    }

}
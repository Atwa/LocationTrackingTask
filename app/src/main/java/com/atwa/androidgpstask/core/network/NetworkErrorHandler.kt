package com.atwa.androidgpstask.core.network

import okhttp3.ResponseBody
import retrofit2.Response

interface NetworkErrorHandler {
    fun <T : Any> resolveApiError(response: Response<T>?): Result<T>
    fun <T:Any> resolveHttpError(errorBody: ResponseBody?, code: Int): Result<T>
    fun <T:Any> resolveNetworkError(e: Throwable): Result<T>
}
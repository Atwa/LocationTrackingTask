package com.atwa.androidgpstask.core.network

import com.atwa.androidgpstask.BuildConfig
import com.atwa.androidgpstask.core.network.NetworkConstants.API_KEY
import com.atwa.androidgpstask.core.network.NetworkConstants.API_KEY_VALUE
import com.atwa.androidgpstask.core.network.NetworkConstants.CONTENT_TYPE_KEY
import com.atwa.androidgpstask.core.network.NetworkConstants.CONTENT_TYPE_VALUE
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request().newBuilder()
            .addHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE)
            .addHeader(API_KEY, BuildConfig.API_KEY)
            .build()
    )
}
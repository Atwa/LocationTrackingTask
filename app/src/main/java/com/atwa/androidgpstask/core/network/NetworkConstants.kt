package com.atwa.androidgpstask.core.network

import com.atwa.androidgpstask.BuildConfig

object NetworkConstants {
    const val BASE_URL_KEY = "BaseUrl"
    const val CONTENT_TYPE_KEY = "Content-Type"
    const val CONTENT_TYPE_VALUE = "application/json"
    const val API_KEY = "apiKey"
    const val WRITE_TIMEOUT = 50L
    const val READ_TIMEOUT = 30L
    const val CONNECT_TIMEOUT = 20L
    val API_KEY_VALUE: String? = System.getenv("LOCATION_TEST_API_KEY")
}
package com.atwa.androidgpstask.core.di

import com.atwa.androidgpstask.core.network.NetworkConnectionInterceptor
import com.atwa.androidgpstask.core.network.NetworkConstants.BASE_URL_KEY
import com.atwa.androidgpstask.core.network.NetworkConstants.CONNECT_TIMEOUT
import com.atwa.androidgpstask.core.network.NetworkConstants.READ_TIMEOUT
import com.atwa.androidgpstask.core.network.NetworkConstants.WRITE_TIMEOUT
import com.atwa.androidgpstask.core.network.NetworkHeaderInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideGsonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder().create()
        )
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkHeaderInterceptor: NetworkHeaderInterceptor,
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(networkHeaderInterceptor)
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        @Named(BASE_URL_KEY) baseUrl: String,
        okHttpClient: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient.build())
            .baseUrl(baseUrl)
            .build()
    }

}
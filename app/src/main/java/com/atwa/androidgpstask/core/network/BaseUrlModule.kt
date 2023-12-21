package com.atwa.androidgpstask.core.network

import com.atwa.androidgpstask.BuildConfig
import com.atwa.androidgpstask.core.network.NetworkConstants.BASE_URL_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BaseUrlModule {
    @Provides
    @Singleton
    @Named(BASE_URL_KEY)
    fun provideBaseUrl() = BuildConfig.BASE_URL
}
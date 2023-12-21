package com.atwa.androidgpstask.feature.tracking.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TrackingApiModule {
    @Provides
    @Singleton
    internal fun provideTrackingApi(retrofit: Retrofit): TrackingApi {
        return retrofit.create(TrackingApi::class.java)
    }
}
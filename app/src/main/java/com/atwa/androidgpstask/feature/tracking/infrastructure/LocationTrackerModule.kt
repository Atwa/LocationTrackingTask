package com.atwa.androidgpstask.feature.tracking.infrastructure

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class LocationTrackerModule {
    @Binds
    @Singleton
    abstract fun bindLocationTracker(locationTrackerImpl: LocationTrackerImpl): LocationTracker

    @Binds
    @Singleton
    abstract fun bindLocationMockDetector(detectorImpl: MockLocationDetectorImpl): MockLocationDetector


}
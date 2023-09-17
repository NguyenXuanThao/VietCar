package com.example.vietcar.di.module

import android.app.Application
import com.example.vietcar.data.db.CarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun providerDatabase(application: Application): CarDatabase =
        CarDatabase.getInstance(application)

    @Singleton
    @Provides
    fun providerNewsFavoriteDao(database: CarDatabase) =
        database.getCarDao()
}
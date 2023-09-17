package com.example.vietcar.di.module

import com.example.vietcar.data.api.CarApi
import com.example.vietcar.data.api.LocationApi
import com.example.vietcar.data.db.CarDao
import com.example.vietcar.di.repository.CarRepository
import com.example.vietcar.di.repository.ICarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        carApi: CarApi,
        locationApi: LocationApi,
        carDao: CarDao
    ): ICarRepository {
        return CarRepository(carApi, locationApi, carDao)
    }
}
package com.example.vietcar.di.module

import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.api.CarApi
import com.example.vietcar.data.api.LocationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOKHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideGson(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @Named("viet_car")
    fun provideRetrofit(
        converterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(DataLocal.BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideCarApi(@Named("viet_car") retrofit: Retrofit): CarApi {
        return retrofit.create(CarApi::class.java)
    }

    @Provides
    @Singleton
    @Named("location")
    fun provideLocationRetrofit(
        converterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://vn-public-apis.fpo.vn/")
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideLocationApi(@Named("location") retrofit: Retrofit): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }
}
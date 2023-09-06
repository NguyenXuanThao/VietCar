package com.example.vietcar.data.api

import com.example.vietcar.data.model.location.city.ListCity
import com.example.vietcar.data.model.location.wards.ListWards
import com.example.vietcar.data.model.location.district.ListDistrict
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("provinces/getAll?limit=-1")
    suspend fun getCity(): ListCity

    @GET("districts/getByProvince")
    suspend fun getDistrict(
        @Query("provinceCode") provinceCode: String,
        @Query("limit") limit: String = "-1",
    ): ListDistrict

    @GET("wards/getByDistrict")
    suspend fun getWards(
        @Query("districtCode") districtCode: String,
        @Query("limit") limit: String = "-1",
    ): ListWards
}
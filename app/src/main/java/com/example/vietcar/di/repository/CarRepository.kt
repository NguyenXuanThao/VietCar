package com.example.vietcar.di.repository

import com.example.vietcar.common.DataLocal
import com.example.vietcar.data.api.CarApi
import com.example.vietcar.data.model.category.ListCategory
import com.example.vietcar.data.model.login.LoginBody
import com.example.vietcar.data.model.login.LoginResponse
import com.example.vietcar.data.model.product.ListProduct
import com.example.vietcar.data.model.product_group.ListProductGroup
import com.example.vietcar.data.model.register.RegisterBody
import com.example.vietcar.data.model.register.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carApi: CarApi
) : ICarRepository {

    override suspend fun getCategory(): ListCategory {
        return withContext(Dispatchers.IO) {
            carApi.getCategory(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun getProductGroup(): ListProductGroup {
        return withContext(Dispatchers.IO) {
            carApi.getProductGroup(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun getListProductCategory(categoryId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getListProductCategory(DataLocal.BEARER_TOKEN, categoryId = categoryId)
        }
    }

    override suspend fun getListProductGroup(groupId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getListProductGroup(DataLocal.BEARER_TOKEN, groupId = groupId)
        }
    }

    override suspend fun getAllProduct(): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getAllProduct(DataLocal.BEARER_TOKEN)
        }
    }

    override suspend fun login(loginBody: LoginBody): LoginResponse {
        return withContext(Dispatchers.IO) {
            carApi.login(token = DataLocal.BEARER_TOKEN, body = loginBody)
        }
    }

    override suspend fun register(registerBody: RegisterBody): RegisterResponse {
        return withContext(Dispatchers.IO) {
            carApi.register(token = DataLocal.BEARER_TOKEN, body = registerBody)
        }
    }

    override suspend fun getRelatedProducts(productId: String): ListProduct {
        return withContext(Dispatchers.IO) {
            carApi.getRelatedProducts(token = DataLocal.BEARER_TOKEN, productId = productId)
        }
    }

}
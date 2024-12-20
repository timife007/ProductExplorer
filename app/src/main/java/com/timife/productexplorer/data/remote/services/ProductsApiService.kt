package com.timife.productexplorer.data.remote.services

import com.timife.productexplorer.data.remote.models.ProductDto
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApiService {

    @GET("products")
    suspend fun getAllProducts(): Response<List<ProductDto>>
}
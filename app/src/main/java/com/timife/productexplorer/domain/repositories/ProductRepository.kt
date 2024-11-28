package com.timife.productexplorer.domain.repositories

import com.timife.productexplorer.domain.Resource
import com.timife.productexplorer.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository{
    fun getProducts(): Flow<Resource<List<Product>>>
    fun getProductById(productId: Int): Flow<Resource<Product>>
}
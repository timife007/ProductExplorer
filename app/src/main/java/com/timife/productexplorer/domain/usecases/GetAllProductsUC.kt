package com.timife.productexplorer.domain.usecases

import com.timife.productexplorer.domain.repositories.ProductRepository
import javax.inject.Inject

class GetAllProductsUC @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke() = productRepository.getProducts()
}
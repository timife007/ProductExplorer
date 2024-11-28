package com.timife.productexplorer.domain.usecases

import com.timife.productexplorer.domain.repositories.ProductRepository
import javax.inject.Inject

class GetSingleProductUC @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke(productId: Int) = productRepository.getProductById(productId)
}
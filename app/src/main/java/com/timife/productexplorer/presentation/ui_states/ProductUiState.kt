package com.timife.productexplorer.presentation.ui_states

import com.timife.productexplorer.domain.model.Product

sealed class ProductUiState{
    data object Loading: ProductUiState()
    data class Success(val products: List<Product>): ProductUiState()
    data class Error(val error: String): ProductUiState()
}
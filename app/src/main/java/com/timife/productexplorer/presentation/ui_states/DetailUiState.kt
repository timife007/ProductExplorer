package com.timife.productexplorer.presentation.ui_states

import com.timife.productexplorer.domain.model.Product

sealed class DetailUiState {
    data object Loading: DetailUiState()
    data class Success(val data: Product): DetailUiState()
    data class Error(val error:String): DetailUiState()
}
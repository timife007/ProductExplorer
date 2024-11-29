package com.timife.productexplorer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.productexplorer.domain.Resource
import com.timife.productexplorer.domain.model.Messages
import com.timife.productexplorer.domain.usecases.GetAllProductsUC
import com.timife.productexplorer.presentation.ui_states.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getAllProductsUC: GetAllProductsUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            getAllProductsUC().collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _uiState.value = ProductUiState.Loading
                    }

                    is Resource.Error -> {
                        _uiState.value =
                            ProductUiState.Error(it.message ?: Messages.UNKNOWN_ERROR)
                    }

                    is Resource.Success -> {
                        if (it.data != null) {
                            _uiState.value =
                                ProductUiState.Success(it.data)
                        }else{
                            _uiState.value = ProductUiState.Error(Messages.NO_PRODUCTS_FOUND)
                        }
                    }
                }
            }
        }
    }
}
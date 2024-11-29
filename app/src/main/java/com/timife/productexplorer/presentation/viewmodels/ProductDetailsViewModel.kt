package com.timife.productexplorer.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timife.productexplorer.domain.Resource
import com.timife.productexplorer.domain.model.Messages
import com.timife.productexplorer.domain.usecases.GetSingleProductUC
import com.timife.productexplorer.presentation.ui_states.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getSingleProductUC: GetSingleProductUC,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailState


    init {
        getProduct(savedStateHandle.getStateFlow(Messages.PRODUCT_ID,- 1).value)
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            getSingleProductUC(id).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _detailState.value = DetailUiState.Loading
                    }

                    is Resource.Error -> {
                        _detailState.value =
                            DetailUiState.Error(it.message ?: Messages.UNKNOWN_ERROR)
                    }

                    is Resource.Success -> {
                        if (it.data != null) {
                            _detailState.value = DetailUiState.Success(it.data)
                        }
                    }
                }
            }
        }
    }
}
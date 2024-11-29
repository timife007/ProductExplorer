package com.timife.productexplorer.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.timife.productexplorer.data.db.productList
import com.timife.productexplorer.data.db.productsDto
import com.timife.productexplorer.data.mappers.toProduct
import com.timife.productexplorer.domain.Resource
import com.timife.productexplorer.domain.model.Product
import com.timife.productexplorer.domain.usecases.GetAllProductsUC
import com.timife.productexplorer.presentation.ui_states.ProductUiState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getAllProductsUC: GetAllProductsUC
    private lateinit var viewModel: ProductListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllProductsUC = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProducts emits Loading and Success states`() = runTest {
        // Mocks the use case to return a flow emitting Loading and Success
        val mockData = productList.map {
            it.toProduct()
        }
        coEvery { getAllProductsUC() } returns flowOf(
            Resource.Loading(),
            Resource.Success(mockData)
        )

        viewModel = ProductListViewModel(getAllProductsUC)

        viewModel.uiState.test {
            assert(awaitItem() is ProductUiState.Loading)

            // Checks that Success state is emitted with mock data
            val successState = awaitItem()
            assert(successState is ProductUiState.Success)
            assert((successState as ProductUiState.Success).products == mockData)
        }

        coVerify { getAllProductsUC() }
    }

    @Test
    fun `getProducts emits Loading and Error states`() = runTest {
        // Mocks the use case to return a flow emitting Loading and Error
        val errorMessage = "Error occurred"
        coEvery { getAllProductsUC() } returns flowOf(
            Resource.Loading(),
            Resource.Error(errorMessage)
        )

        viewModel = ProductListViewModel(getAllProductsUC)

        viewModel.uiState.test {
            assert(awaitItem() is ProductUiState.Loading)

            // Checks that Error state is emitted with correct message
            val errorState = awaitItem()
            assert(errorState is ProductUiState.Error)
            assert((errorState as ProductUiState.Error).error == errorMessage)
        }

        coVerify { getAllProductsUC() }
    }

    @Test
    fun `getProducts emits Loading and Error states when data is null`() = runTest {
        // Mocks the use case to return a flow emitting Loading and Success with null data
        coEvery { getAllProductsUC() } returns flowOf(
            Resource.Loading(),
            Resource.Success(null)
        )

        viewModel = ProductListViewModel(getAllProductsUC)

        viewModel.uiState.test {
            // Checks the initial state is Loading
            assert(awaitItem() is ProductUiState.Loading)

            // Checks that Error state is emitted for null data
            val errorState = awaitItem()
            assert(errorState is ProductUiState.Error)
            assert((errorState as ProductUiState.Error).error == "No item found")
        }

        coVerify { getAllProductsUC() }
    }
}

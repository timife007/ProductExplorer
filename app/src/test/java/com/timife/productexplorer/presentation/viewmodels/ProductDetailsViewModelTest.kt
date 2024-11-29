package com.timife.productexplorer.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.timife.productexplorer.data.db.productList
import com.timife.productexplorer.data.mappers.toProduct
import com.timife.productexplorer.domain.Resource
import com.timife.productexplorer.domain.model.Product
import com.timife.productexplorer.domain.usecases.GetSingleProductUC
import com.timife.productexplorer.presentation.ui_states.DetailUiState
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
class ProductDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getSingleProductUC: GetSingleProductUC
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ProductDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getSingleProductUC = mockk()
        savedStateHandle = SavedStateHandle(mapOf("productId" to 1)) // Default productId for testing
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProduct emits Loading and Success states`() = runTest {
        // Mocks the use case to return a flow emitting Loading and Success
        val mockProduct = productList[0].toProduct()
        coEvery { getSingleProductUC(1) } returns flowOf(
            Resource.Loading(),
            Resource.Success(mockProduct)
        )

        viewModel = ProductDetailsViewModel(getSingleProductUC, savedStateHandle)

        viewModel.detailUiState.test {
            // Checks the initial state is Loading
            assert(awaitItem() is DetailUiState.Loading)

            // Check that Success state is emitted with mock data
            val successState = awaitItem()
            assert(successState is DetailUiState.Success)
            assert((successState as DetailUiState.Success).data == mockProduct)
        }

        coVerify { getSingleProductUC(1) }
    }

    @Test
    fun `getProduct emits Loading and Error states`() = runTest {
        // Mocks the use case to return a flow emitting Loading and Error
        val errorMessage = "Error occurred"
        coEvery { getSingleProductUC(1) } returns flowOf(
            Resource.Loading(),
            Resource.Error(errorMessage)
        )

        viewModel = ProductDetailsViewModel(getSingleProductUC, savedStateHandle)

        viewModel.detailUiState.test {
            // Checks the initial state is Loading
            assert(awaitItem() is DetailUiState.Loading)

            // Checks that Error state is emitted with correct message
            val errorState = awaitItem()
            assert(errorState is DetailUiState.Error)
            assert((errorState as DetailUiState.Error).error == errorMessage)
        }

        coVerify { getSingleProductUC(1) }
    }

    @Test
    fun `getProduct emits Loading and Error states when productId is invalid`() = runTest {
        savedStateHandle = SavedStateHandle(mapOf("productId" to -1)) // Invalid productId
        coEvery { getSingleProductUC(-1) } returns flowOf(
            Resource.Loading(),
            Resource.Error("Invalid product ID")
        )

        viewModel = ProductDetailsViewModel(getSingleProductUC, savedStateHandle)

        viewModel.detailUiState.test {
            //Check that the initial state is Loading
            assert(awaitItem() is DetailUiState.Loading)

            // Check that Error state is emitted with "Invalid product ID" message
            val errorState = awaitItem()
            assert(errorState is DetailUiState.Error)
            assert((errorState as DetailUiState.Error).error == "Invalid product ID")
        }

        coVerify { getSingleProductUC(-1) }
    }
}

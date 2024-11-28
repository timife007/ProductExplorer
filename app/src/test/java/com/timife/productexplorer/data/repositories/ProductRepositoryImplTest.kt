package com.timife.productexplorer.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.timife.productexplorer.data.mappers.toProductEntity
import com.timife.productexplorer.data.remote.services.ProductsApiService
import com.timife.productexplorer.data.repository_impls.ProductRepositoryImpl
import com.timife.productexplorer.domain.Resource
import com.timife.productexplorer.domain.repositories.ProductRepository
import com.timife.productexplorer.data.db.FakeProductDao
import com.timife.productexplorer.data.db.fakeApiResponse
import com.timife.productexplorer.data.db.productList
import com.timife.productexplorer.data.db.productsDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepository
    private lateinit var dao: FakeProductDao
    private lateinit var api: ProductsApiService

    @Before
    fun setUp() {
        dao = FakeProductDao()
        api = mockk(relaxed = true){
            coEvery {
                getAllProducts()
            } returns fakeApiResponse
        }
        repository = ProductRepositoryImpl(
            api,
            dao
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getProducts() = runTest {
        repository.getProducts().test {
            val loading = awaitItem()
            assertThat((loading as Resource.Loading).isLoading).isTrue()
            val items = awaitItem()
            assertThat(items is Resource.Success).isTrue()
            val products = dao.getAllProducts()
            assertThat(products).isNotEmpty()
            assertThat(products).isEqualTo(productsDto.map { it.toProductEntity() })
            awaitComplete()
        }
    }

    @Test
    fun `check if correct id returns the right flow`() = runTest{
        dao.insertProducts(productList)
        repository.getProductById(1).test {
            val loading = awaitItem()
            assertThat((loading as Resource.Loading).isLoading).isTrue()
            val items = awaitItem()
            assertThat(items is Resource.Success).isTrue()
            assertThat(items).isNotNull()
            awaitComplete()
        }

    }

    @Test
    fun `check if incorrect id found returns the right flow`() = runTest{
        dao.insertProducts(productList)
        repository.getProductById(11).test {
            val loading = awaitItem()
            assertThat((loading as Resource.Loading).isLoading).isTrue()
            val items = awaitItem()
            assertThat(items is Resource.Error).isTrue()
            awaitComplete()
        }

    }
}
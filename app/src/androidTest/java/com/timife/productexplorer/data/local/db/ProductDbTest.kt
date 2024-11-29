package com.timife.productexplorer.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.timife.productexplorer.data.local.TestData
import com.timife.productexplorer.data.local.daos.CacheDao
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductDbTest {

    private lateinit var database: ProductDb
    private lateinit var dao: CacheDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ProductDb::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.productDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertProductsAndGetAllProductsReturnsCorrectData() = runTest {
        val products = TestData.productList

        dao.insertProducts(products)
        val result = dao.getAllProducts()

        assertThat(result).hasSize(4)
        assertThat(result).containsExactlyElementsIn(products)
    }

    @Test
    fun clearAllProductsRemovesAllData() = runTest {
        val products = TestData.productList
        dao.insertProducts(products)

        dao.clearAllProducts()
        val result = dao.getAllProducts()

        assertThat(result).isEmpty()
    }

    @Test
    fun getProductByIdReturnsCorrectProduct() = runTest {
        val products = TestData.productList
        dao.insertProducts(products)

        val product = dao.getProductById(1)

        assertThat(product).isNotNull()
        assertThat(product).isEqualTo(products[0])
    }

    @Test
    fun getProductByIdReturnsNullForNonExistentProduct() = runTest {
        val product = dao.getProductById(999)

        assertThat(product).isNull()
    }
}
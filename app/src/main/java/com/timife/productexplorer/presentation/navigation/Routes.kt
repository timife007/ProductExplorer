package com.timife.productexplorer.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.createGraph
import androidx.navigation.fragment.fragment
import com.timife.productexplorer.presentation.screens.ProductDetailsFragment
import com.timife.productexplorer.presentation.screens.ProductListFragment
import kotlinx.serialization.Serializable

@Serializable
object ProductList

@Serializable
data class ProductDetails(
    val productId: Int
)

//Navigation graph for the main destinations outside the auth flow
fun navigationGraph(navController: NavController): NavGraph {
    return navController.createGraph(startDestination = ProductList){
        fragment<ProductListFragment, ProductList>{
            label = "Discover Products"
        }
        fragment<ProductDetailsFragment, ProductDetails>{
            label = "Product Details"
        }
    }
}
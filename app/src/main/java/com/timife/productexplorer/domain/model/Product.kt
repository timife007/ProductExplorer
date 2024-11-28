package com.timife.productexplorer.domain.model

data class Product(
    val id:Int,
    val title:String,
    val price:Double,
    val description:String,
    val category:String,
    val image:String,
    val rating: Double,
    val review: Int
)
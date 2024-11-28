package com.timife.productexplorer.data.mappers

import com.timife.productexplorer.data.local.entities.ProductEntity
import com.timife.productexplorer.data.remote.models.ProductDto
import com.timife.productexplorer.data.remote.models.RatingDto
import com.timife.productexplorer.domain.model.Product
import com.timife.productexplorer.domain.model.Rating


fun ProductDto.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id ?: 0,
        title = title ?: "",
        price = price ?: 0.0,
        description = description ?: "",
        category = category ?: "",
        image = image ?: "",
        rating = ratingDto?.rate ?: 0.0,
        review = ratingDto?.count ?: 0
    )
}

fun RatingDto.toRating(): Rating {
    return Rating(
        rate = rate ?: 0.0,
        count = count ?: 0
    )
}

fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating,
        review = review
    )
}

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating,
        review = review
    )
}
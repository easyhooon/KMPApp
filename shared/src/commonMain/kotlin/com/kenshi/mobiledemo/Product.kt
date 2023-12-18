package com.kenshi.kmpapp

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String,
)

@Serializable
data class Products(
    val items: List<Product>,
)

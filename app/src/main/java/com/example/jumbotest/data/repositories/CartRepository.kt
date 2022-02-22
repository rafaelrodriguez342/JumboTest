package com.example.jumbotest.data.repositories

import com.example.jumbotest.business.model.Order
import com.example.jumbotest.business.model.Product

interface CartRepository {

    fun saveCart(cart: Map<String, Order>)

    fun getCart(products: List<Product>): Map<String, Order>

}

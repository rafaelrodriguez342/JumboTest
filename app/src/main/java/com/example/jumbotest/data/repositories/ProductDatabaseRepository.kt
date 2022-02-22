package com.example.jumbotest.data.repositories

import com.example.jumbotest.business.model.Product

interface ProductDatabaseRepository : ProductRepository {

    fun saveProducts(products: List<Product>)
}

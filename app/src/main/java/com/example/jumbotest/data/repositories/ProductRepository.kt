package com.example.jumbotest.data.repositories

import com.example.jumbotest.business.model.Product

interface ProductRepository {

    fun getProducts(): RepositoryResponse<List<Product>>

}

class RepositoryResponse<T>(val isSuccess: Boolean, val payload: T?, val errorMessage: String?)

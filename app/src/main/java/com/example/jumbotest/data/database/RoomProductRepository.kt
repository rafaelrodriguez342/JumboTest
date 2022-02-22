package com.example.jumbotest.data.database

import android.content.Context
import com.example.jumbotest.R
import com.example.jumbotest.business.model.Product
import com.example.jumbotest.data.database.daos.ProductsDao
import com.example.jumbotest.data.database.entities.ProductPersistenceModel
import com.example.jumbotest.data.repositories.ProductDatabaseRepository
import com.example.jumbotest.data.repositories.RepositoryResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class RoomProductRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productsDao: ProductsDao
) :
    ProductDatabaseRepository {

    override fun getProducts(): RepositoryResponse<List<Product>> {
        return try {
            RepositoryResponse(
                true, productsDao.getProducts()
                    .map {
                        Product(
                            it.id,
                            it.name,
                            it.imageUrl,
                            it.price,
                            it.currency,
                            it.available
                        )
                    }, null
            )
        } catch (e: IOException) {
            RepositoryResponse(false, null, context.getString(R.string.cache_error))
        }
    }

    override fun saveProducts(products: List<Product>) {
        productsDao.saveProducts(products.map {
            ProductPersistenceModel(
                it.id,
                it.name,
                it.imageUrl,
                it.price,
                it.currency,
                it.available
            )
        })
    }
}

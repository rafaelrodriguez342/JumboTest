package com.example.jumbotest.data.network

import android.content.Context
import com.example.jumbotest.R
import com.example.jumbotest.business.model.Product
import com.example.jumbotest.data.repositories.ProductRepository
import com.example.jumbotest.data.repositories.RepositoryResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class ApiClientProductRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val retrofitApiClient: RetrofitApiClient
) :
    ProductRepository {

    override fun getProducts(): RepositoryResponse<List<Product>> {
        return try {
            val response = retrofitApiClient.getProductList().execute()
            if (response.isSuccessful) {
                RepositoryResponse(true, response.body()?.products?.map {
                    Product(
                        it.id,
                        it.title,
                        it.imageInfo.primaryView.getOrNull(1)?.url
                            ?: "",//"https://imagizer.imageshack.com/img923/8378/qg9psm.jpg",
                        it.prices.price.amount.toString(),
                        it.prices.price.currency,
                        it.available
                    )
                }, null)

            } else {
                RepositoryResponse(
                    false,
                    null,
                    context.getString(R.string.server_error)
                )
            }
        } catch (e: IOException) {
            RepositoryResponse(
                false,
                null,
                context.getString(R.string.network_error)
            )
        }
    }
}

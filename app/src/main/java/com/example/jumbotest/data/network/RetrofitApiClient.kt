package com.example.jumbotest.data.network

import com.example.jumbotest.data.dto.ProductResponseDTO
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitApiClient {

    @GET("products.json")
    fun getProductList(): Call<ProductResponseDTO>

}


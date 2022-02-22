package com.example.jumbotest.data.dto

data class ProductResponseDTO(val products: List<ProductDTO>)

data class ProductDTO(val id: String, val title: String, val prices: PriceContainerDTO,
                      val imageInfo: ImageInfo, val available: Boolean)

data class PriceContainerDTO(val price: PriceDTO, val unitPrice: UnitPriceDTO)

data class PriceDTO(val currency: String, val amount: Int)

data class UnitPriceDTO(val unit: String, val price: PriceDTO)

data class ImageInfo(val primaryView: List<ImageData>)

data class ImageData(val url: String)

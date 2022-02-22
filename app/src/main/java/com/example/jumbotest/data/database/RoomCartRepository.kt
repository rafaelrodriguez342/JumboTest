package com.example.jumbotest.data.database

import com.example.jumbotest.business.model.Order
import com.example.jumbotest.business.model.Product
import com.example.jumbotest.data.database.daos.OrderDao
import com.example.jumbotest.data.database.entities.OrderPersistenceModel
import com.example.jumbotest.data.repositories.CartRepository
import javax.inject.Inject

class RoomCartRepository @Inject constructor(private val dao: OrderDao) : CartRepository {

    override fun saveCart(cart: Map<String, Order>) {
        dao.deleteCart()
        dao.insertCart(cart.map { OrderPersistenceModel(it.key, it.value.quantity) })
    }

    /**
     * Gets a list of saved products updates with latest data from product list, Im saving only product id and doing this
     * forEach because Im assuming is important to have latest info from updated list, since price and other fields
     * could change after user resumes the app later.
     *
     * @param products current product list.
     */
    override fun getCart(products: List<Product>): Map<String, Order> {
        val savedOrders = dao.getCart()
        val orderIds = savedOrders.associateBy({ it.id }, { it.amount })
        val cart = mutableMapOf<String, Order>()
        products.forEach { product ->
            orderIds[product.id]?.let {
                cart[product.id] = Order(product, it)
            }
        }
        return cart
    }
}

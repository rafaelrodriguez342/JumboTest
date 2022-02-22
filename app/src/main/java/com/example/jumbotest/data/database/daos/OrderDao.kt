package com.example.jumbotest.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jumbotest.data.database.entities.OrderPersistenceModel

@Dao
interface OrderDao {

    companion object {
        const val ORDER_TABLE = "order_table"
    }

    @Query("SELECT * FROM $ORDER_TABLE")
    fun getCart(): List<OrderPersistenceModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(cart: List<OrderPersistenceModel>)

    @Query("DELETE FROM $ORDER_TABLE")
    fun deleteCart()
}

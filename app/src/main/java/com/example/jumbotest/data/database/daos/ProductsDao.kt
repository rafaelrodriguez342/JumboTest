package com.example.jumbotest.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jumbotest.data.database.entities.ProductPersistenceModel

@Dao
interface ProductsDao {

    companion object {
        const val PRODUCTS_TABLE = "products_table"
    }

    @Query("SELECT * FROM $PRODUCTS_TABLE")
    fun getProducts(): List<ProductPersistenceModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProducts(products: List<ProductPersistenceModel>)

    @Query("DELETE FROM $PRODUCTS_TABLE")
    fun deleteCart()
}

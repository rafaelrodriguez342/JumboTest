package com.example.jumbotest.data.database.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jumbotest.data.database.daos.OrderDao
import com.example.jumbotest.data.database.daos.ProductsDao
import com.example.jumbotest.data.database.entities.OrderPersistenceModel
import com.example.jumbotest.data.database.entities.ProductPersistenceModel

@Database(entities = [OrderPersistenceModel::class, ProductPersistenceModel::class], version = 1)
abstract class JumboDatabase: RoomDatabase() {

    abstract fun getOrderDao(): OrderDao

    abstract fun getProductDao(): ProductsDao
}

package com.example.jumbotest.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jumbotest.data.database.daos.ProductsDao.Companion.PRODUCTS_TABLE

@Entity(tableName = PRODUCTS_TABLE)
class ProductPersistenceModel(
    @ColumnInfo @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val imageUrl: String,
    @ColumnInfo val price: String,
    @ColumnInfo val currency: String,
    @ColumnInfo val available: Boolean
)

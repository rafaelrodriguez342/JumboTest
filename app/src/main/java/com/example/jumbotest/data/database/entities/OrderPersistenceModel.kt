package com.example.jumbotest.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jumbotest.data.database.daos.OrderDao.Companion.ORDER_TABLE

@Entity(tableName = ORDER_TABLE)
class OrderPersistenceModel(
    @ColumnInfo @PrimaryKey
    val id: String,
    @ColumnInfo
    val amount: Int
)

package com.timife.productexplorer.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timife.productexplorer.data.local.daos.CacheDao
import com.timife.productexplorer.data.local.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDb : RoomDatabase(){
    abstract val productDao: CacheDao
}
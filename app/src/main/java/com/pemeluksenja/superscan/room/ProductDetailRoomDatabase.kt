package com.pemeluksenja.superscan.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductDetail::class], version = 1)
abstract class ProductDetailRoomDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDetailDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDetailRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): ProductDetailRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ProductDetailRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ProductDetailRoomDatabase::class.java, "product_database")
                        .build()
                }
            }
            return INSTANCE as ProductDetailRoomDatabase
        }
    }
}
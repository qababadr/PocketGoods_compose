package com.badrqaba.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.dao.AppSettingsDao
import com.badrqaba.core.data.local.dao.ImageDao
import com.badrqaba.core.data.local.dao.ProductDao
import com.badrqaba.core.data.local.dao.UserDao
import com.badrqaba.core.data.local.dao.WishlistDao
import com.badrqaba.core.data.local.entity.AppSettingsEntity
import com.badrqaba.core.data.local.entity.ImageEntity
import com.badrqaba.core.data.local.entity.ProductEntity
import com.badrqaba.core.data.local.entity.UserEntity
import com.badrqaba.core.data.local.entity.WishlistItemEntity

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        UserEntity::class,
        WishlistItemEntity::class,
        ProductEntity::class,
        ImageEntity::class,
        AppSettingsEntity::class
    ]
)
@TypeConverters(ControlledConverter::class)
abstract class PocketGoodsDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun productDao(): ProductDao
    abstract fun imageDao(): ImageDao
    abstract fun wishlistDao(): WishlistDao
}
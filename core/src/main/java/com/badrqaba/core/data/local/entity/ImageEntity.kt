package com.badrqaba.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.badrqaba.core.util.IMAGES_TABLE

@Entity(tableName = IMAGES_TABLE)
data class ImageEntity(
    @PrimaryKey(autoGenerate = false)
    val uuid: String,
    @ColumnInfo(name = "model_id")
    val modelId: Long,
    val filename: String,
    val preview: String,
    val original: String,
)

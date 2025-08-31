package com.badrqaba.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.badrqaba.core.util.USERS_TABLE
import java.util.Date

@Entity(tableName = USERS_TABLE)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val email: String,
    val token: String,
    @ColumnInfo(name = "email_verified_at")
    val emailVerifiedAt: Date?,
)

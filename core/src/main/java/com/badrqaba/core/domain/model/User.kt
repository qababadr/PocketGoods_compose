package com.badrqaba.core.domain.model

import java.util.Date

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val emailVerifiedAt: Date?,
    val wishlist: List<WishlistItem>
)

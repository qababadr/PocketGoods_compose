package com.badrqaba.core.data.mapper

import com.badrqaba.core.data.local.entity.UserEntity
import com.badrqaba.core.data.local.relation.UserWithWishlist
import com.badrqaba.core.data.local.relation.UserWithWishlistAndProductAndImages
import com.badrqaba.core.data.remote.dto.UserDTO
import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.toDateTime
import java.util.Date

fun UserWithWishlist.toUser(): User {
    return User(
        id = user.id,
        name = user.name,
        email = user.email,
        emailVerifiedAt = user.emailVerifiedAt,
        wishlist = wishlist.map { it.toWishlistItem() }
    )
}

fun UserWithWishlistAndProductAndImages.toUser(): User {
    return User(
        id = user.id,
        name = user.name,
        email = user.email,
        emailVerifiedAt = user.emailVerifiedAt,
        wishlist = wishlist.map { it.toWishlistItem() }
    )
}

fun UserDTO.toUserEntity(token: String): UserEntity {
    val date: Date? = try {
        emailVerifiedAt?.toDateTime()
    } catch (e: Exception){
        null
    }

    return UserEntity(
        id = id,
        name = name,
        email = email,
        token = token,
        emailVerifiedAt = date
    )
}

fun UserDTO.toUser(): User {
    return User(
        id = id,
        name = name,
        email = email,
        emailVerifiedAt = emailVerifiedAt?.toDateTime(),
        wishlist = wishlist.map { it.toWishlistItem() }
    )
}

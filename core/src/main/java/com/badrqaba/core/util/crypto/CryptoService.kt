package com.badrqaba.core.util.crypto

interface CryptoService {
    fun encrypt(data: String): String

    fun decrypt(data: String): String
}
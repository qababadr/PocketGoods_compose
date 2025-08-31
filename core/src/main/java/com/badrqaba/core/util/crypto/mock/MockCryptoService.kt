package com.badrqaba.core.util.crypto.mock

import com.badrqaba.core.util.crypto.CryptoService

class MockCryptoService: CryptoService  {
    override fun encrypt(data: String): String {
        return "encrypted_${data}"
    }

    override fun decrypt(data: String): String {
        return data.removePrefix("encrypted_")

    }
}
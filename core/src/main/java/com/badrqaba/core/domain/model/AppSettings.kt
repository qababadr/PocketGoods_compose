package com.badrqaba.core.domain.model

data class AppSettings(
    val isDarkMode: Boolean
) {
    companion object {
        val DEFAULT = AppSettings(
            isDarkMode = false
        )
    }

}

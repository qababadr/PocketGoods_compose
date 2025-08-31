package com.badrqaba.core.util

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDateTime(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"): Date {
    val formatter = SimpleDateFormat(pattern, Locale.CANADA)
    return formatter.parse(this) ?: throw Exception("Unable to parse the current date")
}

fun NavHostController.navigate(
    route: String,
    onBeforeNavigate: (() -> Unit)? = null,
    navOptions: NavOptions? = null
) {
    onBeforeNavigate?.invoke()
    navigate(route, navOptions)
}

fun String.stringAvatar(): String {
    val parts = trim().split("\\s+".toRegex())
    return when {
        parts.size >= 2 -> "${parts[0].firstOrNull()?.uppercase() ?: ""}${
            parts[1].firstOrNull()?.uppercase() ?: ""
        }"

        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> ""
    }
}

fun String.matchesRoute(pattern: String): Boolean {
    val regex = Regex(pattern.replace("{id}", "\\d+"))
    return this.matches(regex)
}
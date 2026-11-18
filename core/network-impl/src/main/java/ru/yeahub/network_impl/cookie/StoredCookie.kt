package ru.yeahub.network_impl.cookie

import okhttp3.Cookie

internal data class StoredCookie(
    val name: String,
    val value: String,
    val expiresAt: Long,
    val domain: String,
    val path: String,
    val secure: Boolean,
    val httpOnly: Boolean,
    val hostOnly: Boolean,
    val persistent: Boolean,
)

internal fun Cookie.toStoredCookie(): StoredCookie {
    return StoredCookie(
        name = name(),
        value = value(),
        expiresAt = expiresAt(),
        domain = domain(),
        path = path(),
        secure = secure(),
        httpOnly = httpOnly(),
        hostOnly = hostOnly(),
        persistent = persistent(),
    )
}

internal fun StoredCookie.toCookie(): Cookie {
    return Cookie.Builder()
        .name(name)
        .value(value)
        .path(path)
        .apply {
            if (hostOnly) {
                hostOnlyDomain(domain)
            } else {
                domain(domain)
            }

            if (persistent) {
                expiresAt(expiresAt)
            }

            if (secure) {
                secure()
            }

            if (httpOnly) {
                httpOnly()
            }
        }
        .build()
}
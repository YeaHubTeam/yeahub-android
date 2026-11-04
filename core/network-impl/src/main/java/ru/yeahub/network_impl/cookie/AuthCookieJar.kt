package ru.yeahub.network_impl.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import ru.yeahub.network_api.AuthCookieManager
import java.util.concurrent.ConcurrentHashMap

class AuthCookieJar : CookieJar, AuthCookieManager {

    private val cookieStore = ConcurrentHashMap<String, Cookie>()

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>,
    ) {
        val currentTime = System.currentTimeMillis()

        cookies.forEach { cookie ->
            val cookieKey = createCookieKey(cookie)

            if (cookie.expiresAt() <= currentTime) {
                cookieStore.remove(cookieKey)
            } else {
                cookieStore[cookieKey] = cookie
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val currentTime = System.currentTimeMillis()

        cookieStore.entries
            .filter { (_, cookie) ->
                cookie.expiresAt() <= currentTime
            }
            .forEach { (key, cookie) ->
                cookieStore.remove(key, cookie)
            }

        return cookieStore.values.filter { cookie ->
            cookie.matches(url)
        }
    }

    override fun clear() {
        cookieStore.clear()
    }

    private fun createCookieKey(cookie: Cookie): String {
        return "${cookie.name()}|${cookie.domain()}|${cookie.path()}"
    }
}
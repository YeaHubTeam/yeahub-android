package ru.yeahub.network_impl.cookie

import com.google.gson.Gson
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import ru.yeahub.datastore_api.AuthCookieDataStore
import ru.yeahub.network_api.AuthCookieManager
import java.util.concurrent.ConcurrentHashMap
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlinx.coroutines.runBlocking
class AuthCookieJar(
    private val authCookieDataStore: AuthCookieDataStore,
    private val gson: Gson = Gson(),
) : CookieJar, AuthCookieManager {

    private val cookieStore = ConcurrentHashMap<String, Cookie>()
    private val restoreLock = Any()

    @Volatile
    private var cookiesRestored = false

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>,
    ) {
        restoreCookiesIfNeeded()

        val currentTime = System.currentTimeMillis()

        cookies.forEach { cookie ->
            val cookieKey = createCookieKey(cookie)

            if (cookie.expiresAt() <= currentTime) {
                cookieStore.remove(cookieKey)
            } else {
                cookieStore[cookieKey] = cookie
            }
        }
        persistCookies()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        restoreCookiesIfNeeded()

        val currentTime = System.currentTimeMillis()

        val expiredCookies = cookieStore.entries
            .filter { (_, cookie) ->
                cookie.expiresAt() <= currentTime
            }

        expiredCookies.forEach { (key, cookie) ->
            cookieStore.remove(key, cookie)
        }

        if (expiredCookies.isNotEmpty()) {
            persistCookies()
        }

        return cookieStore.values.filter { cookie ->
            cookie.matches(url)
        }
    }

    override fun clear() {
        synchronized(restoreLock) {
            cookieStore.clear()

            runBlocking {
                authCookieDataStore.clearCookies()
            }

            cookiesRestored = true
        }
    }

    private fun persistCookies() {
        val storedCookies = cookieStore.values
            .map { cookie ->
                cookie.toStoredCookie()
            }

        runBlocking {
            if (storedCookies.isEmpty()) {
                authCookieDataStore.clearCookies()
            } else {
                val serializedCookies = gson.toJson(
                    storedCookies,
                    COOKIE_LIST_TYPE,
                )

                authCookieDataStore.saveCookies(
                    serializedCookies,
                )
            }
        }
    }

    private fun restoreCookiesIfNeeded() {
        if (cookiesRestored) {
            return
        }

        synchronized(restoreLock) {
            if (cookiesRestored) {
                return
            }

            val serializedCookies = runCatching {
                runBlocking {
                    authCookieDataStore.getCookies()
                }
            }.getOrNull()

            val restoredCookies = serializedCookies
                ?.let(::deserializeCookies)
                .orEmpty()

            val currentTime = System.currentTimeMillis()

            restoredCookies
                .filter { cookie ->
                    cookie.expiresAt() > currentTime
                }
                .forEach { cookie ->
                    cookieStore[createCookieKey(cookie)] = cookie
                }

            cookiesRestored = true
        }
    }

    private fun deserializeCookies(
        serializedCookies: String,
    ): List<Cookie> {
        return runCatching {
            gson.fromJson<List<StoredCookie>>(
                serializedCookies,
                COOKIE_LIST_TYPE,
            )
        }.getOrNull()
            .orEmpty()
            .map(StoredCookie::toCookie)
    }

    private fun createCookieKey(cookie: Cookie): String {
        return "${cookie.name()}|${cookie.domain()}|${cookie.path()}"
    }

    private companion object {
        private val COOKIE_LIST_TYPE: Type =
            object : TypeToken<List<StoredCookie>>() {}.type
    }
}
package ru.yeahub.network_impl

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_impl.cookie.AuthCookieJar
import ru.yeahub.network_impl.interceptor.AccessTokenInterceptor
import ru.yeahub.network_impl.interceptor.TokenRefreshInterceptor
import ru.yeahub.network_impl.refresh.RefreshTokenService
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import ru.yeahub.network_api.AuthCookieManager

var a: String? = null
var b: String? = null

val networkModule = module {

    single {
        AuthCookieJar(
            authCookieDataStore = get(),
        )
    }

    single<AuthCookieManager> {
        get<AuthCookieJar>()
    }

    single<OkHttpClient>(named(REFRESH_HTTP_CLIENT)) {
        getUnsafeOkHttpClientBuilder(
            authCookieJar = get(),
        ).build()
    }

    single<Retrofit>(named(REFRESH_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named(REFRESH_HTTP_CLIENT)))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(named(REFRESH_RETROFIT))
            .create(RefreshTokenService::class.java)
    }

    single {
        AccessTokenInterceptor(
            tokenDataStore = get(),
        )
    }

    single {
        TokenRefreshInterceptor(
            refreshTokenService = get(),
            tokenDataStore = get(),
            authCookieJar = get(),
        )
    }

    single<OkHttpClient> {
        getUnsafeOkHttpClientBuilder(
            authCookieJar = get(),
        )
            .addInterceptor(get<AccessTokenInterceptor>())
            .addInterceptor(get<TokenRefreshInterceptor>())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<RetrofitApiService> {
        get<Retrofit>().create(RetrofitApiService::class.java)
    }

    single<ApiService> {
        get<RetrofitApiService>()
    }

    single<NetworkProvider> {
        object : NetworkProvider {
            override val apiService: ApiService = get()
        }
    }
}

fun getUnsafeOkHttpClientBuilder(
    authCookieJar: AuthCookieJar,
): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            val certificate = chain[0]
            a = certificate.toString()
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            val certificate = chain[0]
            b = certificate.toString()
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())

    return OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .cookieJar(authCookieJar)
}

private const val BASE_URL = "https://api.yeatwork.ru"
private const val REFRESH_HTTP_CLIENT = "refreshHttpClient"
private const val REFRESH_RETROFIT = "refreshRetrofit"
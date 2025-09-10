package ru.yeahub.network_impl

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.NetworkProvider
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

var a: String? = null
var b: String? = null

val networkModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.yeatwork.ru")
            .client(getUnsafeOkHttpClient())
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

fun getUnsafeOkHttpClient(): OkHttpClient {
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
        .build()
}

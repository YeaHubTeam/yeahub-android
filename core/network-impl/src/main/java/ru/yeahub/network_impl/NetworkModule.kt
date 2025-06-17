package ru.yeahub.network_impl

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.NetworkProvider

val networkModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.yeatwork.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<RetrofitApiService> {
        get<Retrofit>().create(RetrofitApiService::class.java)
    }

    single<ApiService> {
        ApiServiceImpl(get())
    }

    single<NetworkProvider> {
        object : NetworkProvider {
            override val apiService: ApiService = get()
        }
    }
}

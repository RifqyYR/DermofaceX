package com.makassar.dermofacex.di

import com.google.gson.GsonBuilder
import com.makassar.dermofacex.BuildConfig
import com.makassar.dermofacex.data.network.ApiService
import com.makassar.dermofacex.data.network.LoggingInterceptor
import com.makassar.dermofacex.data.repository.GeneralRepository
import com.makassar.dermofacex.ui.viewModel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(LoggingInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        GsonBuilder()
            .setLenient()
            .create()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }

}

val repositoryModule = module {
    single { GeneralRepository(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}
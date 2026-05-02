package com.example.foodiego.di


import androidx.room.Room
import com.app.task_itzon.BuildConfig
import com.app.task_itzon.data.local.AppDatabase
import com.app.task_itzon.data.repo.ProductsRepository
import com.app.task_itzon.ui.viewmodel.ProductsViewModel
import com.app.task_itzon.ui.viewmodel.SearchViewModel
import com.app.task_itzon.util.ConnectivityInterceptor
import com.app.task_itzon.util.LogUtil
import com.app.task_itzon.util.ToastUtil
import com.example.foodiego.data.remote.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "task_db"
        ).build()
    }

    single { get<AppDatabase>().favoriteProductDao() }
}

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single { ConnectivityInterceptor(androidContext()) }


    single {
        OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<ConnectivityInterceptor>()).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder().header("Content-Type", "application/json")
                    .header("User-Agent", "Task/1.0 (Android)").build()

                // LOG REQUEST
                LogUtil.d("--- HTTP REQUEST ---")
                LogUtil.d("URL: ${request.url}")
                LogUtil.d("Method: ${request.method}")
                request.body?.let { body ->
                    val buffer = okio.Buffer()
                    body.writeTo(buffer)
                    LogUtil.d("Body: ${buffer.readUtf8()}")
                }

                val response = chain.proceed(request)

                // LOG RESPONSE
                LogUtil.d("--- HTTP RESPONSE ---")
                LogUtil.d("Code: ${response.code}")

                // NEW: Log Response Body (JSON from Server)
                val responseBody = response.peekBody(Long.MAX_VALUE)
                LogUtil.d("Response Body: ${responseBody.string()}")

                response
            }.build()
    }

    single {
        val jsonConfig = Json {
            ignoreUnknownKeys = true // High priority for stable apps
            isLenient = true
            encodeDefaults = true
        }
        val contentType = "application/json".toMediaType()
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(get())
            .addConverterFactory(jsonConfig.asConverterFactory(contentType)).build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }

}

val repositoryModule = module {

    singleOf(::ProductsRepository)

}

val viewModelModule = module {
    viewModelOf(::ProductsViewModel)
    viewModelOf(::SearchViewModel)
}
val utilModule = module {
    single { ToastUtil(androidContext()) }
}

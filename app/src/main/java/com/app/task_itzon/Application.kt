package com.app.task_itzon

import android.app.Application
import com.example.foodiego.di.databaseModule
import com.example.foodiego.di.networkModule
import com.example.foodiego.di.repositoryModule
import com.example.foodiego.di.utilModule
import com.example.foodiego.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@AppClass)
            modules(listOf(databaseModule, networkModule, repositoryModule, viewModelModule, utilModule))
        }
    }
}
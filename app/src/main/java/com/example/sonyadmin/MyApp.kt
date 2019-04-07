package com.example.sonyadmin

import android.app.Application
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.RepositoryImpl
import com.example.sonyadmin.gameList.MyModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}

val appModule = module {
    single<Repository> { RepositoryImpl(get()) }

    // MyViewModel ViewModel
    viewModel { MyModel(get()) }

}
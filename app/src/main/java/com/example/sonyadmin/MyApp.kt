package com.example.sonyadmin

import android.app.Application
import com.example.sonyadmin.data.Dao
import com.example.sonyadmin.data.GameProcessDataBase
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.RepositoryImpl
import com.example.sonyadmin.gameList.MyModel
import com.example.sonyadmin.gameList.ScopeProvider
import com.example.sonyadmin.gemaDetails.DetailsViewModel
import com.example.sonyadmin.infoPerDay.DailyInfoViewModel
import net.danlew.android.joda.JodaTimeAndroid
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
        JodaTimeAndroid.init(this)
    }
}

val appModule = module {
    single <Dao>{ GameProcessDataBase.getInstance(get()).gameProcesDao()}
    single<Repository> { RepositoryImpl(get()) }
    viewModel { MyModel(get(),get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { DailyInfoViewModel(get()) }
}
package com.example.sonyadmin

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.sonyadmin.bar.category.BarViewModel
import com.example.sonyadmin.bar.product.ProductViewModel
import com.example.sonyadmin.data.Dao
import com.example.sonyadmin.data.GameProcessDataBase
import com.example.sonyadmin.data.Repository.*
import com.example.sonyadmin.data.service.Api
import com.example.sonyadmin.data.service.ApiImpl
import com.example.sonyadmin.data.service.UserRepository
import com.example.sonyadmin.gameList.MyModel
import com.example.sonyadmin.gemaDetails.DetailsViewModel
import com.example.sonyadmin.infoPerDay.DailyInfoViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.navendra.retrofitkotlindeferred.service.UserService
import net.danlew.android.joda.JodaTimeAndroid
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule)
        }
        JodaTimeAndroid.init(this)
    }

}

val appModule = module {
    single<Dao> { GameProcessDataBase.getInstance(androidApplication()).gameProcesDao() }
    single<Repository> { RepositoryImpl(get(),get()) }
    single<FirebaseRepository>{FirebaseRepoImpl()}
    viewModel { MyModel(get(), get(), get(), get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { BarViewModel(get()) }
    single<CategoryRepository>{CategoryRepoImpl()}
    viewModel{ ProductViewModel(get()) }
    single<ProductRepository>{ProductRepoImpl()}
    viewModel { DailyInfoViewModel(get()) }
    single<EveryDayUpdateCashWorker> { EveryDayUpdateCashWorkerImpl() }
    factory<Interceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    factory {
        OkHttpClient.Builder().addInterceptor(get())
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("http://jsonplaceholder.typicode.com/")
//            .baseUrl("http://192.168.43.9:5000")//redme
//            .baseUrl("http://192.168.0.109:5000/")//baza
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory { get<Retrofit>().create(UserService::class.java) }
    factory { UserRepository(get()) }
    factory<Api> { ApiImpl(get()) }
}


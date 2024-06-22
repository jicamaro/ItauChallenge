package cl.itau.challenge.data.di

import androidx.room.Room
import cl.itau.challenge.BASE_URL
import cl.itau.challenge.USER_DATABASE
import cl.itau.challenge.data.database.UserDatabase
import cl.itau.challenge.data.datasource.EarthquakeRemoteDataSource
import cl.itau.challenge.data.datasource.UserLocalDataSource
import cl.itau.challenge.data.repository.EarthquakeRepository
import cl.itau.challenge.data.repository.UserRepository
import cl.itau.challenge.data.source.EarthquakeAPI
import com.google.gson.GsonBuilder
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single {
        EarthquakeRemoteDataSource(get())
    }

    single {
        EarthquakeRepository(get())
    }

    single {
        GsonBuilder()
            .serializeNulls()
            .create()
    }

    single {
        GsonConverterFactory.create(get())
    }

    single {
        Retrofit.Builder()
            .baseUrl(get<String>(named(BASE_URL)))
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addNetworkInterceptor(get<HttpLoggingInterceptor>())
            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
            .build()
    }

    single {
        get<Retrofit>().create(EarthquakeAPI::class.java)
    }

    single {
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    single<UserDatabase> {
        Room.databaseBuilder(
            context = androidContext(),
            UserDatabase::class.java,
            get<String>(
                named(USER_DATABASE)
            )
        ).build()
    }

    single {
        get<UserDatabase>().userDao()
    }

    single {
        UserRepository(get())
    }

    single {
        UserLocalDataSource(get())
    }
}
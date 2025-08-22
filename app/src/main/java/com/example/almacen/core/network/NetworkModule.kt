package com.example.almacen.core.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //const val BASE_URL = "http://10.0.2.2:8080/"
    //const val BASE_URL = "http://45.149.207.118:5015/"
    const val BASE_URL = "https://apps.fastdye.pe/"
    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS) // Tiempo para conectar
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)    // Tiempo para leer la respuesta
            .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)   // Tiempo para enviar el cuerpo
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.NONE
                }
            )
            .build()

    @Provides @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
}
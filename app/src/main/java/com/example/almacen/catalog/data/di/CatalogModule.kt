package com.example.almacen.catalog.data.di

import com.example.almacen.catalog.data.remote.api.CatalogApi
import com.example.almacen.catalog.data.repository.CatalogRepositoryImpl
import com.example.almacen.catalog.domain.repository.CatalogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatalogModule {
    @Provides @Singleton
    fun provideCatalogApi(retrofit: Retrofit): CatalogApi =
        retrofit.create(CatalogApi::class.java)

    @Provides @Singleton
    fun provideCatalogRepository(api: CatalogApi): CatalogRepository =
        CatalogRepositoryImpl(api)
}

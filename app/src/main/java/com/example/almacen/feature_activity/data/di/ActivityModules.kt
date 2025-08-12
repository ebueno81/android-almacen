package com.example.almacen.feature_activity.data.di

import com.example.almacen.feature_activity.data.remote.api.ActivityApi
import com.example.almacen.feature_activity.data.repository.ActivityRepositoryImpl
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityProvideModule {
    @Provides @Singleton
    fun provideActivityApi(retrofit: Retrofit): ActivityApi =
        retrofit.create(ActivityApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityBindModule {
    @Binds @Singleton
    abstract fun bindActivityRepo(impl: ActivityRepositoryImpl): ActivityRepository
}
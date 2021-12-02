package com.hickar.restly.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hickar.restly.repository.dao.CollectionDao
import com.hickar.restly.repository.dao.RequestDao
import com.hickar.restly.repository.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideRequestDao(database: AppDatabase): RequestDao {
        return database.requestDao()
    }

    @Provides
    fun provideCollectionDao(database: AppDatabase): CollectionDao {
        return database.collectionDao()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }
}
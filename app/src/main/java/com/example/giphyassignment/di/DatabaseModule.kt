package com.example.giphyassignment.di

import android.app.Application
import com.example.giphyassignment.data.db.dao.FavoriteDao
import com.example.giphyassignment.data.db.database.GiphyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun getAppDatabase(context: Application): GiphyDatabase {
        return GiphyDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun getFavouritesDao(appDatabase: GiphyDatabase): FavoriteDao {
        return appDatabase.getFavoritesDao()
    }
}
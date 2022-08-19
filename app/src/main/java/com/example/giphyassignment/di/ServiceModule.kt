package com.example.giphyassignment.di

import com.example.giphyassignment.data.network.GiphyService
import com.example.giphyassignment.data.network.RemoteInjector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun getRetroServiceInstance(): GiphyService {
        return RemoteInjector.injectGiphyService()
    }
}
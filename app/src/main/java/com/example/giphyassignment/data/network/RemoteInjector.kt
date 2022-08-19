package com.example.giphyassignment.data.network

import com.example.giphyassignment.utilities.Constants.API_KEY
import com.example.giphyassignment.utilities.Constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RemoteInjector {
    /*Fetch Retrofit Client*/
    fun injectGiphyService(retrofit: Retrofit = getRetrofit()): GiphyService {
        return retrofit.create(GiphyService::class.java)
    }

    /*Fetch Retrofit Client*/
    private fun getRetrofit(okHttpClient: OkHttpClient = getOkHttpClient()): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /*Attaching Interceptors To Retrofit Calls*/
    private fun getOkHttpClient(
        okHttpLogger: HttpLoggingInterceptor = getHttpLogger(),
        okHttpAuthInterceptor: Interceptor = getAuthInterceptor(),
        okHttpNetworkInterceptor: Interceptor = getOkHttpNetworkInterceptor()
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(okHttpLogger)
            .addInterceptor(okHttpAuthInterceptor)
            .addInterceptor(okHttpNetworkInterceptor)
            .build()
    }

    /*Logging Interceptor*/
    private fun getHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /*Attach Auth Interceptor For Common Query Parameters*/
    private fun getAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            var req = chain.request()
            req = req.newBuilder().url(
                req.url.newBuilder().addQueryParameter("api_key", API_KEY).build()
            ).build()
            chain.proceed(req)
        }
    }

    /*Attach Request Headers*/
    private fun getOkHttpNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newRequest =
                chain.request().newBuilder().addHeader("api_key", API_KEY).build()
            chain.proceed(newRequest)
        }
    }
}
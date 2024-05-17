package com.test.landmarkremark.presentation.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.test.landmarkremark.BuildConfig
import com.test.landmarkremark.R
import com.test.landmarkremark.data.sources.remote.APIService
import com.test.landmarkremark.utils.Constants
import com.test.landmarkremark.utils.SavedStore
import com.test.landmarkremark.utils.Utils
import com.test.landmarkremark.utils.toJson
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideGsonConvertFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideAPIClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(Interceptor { chain ->
                val ongoing: Request.Builder = chain.request().newBuilder().apply {
                    header("Accept", "application/json")
                    header("Content-Type", "application/json")
                }
//                if (Gson().fromJson(
//                        SavedStore.getString(
//                            Constants.PREFS_LANGUAGE_MODEL,
//                            CommonItemModel(1, Utils.getString(R.string.vietnamese), R.drawable.flag_viet_nam).toJson()
//                        ), CommonItemModel::class.java
//                    ).id == 2
//                ) {
//                    ongoing.header("language", "EN")
//                } else {
//                    ongoing.header("language", "VN")
//                }
                chain.proceed(ongoing.build())
            })
            .addNetworkInterceptor {
                val request: Request = it.request().newBuilder().addHeader("Connection", "Close").build()
                it.proceed(request)
            }
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitApi(): APIService {
        val retrofit =
            Retrofit.Builder().addConverterFactory(provideGsonConvertFactory()).baseUrl(BuildConfig.SERVER_NORMAL_URL).client(
                provideAPIClient()
            ).build()
        return retrofit.create(APIService::class.java)
    }
}
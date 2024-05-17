package com.test.landmarkremark.presentation.di

import com.test.landmarkremark.data.repositories.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.test.landmarkremark.data.repositories.NoteRepositoryImpl
import com.test.landmarkremark.domain.usecases.AuthUseCase
import com.test.landmarkremark.domain.usecases.NoteUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideCourseUseCase(courseRepositoryImpl: NoteRepositoryImpl): NoteUseCase {
        return NoteUseCase(courseRepositoryImpl)
    }
    @Singleton
    @Provides
    fun provideAuthUseCase(authRepositoryImpl: AuthRepositoryImpl): AuthUseCase {
        return AuthUseCase(authRepositoryImpl)
    }
}
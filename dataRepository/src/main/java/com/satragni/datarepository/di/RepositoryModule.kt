package com.satragni.datarepository.di

import com.satragni.datarepository.ImageDetailsRepositoryImpl
import com.satragni.datarepository.SearchImageRepositoryImpl
import com.satragni.domain.ImageDetailsRepository
import com.satragni.domain.SearchImageRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun searchRepository(searchRepositoryImpl: SearchImageRepositoryImpl): SearchImageRepository

    @Binds
    abstract fun imageDetailsRepository(imageDetailsRepositoryImpl: ImageDetailsRepositoryImpl): ImageDetailsRepository
}
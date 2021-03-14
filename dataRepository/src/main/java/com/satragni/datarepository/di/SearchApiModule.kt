package com.satragni.datarepository.di

import com.satragni.datarepository.NetworkClient
import com.satragni.datarepository.SearchImageService
import dagger.Module
import dagger.Provides


@Module
abstract class SearchApiModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun resourceService(client: NetworkClient) = client.create(SearchImageService::class)
    }
}
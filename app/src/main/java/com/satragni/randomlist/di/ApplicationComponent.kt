package com.satragni.randomlist.di

import android.app.Application
import androidx.annotation.Keep
import com.satragni.datarepository.di.RepositoryModule
import com.satragni.datarepository.di.RetrofitModule
import com.satragni.datarepository.di.RoomDbModule
import com.satragni.datarepository.di.SearchApiModule
import com.satragni.domain.di.UseCaseModule
import com.satragni.randomlist.RandomListingApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        RepositoryModule::class,
        RoomDbModule::class,
        RetrofitModule::class,
        UseCaseModule::class,
        SearchApiModule::class,
        ActivitiesModule::class,
    ]
)
interface ApplicationComponent : AndroidInjector<RandomListingApplication> {
    @Keep
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }
}
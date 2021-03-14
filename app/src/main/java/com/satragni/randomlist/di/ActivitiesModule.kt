package com.satragni.randomlist.di

import com.satragni.randomlist.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {
    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity
}
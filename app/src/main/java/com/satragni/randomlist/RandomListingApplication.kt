package com.satragni.randomlist

import com.satragni.randomlist.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class RandomListingApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<RandomListingApplication> {
        return DaggerApplicationComponent.factory()
            .create(this)
    }
}
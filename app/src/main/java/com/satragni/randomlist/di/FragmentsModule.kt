package com.satragni.randomlist.di

import androidx.lifecycle.ViewModel
import com.satragni.randomlist.listDetails.ImageDetailFragment
import com.satragni.randomlist.listDetails.ImageDetailViewModel
import com.satragni.randomlist.search.SearchImageFragment
import com.satragni.randomlist.search.SearchImageViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract fun provideSearchImageFragment(): SearchImageFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchImageViewModel::class)
    abstract fun bindSearchImageViewModel(searchImageViewModel: SearchImageViewModel): ViewModel


    @ContributesAndroidInjector
    abstract fun provideImageDetailFragment(): ImageDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(ImageDetailViewModel::class)
    abstract fun bindImageDetailViewModel(imageDetailViewModel: ImageDetailViewModel): ViewModel
}
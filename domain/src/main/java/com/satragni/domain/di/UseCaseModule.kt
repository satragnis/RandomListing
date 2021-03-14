package com.satragni.domain.di

import com.satragni.domain.GetCommentUseCase
import com.satragni.domain.GetCommentUseCaseImpl
import com.satragni.domain.PostCommentUseCase
import com.satragni.domain.PostCommentUseCaseImpl
import com.satragni.domain.SearchImageUseCase
import com.satragni.domain.SearchImageUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun searchRepository(searchRepositoryImpl: SearchImageUseCaseImpl): SearchImageUseCase

    @Binds
    abstract fun postCommentUseCase(postCommentUseCaseImpl: PostCommentUseCaseImpl): PostCommentUseCase

    @Binds
    abstract fun getCommentUseCase(getCommentUseCaseImpl: GetCommentUseCaseImpl): GetCommentUseCase
}
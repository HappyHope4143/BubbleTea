package com.happyhope.bubbletea.di

import com.happyhope.bubbletea.domain.usecase.GetNewsUseCase
import com.happyhope.bubbletea.domain.usecase.GetNewsUseCaseImpl
import com.happyhope.bubbletea.domain.usecase.LoadMoreNewsUseCase
import com.happyhope.bubbletea.domain.usecase.LoadMoreNewsUseCaseImpl
import com.happyhope.bubbletea.domain.usecase.RefreshNewsUseCase
import com.happyhope.bubbletea.domain.usecase.RefreshNewsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    
    @Binds
    abstract fun bindGetNewsUseCase(
        impl: GetNewsUseCaseImpl
    ): GetNewsUseCase
    
    @Binds
    abstract fun bindRefreshNewsUseCase(
        impl: RefreshNewsUseCaseImpl
    ): RefreshNewsUseCase
    
    @Binds
    abstract fun bindLoadMoreNewsUseCase(
        impl: LoadMoreNewsUseCaseImpl
    ): LoadMoreNewsUseCase
}
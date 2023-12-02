package com.example.frhtml.di

import androidx.lifecycle.ViewModel
import com.example.frhtml.presentation.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
@Binds
@IntoMap
@ViewModelKey(MainViewModel::class)
    fun bingViewModel(viewModel: MainViewModel): ViewModel
}
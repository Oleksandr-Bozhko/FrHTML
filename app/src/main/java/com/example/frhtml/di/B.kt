package com.example.frhtml.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class B {
    @Provides
    fun provideContext(application: Application): Context = application
}
package com.example.frhtml.di

import android.app.Application
import android.content.Context
import com.example.frhtml.data.RepositoryImpl
import com.example.frhtml.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule{

    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository

}

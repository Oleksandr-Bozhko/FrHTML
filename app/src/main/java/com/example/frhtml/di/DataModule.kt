package com.example.frhtml.di

import com.example.frhtml.data.RepositoryImpl
import com.example.frhtml.domain.Repository
import dagger.Binds
import dagger.Module

@Module
interface DataModule{

    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository
}

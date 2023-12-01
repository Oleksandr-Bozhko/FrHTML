package com.example.frhtml.di

import android.app.Application
import com.example.frhtml.presentation.MainActivity
import com.example.frhtml.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Inject


@Component(modules = [DataModule::class, ViewModelFactory::class])
interface AppComponent {

    @Inject
    fun inject(activity: MainActivity)
    @Component.Factory
   interface Factory{
       fun create (@BindsInstance application: Application): AppComponent
    }


}
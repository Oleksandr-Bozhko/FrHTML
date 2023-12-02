package com.example.frhtml.di

import android.app.Application
import com.example.frhtml.presentation.MainActivity
import com.example.frhtml.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Inject


@Component(modules = [DataModule::class,B::class,ViewModelModule::class])
interface AppComponent {


    fun inject(activity: MainActivity)
    @Component.Factory
   interface Factory{
       fun create (@BindsInstance application: Application): AppComponent
    }


}
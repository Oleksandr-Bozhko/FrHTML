package com.example.frhtml.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component



@Component(modules = [DataModule::class])
interface AppComponent {
    @Component.Factory
   interface Factory{
       fun create (@BindsInstance application: Application): AppComponent
    }


}
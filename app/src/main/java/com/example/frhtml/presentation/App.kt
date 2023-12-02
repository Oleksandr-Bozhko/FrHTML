package com.example.frhtml.presentation

import android.app.Application
import com.example.frhtml.di.DaggerAppComponent

class App: Application() {
    val component by lazy {
DaggerAppComponent.factory().create(this)
    }
}
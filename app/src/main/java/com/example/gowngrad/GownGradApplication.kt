package com.example.gowngrad

import android.app.Application
import com.example.gowngrad.data.AppContainer
import com.example.gowngrad.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class GownGradApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
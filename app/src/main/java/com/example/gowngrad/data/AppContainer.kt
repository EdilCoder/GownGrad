package com.example.gowngrad.data

import android.content.Context
import com.example.gowngrad.data.local.GownGradLocalDatabase

interface AppContainer{
    val gownGradRepository : GownGradRepository
}

class AppDataContainer(private val context: Context) : AppContainer{

    private val database: GownGradLocalDatabase by lazy {
        GownGradLocalDatabase.getDatabase(context)
    }

    override val gownGradRepository: GownGradRepository by lazy {
        LocalRepository(database)
    }

}
package com.example.gowngrad.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gowngrad.data.local.institution.InstitutionItem
import com.example.gowngrad.data.local.institution.InstitutionItenDao
import com.example.gowngrad.data.local.size.SizeItem
import com.example.gowngrad.data.local.size.SizeItemDao

@Database(entities = [InstitutionItem::class, SizeItem::class], version = 1, exportSchema = false)
abstract class GownGradLocalDatabase : RoomDatabase() {

    abstract fun institutionItemDao(): InstitutionItenDao
    abstract fun sizeItemDao(): SizeItemDao

    companion object {
        @Volatile
        private var Instance: GownGradLocalDatabase? = null

        fun getDatabase(context: Context): GownGradLocalDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GownGradLocalDatabase::class.java, "gowngrad_database")
                    .fallbackToDestructiveMigration()//its gonna delete current database
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
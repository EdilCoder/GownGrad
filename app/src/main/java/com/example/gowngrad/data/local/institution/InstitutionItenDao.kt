package com.example.gowngrad.data.local.institution

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InstitutionItenDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(institutionItem: InstitutionItem)

    @Update
    suspend fun update(institutionItem: InstitutionItem)

    @Delete
    suspend fun delete(institutionItem: InstitutionItem)

    @Query("SELECT * FROM institutions ORDER BY id DESC LIMIT 1")
    fun getItem(): Flow<InstitutionItem>

    /*@Query("SELECT * FROM institutions ORDER BY institutionName ASC")
    fun getAllItems(): Flow<List<InstitutionItem>>*/
}
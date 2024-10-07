package com.example.gowngrad.data.local.size

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SizeItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sizeItem: SizeItem)

    @Update
    suspend fun update(sizeItem: SizeItem)

    @Delete
    suspend fun delete(sizeItem: SizeItem)

    /*@Query("SELECT * FROM size WHERE id = :id")*/
    @Query("SELECT * FROM size ORDER BY id DESC LIMIT 1")
    fun getItem(): Flow<SizeItem>

    /*@Query("SELECT * FROM institutions ORDER BY institutionName ASC")
    fun getAllItems(): Flow<List<InstitutionItem>>*/

}

package com.example.gowngrad.data

import com.example.gowngrad.data.local.institution.InstitutionItem
import com.example.gowngrad.data.local.size.SizeItem
import kotlinx.coroutines.flow.Flow

interface GownGradRepository {


    //local
    fun getInstitutionItemStream(): Flow<InstitutionItem?>

    suspend fun insertInstitutionItem(item: InstitutionItem)

    suspend fun deleteInstitutionItem(item: InstitutionItem)

    suspend fun updateInstitutionItem(item: InstitutionItem)

    fun getSizeItemStream(): Flow<SizeItem?>

    suspend fun insertSizeItem(item: SizeItem)

    suspend fun deleteSizeItem(item: SizeItem)

    suspend fun updateSizeItem(item: SizeItem)

}
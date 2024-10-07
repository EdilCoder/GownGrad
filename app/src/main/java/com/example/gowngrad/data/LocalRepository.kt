package com.example.gowngrad.data

import com.example.gowngrad.data.local.GownGradLocalDatabase
import com.example.gowngrad.data.local.institution.InstitutionItem
import com.example.gowngrad.data.local.size.SizeItem
import kotlinx.coroutines.flow.Flow

class LocalRepository(private val database : GownGradLocalDatabase) : GownGradRepository {

    override fun getInstitutionItemStream(): Flow<InstitutionItem?> = database.institutionItemDao().getItem()

    override suspend fun insertInstitutionItem(item: InstitutionItem) = database.institutionItemDao().insert(item)

    override suspend fun deleteInstitutionItem(item: InstitutionItem) = database.institutionItemDao().delete(item)

    override suspend fun updateInstitutionItem(item: InstitutionItem) = database.institutionItemDao().update(item)

    override fun getSizeItemStream(): Flow<SizeItem?> = database.sizeItemDao().getItem()

    override suspend fun insertSizeItem(item: SizeItem) = database.sizeItemDao().insert(item)

    override suspend fun deleteSizeItem(item: SizeItem) = database.sizeItemDao().delete(item)

    override suspend fun updateSizeItem(item: SizeItem) = database.sizeItemDao().update(item)

}

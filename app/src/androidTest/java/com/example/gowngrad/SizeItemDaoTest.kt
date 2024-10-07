package com.example.gowngrad

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gowngrad.data.local.GownGradLocalDatabase
import com.example.gowngrad.data.local.size.SizeItem
import com.example.gowngrad.data.local.size.SizeItemDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SizeItemDaoTest {

    private lateinit var database: GownGradLocalDatabase
    private lateinit var sizeItemDao: SizeItemDao

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, GownGradLocalDatabase::class.java
        ).allowMainThreadQueries().build()

        sizeItemDao = database.sizeItemDao()
    }

    @After
    fun tearDown() {

        database.close()
    }

    @Test
    fun testInsertAndGetItem() = runBlocking {

        val sizeItem = SizeItem(id = 1, height = 80.0, chest = 60.0, hat = 70.0)
        sizeItemDao.insert(sizeItem)


        val result = sizeItemDao.getItem().first()


        assertThat(result.height, `is`(80.0))
        assertThat(result.chest, `is`(60.0))
        assertThat(result.hat, `is`(70.0))
    }

    @Test
    fun testUpdateSizeItem() = runBlocking {

        val sizeItem = SizeItem(id = 1, height = 80.0, chest = 60.0, hat = 70.0)
        sizeItemDao.insert(sizeItem)


        val updatedSizeItem = sizeItem.copy(height = 85.0, chest = 65.0, hat = 75.0)
        sizeItemDao.update(updatedSizeItem)

        val result = sizeItemDao.getItem().first()

        assertThat(result.height, `is`(85.0))
        assertThat(result.chest, `is`(65.0))
        assertThat(result.hat, `is`(75.0))
    }

    @Test
    fun testDeleteSizeItem() = runBlocking {

        val sizeItem = SizeItem(id = 1, height = 80.0, chest = 60.0, hat = 70.0)
        sizeItemDao.insert(sizeItem)

        sizeItemDao.delete(sizeItem)

        val result = sizeItemDao.getItem().firstOrNull()

        assertThat(result, `is`(nullValue()))
    }
}


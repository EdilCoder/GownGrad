package com.example.gowngrad.data.local.size

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "size")
data class SizeItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val height: Double,
    val chest: Double,
    val hat: Double,
)

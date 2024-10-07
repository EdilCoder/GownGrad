package com.example.gowngrad.data.local.institution

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "institutions")
data class InstitutionItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val institutionName: String,
    val degreeType: String
)

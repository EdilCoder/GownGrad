package com.example.gowngrad.data.remote.firebase

import com.google.firebase.firestore.DocumentId

data class OrderItem(
    @DocumentId val id: String = "",
    val institution: String = "",
    val degreeType: String = "",
    val height: String = "",
    val chest: String = "",
    val hat: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


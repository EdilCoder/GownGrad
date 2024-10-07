package com.example.gowngrad.data.remote.firebase

import com.google.firebase.firestore.DocumentId

data class Item(
    @DocumentId val id: String = "",
    val fullName: String = "",
    val streetAddress: String = "",
    val zipCode: String = "",
    val phoneNumber: String = "",
    val completed: Boolean = false,
    val userId: String = ""
)

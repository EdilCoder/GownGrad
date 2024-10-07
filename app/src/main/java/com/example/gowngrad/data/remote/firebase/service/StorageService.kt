package com.example.gowngrad.data.remote.firebase.service

import kotlinx.coroutines.flow.Flow
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.OrderItem

interface StorageService {
    val items: Flow<List<Item>>
    val orders: Flow<List<OrderItem>>
    suspend fun getItem(itemId: String): Item?
    suspend fun save(item: Item): String
    suspend fun update(item: Item)
    suspend fun delete(itemId: String)
    suspend fun saveOrders(order: OrderItem): String
    suspend fun deleteOrders(orderId: String)
    suspend fun getUserOrders(orderId: String): OrderItem?
}
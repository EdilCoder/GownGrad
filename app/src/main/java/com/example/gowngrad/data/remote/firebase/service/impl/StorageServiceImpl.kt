package com.example.gowngrad.data.remote.firebase.service.impl

import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.OrderItem
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.data.remote.firebase.service.trace
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
    StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val items: Flow<List<Item>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(ITEM_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val orders: Flow<List<OrderItem>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(ITEM_ORDER).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
        }

    override suspend fun getItem(itemId: String): Item? =
        firestore.collection(ITEM_COLLECTION).document(itemId).get().await().toObject()


    override suspend fun save(item: Item): String =
        trace(SAVE_ITEM_TRACE) {
            val itemWithUserId = item.copy(userId = auth.currentUserId)
            firestore.collection(ITEM_COLLECTION).add(itemWithUserId).await().id
        }

    override suspend fun update(item: Item): Unit =
        trace(UPDATE_ITEM_TRACE) {
            firestore.collection(ITEM_COLLECTION).document(item.id).set(item).await()
        }

    override suspend fun delete(itemId: String) {
        firestore.collection(ITEM_COLLECTION).document(itemId).delete().await()
    }

    override suspend fun saveOrders(order: OrderItem): String =
        trace(SAVE_ORDER_TRACE) {
            val orderWithUserId = order.copy(userId = auth.currentUserId)
            firestore.collection(ITEM_ORDER).add(orderWithUserId).await().id
        }

    override suspend fun getUserOrders(orderId: String): OrderItem? {
        return firestore.collection(ITEM_ORDER).document(orderId).get().await().toObject()
    }

    override suspend fun deleteOrders(orderId: String) {
        firestore.collection(ITEM_ORDER).document(orderId).delete().await()
    }


    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val ITEM_COLLECTION = "items"
        private const val SAVE_ITEM_TRACE = "saveItem"
        private const val UPDATE_ITEM_TRACE = "updateItem"
        private const val ITEM_ORDER = "orders"
        private const val SAVE_ORDER_TRACE = "saveOrder"
    }
}

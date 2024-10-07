package com.example.gowngrad.ui.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.OrderItem
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class GownGradViewModel @Inject constructor(
    val logService: LogService,
    protected val accountService: AccountService,
    protected val storageService: StorageService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var selectedAddressId: MutableState<String?> = mutableStateOf(savedStateHandle["selectedAddressId"])

    var selectedAddress: MutableState<Item?> = mutableStateOf(null)
        private set

    var institution: String = ""
    var degreeType: String = ""
    var height: String = ""
    var chest: String = ""
    var hat: String = ""

    fun placeOrderToFirebase(onComplete: () -> Unit) {
        viewModelScope.launch {
            Log.d("GownGradViewModel", "Placing Order - Institution: $institution")
            Log.d("GownGradViewModel", "Degree Type: $degreeType")
            Log.d("GownGradViewModel", "Height: $height")
            Log.d("GownGradViewModel", "Chest: $chest")
            Log.d("GownGradViewModel", "Hat: $hat")

            val orderItem = OrderItem(
                institution = institution,
                degreeType = degreeType,
                height = height,
                chest = chest,
                hat = hat
            )
            storageService.saveOrders(orderItem)
            onComplete()
        }
    }

    fun selectAddress(item: Item) {
        selectedAddress.value = item
        selectedAddressId.value = item.id
        savedStateHandle["selectedAddressId"] = item.id

        Log.d("GownGradViewModel", "Selected Address ID Saved: ${savedStateHandle.get<String>("selectedAddressId")}")
    }


    val uiState: StateFlow<GownGradUiState> = accountService.currentUser.map {
        GownGradUiState(it.isAnonymous)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, GownGradUiState())

    protected fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}
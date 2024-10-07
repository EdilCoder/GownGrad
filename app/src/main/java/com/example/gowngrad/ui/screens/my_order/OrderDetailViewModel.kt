package com.example.gowngrad.ui.screens.my_order

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.OrderItem
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.common.idFromParameter
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    accountService: AccountService,
    storageService: StorageService,
    logService: LogService,
    savedStateHandle: SavedStateHandle
): GownGradViewModel(logService, accountService, storageService ,savedStateHandle)  {

    val orderItem = mutableStateOf(OrderItem())
    init {
        val ordrItemId = savedStateHandle.get<String>(OrderDetailDestination.orderIdArg)
        launchCatching {
            /*orderItem.value = storageService.getUserOrders(ordrItemId!!.idFromParameter()) ?: OrderItem()*/
            ordrItemId?.let { id ->
                orderItem.value = storageService.getUserOrders(id.idFromParameter()) ?: OrderItem()
            } ?: run {

                Log.d("OrderError", "ordrItemId is null")
            }
        }
    }
}

package com.example.gowngrad.ui.screens.my_order

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.OrderItem
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import com.example.gowngrad.ui.screens.settings.ItemActionOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MyOrderViewModel @Inject constructor(
    accountService: AccountService,
    storageService: StorageService,
    logService: LogService,
    savedStateHandle: SavedStateHandle
): GownGradViewModel(logService, accountService, storageService ,savedStateHandle)  {

    val orders = storageService.orders
    val options = mutableStateOf<List<String>>(listOf())

    fun onOrderActionClick(openScreen: (String) -> Unit, orderItem: OrderItem, action: String) {
        when (OrderActionOption.getByTitle(action)) {
            OrderActionOption.OrderDetailTask -> openScreen("${OrderDetailDestination.route}/${orderItem.id}")
            OrderActionOption.DeleteTask -> onDeleteOrderItemClick(orderItem)
        }
    }

    fun loadOrderItemOptions() {
        options.value = OrderActionOption.getOptions()
    }

    private fun onDeleteOrderItemClick(orderItem: OrderItem) {
        launchCatching { storageService.deleteOrders(orderItem.id) }
    }
}

enum class OrderActionOption(val title: String) {
    OrderDetailTask("More details"),
    DeleteTask("Delete Order");

    companion object {
        fun getByTitle(title: String): OrderActionOption {
            values().forEach { action -> if (title == action.title) return action }

            return OrderDetailTask
        }

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { taskAction ->
                options.add(taskAction.title)
            }
            return options
        }
    }
}
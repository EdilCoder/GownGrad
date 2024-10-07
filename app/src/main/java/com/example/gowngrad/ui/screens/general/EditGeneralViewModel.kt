package com.example.gowngrad.ui.screens.general

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.common.idFromParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditGeneralViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    storageService: StorageService,
    accountService: AccountService,
) : GownGradViewModel(logService, accountService, storageService, savedStateHandle) {

    val item = mutableStateOf(Item())
    init {
        val itemId = savedStateHandle.get<String>(EditGeneralDestination.itemIdArg)
        if (itemId != null) {
            launchCatching {
                item.value = storageService.getItem(itemId.idFromParameter()) ?: Item()
            }
        }
    }

    fun onFullNameChange(newValue: String) {
        item.value = item.value.copy(fullName = newValue)
    }

    fun onStreetAddressChange(newValue: String) {
        item.value = item.value.copy(streetAddress = newValue)
    }

    fun onZipChange(newValue: String) {
        item.value = item.value.copy(zipCode = newValue)
    }

    fun onPhoneNumberChange(newValue: String) {
        item.value = item.value.copy(phoneNumber = newValue)
    }

    fun onSaveClick(onComplete: () -> Unit) {
        launchCatching {
            val editedItem = item.value
            storageService.update(editedItem)
            onComplete()
        }
    }
}

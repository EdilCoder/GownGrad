package com.example.gowngrad.ui.screens.payment

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.common.idFromParameter
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import com.example.gowngrad.ui.screens.settings.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    storageService: StorageService,
    logService: LogService,
    accountService: AccountService,
) : GownGradViewModel(logService, accountService, storageService, savedStateHandle,) {

    val item = mutableStateOf(Item())

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

    fun onSaveClick() {
        launchCatching {
            val editedItem = item.value
            storageService.update(editedItem)
        }
    }
}



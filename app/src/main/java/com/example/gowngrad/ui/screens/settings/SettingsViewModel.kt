package com.example.gowngrad.ui.screens.settings
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gowngrad.R
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.ui.screens.GownGradViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.gowngrad.ui.screens.GownGradUiState
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import com.example.gowngrad.ui.screens.payment.CheckoutDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    accountService: AccountService,
    storageService: StorageService,
    logService: LogService,
    savedStateHandle: SavedStateHandle
): GownGradViewModel(logService, accountService, storageService ,savedStateHandle)  {
    val options = mutableStateOf<List<String>>(listOf())

    val items = storageService.items
    var errorMessage = mutableStateOf<Int?>(null)
        private set


    fun onItemCheckChange(item: Item, isChecked: Boolean) {

        if (isChecked) {

            selectedAddressId.value = item.id
        }


        launchCatching { storageService.update(item.copy(completed = isChecked)) }
    }

    fun onItemActionClick(openScreen: (String) -> Unit, item: Item, action: String) {
        when (ItemActionOption.getByTitle(action)) {
            ItemActionOption.EditTask -> openScreen("${EditGeneralDestination.route}/${item.id}")
            ItemActionOption.DeleteTask -> onDeleteItemClick(item)
        }
    }

    private fun onDeleteItemClick(item: Item) {
        launchCatching { storageService.delete(item.id) }
    }

    fun loadItemOptions() {
        options.value = ItemActionOption.getOptions()
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                accountService.signOut()
                onSignOutComplete()
            } catch (e: Exception) {
                showErrorMessage(R.string.sign_out_error)
            }
        }
    }

    fun deleteAccount(onAccountDeleted: () -> Unit) {
        viewModelScope.launch {

            try {
                accountService.deleteAccount()
                onAccountDeleted()
            } catch (e: Exception) {
                showErrorMessage(R.string.delete_account_error)
            }
        }
    }
    private fun showErrorMessage(messageResId: Int) {
        errorMessage.value = messageResId
        viewModelScope.launch {
            delay(5000)
            errorMessage.value = null
        }
    }
}

enum class ItemActionOption(val title: String) {
    EditTask("Edit Address"),
    DeleteTask("Delete Address");

    companion object {
        fun getByTitle(title: String): ItemActionOption {
            values().forEach { action -> if (title == action.title) return action }

            return EditTask
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


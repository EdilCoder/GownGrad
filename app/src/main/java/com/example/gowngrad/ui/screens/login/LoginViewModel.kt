package com.example.gowngrad.ui.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gowngrad.R
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.ui.screens.common.isValidEmail
import com.example.gowngrad.ui.screens.general.GeneralDestination
import com.example.gowngrad.ui.screens.settings.SettingsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
): ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    var errorMessage = mutableStateOf<Int?>(null)
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit, returnDestination: String?, navController: NavController) {
        if (!email.isValidEmail()) {
            showErrorMessage(R.string.email_error)
            return
        }

        if (password.isBlank()) {
            showErrorMessage(R.string.empty_password_error)
            return
        }

        viewModelScope.launch {
            try {
                accountService.authenticate(email, password)

                val userItems = storageService.items.first()
                val hasAddress = userItems.any { it.fullName.isNotBlank() }

                if (!hasAddress) {

                    navController.navigate("${GeneralDestination.route}/$returnDestination")

                }else{
                    val destination = returnDestination ?: SettingsDestination.route
                    Log.d("LoginViewModel", "destination: $destination")
                    openAndPopUp(destination, LoginDestination.route)
                }

            } catch (e: Exception) {
                showErrorMessage(R.string.login_in_failed)
            }
        }

    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            showErrorMessage(R.string.email_error)
            return
        }

        viewModelScope.launch {
            try {
                accountService.sendRecoveryEmail(email)
                showErrorMessage(R.string.recovery_email_sent)
            } catch (e: Exception) {
                showErrorMessage(R.string.error_recovery_email)
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
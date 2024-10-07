package com.example.gowngrad.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.welcome.StartDestination
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    /*private val accountService: AccountService,*/
    accountService: AccountService,
    storageService: StorageService,
    logService: LogService,
    savedStateHandle: SavedStateHandle
) : GownGradViewModel(logService, accountService, storageService, savedStateHandle) {

    val showError = mutableStateOf(false)

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        showError.value = false
        if (accountService.hasUser) {
            openAndPopUp(StartDestination.route, SplashDestination.route)
        } else {
            createAnonymousAccount(openAndPopUp)
        }
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching() {
            try {
                accountService.createAnonymousAccount()

            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }
            openAndPopUp(StartDestination.route, SplashDestination.route)
        }
    }
}

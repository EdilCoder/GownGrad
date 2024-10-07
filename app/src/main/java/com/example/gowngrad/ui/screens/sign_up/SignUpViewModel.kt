package com.example.gowngrad.ui.screens.sign_up


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gowngrad.R
import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.ui.screens.common.isValidEmail
import com.example.gowngrad.ui.screens.common.isValidPassword
import com.example.gowngrad.ui.screens.common.passwordMatches
import com.example.gowngrad.ui.screens.general.GeneralDestination
import com.example.gowngrad.ui.screens.payment.CheckoutDestination
import com.example.gowngrad.ui.screens.settings.SettingsDestination
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    var uiState = mutableStateOf(SignUpUiState())
        private set

    var errorMessage = mutableStateOf<String?>(null)
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

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit, returnDestination: String?, navController: NavController) {
        Log.d("SignUp", "SignUp button clicked")

        if (!email.isValidEmail()) {
            /*showErrorMessage(R.string.email_error)*/
            showErrorMessage("Invalid email: $email")
            Log.e("SignUp", "Invalid email: $email")
            return
        }

        if (!password.isValidPassword()) {
            /*showErrorMessage(R.string.password_error)*/
            showErrorMessage("Invalid password: $password")
            Log.e("SignUp", "Invalid password")
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            /*showErrorMessage(R.string.password_match_error)*/
            showErrorMessage("Passwords do not match")
            Log.e("SignUp", "Passwords do not match")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("SignUp", "Attempting to sign up with email: $email, password: $password")
                accountService.linkAccount(email, password)
                Log.d("SignUp", "Sign up successful, navigating...")
                /*showErrorMessage(R.string.sign_up_success)*/
                Log.d("SignUp", "returnDestination: $returnDestination")
                if (returnDestination == CheckoutDestination.route) {
                    openAndPopUp(SettingsDestination.route, SignUpDestination.route)
                } else {
                    navController.navigate("${GeneralDestination.route}/$returnDestination")
                }
            } catch (e: IllegalStateException) {
                Log.e("SignUp", "Sign up failed: ${e.message}")
                showErrorMessage("Sign up failed: ${e.message}")
            } catch (e: FirebaseAuthException) {
                Log.e("SignUp", "Firebase auth failed: ${e.message}")
                /*showErrorMessage(R.string.firebase_auth_error)*/
                showErrorMessage("Firebase auth failed: ${e.message}")
            } catch (e: Exception) {
                Log.e("SignUp", "Sign up failed with exception: ${e.message}")
                e.printStackTrace()
                /*showErrorMessage(R.string.sign_in_failed)*/
                showErrorMessage("Sign up failed with exception: ${e.message}")
            }
        }
    }

    private fun showErrorMessage(message: String) {
        errorMessage.value = message
        viewModelScope.launch {
            delay(5000)
            errorMessage.value = null
        }
    }

}

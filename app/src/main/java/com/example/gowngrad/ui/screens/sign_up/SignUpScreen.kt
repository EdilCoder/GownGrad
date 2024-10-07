package com.example.gowngrad.ui.screens.sign_up

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.common.BasicButton
import com.example.gowngrad.ui.screens.common.EmailField
import com.example.gowngrad.ui.screens.common.PasswordField
import com.example.gowngrad.ui.screens.common.RepeatPasswordField


object SignUpDestination : NavigationDestination {
    override val route = "sign up"
    override val titleRes = R.string.sign_up
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    returnDestination: String?,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState
    val errorMessage by viewModel.errorMessage

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(SignUpDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = {}
            )
        }
    ){innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            SignUpScreenContent(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
                onSignUpClick = {
                    Log.d("SignUp", "Create Account button clicked")
                    viewModel.onSignUpClick(openAndPopUp,returnDestination,navController) },
                modifier = Modifier.padding(innerPadding)
            )

            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                )
            }
        }

    }

}

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit
) {
    val fieldModifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp)

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, onEmailChange, fieldModifier)
        PasswordField(uiState.password, onPasswordChange, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, onRepeatPasswordChange, fieldModifier)

        BasicButton(R.string.create_account, Modifier.fillMaxWidth().padding(16.dp, 8.dp)) {
            onSignUpClick()
        }
    }
}

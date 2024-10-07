package com.example.gowngrad.ui.screens.login


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.common.BasicButton
import com.example.gowngrad.ui.screens.common.BasicTextButton
import com.example.gowngrad.ui.screens.common.EmailField
import com.example.gowngrad.ui.screens.common.PasswordField
import com.example.gowngrad.ui.theme.GownGradTheme

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    openAndPopUp: (String, String) -> Unit,
    returnDestination: String?,
    viewModel: LoginViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState

    val errorMessageResId by viewModel.errorMessage

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(LoginDestination.titleRes),
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
            LoginScreenContent(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onSignInClick = {viewModel.onSignInClick(openAndPopUp,returnDestination,navController)},
                onForgotPasswordClick = viewModel::onForgotPasswordClick,
                modifier = Modifier.padding(innerPadding)
            )

            errorMessageResId?.let { resId ->
                Text(
                    text = stringResource(id = resId),
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
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailField(uiState.email, onEmailChange, Modifier.fillMaxWidth().padding(16.dp, 4.dp))
        PasswordField(uiState.password, onPasswordChange, Modifier.fillMaxWidth().padding(16.dp, 4.dp))

        BasicButton(R.string.sign_in, Modifier.fillMaxWidth().padding(16.dp, 8.dp)) { onSignInClick() }

        BasicTextButton(R.string.forgot_password, Modifier.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)) {
            onForgotPasswordClick()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val uiState = LoginUiState(
        email = "email@test.com"
    )

    GownGradTheme {
        LoginScreenContent(
            uiState = uiState,
            onEmailChange = { },
            onPasswordChange = { },
            onSignInClick = { },
            onForgotPasswordClick = { }
        )
    }
}

package com.example.gowngrad.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gowngrad.R
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.common.BasicButton
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

object SplashDestination : NavigationDestination {
    override val route = "splash"
    override val titleRes = R.string.splash_title
}

@Composable
fun SplashScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    SplashScreenContent(
        onAppStart = { viewModel.onAppStart(openAndPopUp) },
        shouldShowError =  viewModel.showError.value
    )
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    onAppStart: () -> Unit,
    shouldShowError: Boolean
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (shouldShowError) {
            Text(text = stringResource(R.string.generic_error))

            BasicButton(R.string.try_again, Modifier.fillMaxWidth().padding(16.dp, 8.dp)) { onAppStart() }
        } else {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        }
    }

    LaunchedEffect(Unit) {
        delay(SPLASH_TIMEOUT)
        if (!shouldShowError) {
            onAppStart()
        }
    }
}


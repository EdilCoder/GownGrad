package com.example.gowngrad.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gowngrad.R
import com.example.gowngrad.ui.navigation.NavigationDestination

object StartDestination : NavigationDestination {
    override val route = "start"
    override val titleRes = R.string.app_name
}

@Composable
fun StartScreen(
    navigateToInstitution: () -> Unit,
    modifier: Modifier = Modifier
) {
    val image = painterResource(R.drawable.startscreenbackground)
    val viewModel: StartViewModel = hiltViewModel()

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier
                /*.align(Alignment.CenterHorizontally)*/
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            /*horizontalAlignment = Alignment.CenterHorizontally*/
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                text = stringResource(R.string.start_screen_welcome),
                style = TextStyle(
                    fontSize = 45.sp
                )/*MaterialTheme.typography.displayMedium*/,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 150.dp, start = 30.dp, end = 30.dp)
            )
            Text(
                text = stringResource(R.string.start_screen_describe),
                modifier = Modifier
                    .padding(top = 10.dp)
                    /*.align(alignment = Alignment.CenterHorizontally)*/
            )
            Spacer(modifier = Modifier.height(390.dp))
            Button(
                onClick = {
                    viewModel.onStartButtonClicked()
                    navigateToInstitution()
                },
                Modifier
                    .widthIn(min = 250.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(stringResource(R.string.sign_in_button).uppercase())
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun StartScreenPreview(){
    StartScreen(
        navigateToInstitution = {}
    )
}
package com.example.gowngrad.ui.screens.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.bag.BagDestination
import com.example.gowngrad.ui.theme.GownGradTheme


object PaymentSuccessDestination : NavigationDestination {
    override val route = "PaymentSuccess"
    override val titleRes = R.string.payment_success
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSuccessScreen(
    onNavigateUp: () -> Unit,
    onNavigateSetting: () -> Unit,
    canNavigateBack: Boolean = false,
    onContinueClick: () -> Unit
) {
    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(PaymentSuccessDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = onNavigateSetting
            )
        }
    ){innerPadding ->

        PaymentSuccessContent(
            modifier = Modifier
                .padding(innerPadding),
            onContinueClick = onContinueClick
        )

    }
}

@Composable
private fun PaymentSuccessContent(
    modifier: Modifier,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            painter = painterResource(id = R.drawable.payment_success),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            text = stringResource(id = R.string.payment_success),
            textAlign = TextAlign.Center
        )
        Button(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = onContinueClick,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.continue_shopping),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}



@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun PaymentSuccessContentPreview() {
    GownGradTheme {
        Surface {
            PaymentSuccessContent(
                onContinueClick = {},
                modifier = Modifier
            )
        }
    }
}
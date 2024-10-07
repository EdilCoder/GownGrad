package com.example.gowngrad.ui.screens.general

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.common.BasicField
import com.example.gowngrad.ui.screens.login.LoginDestination
import com.example.gowngrad.ui.screens.settings.SettingsDestination
import com.example.gowngrad.ui.theme.GownGradTheme

object GeneralDestination : NavigationDestination {
    override val route = "general"
    override val titleRes = R.string.general
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralScreen(
    navController: NavController,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: GeneralViewModel = hiltViewModel(),
){

    val item by viewModel.item

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(GeneralDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = {}
            )
        }
    ){
            innerPadding ->
        GeneralBody(
            item = item,
            onFullNameChange = viewModel::onFullNameChange,
            onStreetAddressChange = viewModel::onStreetAddressChange,
            onZipChange = viewModel::onZipChange,
            onPhoneNumberChange = viewModel::onPhoneNumberChange,
            onSaveClick = {
                viewModel.onSaveClick {
                    /*navController.previousBackStackEntry?.savedStateHandle?.set("isAddressAdded", true)
                    navController.popBackStack()*/
                    val returnDestination = navController.previousBackStackEntry?.arguments?.getString("returnDestination")
                    Log.d("GeneralScreen", "returnDestination: $returnDestination")
                    navController.navigate("${SettingsDestination.route}/$returnDestination") {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun GeneralBody(
    item: Item,
    onFullNameChange: (String) -> Unit,
    onStreetAddressChange: (String) -> Unit,
    onZipChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val isFullNameError = item.fullName.isBlank() && hasAttemptedSubmit
    val isStreetAddressError = item.streetAddress.isBlank() && hasAttemptedSubmit
    val isZipCodeError = item.zipCode.isBlank() && hasAttemptedSubmit
    val isPhoneNumberError = item.phoneNumber.isBlank() && hasAttemptedSubmit

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fieldModifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp)

        BasicField(
            R.string.full_name,
            item.fullName,
            onNewValue = {
                onFullNameChange(it)
                hasAttemptedSubmit = true
            },
            fieldModifier,
            isFullNameError
        )

        BasicField(
            R.string.street_address,
            item.streetAddress,
            onNewValue = {
                onStreetAddressChange(it)
                hasAttemptedSubmit = true
            },
            fieldModifier,
            isStreetAddressError
        )

        BasicField(
            R.string.zip_code,
            item.zipCode,
            onNewValue = {
                onZipChange(it)
                hasAttemptedSubmit = true
            },
            fieldModifier,
            isZipCodeError
        )

        BasicField(
            R.string.phone_number,
            item.phoneNumber,
            onNewValue = {
                onPhoneNumberChange(it)
                hasAttemptedSubmit = true
            },
            fieldModifier,
            isPhoneNumberError
        )

        Button(
            onClick = {
                hasAttemptedSubmit = true
                onSaveClick()
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GeneralBodyPreview() {
    GownGradTheme {
        GeneralBody(
            item = Item(),
            onFullNameChange = {},
            onStreetAddressChange = {},
            onZipChange = {},
            onPhoneNumberChange = {},
            onSaveClick = {}
        )
    }
}

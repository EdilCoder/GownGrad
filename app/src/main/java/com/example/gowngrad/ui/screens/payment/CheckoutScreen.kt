package com.example.gowngrad.ui.screens.payment


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gowngrad.R
import com.example.gowngrad.ui.navigation.NavigationDestination
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.LocalGownGradViewModel
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.ui.screens.GownGradUiState
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.common.BasicField
import com.example.gowngrad.ui.screens.common.ShowField
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import com.example.gowngrad.ui.screens.settings.SettingsUiState

object CheckoutDestination : NavigationDestination {
    override val route = "CHECKOUT"
    override val titleRes = R.string.checkout
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onNavigateUp: () -> Unit,
    onNavigateSetting: () -> Unit,
    canNavigateBack: Boolean = true,
    onNavigateToSignUpScreen:() -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel(),
    onNavigateToSuccess: () -> Unit
){
    val uiState by viewModel.uiState.collectAsState(
        initial = GownGradUiState(false)
    )
    val item by viewModel.item

    val gownGradViewModel = LocalGownGradViewModel.current
    val selectedAddress = gownGradViewModel.selectedAddress.value
    Log.d("CheckoutScreen", "Selected Address: $selectedAddress")

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(CheckoutDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = onNavigateSetting
            )
        }
    ) {innerPadding ->

        CheckoutBody(
            isUserRegistered = uiState,
            item = item,
            showItem = selectedAddress ?: Item(),
            onNavigateToSignUpScreen = onNavigateToSignUpScreen,
            onNavigateToSuccess = onNavigateToSuccess,
            onFullNameChange = viewModel::onFullNameChange,
            onStreetAddressChange = viewModel::onStreetAddressChange,
            onZipChange = viewModel::onZipChange,
            onPhoneNumberChange = viewModel::onPhoneNumberChange,
            onSaveClick = {
                viewModel.onSaveClick()
            },
            gownGradViewModel = gownGradViewModel,
            modifier = Modifier.padding(innerPadding)
        )

    }
}

@Composable
fun CheckoutBody(
    isUserRegistered: GownGradUiState,
    item: Item,
    showItem: Item,
    onNavigateToSignUpScreen: () -> Unit,
    onNavigateToSuccess: () -> Unit = {},
    onFullNameChange: (String) -> Unit,
    onStreetAddressChange: (String) -> Unit,
    onZipChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    gownGradViewModel: GownGradViewModel,
    modifier: Modifier = Modifier
) {
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val isFullNameError = item.fullName.isBlank() && hasAttemptedSubmit
    val isStreetAddressError = item.streetAddress.isBlank() && hasAttemptedSubmit
    val isZipCodeError = item.zipCode.isBlank() && hasAttemptedSubmit
    val isPhoneNumberError = item.phoneNumber.isBlank() && hasAttemptedSubmit

    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }

    val isCardNumberError = cardNumber.isBlank() || cardNumber.length != 16
    val isExpiryDateError = expiryDate.isBlank() || expiryDate.length != 4
    val isCvcError = cvc.isBlank() || cvc.length != 3

    /*val isAddressValid = if (addressEntered) {
        !isFullNameError && !isStreetAddressError && !isZipCodeError && !isPhoneNumberError
    } else {
        true
    }*/

    val isPaymentValid = !isCardNumberError && !isExpiryDateError && !isCvcError

    /*val isFormValid = if (isUserRegistered.isAnonymousAccount) {
        addressEntered && isAddressValid && isPaymentValid
    } else {
        isPaymentValid
    }*/

    Log.d("CheckoutBody", "showItem: $showItem")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BillingInfo(
            isUserRegistered = isUserRegistered,
            item = item,
            showItem = showItem,
            onNavigateToSignUpScreen = onNavigateToSignUpScreen,
            onFullNameChange = onFullNameChange,
            onStreetAddressChange = onStreetAddressChange,
            onZipChange = onZipChange,
            onPhoneNumberChange = onPhoneNumberChange,
            onSavedClick = onSaveClick,

            /*onEnterAddress = { address ->
                onEnterAddress(address)
                hasAttemptedSubmit = true
                addressEntered = true
            },*/
            /*addressEntered = addressEntered*/
        )
        PaymentInfo(
            cardNumber = cardNumber,
            onCardNumberChange = { cardNumber = it },
            expiryDate = expiryDate,
            onExpiryDateChange = { expiryDate = it },
            cvc = cvc,
            onCvcChange = { cvc = it }
        )
        OrderSummary()
        ReturnAddressDisplay()
        Button(
            onClick = {
                hasAttemptedSubmit = true
                /*if (isFormValid) {
                    onNavigateToSuccess()
                }*/
                onNavigateToSuccess()
                if (!isUserRegistered.isAnonymousAccount){
                    gownGradViewModel.placeOrderToFirebase {

                        Log.d("CheckoutBody", "Order placed successfully in Firebase")
                    }
                }
            },
            enabled = isPaymentValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Place Order")
        }
    }
}



@Composable
fun BillingInfo(
    isUserRegistered: GownGradUiState,
    item: Item,
    showItem: Item,
    onNavigateToSignUpScreen: () -> Unit,
    /*onEnterAddress: (Item) -> Unit,
    addressEntered: Boolean*/
    onFullNameChange: (String) -> Unit,
    onStreetAddressChange: (String) -> Unit,
    onZipChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSavedClick: () -> Unit
){

    val showEnterAddress = remember { mutableStateOf(false) }
    val showButton = remember { mutableStateOf(true) }
    val showDialog = remember { mutableStateOf(false) }

    Log.d("BillingInfo", "BillingInfo showItem: $showItem")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.billing_address),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (!isUserRegistered.isAnonymousAccount){
            Log.d("CheckoutScreen", "Show form UnAnonymousAccount")
            ShowingAddress(
                item = showItem,
                onFullNameChange = {},
                onStreetAddressChange = {},
                onZipChange = {},
                onPhoneNumberChange = {},
                modifier = Modifier.fillMaxWidth()
            )
        }else{
            Log.d("CheckoutScreen", "Show form AnonymousAccount")
            if(showButton.value){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick = onNavigateToSignUpScreen,
                        shape = RectangleShape,
                        modifier = Modifier
                            .size(110.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.create_account_checkout),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        onClick = {
                            showDialog.value = true;
                            showButton.value = false
                        },
                        shape = RectangleShape,
                        modifier = Modifier
                            .size(110.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.enter_address_checkout),
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                title = {
                    Text(stringResource(id = R.string.notice))
                },
                text = {
                    Text(stringResource(id = R.string.notice_text))
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog.value = false
                            showEnterAddress.value = true
                        }
                    ) {
                        Text(stringResource(id = R.string.ok_button))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog.value = false;
                        showButton.value = true}
                    ) {
                        Text(stringResource(id = R.string.cancel_button))
                    }
                }
            )
        }
        /*if (showEnterAddress.value || addressEntered){
            EnterAddress(
                item = item,
                onFullNameChange = { name -> onEnterAddress(item.copy(fullName = name)) },
                onStreetAddressChange = { address -> onEnterAddress(item.copy(streetAddress = address)) },
                onZipChange = { zip -> onEnterAddress(item.copy(zipCode = zip)) },
                onPhoneNumberChange = { phone -> onEnterAddress(item.copy(phoneNumber = phone)) },
                modifier = Modifier.fillMaxWidth()
            )
        }*/
        if (showEnterAddress.value){
            EnterAddress(
                item = item,
                onFullNameChange = onFullNameChange,
                onStreetAddressChange = onStreetAddressChange,
                onZipChange = onZipChange,
                onPhoneNumberChange = onPhoneNumberChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EnterAddress(
    item: Item,
    onFullNameChange: (String) -> Unit,
    onStreetAddressChange: (String) -> Unit,
    onZipChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fieldModifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp)
        BasicField(R.string.full_name, item.fullName, onFullNameChange,fieldModifier)
        BasicField(R.string.street_address, item.streetAddress, onStreetAddressChange, fieldModifier)
        BasicField(R.string.zip_code, item.zipCode, onZipChange, fieldModifier)
        BasicField(R.string.phone_number, item.phoneNumber, onPhoneNumberChange, fieldModifier)
    }
}

@Composable
fun ShowingAddress(
    item: Item,
    onFullNameChange: (String) -> Unit,
    onStreetAddressChange: (String) -> Unit,
    onZipChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier
){

    Log.d("ShowingAddress", "Showing address for item: $item.fullName")

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fieldModifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp)
        ShowField(R.string.full_name, item.fullName, onFullNameChange,fieldModifier)
        ShowField(R.string.street_address, item.streetAddress, onStreetAddressChange, fieldModifier)
        ShowField(R.string.zip_code, item.zipCode, onZipChange, fieldModifier)
        ShowField(R.string.phone_number, item.phoneNumber, onPhoneNumberChange, fieldModifier)
    }
}

@Composable
fun PaymentInfo(
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    expiryDate: String,
    onExpiryDateChange: (String) -> Unit,
    cvc: String,
    onCvcChange: (String) -> Unit
) {
    var cardNumberError by remember { mutableStateOf<String?>(null) }
    var expiryDateError by remember { mutableStateOf<String?>(null) }
    var cvcError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.payment_information),
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            value = cardNumber,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } && newValue.length <= 16) {
                    onCardNumberChange(newValue)
                    cardNumberError = if (newValue.length == 16) null else "Card number must be 16 digits"
                } else {
                    cardNumberError = "Invalid card number"
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(text = stringResource(id = R.string.card_num))
            },
            isError = cardNumberError != null
        )

        cardNumberError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                value = expiryDate,
                onValueChange = { newValue ->
                    val cleanedValue = newValue.filter { it.isDigit() }
                    if (cleanedValue.length <= 4) {
                        onExpiryDateChange(cleanedValue)
                        expiryDateError = if (cleanedValue.length == 4) null else "Expiry date must be MM/YY"
                    } else {
                        expiryDateError = "Invalid expiry date"
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = stringResource(id = R.string.expiry_date))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.expiry_date_placeholder))
                },
                isError = expiryDateError != null
            )

            expiryDateError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                value = cvc,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                        onCvcChange(newValue)
                        cvcError = if (newValue.length == 3) null else "CVC must be 3 digits"
                    } else {
                        cvcError = "Invalid CVC"
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = stringResource(id = R.string.cvc))
                },
                isError = cvcError != null
            )
            cvcError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}



@Composable
fun OrderSummary(){
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        Text(
            text = "ORDER SUMMARY",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Row(
                /*horizontalArrangement = Arrangement.spacedBy(50.dp),*/
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            ) {
                Text(
                    style = MaterialTheme.typography.labelLarge,
                    text = "HIRE PRICE:")
                Spacer(modifier = Modifier.width(110.dp))
                Text(
                    style = MaterialTheme.typography.labelLarge,
                    text = "￡30")
            }
            Row(
                /*horizontalArrangement = Arrangement.spacedBy(50.dp),*/
                modifier = Modifier.padding()
            ) {
                Text(
                    style = MaterialTheme.typography.labelLarge,text = "DEPOSIT PRICE:")
                Spacer(modifier = Modifier.width(84.dp))
                Text(style = MaterialTheme.typography.labelLarge,text = "￡50")
            }
            /*HorizontalDivider(
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
                thickness = dimensionResource(R.dimen.thickness_divider)
            )*/
            Row(
                modifier = Modifier.padding()
            ) {
                Text(style = MaterialTheme.typography.labelLarge,text = "TOTAL PRICE:")
                Spacer(modifier = Modifier.width(98.dp))
                Text(style = MaterialTheme.typography.labelLarge,text = "￡80")
            }
        }

    }
}

@Composable
fun ReturnAddressDisplay() {
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        Text(
        text = "RETURN ADDRESS",
        style = MaterialTheme.typography.titleLarge,
        /*modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(id = R.dimen.two_level_margin))*/
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
            text = "32 Leavygreave Road, Sheffield S3 7RD, United Kingdom, Contact: Mr. Ye, 123-456-7890",
            style = MaterialTheme.typography.labelLarge
        )
    }

}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EnterAddressPreview() {
    EnterAddress(
        item = Item(
            fullName = "John Doe",
            streetAddress = "123 Main St",
            zipCode = "12345",
            phoneNumber = "123-456-7890"
        ),
        onFullNameChange = {},
        onStreetAddressChange = {},
        onZipChange = {},
        onPhoneNumberChange = {}
    )
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BillingInfoPreview() {
    val uiState = GownGradUiState(isAnonymousAccount = false)
    BillingInfo(
        isUserRegistered = uiState,
        item = Item(
            fullName = "John Doe",
            streetAddress = "123 Main St",
            zipCode = "12345",
            phoneNumber = "123-456-7890"
        ),
        onNavigateSettingGeneral = {},
        onEnterAddress = {},
        addressEntered = false
    )
}*/


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentInfoPreview(){
    PaymentInfo(
        cardNumber = "1234567890123456",
        onCardNumberChange = {},
        expiryDate = "12/25",
        onExpiryDateChange = {},
        cvc = "123",
        onCvcChange = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderSummaryPreview(){
    OrderSummary()
}

/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckoutScreenPreview() {
    val uiState = GownGradUiState(isAnonymousAccount = true)
    CheckoutBody(
        isUserRegistered = uiState,
        item = Item(
            fullName = "John Doe",
            streetAddress = "123 Main St",
            zipCode = "12345",
            phoneNumber = "123-456-7890"
        ),
        onNavigateSettingGeneral = {},
        onEnterAddress = {},
        onNavigateToSuccess = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckoutScreenFalsePreview() {
    val uiState = GownGradUiState(isAnonymousAccount = false)
    CheckoutBody(
        isUserRegistered = uiState,
        item = Item(),
        onNavigateSettingGeneral = {
        },
        onEnterAddress = { address ->
        },
        onNavigateToSuccess = {
        }
    )
}*/




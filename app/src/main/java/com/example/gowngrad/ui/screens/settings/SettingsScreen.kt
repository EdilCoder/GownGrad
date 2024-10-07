package com.example.gowngrad.ui.screens.settings

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.theme.GownGradTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.gowngrad.LocalGownGradViewModel
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.ui.screens.GownGradUiState
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.common.ItemsList
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import com.example.gowngrad.ui.screens.general.GeneralDestination
import com.example.gowngrad.ui.screens.payment.CheckoutDestination

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutComplete: () -> Unit,
    onDeleteAccountComplete: () -> Unit,
    onGeneralClick: () -> Unit,
    onMyOrdersClick: () -> Unit,
    openScreen: (String) -> Unit,
    canNavigateBack: Boolean = true,
    returnDestination: String,
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState(
        initial = GownGradUiState(false)
    )

    val errorMessageResId by viewModel.errorMessage
    val items = viewModel
        .items
        .collectAsStateWithLifecycle(emptyList())
    val options by viewModel.options
    LaunchedEffect(viewModel) { viewModel.loadItemOptions() }
    val gownGradViewModel = LocalGownGradViewModel.current

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(SettingsDestination.titleRes),
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

            SettingsScreenContent(
                uiState = uiState,
                items = items.value,
                options = options,
                onLoginClick = { onLoginClick() },
                onSignUpClick = { onSignUpClick() },
                onSignOutClick = {viewModel.signOut(onSignOutComplete)  },
                onDeleteMyAccountClick = { viewModel.deleteAccount(onDeleteAccountComplete) },
                onGeneralClick = {
                    onGeneralClick()
                },
                onMyOrdersClick = { onMyOrdersClick() },
                onItemActionClick = viewModel::onItemActionClick,
                openScreen = openScreen,
                onItemCheckChange = viewModel::onItemCheckChange,
                viewModel = viewModel,
                gownGradViewModel = gownGradViewModel,
                returnDestination = returnDestination,
                navController = navController,
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
fun SettingsScreenContent(
    items: List<Item>,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    uiState: GownGradUiState,
    options: List<String>,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit,
    onGeneralClick: () -> Unit,
    onMyOrdersClick: () -> Unit,
    onItemActionClick: ((String) -> Unit, Item, String) -> Unit,
    onItemCheckChange: (Item, Boolean) -> Unit,
    viewModel: SettingsViewModel,
    gownGradViewModel: GownGradViewModel,
    returnDestination: String,
    navController: NavController

){
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        /*item {
            Spacer(modifier = modifier
                .fillMaxWidth()
                .padding(12.dp))
        }*/

        if (uiState.isAnonymousAccount) {
            item {
                RegularCardEditor(
                    R.string.sign_in,
                    R.drawable.ic_sign_in,
                    "",
                    Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
                ) {
                    onLoginClick()
                }
            }
            item {
                RegularCardEditor(
                    R.string.create_account,
                    R.drawable.ic_create_account,
                    "",
                    Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
                ) {
                    onSignUpClick()
                }
            }
        } else {
            item {
                SignOutCard { onSignOutClick() }
            }
            item {
                DeleteMyAccountCard { onDeleteMyAccountClick() }
            }
            item {
                RegularCardEditor(
                    R.string.my_order_button_text,
                    R.drawable.ic_settings,
                    "",
                    Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
                ) {
                    onMyOrdersClick()
                }
            }
            item {
                Text(text = "Profile Information")
            }
            item {
                RegularCardEditor(
                    R.string.add_address,
                    R.drawable.ic_settings,
                    "",
                    Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
                ) {
                    onGeneralClick()
                }
            }

            items(items, key = { it.id }) { item ->

                Log.d("DropdownOptions", "Options: $options")
                ItemsList(
                    item = item,
                    options = options,
                    isChecked = viewModel.selectedAddressId.value == item.id,
                    onCheckChange = { isChecked ->

                        onItemCheckChange(item, isChecked)

                        if (isChecked) {
                            Log.d("SettingsScreen", "Address selected: $item")
                            gownGradViewModel.selectAddress(item)
                            if (returnDestination == CheckoutDestination.route) {
                                navController.navigate(CheckoutDestination.route)
                            }
                        }
                    },
                    onActionClick = { action -> onItemActionClick(openScreen, item, action) }
                )
            }
        }
    }
}

@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
){
    Card(
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) { Text(stringResource(title)) }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(painter = painterResource(icon), contentDescription = "Icon")
        }
    }
}

@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        R.string.delete_my_account,
        R.drawable.ic_delete_my_account,
        "",
        Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.delete_account_title)) },
            text = { Text(stringResource(R.string.delete_account_description)) },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.delete_my_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(R.string.sign_out, R.drawable.ic_exit, "", Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.sign_out_title)) },
            text = { Text(stringResource(R.string.sign_out_description)) },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Composable
fun DialogCancelButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun DialogConfirmButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun DangerousCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) { Text(stringResource(title)) }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(painter = painterResource(icon), contentDescription = "Icon")
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val uiState = GownGradUiState(isAnonymousAccount = false)
    GownGradTheme {
        SettingsScreenContent(
            uiState = uiState,
            onLoginClick = { },
            onSignUpClick = { },
            onSignOutClick = { },
            onDeleteMyAccountClick = { },
            onGeneralClick = { },
            onMyOrdersClick = { },
            onEditGeneralClick = { },
            *//*address = null,*//*
            modifier = Modifier，

        )
    }
}*/

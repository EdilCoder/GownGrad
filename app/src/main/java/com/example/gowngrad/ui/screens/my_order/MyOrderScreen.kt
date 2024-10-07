package com.example.gowngrad.ui.screens.my_order

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.LocalGownGradViewModel
import com.example.gowngrad.R
import com.example.gowngrad.data.remote.firebase.Item
import com.example.gowngrad.data.remote.firebase.OrderItem
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.GownGradUiState
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.common.ItemsList
import com.example.gowngrad.ui.screens.common.OrderItemsList
import com.example.gowngrad.ui.screens.settings.SettingsDestination
import com.example.gowngrad.ui.screens.settings.SettingsScreenContent
import com.example.gowngrad.ui.screens.settings.SettingsViewModel

object MyOrderDestination : NavigationDestination {
    override val route = "my_order"
    override val titleRes = R.string.my_order
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrderScreen(
    onNavigateUp: () -> Unit,
    openScreen: (String) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: MyOrderViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState(
        initial = GownGradUiState(false)
    )
    val orders = viewModel
        .orders
        .collectAsStateWithLifecycle(emptyList())
    val options by viewModel.options
    LaunchedEffect(viewModel) { viewModel.loadOrderItemOptions() }
    val gownGradViewModel = LocalGownGradViewModel.current

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(MyOrderDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = {}
            )
        }
    ){innerPadding ->

        MyOrderContent(
            uiState = uiState,
            ordersItems = orders.value,
            options = options,
            onOrderActionClick = viewModel::onOrderActionClick,
            openScreen = openScreen,
            viewModel = viewModel,
            gownGradViewModel = gownGradViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MyOrderContent(
    uiState: GownGradUiState,
    ordersItems: List<OrderItem>,
    options: List<String>,
    onOrderActionClick: ((String) -> Unit, OrderItem, String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: MyOrderViewModel,
    gownGradViewModel: GownGradViewModel,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(ordersItems, key = { it.id }) { orderItem ->

            Log.d("DropdownOptions", "Options: $options")
            OrderItemsList(
                orderItem = orderItem,
                options = options,
                onActionClick = { action -> onOrderActionClick(openScreen, orderItem, action) }
            )
        }
    }
}



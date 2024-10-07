package com.example.gowngrad.ui.screens.my_order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.data.remote.firebase.OrderItem
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import com.example.gowngrad.ui.screens.general.EditGeneralViewModel

object OrderDetailDestination : NavigationDestination {
    override val route = "my_order_detail"
    override val titleRes = R.string.my_order_detail
    const val orderIdArg = "itemId"
    val routeWithArgs = "$route/{$orderIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {

    val orderItem by viewModel.orderItem

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(OrderDetailDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = {}
            )
        }
    ){innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 30.dp)
                .verticalScroll(rememberScrollState()),
        ){

            OrderDetailContent(
                orderItem = orderItem,
                modifier = Modifier.padding(innerPadding)
            )
            PriceCard()
        }

    }
}

@Composable
fun OrderDetailContent(
    orderItem: OrderItem,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.padding(100.dp))
        Text(
            text = stringResource(R.string.order_details),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(text = "Institution: ${orderItem.institution}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Degree Type: ${orderItem.degreeType}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Height: ${orderItem.height}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Chest: ${orderItem.chest}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Hat: ${orderItem.hat}", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun PriceCard(){
    Column(
        modifier = Modifier.padding(top = 30.dp,start = 20.dp),
        horizontalAlignment = Alignment.Start

    ) {
        Row(
            /*horizontalArrangement = Arrangement.spacedBy(50.dp),*/
            modifier = Modifier.padding(start = 60.dp)
        ) {
            Text(
                style = MaterialTheme.typography.labelLarge,
                text = stringResource(R.string.hire_price_title))
            Spacer(modifier = Modifier.width(110.dp))
            Text(
                style = MaterialTheme.typography.labelLarge,
                text = stringResource(R.string.hire_price))
        }
        Row(
            /*horizontalArrangement = Arrangement.spacedBy(50.dp),*/
            modifier = Modifier.padding(start = 60.dp)
        ) {
            Text(
                style = MaterialTheme.typography.labelLarge,
                text = stringResource(R.string.deposit_price_title))
            Spacer(modifier = Modifier.width(84.dp))
            Text(style = MaterialTheme.typography.labelLarge,
                text = stringResource(R.string.deposit_price))
        }
        HorizontalDivider(
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
            thickness = dimensionResource(R.dimen.thickness_divider)
        )
        Row(
            modifier = Modifier.padding(start = 60.dp)
        ) {
            Text(style = MaterialTheme.typography.labelLarge,
                text = stringResource(R.string.total_price_title))
            Spacer(modifier = Modifier.width(98.dp))
            Text(style = MaterialTheme.typography.labelLarge,
                text = stringResource(R.string.total_price))
        }

    }
}

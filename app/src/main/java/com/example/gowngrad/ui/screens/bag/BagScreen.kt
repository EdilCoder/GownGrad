package com.example.gowngrad.ui.screens.bag

import android.text.Layout
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.data.local.institution.InstitutionItem
import com.example.gowngrad.data.local.size.SizeItem
import com.example.gowngrad.ui.AppViewModelProvider
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.GownGradViewModel
import com.example.gowngrad.ui.screens.institution.InstitutionItemDetails
import com.example.gowngrad.ui.screens.institution.toItem
import com.example.gowngrad.ui.screens.size.SizeItemDetails
import com.example.gowngrad.ui.screens.size.toItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object BagDestination : NavigationDestination {
    override val route = "BAG"
    override val titleRes = R.string.academic_dress_hire_section
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BagScreen(
    gownGradViewModel: GownGradViewModel,
    onNavigateUp: () -> Unit,
    onNavigateSetting: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: BagViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToCheckout: () -> Unit,
    onNavigateToEdit: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(BagDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = onNavigateSetting
            )
        }
    ) {innerPadding ->
        BagBody(
            itemDetailsUiState = uiState.value,
            checkout = {
                       coroutineScope.launch {

                           Log.d("BagScreen", "Institution: ${uiState.value.institutionItemDetails.institutionName}")
                           Log.d("BagScreen", "Degree Type: ${uiState.value.institutionItemDetails.degreeType}")
                           Log.d("BagScreen", "Height: ${uiState.value.sizeItemDetails.height}")
                           Log.d("BagScreen", "Chest: ${uiState.value.sizeItemDetails.chest}")
                           Log.d("BagScreen", "Hat: ${uiState.value.sizeItemDetails.hat}")

                           gownGradViewModel.institution = uiState.value.institutionItemDetails.institutionName
                           gownGradViewModel.degreeType = uiState.value.institutionItemDetails.degreeType
                           gownGradViewModel.height = uiState.value.sizeItemDetails.height
                           gownGradViewModel.chest = uiState.value.sizeItemDetails.chest
                           gownGradViewModel.hat = uiState.value.sizeItemDetails.hat
                           onNavigateToCheckout()
                       }
            },
            edit = {
                   coroutineScope.launch {
                       onNavigateToEdit()
                   }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BagBody(
    itemDetailsUiState: ItemDetailsUiState,
    checkout: () -> Unit,
    edit: () -> Unit,
    modifier: Modifier
){

    var isAgreeEnabled by remember { mutableStateOf(false) }
    var isCheckoutEnabled by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(3) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 30.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        PriceCard()
        Text(
            fontSize = 14.sp,
            modifier = Modifier
                .padding(16.dp)
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            text = stringResource(R.string.hire_note))

        Button(
            onClick = { isCheckoutEnabled = true },
            enabled = isAgreeEnabled,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(if (isAgreeEnabled) "I agree" else "I agree ($countdown)")
        }

        LaunchedEffect(Unit) {
            while (countdown > 0) {
                delay(1000L)
                countdown--
            }
            isAgreeEnabled = true
        }

        DressDetail(
            institutionItem = itemDetailsUiState.institutionItemDetails.toItem(),
            sizeItem = itemDetailsUiState.sizeItemDetails.toItem(),
            checkout = {
                if (isCheckoutEnabled) {
                    checkout()
                }
            },
            edit = edit,
            isCheckoutEnabled = isCheckoutEnabled
        )
    }
}

@Composable
fun PriceCard(){
    Column(
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

@Composable
fun DressDetail(
    institutionItem: InstitutionItem,
    sizeItem: SizeItem,
    checkout: () -> Unit,
    edit: () -> Unit,
    isCheckoutEnabled: Boolean
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
            painter = painterResource(R.drawable.banchelors),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(220.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(
            text = stringResource(R.string.gowns_details_bag),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = stringResource(R.string.institution_text, institutionItem.institutionName),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.degree_text, institutionItem.degreeType),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.height_text, sizeItem.height),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.chest_text, sizeItem.chest),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.hat_text, sizeItem.hat),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.padding(top = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(50.dp)
        ){
            Button(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .widthIn(min = 130.dp),
                onClick = edit,
            ){
                Text(text = stringResource(R.string.edit_button))
            }

            Button(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .widthIn(min = 100.dp),
                onClick = checkout,
                enabled = isCheckoutEnabled
            ){
                Text(text = stringResource(R.string.checkout_button))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PriceCardPreview(){
    PriceCard()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DressDetailPreview(){
    DressDetail(
        institutionItem = InstitutionItem(
            id = 1,
            institutionName = "University of Oxford",
            degreeType = "Bachelor"
        ),
        sizeItem = SizeItem(
            id = 1,
            height = 170.0,
            chest = 90.0,
            hat = 30.0),
        checkout = {},
        edit = {},
        isCheckoutEnabled = true,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BagBodyPreview(){
    BagBody(
        itemDetailsUiState = ItemDetailsUiState(
            institutionItemDetails = InstitutionItemDetails(
                id = 1,
                institutionName = "University of Oxford",
                degreeType = "Bachelor"
            ),
            sizeItemDetails = SizeItemDetails(
                id = 1,
                height = 170.0.toString(),
                chest = 90.0.toString(),
                hat = 30.0.toString())
        ),
        checkout = {},
        edit = {},
        modifier = Modifier
    )
}
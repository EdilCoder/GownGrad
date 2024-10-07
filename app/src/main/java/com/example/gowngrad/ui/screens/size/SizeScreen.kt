package com.example.gowngrad.ui.screens.size

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.ui.AppViewModelProvider
import com.example.gowngrad.ui.navigation.NavigationDestination

import kotlinx.coroutines.launch

object SizeDestination : NavigationDestination {
    override val route = "SIZE"
    override val titleRes = R.string.size_details
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SizeScreen(
    onNavigateUp: () -> Unit,
    onNavigateSetting: () -> Unit,
    onNavigateBag: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: SizeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(SizeDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = onNavigateSetting
            )
        }
    ) {innerPadding ->

        SizeBody(
            sizeUiState = viewModel.sizeUiState,
            onSizeValueChange = viewModel::updateUiState,
            onBagClick = {
                coroutineScope.launch {
                    viewModel.addToSizeItem()
                    onNavigateBag()
                }
            },
            modifier = Modifier.padding(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding()
            )
        )
    }
}

@Composable
fun SizeBody(
    sizeUiState: SizeUiState,
    onSizeValueChange: (SizeItemDetails) -> Unit,
    onBagClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(rememberScrollState()),
    ){
        Spacer(modifier = Modifier.height(120.dp))
        ItemInputForm(

            itemDetails = sizeUiState.itemDetails,
            onValueChange = onSizeValueChange,//
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
        ){
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onBagClick,
                enabled = sizeUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .padding(bottom = 10.dp)
            ){
                Text(text = "ADD TO BAG")
            }
        }
    }
}

@Composable
fun ItemInputForm(
    itemDetails: SizeItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (SizeItemDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
    ) {
        Row(){
            Text(
                text = stringResource(R.string.height_details),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            SizeTips()
        }


        TextField(
            value = itemDetails.height,
            onValueChange ={ onValueChange(itemDetails.copy(height = it)) } ,
            placeholder = { Text(text = stringResource(R.string.height_details_ex))},
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )

        Text(
            text = stringResource(R.string.chest_details),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )

        TextField(
            value = itemDetails.chest,
            onValueChange ={ onValueChange(itemDetails.copy(chest = it)) } ,
            placeholder = { Text(text =stringResource(R.string.chest_details_ex))},
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )

        Text(
            text = stringResource(R.string.hat_details),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )

        TextField(
            value = itemDetails.hat,
            onValueChange ={ onValueChange(itemDetails.copy(hat = it)) } ,
            placeholder = { Text(text = stringResource(R.string.hat_details_ex))},
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun SizeTips(){
    var showDialog by remember { mutableStateOf(false) }

    TextButton(
        onClick = { showDialog = true },
        modifier = Modifier
            .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.size_tips),
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = TextDecoration.Underline
            )
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = stringResource(R.string.size_tips))
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    Text(text= stringResource(R.string.gowns),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                            )
                    Text(modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        text = stringResource(R.string.gowns_details))

                    Text(text = stringResource(R.string.hats),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .paddingFromBaseline(top = 40.dp, bottom = 16.dp)

                    )
                    Text(
                        modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        text= stringResource(R.string.hats_details))

                    Image(
                        painter = painterResource(id = R.drawable.hat_size),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(168.dp)
                    )

                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SizeScreenPreview(){
    SizeBody(
        sizeUiState = SizeUiState(
            itemDetails = SizeItemDetails(
                height = "180",
                chest = "80",
                hat = "56"
            ),
            isEntryValid = true
        ),
        onSizeValueChange = {},
        onBagClick = {}
    )
}
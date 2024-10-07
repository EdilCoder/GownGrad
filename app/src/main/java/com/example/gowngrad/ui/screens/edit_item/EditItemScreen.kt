package com.example.gowngrad.ui.screens.edit_item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gowngrad.GownGrradTopAppBar
import com.example.gowngrad.R
import com.example.gowngrad.ui.AppViewModelProvider
import com.example.gowngrad.ui.navigation.NavigationDestination
import com.example.gowngrad.ui.screens.institution.DegreeDropdown
import com.example.gowngrad.ui.screens.institution.InstitutionDropdown
import com.example.gowngrad.ui.screens.institution.InstitutionItemDetails
import com.example.gowngrad.ui.screens.institution.InstitutionUiState
import com.example.gowngrad.ui.screens.size.SizeItemDetails
import com.example.gowngrad.ui.screens.size.SizeUiState
import kotlinx.coroutines.launch

object EditItemDestination : NavigationDestination {
    override val route = "Edit Item"
    override val titleRes = R.string.edit_item
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: EditItemViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(EditItemDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = null
            )
        },
        modifier = Modifier
    ) {innerPadding ->

        EditItemBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun EditItemBody(
    itemUiState: ItemUiState,
    onItemValueChange: (InstitutionItemDetails, SizeItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {

        InstitutionItemInputForm(
            itemDetails = itemUiState.institutionUiState.itemDetails,
            onValueChange = { updatedInstitutionDetails ->
                onItemValueChange(updatedInstitutionDetails, itemUiState.sizeUiState.itemDetails)
            }
        )

        SizeItemInputForm(
            itemDetails = itemUiState.sizeUiState.itemDetails,
            onValueChange = { updatedSizeDetails ->
                onItemValueChange(itemUiState.institutionUiState.itemDetails, updatedSizeDetails)
            }
        )
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = stringResource(R.string.save))
        }
    }
}

@Composable
fun InstitutionItemInputForm(
    itemDetails: InstitutionItemDetails,
    onValueChange: (InstitutionItemDetails) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
){
    Column(){
        /*OutlinedTextField(
            value = itemDetails.institutionName,
            onValueChange = {onValueChange(itemDetails.copy(institutionName = it))},
            label = { Text(text = stringResource(R.string.institution_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true

        )*/
        InstitutionDropdown(
            selectedInstitution = itemDetails.institutionName,
            onInstitutionSelected = { selectedInstitution ->
                onValueChange(itemDetails.copy(institutionName = selectedInstitution))
            },
            enabled = enabled
        )
        /*OutlinedTextField(
            value = itemDetails.degreeType,
            onValueChange = {onValueChange(itemDetails.copy(degreeType = it))},
            label = { Text(text = stringResource(R.string.degree_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )*/
        DegreeDropdown(
            selectedDegree = itemDetails.degreeType,
            onDegreeSelected = { selectedDegree ->
                onValueChange(itemDetails.copy(degreeType = selectedDegree))
            },
            enabled = enabled
        )
    }
}

@Composable
fun SizeItemInputForm(
    itemDetails: SizeItemDetails,
    onValueChange: (SizeItemDetails) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
){
    Column(){
        OutlinedTextField(
            value = itemDetails.height,
            onValueChange = {onValueChange(itemDetails.copy(height = it))},
            label = { Text(text = stringResource(R.string.height_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
            )
        OutlinedTextField(
            value = itemDetails.chest,
            onValueChange = {onValueChange(itemDetails.copy(chest = it))},
            label = { Text(text = stringResource(R.string.chest_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = itemDetails.hat,
            onValueChange = {onValueChange(itemDetails.copy(hat = it))},
            label = { Text(text = stringResource(R.string.hat_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditItemBodyPreview() {
    val sampleItemUiState = ItemUiState(
        institutionUiState = InstitutionUiState(
            itemDetails = InstitutionItemDetails(
                institutionName = "Sample Institution",
                degreeType = "Bachelor's"
            ),
            isEntryValid = true
        ),
        sizeUiState = SizeUiState(
            itemDetails = SizeItemDetails(
                height = "170 cm",
                chest = "90 cm",
                hat = "58 cm"
            ),
            isEntryValid = true
        ),
        isEntryValid = true
    )

    val onItemValueChange: (InstitutionItemDetails, SizeItemDetails) -> Unit = { _, _ -> }
    val onSaveClick: () -> Unit = {}

    EditItemBody(
        itemUiState = sampleItemUiState,
        onItemValueChange = onItemValueChange,
        onSaveClick = onSaveClick
    )
}


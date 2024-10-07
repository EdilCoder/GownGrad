package com.example.gowngrad.ui.screens.institution

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.gowngrad.ui.screens.welcome.StartScreen
import kotlinx.coroutines.launch


object InstitutionDestination : NavigationDestination {
    override val route = "institution"
    override val titleRes = R.string.institution_details
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstitutionScreen(
    onNavigateUp: () -> Unit,
    onNavigateSetting: () -> Unit,
    onNavigateSize: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: InstitutionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            GownGrradTopAppBar(
                title = stringResource(InstitutionDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateSetting = onNavigateSetting
            )
        }
    ) { innerPadding ->
        InstitutionBody(
            institutionUiState = viewModel.institutionUiState,
            onInstitutionValueChange = viewModel::updateUiState,
            onSubmit = {
                coroutineScope.launch {
                    viewModel.addToInstiutionItem()
                    onNavigateSize()
                }

            },
        modifier = Modifier
            .padding(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding()
            )
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
        )
    }
}

@Composable
fun InstitutionBody(
    institutionUiState: InstitutionUiState,
    onInstitutionValueChange: (InstitutionItemDetails) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(

        /*verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),*/
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))

    ) {

        Spacer(modifier = Modifier.height(120.dp))
        ItemInputForm(
            itemDetails = institutionUiState.itemDetails,
            onValueChange = onInstitutionValueChange,//
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(200.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
        ){
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onSubmit,
                enabled = institutionUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .padding(bottom = 10.dp)
            ){
                Text(text = "Submit")
            }
        }
    }
}

@Composable
fun ItemInputForm(
    itemDetails: InstitutionItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (InstitutionItemDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        /*verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))*/
    ) {
        Text(
            text = stringResource(R.string.enter_institution),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        /*TextField(
            value = itemDetails.institutionName,
            onValueChange ={ onValueChange(itemDetails.copy(institutionName = it)) } ,
            placeholder = { Text(text = stringResource(R.string.enter_institution_ex))},
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )*/
        InstitutionDropdown(
            selectedInstitution = itemDetails.institutionName,
            onInstitutionSelected = { selectedInstitution ->
                onValueChange(itemDetails.copy(institutionName = selectedInstitution))
            },
            enabled = enabled
        )

        Text(
            text = stringResource(R.string.enter_degree),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )

        /*TextField(
            value = itemDetails.degreeType,
            onValueChange ={ onValueChange(itemDetails.copy(degreeType = it)) } ,
            placeholder = { Text(text = stringResource(R.string.enter_degree_ex))},
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )*/
        DegreeDropdown(
            selectedDegree = itemDetails.degreeType,
            onDegreeSelected = { selectedDegree ->
                onValueChange(itemDetails.copy(degreeType = selectedDegree))
            },
            enabled = enabled
        )

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstitutionDropdown(
    selectedInstitution: String,
    onInstitutionSelected: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedInstitution,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(text = stringResource(R.string.enter_institution_ex)) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            InstitutionOptions.forEach { institution ->
                DropdownMenuItem(
                    text = { Text(text = institution) },
                    onClick = {
                        onInstitutionSelected(institution)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DegreeDropdown(
    selectedDegree: String,
    onDegreeSelected: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedDegree,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(text = stringResource(R.string.enter_degree_ex)) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            degreeOptions.forEach { degree ->
                DropdownMenuItem(
                    text = { Text(text = degree) },
                    onClick = {
                        onDegreeSelected(degree)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InstitutionScreenPreview(){
    InstitutionBody(
        institutionUiState = InstitutionUiState(
            InstitutionItemDetails(
                institutionName = "", degreeType = ""
            )
        ),
        onInstitutionValueChange = {},
        onSubmit = {}
    )
}

val InstitutionOptions = listOf(
    "University of Oxford",
    "University of Cambridge",
    "Imperial College London",
    "London School of Economics and Political Science (LSE)",
    "University College London (UCL)",
    "University of Edinburgh",
    "King's College London",
    "University of Manchester",
    "University of Warwick",
    "University of Bristol",
    "University of Sheffield"
)


val degreeOptions = listOf(
    "Bachelors/1st Degrees - BA",
    "Bachelors/1st Degrees - BEd",
    "Bachelors/1st Degrees - BSc",
    "Bachelors/1st Degrees - LLB",
    "Certificates & Diplomas - AdvDiploma",
    "Certificates & Diplomas - Cert HE",
    "Certificates & Diplomas - CertEd",
    "Certificates & Diplomas - DipHE",
    "Certificates & Diplomas - Higher National Certificate (HNC)",
    "Certificates & Diplomas - Higher National Diploma (HND)",
    "Foundation Degrees - FDeg (Arts)",
    "Foundation Degrees - FDeg (Engineering)",
    "Foundation Degrees - FDeg (Science)",
    "Graduate Diploma - GDip",
    "Integrated Masters Degrees - MArch",
    "Integrated Masters Degrees - MArt",
    "Integrated Masters Degrees - MChem",
    "Integrated Masters Degrees - MComp",
    "Integrated Masters Degrees - MDes",
    "Integrated Masters Degrees - MEng",
    "Integrated Masters Degrees - MPlan",
    "Integrated Masters Degrees - MSci",
    "Masters Degrees - LLM",
    "Masters Degrees - MA",
    "Masters Degrees - MBA",
    "Masters Degrees - MProf",
    "Masters Degrees - MRes",
    "Masters Degrees - MSc",
    "Masters Degrees - MSW",
    "Masters Degrees - MTL",
    "MPhil",
    "Other Doctorates - DBA",
    "Other Doctorates - DEng",
    "Other Doctorates - DLitt",
    "Other Doctorates - DProf",
    "Other Doctorates - DSc",
    "Other Doctorates - EdD",
    "Other Doctorates - LLD",
    "PCE - Professional Graduate Certificate in Education",
    "PGCE",
    "PhD/DPhil - PhD (All Faculties/Schools)",
    "Postgraduate Awards - Graduate Certificate",
    "Postgraduate Awards - Postgraduate Diploma",
    "Postgraduate Awards - Postgraduate Certificate"
)


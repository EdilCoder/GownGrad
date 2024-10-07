package com.example.gowngrad.ui.screens.institution

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gowngrad.data.GownGradRepository
import com.example.gowngrad.data.LocalRepository
import com.example.gowngrad.data.local.institution.InstitutionItem

class InstitutionViewModel(private val institutionRepository: GownGradRepository) : ViewModel(){

    var institutionUiState by mutableStateOf(InstitutionUiState())
    private set

    fun updateUiState(institutionItemDetails: InstitutionItemDetails) {
        institutionUiState =
            InstitutionUiState(itemDetails = institutionItemDetails, isEntryValid = validateInput(institutionItemDetails))
    }

    suspend fun addToInstiutionItem() {
        if (validateInput()) {
            institutionRepository.insertInstitutionItem(institutionUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: InstitutionItemDetails = institutionUiState.itemDetails): Boolean {
        return with(uiState) {
            institutionName.isNotBlank() && degreeType.isNotBlank()
        }
    }

}

data class InstitutionUiState(
    val itemDetails: InstitutionItemDetails = InstitutionItemDetails(),
    val isEntryValid: Boolean = false
)


data class InstitutionItemDetails(
    val id: Int = 0,
    val institutionName: String = "",
    val degreeType: String = ""
)

fun InstitutionItemDetails.toItem(): InstitutionItem = InstitutionItem(
    id = id,
    institutionName = institutionName,
    degreeType = degreeType
)

fun InstitutionItem.toItemUiState(isEntryValid: Boolean = false): InstitutionUiState = InstitutionUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun InstitutionItem.toItemDetails(): InstitutionItemDetails = InstitutionItemDetails(
    id = id,
    institutionName = institutionName,
    degreeType = degreeType
)
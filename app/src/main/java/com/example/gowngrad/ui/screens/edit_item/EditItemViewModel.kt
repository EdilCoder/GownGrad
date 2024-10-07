package com.example.gowngrad.ui.screens.edit_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gowngrad.data.GownGradRepository
import com.example.gowngrad.ui.screens.bag.BagViewModel
import com.example.gowngrad.ui.screens.bag.ItemDetailsUiState
import com.example.gowngrad.ui.screens.institution.InstitutionItemDetails
import com.example.gowngrad.ui.screens.institution.InstitutionUiState
import com.example.gowngrad.ui.screens.institution.toItem
import com.example.gowngrad.ui.screens.institution.toItemDetails
import com.example.gowngrad.ui.screens.institution.toItemUiState
import com.example.gowngrad.ui.screens.size.SizeItemDetails
import com.example.gowngrad.ui.screens.size.SizeUiState
import com.example.gowngrad.ui.screens.size.toItem
import com.example.gowngrad.ui.screens.size.toItemDetails
import com.example.gowngrad.ui.screens.size.toItemUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditItemViewModel(
    private val editItemRepository: GownGradRepository
) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    init {
        viewModelScope.launch {
            val institutionItem = editItemRepository.getInstitutionItemStream()
                .filterNotNull()
                .first()
                .toItemUiState(true)
            val sizeItem = editItemRepository.getSizeItemStream()
                .filterNotNull()
                .first()
                .toItemUiState(false)
            itemUiState = ItemUiState(
                institutionUiState = institutionItem,
                sizeUiState = sizeItem,
                isEntryValid = validateInput(institutionItem.itemDetails, sizeItem.itemDetails)
            )
        }
    }

    suspend fun updateItem() {
        if (itemUiState.isEntryValid) {
            editItemRepository.updateInstitutionItem(itemUiState.institutionUiState.itemDetails.toItem())
            editItemRepository.updateSizeItem(itemUiState.sizeUiState.itemDetails.toItem())
        }
    }

    fun updateUiState(newInstitutionDetails: InstitutionItemDetails, newSizeDetails: SizeItemDetails) {
        itemUiState = ItemUiState(
            institutionUiState = InstitutionUiState(itemDetails = newInstitutionDetails, isEntryValid = validateInstitutionInput(newInstitutionDetails)),
            sizeUiState = SizeUiState(itemDetails = newSizeDetails, isEntryValid = validateSizeInput(newSizeDetails)),
            isEntryValid = validateInput(newInstitutionDetails, newSizeDetails)
        )
    }

    private fun validateInput(
        institutionDetails: InstitutionItemDetails,
        sizeDetails: SizeItemDetails
    ): Boolean {
        return validateInstitutionInput(institutionDetails) && validateSizeInput(sizeDetails)
    }

    private fun validateInstitutionInput(details: InstitutionItemDetails): Boolean {
        return details.institutionName.isNotBlank() && details.degreeType.isNotBlank()
    }

    private fun validateSizeInput(details: SizeItemDetails): Boolean {
        return details.height.isNotBlank() && details.chest.isNotBlank() && details.hat.isNotBlank()
    }

}

data class ItemUiState(
    val institutionUiState: InstitutionUiState = InstitutionUiState(),
    val sizeUiState: SizeUiState = SizeUiState(),
    val isEntryValid: Boolean = false
)



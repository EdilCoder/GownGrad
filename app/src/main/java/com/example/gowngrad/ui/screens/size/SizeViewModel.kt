package com.example.gowngrad.ui.screens.size

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gowngrad.data.GownGradRepository
import com.example.gowngrad.data.local.size.SizeItem

class SizeViewModel(private val sizeRepository: GownGradRepository) : ViewModel() {

    var sizeUiState by mutableStateOf(SizeUiState())
        private set

    fun updateUiState(sizeItemDetails: SizeItemDetails) {
        sizeUiState =
            SizeUiState(
                itemDetails = sizeItemDetails,
                isEntryValid = validateInput(sizeItemDetails)
            )
    }

    suspend fun addToSizeItem() {
        if (validateInput()) {
            sizeRepository.insertSizeItem(sizeUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: SizeItemDetails = sizeUiState.itemDetails): Boolean {
        return with(uiState) {
            height.isNotBlank() && chest.isNotBlank() && hat.isNotBlank()
        }
    }
}

data class SizeUiState(
    val itemDetails: SizeItemDetails = SizeItemDetails(),
    val isEntryValid: Boolean = false
)

data class SizeItemDetails(
    val id: Int = 0,
    val height: String = "",
    val chest: String = "",
    val hat: String = "",
)

fun SizeItemDetails.toItem() : SizeItem = SizeItem(
    id = id,
    height = height.toDoubleOrNull() ?: 0.0,
    chest = chest.toDoubleOrNull() ?: 0.0,
    hat = hat.toDoubleOrNull() ?: 0.0,
)

fun SizeItem.toItemUiState(isEntryValid: Boolean = false) : SizeUiState = SizeUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun SizeItem.toItemDetails() : SizeItemDetails = SizeItemDetails(
    id = id,
    height = height.toString(),
    chest = chest.toString(),
    hat = hat.toString()
)
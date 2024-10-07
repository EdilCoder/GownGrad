package com.example.gowngrad.ui.screens.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gowngrad.data.GownGradRepository
import com.example.gowngrad.ui.screens.institution.InstitutionItemDetails
import com.example.gowngrad.ui.screens.institution.toItemDetails
import com.example.gowngrad.ui.screens.size.SizeItemDetails
import com.example.gowngrad.ui.screens.size.toItemDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class BagViewModel(
    bagRepository: GownGradRepository

): ViewModel(){
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val institutionItemFlow = bagRepository.getInstitutionItemStream()
    private val sizeItemFlow = bagRepository.getSizeItemStream()

    val uiState: StateFlow<ItemDetailsUiState> = combine(
        institutionItemFlow.filterNotNull(),
        sizeItemFlow.filterNotNull()
    ){
     institutionItem, sizeItem ->
        ItemDetailsUiState(
            institutionItemDetails = institutionItem.toItemDetails(),
            sizeItemDetails = sizeItem.toItemDetails()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ItemDetailsUiState()
    )
}

data class ItemDetailsUiState(
    val institutionItemDetails: InstitutionItemDetails = InstitutionItemDetails(),
    val sizeItemDetails : SizeItemDetails = SizeItemDetails()
)
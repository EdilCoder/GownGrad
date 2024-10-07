package com.example.gowngrad.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gowngrad.GownGradApplication
import com.example.gowngrad.ui.screens.bag.BagViewModel
import com.example.gowngrad.ui.screens.edit_item.EditItemViewModel
import com.example.gowngrad.ui.screens.institution.InstitutionViewModel
import com.example.gowngrad.ui.screens.size.SizeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            InstitutionViewModel(gownGradApplication().container.gownGradRepository)
        }
        initializer {
            SizeViewModel(gownGradApplication().container.gownGradRepository)
        }
        initializer {
            BagViewModel(
                gownGradApplication().container.gownGradRepository
            )
        }
        initializer {
            EditItemViewModel(
                gownGradApplication().container.gownGradRepository
            )
        }
    }
}

fun CreationExtras.gownGradApplication(): GownGradApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GownGradApplication)
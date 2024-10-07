package com.example.gowngrad

import androidx.compose.runtime.compositionLocalOf
import com.example.gowngrad.ui.screens.GownGradViewModel

val LocalGownGradViewModel = compositionLocalOf<GownGradViewModel> {
    error("No GownGradViewModel provided")
}
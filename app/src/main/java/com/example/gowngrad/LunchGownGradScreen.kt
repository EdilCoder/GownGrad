package com.example.gowngrad

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gowngrad.ui.navigation.GownGradNavHost
import com.example.gowngrad.ui.screens.GownGradUiState
import com.example.gowngrad.ui.screens.GownGradViewModel

@Composable
fun LunchGownGradApp(
    navController: NavHostController = rememberNavController(),
    gownGradViewModel: GownGradViewModel = hiltViewModel()
){
    CompositionLocalProvider(LocalGownGradViewModel provides gownGradViewModel) {
        GownGradNavHost(navController = navController)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GownGrradTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    navigateSetting : (() -> Unit)? = null
) {

    val gownGradViewModel = LocalGownGradViewModel.current
    val uiState by gownGradViewModel.uiState.collectAsState(
        initial = GownGradUiState(false)
    )

    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            if (canNavigateBack && navigateSetting != null) {
                IconButton(onClick = navigateSetting) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = if (!uiState.isAnonymousAccount) Color.Green else LocalContentColor.current
                    )
                }
            }
        }
    )
}
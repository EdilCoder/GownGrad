package com.example.gowngrad.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gowngrad.LocalGownGradViewModel
import com.example.gowngrad.ui.screens.bag.BagDestination
import com.example.gowngrad.ui.screens.bag.BagScreen
import com.example.gowngrad.ui.screens.edit_item.EditItemDestination
import com.example.gowngrad.ui.screens.edit_item.EditItemScreen
import com.example.gowngrad.ui.screens.general.EditGeneralDestination
import com.example.gowngrad.ui.screens.general.EditGeneralScreen
import com.example.gowngrad.ui.screens.general.EditGeneralViewModel
import com.example.gowngrad.ui.screens.general.GeneralDestination
import com.example.gowngrad.ui.screens.general.GeneralScreen
import com.example.gowngrad.ui.screens.general.GeneralViewModel
import com.example.gowngrad.ui.screens.institution.InstitutionDestination
import com.example.gowngrad.ui.screens.institution.InstitutionScreen
import com.example.gowngrad.ui.screens.login.LoginDestination
import com.example.gowngrad.ui.screens.login.LoginScreen
import com.example.gowngrad.ui.screens.my_order.MyOrderDestination
import com.example.gowngrad.ui.screens.my_order.MyOrderScreen
import com.example.gowngrad.ui.screens.my_order.OrderDetailDestination
import com.example.gowngrad.ui.screens.my_order.OrderDetailScreen
import com.example.gowngrad.ui.screens.payment.CheckoutDestination
import com.example.gowngrad.ui.screens.payment.CheckoutScreen
import com.example.gowngrad.ui.screens.payment.PaymentSuccessDestination
import com.example.gowngrad.ui.screens.payment.PaymentSuccessScreen
import com.example.gowngrad.ui.screens.settings.SettingsDestination
import com.example.gowngrad.ui.screens.settings.SettingsScreen
import com.example.gowngrad.ui.screens.sign_up.SignUpDestination
import com.example.gowngrad.ui.screens.sign_up.SignUpScreen
import com.example.gowngrad.ui.screens.size.SizeDestination
import com.example.gowngrad.ui.screens.size.SizeScreen
import com.example.gowngrad.ui.screens.splash.SplashDestination
import com.example.gowngrad.ui.screens.splash.SplashScreen
import com.example.gowngrad.ui.screens.welcome.StartDestination
import com.example.gowngrad.ui.screens.welcome.StartScreen



@Composable
fun GownGradNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashDestination.route,
        modifier = modifier
    ){

        composable(route = SplashDestination.route) {
            SplashScreen(
                openAndPopUp = { route, popUp ->
                    navController.navigate(route) {
                        popUpTo(popUp) { inclusive = true }
                    }
                }
            )
        }

        composable(route = StartDestination.route) {
            StartScreen(
                navigateToInstitution = {
                    navController.navigate(InstitutionDestination.route)
                }
            )
        }


        composable(route = "${SettingsDestination.route}/{returnDestination}") {
            backStackEntry ->
            val returnDestination = backStackEntry.arguments?.getString("returnDestination") ?: SettingsDestination.route
            Log.d("SettingsDestination", "returnDestination: $returnDestination")
            SettingsScreen(
                onNavigateUp = {
                    if (returnDestination == "institution") {
                        navController.navigate(InstitutionDestination.route) {
                            popUpTo(LoginDestination.route) { inclusive = true }
                        }
                    } else {
                        navController.navigateUp()
                    }
                },
                onLoginClick = { navController.navigate("${LoginDestination.route}/$returnDestination")},
                onSignUpClick = {navController.navigate("${SignUpDestination.route}/$returnDestination")},
                onSignOutComplete = {
                    navController.navigate(StartDestination.route) {
                        launchSingleTop = true
                        popUpTo(0) { inclusive = true }
                    }
                },
                /*onGeneralClick = { navController.navigate(GeneralDestination.route) },*/
                onDeleteAccountComplete = {
                    navController.navigate(StartDestination.route) {
                        launchSingleTop = true
                        popUpTo(0) { inclusive = true }
                    }
                },
                onGeneralClick = {
                    navController.navigate("${GeneralDestination.route}/$returnDestination")
                },

                onMyOrdersClick = {
                    navController.navigate(MyOrderDestination.route)
                },
                openScreen = { route -> navController.navigate(route) },
                returnDestination = returnDestination,
                navController = navController
                )
        }


        composable(route = "${GeneralDestination.route}/{returnDestination}") { backStackEntry ->
            val returnDestination = backStackEntry.arguments?.getString("returnDestination") ?: SettingsDestination.route

            GeneralScreen(
                navController = navController,
                onNavigateUp = {
                    Log.d("GeneralScreen", "returnDestination: $returnDestination")
                    navController.navigate("${SettingsDestination.route}/$returnDestination") {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = EditGeneralDestination.routeWithArgs,
            arguments = listOf(navArgument(EditGeneralDestination.itemIdArg) {
                nullable = true
                defaultValue = null
            })
        ) {
            EditGeneralScreen(
                navController = navController,
                popUpScreen = { navController.popBackStack() }
            )
        }

        composable(
            route = CheckoutDestination.route
        ){
            CheckoutScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToSuccess = {navController.navigate(PaymentSuccessDestination.route) },
                onNavigateSetting = { navController.navigate("${SettingsDestination.route}/${CheckoutDestination.route}") },
                onNavigateToSignUpScreen = {
                    navController.navigate("${SignUpDestination.route}/${CheckoutDestination.route}")
                }
            )
        }

        composable(route = MyOrderDestination.route){
            MyOrderScreen(
                onNavigateUp = { navController.navigateUp() },
                openScreen = { route -> navController.navigate(route) }
            )
        }

        composable(route = OrderDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(OrderDetailDestination.orderIdArg) {
                nullable = true
                defaultValue = null
            }))
        {
            OrderDetailScreen(
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable(route = "${SignUpDestination.route}/{returnDestination}") {
                backStackEntry ->
            val returnDestination = backStackEntry.arguments?.getString("returnDestination") ?: SettingsDestination.route
            SignUpScreen(
                onNavigateUp = { navController.navigateUp() },
                openAndPopUp = { route, popUp ->
                    navController.navigate("$route/$returnDestination") {
                        popUpTo(popUp) { inclusive = true }
                    }
                },
                returnDestination = returnDestination,
                navController = navController
            )
        }

        composable(route = "${LoginDestination.route}/{returnDestination}") { backStackEntry ->
            val returnDestination = backStackEntry.arguments?.getString("returnDestination")
            LoginScreen(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                openAndPopUp = { route, popUp ->
                    navController.navigate(route) {
                        popUpTo(popUp) { inclusive = true }
                    }
                },

                returnDestination = returnDestination
            )
        }

        composable(route = InstitutionDestination.route) {
            InstitutionScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateSize = {
                    navController.navigate(SizeDestination.route)

                },
                onNavigateSetting = { navController.navigate("${SettingsDestination.route}/${InstitutionDestination.route}") },
            )
        }
        composable(
            route = SizeDestination.route
        ) {
            SizeScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateBag = {
                    navController.navigate(BagDestination.route)
                                },
                onNavigateSetting = { navController.navigate("${SettingsDestination.route}/${SizeDestination.route}") },
            )
        }
        composable(
            route = BagDestination.route
        ) {
            BagScreen(
                gownGradViewModel = LocalGownGradViewModel.current,
                onNavigateUp = { navController.navigateUp() },
                onNavigateToEdit = { navController.navigate(EditItemDestination.route) },
                onNavigateSetting = { navController.navigate("${SettingsDestination.route}/${BagDestination.route}") },
                onNavigateToCheckout = { navController.navigate(CheckoutDestination.route) }
            )
        }

        composable(
            route = EditItemDestination.route
        ){
            EditItemScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = PaymentSuccessDestination.route
        ){
            PaymentSuccessScreen(
                onNavigateUp = { },
                onNavigateSetting = { },
                onContinueClick = {
                    navController.navigate(StartDestination.route){
                        popUpTo(PaymentSuccessDestination.route){inclusive = true}
                    }
                }
            )
        }
    }
}
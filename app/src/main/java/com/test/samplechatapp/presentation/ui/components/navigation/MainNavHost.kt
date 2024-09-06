package com.test.samplechatapp.presentation.ui.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.test.samplechatapp.presentation.ui.screens.chatList.ChatListScreen
import com.test.samplechatapp.presentation.ui.screens.chat.ChatScreen
import com.test.samplechatapp.presentation.ui.screens.profile.ProfileScreen
import com.test.samplechatapp.presentation.ui.screens.signin.PhoneNumberScreen
import com.test.samplechatapp.presentation.ui.screens.registration.RegistrationScreen
import com.test.samplechatapp.presentation.ui.screens.verificationCode.SmsCodeScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            startDestination = ScreenType.PhoneNumberScreen.route,
            route = ScreenType.LoginGraph.route
        ) {
            composable(ScreenType.PhoneNumberScreen.route) {
                PhoneNumberScreen(navController = navController, paddingValues = paddingValues)
            }
            composable(ScreenType.SmsCodeScreen.route) { backStackEntry ->
                SmsCodeScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    phoneNumber = backStackEntry.arguments?.getString("phone") ?: ""
                )
            }
            composable(ScreenType.RegistrationScreen.route) { backStackEntry ->
                RegistrationScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    phoneNumber = backStackEntry.arguments?.getString("phone") ?: ""
                )
            }
        }

        navigation(
            startDestination = ScreenType.ChatListScreen.route,
            route = ScreenType.ChatsGraph.route
        ) {
            composable(ScreenType.ChatListScreen.route) {
                ChatListScreen(navController, paddingValues)
            }
            composable(ScreenType.ChatScreen.route) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId")?.toIntOrNull() ?: 0
                ChatScreen(navController = navController, chatId = chatId)
            }
        }

        navigation(
            startDestination = ScreenType.ProfileScreen.route,
            route = ScreenType.ProfileGraph.route
        ) {
            composable(ScreenType.ProfileScreen.route) {
                ProfileScreen(navController = navController)
            }
            composable(ScreenType.EditProfileScreen.route) {
            }
        }
    }
}

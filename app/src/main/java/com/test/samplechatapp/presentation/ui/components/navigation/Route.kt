package com.test.samplechatapp.presentation.ui.components.navigation

sealed class ScreenType(val route: String) {
    data object PhoneNumberScreen : ScreenType("phone_number")
    data object SmsCodeScreen : ScreenType("sms_code/{phone}") {
        fun createRoute(phone: String) = "sms_code/$phone"
    }
    data object RegistrationScreen : ScreenType("registration/{phone}") {
        fun createRoute(phone: String) = "registration/$phone"
    }
    data object ChatListScreen : ScreenType("chat_list")
    data object ChatScreen : ScreenType("chat/{chatId}") {
        fun createRoute(chatId: Int) = "chat/$chatId"
    }
    data object ProfileScreen : ScreenType("profile")
    data object EditProfileScreen : ScreenType("edit_profile")

    data object LoginGraph : ScreenType("login_graph")
    data object ChatsGraph : ScreenType("chats_graph")
    data object ProfileGraph : ScreenType("profile_graph")
}

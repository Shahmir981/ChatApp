package com.test.samplechatapp.presentation.ui.screens.chatList

sealed class ChatListIntent {
    data object LoadChats : ChatListIntent()
    data class OpenChat(val chatId: Int) : ChatListIntent()
    data object OpenProfile : ChatListIntent()
}

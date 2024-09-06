package com.test.samplechatapp.presentation.ui.screens.chatList

import com.test.samplechatapp.domain.model.ChatInfoModel

data class ChatListState(
    val chats: List<ChatInfoModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

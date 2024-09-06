package com.test.samplechatapp.presentation.ui.screens.chat

import com.test.samplechatapp.domain.model.ChatInfoModel
import com.test.samplechatapp.domain.model.MessageModel

data class ChatState(
    val chat: ChatInfoModel? = null,
    val messages: List<MessageModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

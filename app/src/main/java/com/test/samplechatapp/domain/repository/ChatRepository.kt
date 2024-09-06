package com.test.samplechatapp.domain.repository

import com.test.samplechatapp.data.network.dto.ChatDto
import com.test.samplechatapp.data.network.dto.MessageDto

interface ChatRepository {
    suspend fun getAllChats() : List<ChatDto>
    suspend fun getChatById(chatId: Int) : ChatDto
    suspend fun getMessagesByChatId(chatId: Int): List<MessageDto>
}

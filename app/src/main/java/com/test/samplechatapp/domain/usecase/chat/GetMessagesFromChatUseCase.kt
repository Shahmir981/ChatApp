package com.test.samplechatapp.domain.usecase.chat

import com.test.samplechatapp.data.network.utils.mappers.toMessageModel
import com.test.samplechatapp.domain.model.MessageModel
import com.test.samplechatapp.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessagesFromChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: Int) : List<MessageModel> {
        return chatRepository.getMessagesByChatId(chatId).map { it.toMessageModel() }
    }
}

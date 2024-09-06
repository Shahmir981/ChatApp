package com.test.samplechatapp.domain.usecase.chat

import com.test.samplechatapp.data.network.utils.mappers.toChatInfoModel
import com.test.samplechatapp.domain.model.ChatInfoModel
import com.test.samplechatapp.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke() : List<ChatInfoModel> {
        return chatRepository.getAllChats().map { it.toChatInfoModel() }
    }
}


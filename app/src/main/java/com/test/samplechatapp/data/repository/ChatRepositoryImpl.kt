package com.test.samplechatapp.data.repository

import com.test.samplechatapp.data.network.dto.ChatDto
import com.test.samplechatapp.data.network.dto.MessageDto
import com.test.samplechatapp.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val chats = listOf(
        ChatDto(
            id = 1,
            imageUrl = "https://i.pravatar.cc/200?img=10",
            title = "Creative Graphic Designer",
            description = "Bringing Visions to Life with Color and Composition"
        ),
        ChatDto(
            id = 2,
            imageUrl = "https://i.pravatar.cc/200?img=11",
            title = "Digital Marketing Strategist",
            description = "Driving Growth Through Targeted Online Campaigns"
        ),
        ChatDto(
            id = 3,
            imageUrl = "https://i.pravatar.cc/200?img=12",
            title = "Data Science Specialist",
            description = "Transforming Raw Data into Actionable Insights"
        ),
        ChatDto(
            id = 4,
            imageUrl = "https://i.pravatar.cc/200?img=13",
            title = "Environmental Scientist",
            description = "Advancing Sustainable Practices for a Greener Future"
        ),
        ChatDto(
            id = 5,
            imageUrl = "https://i.pravatar.cc/200?img=14",
            title = "Experienced Project Manager",
            description = "Leading Teams to Success with Strategic Planning\n"
        ),
        ChatDto(
            id = 6,
            imageUrl = "https://i.pravatar.cc/200?img=15",
            title = "Content Writer",
            description = "Crafting Engaging and Persuasive Copy for Diverse Audiences"
        ),
        ChatDto(
            id = 7,
            imageUrl = "https://i.pravatar.cc/200?img=16",
            title = "Cybersecurity Analyst",
            description = "Protecting Digital Assets with Advanced Security Solutions"
        ),
        ChatDto(
            id = 8,
            imageUrl = "https://i.pravatar.cc/200?img=17",
            title = "Software Architect",
            description = "Crafting Scalable Systems with Robust Design Principles"
        ),
        ChatDto(
            id = 9,
            imageUrl = "https://i.pravatar.cc/200?img=18",
            title = "Financial Analyst",
            description = "Delivering In-Depth Market Analysis and Investment Strategies"
        ),
        ChatDto(
            id = 10,
            imageUrl = "https://i.pravatar.cc/200?img=19",
            title = "Human Resources Manager",
            description = "Building Strong Teams with Effective Talent Management."
        ),
        ChatDto(
            id = 11,
            imageUrl = "https://i.pravatar.cc/200?img=20",
            title = "Education Consultant",
            description = "Enhancing Learning Outcomes with Tailored Solutions."
        ),
        ChatDto(
            id = 12,
            imageUrl = "https://i.pravatar.cc/200?img=21",
            title = "Healthcare Professional",
            description = "Providing Compassionate Care with Clinical Expertise"
        ),
        ChatDto(
            id = 13,
            imageUrl = "https://i.pravatar.cc/200?img=22",
            title = "Mechanical Engineer",
            description = "Designing Innovative Solutions for Complex Challenges"
        )
    )

    private val messages = listOf(
        MessageDto(
            id = 1,
            chatId = 1,
            sender = "Armen",
            content = "Great!",
            timestamp = "2024-07-20 15:11:11"
        ),
        MessageDto(
            id = 2,
            chatId = 2,
            sender = "Ivan",
            content = "sounds good))",
            timestamp = "2024-05-20 12:21:23"
        ),
        MessageDto(
            id = 3,
            chatId = 3,
            sender = "Vle",
            content = "very good!",
            timestamp = "2024-02-20 13:13:13"
        ),
        MessageDto(
            id = 4,
            chatId = 13,
            sender = "Vle",
            content = "very good!",
            timestamp = "2024-02-20 13:13:13"
        )

    )

    override suspend fun getAllChats(): List<ChatDto> {
        return chats
    }

    override suspend fun getChatById(id: Int): ChatDto {
        return chats.find { it.id == id }
            ?: throw NoSuchElementException("Chat not found") // error handling
    }

    override suspend fun getMessagesByChatId(chatId: Int): List<MessageDto> {
        return messages.filter { it.chatId == chatId }
    }
}

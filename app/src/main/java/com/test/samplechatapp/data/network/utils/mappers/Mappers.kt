package com.test.samplechatapp.data.network.utils.mappers

import com.test.samplechatapp.data.network.dto.AvatarDto
import com.test.samplechatapp.data.network.dto.ChatDto
import com.test.samplechatapp.data.network.dto.CountryDto
import com.test.samplechatapp.data.network.dto.ProfileDto
import com.test.samplechatapp.data.network.dto.MessageDto
import com.test.samplechatapp.data.network.dto.UpdateProfileDto
import com.test.samplechatapp.domain.model.AvatarModel
import com.test.samplechatapp.domain.model.ChatInfoModel
import com.test.samplechatapp.domain.model.CountryModel
import com.test.samplechatapp.domain.model.MessageModel
import com.test.samplechatapp.domain.model.UpdateProfileModel
import com.test.samplechatapp.domain.model.ProfileModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ProfileDto.toUserProfileModel(): ProfileModel {
    val avatarUrl = profileData?.avatars?.bigAvatar?.let {
        if (it.startsWith("http")) it else "https://plannerok.ru/$it"
    } ?: ""

    val birthDate = profileData?.birthday.orEmpty()
    val username = profileData?.username.orEmpty()
    val city = profileData?.city.orEmpty()
    val phone = profileData?.phone.orEmpty()
    val status = profileData?.status.orEmpty()

    val formattedBirthDate = formatDateStringFromDto(birthDate)

    return ProfileModel(
        birthDate = formattedBirthDate,
        username = username,
        city = city,
        avatarUrl = avatarUrl,
        phone = phone,
        status = status
    )
}

fun MessageDto.toMessageModel() : MessageModel {
    return MessageModel(
        id, chatId, sender, content, timestamp
    )
}

fun UpdateProfileModel.toUpdateProfileDto(): UpdateProfileDto {
    return UpdateProfileDto(
        name = this.name,
        username = this.username,
        birthday = formatDateStringToDto(this.birthday ?: ""),
        city = this.city,
        vk = "",
        instagram = "",
        status = this.status,
        avatarDto = this.avatar?.toAvatarDto()
    )
}

fun ChatDto.toChatInfoModel() : ChatInfoModel {
    return ChatInfoModel(
        id, imageUrl, title, description
    )
}

fun CountryDto.toCountryModel() : CountryModel {
    return CountryModel(
        name, code, countryCode, flagResource
    )
}

fun formatDateStringFromDto(dateString: String): String {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val date: Date? = originalFormat.parse(dateString)
        targetFormat.format(date ?: Date())
    } catch (e: Exception) {
        ""
    }
}

fun AvatarModel.toAvatarDto(): AvatarDto {
    return AvatarDto(
        filename = this.filename,
        base64 = this.base64
    )
}

fun formatDateStringToDto(dateString: String): String {
    return try {
        val originalFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date? = originalFormat.parse(dateString)
        targetFormat.format(date ?: Date())
    } catch (e: Exception) {
        ""
    }
}

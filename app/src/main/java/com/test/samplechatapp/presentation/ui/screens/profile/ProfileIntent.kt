package com.test.samplechatapp.presentation.ui.screens.profile

import android.net.Uri
import com.test.samplechatapp.domain.model.UpdateProfileModel

sealed class ProfileIntent {
    data object FetchProfile : ProfileIntent()
    data object StartEditing : ProfileIntent()
    data object CancelEditing : ProfileIntent()
    data class UpdateProfile(val profileModel: UpdateProfileModel) : ProfileIntent()
    data class SetAvatarUri(val uri: Uri) : ProfileIntent()
    data object Logout : ProfileIntent()
}

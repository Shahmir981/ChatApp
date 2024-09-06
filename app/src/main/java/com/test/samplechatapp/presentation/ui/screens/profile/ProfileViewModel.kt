package com.test.samplechatapp.presentation.ui.screens.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.samplechatapp.domain.model.UpdateProfileModel
import com.test.samplechatapp.domain.usecase.profile.GetProfileUseCase
import com.test.samplechatapp.domain.usecase.auth.LogoutUseCase
import com.test.samplechatapp.domain.usecase.profile.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Base64
import com.test.samplechatapp.domain.exception.AuthException
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _ProfileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _ProfileState

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    private val _avatarUri = MutableStateFlow<Uri?>(null)
    val avatarUri: StateFlow<Uri?> = _avatarUri

    private val _editableName = MutableStateFlow("")
    val editableName: StateFlow<String> = _editableName

    private val _editableCity = MutableStateFlow("")
    val editableCity: StateFlow<String> = _editableCity

    private val _editableBirthday = MutableStateFlow("")
    val editableBirthday: StateFlow<String> = _editableBirthday

    private val _editableStatus = MutableStateFlow("")
    val editableStatus: StateFlow<String> = _editableStatus

    init {
        handleIntent(ProfileIntent.FetchProfile)
    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.FetchProfile -> fetchUserProfile()
            is ProfileIntent.StartEditing -> _isEditing.value = true
            is ProfileIntent.CancelEditing -> cancelEditing()
            is ProfileIntent.UpdateProfile -> updateProfile(intent.profileModel)
            is ProfileIntent.SetAvatarUri -> _avatarUri.value = intent.uri
            is ProfileIntent.Logout -> logout()
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            _ProfileState.value = ProfileState.Loading
            val result = getProfileUseCase()
            _ProfileState.value = when {
                result.isSuccess -> {
                    val userProfile = result.getOrNull()
                    if (userProfile != null) {
                        _editableName.value = userProfile.username.ifBlank { "Unknown" }
                        _editableCity.value = userProfile.city.ifBlank { "" }
                        _editableBirthday.value = userProfile.birthDate.ifBlank { ""}
                        ProfileState.Success(userProfile)
                    } else {
                        ProfileState.Error("User profile is null")
                    }
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull() ?: AuthException.UnknownError()
                    _effect.emit(ProfileEffect.ShowErrorMessage(exception.message ?: "Unknown error"))
                    ProfileState.Error(exception.message ?: "Unknown error")
                }
                else -> ProfileState.Error("Unknown error")
            }
        }
    }

    private fun updateProfile(profileModel: UpdateProfileModel) {
        viewModelScope.launch {
            val result = updateProfileUseCase(profileModel)
            if (result.isSuccess) {
                fetchUserProfile()
                _isEditing.value = false
                _effect.emit(ProfileEffect.ProfileUpdated)
            } else {
                val exception = result.exceptionOrNull() ?: AuthException.UnknownError()
                _effect.emit(ProfileEffect.ShowErrorMessage(exception.message ?: "Unknown error"))
                _ProfileState.value = ProfileState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    private fun cancelEditing() {
        _isEditing.value = false
        fetchUserProfile()
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.emit(ProfileEffect.NavigateToPhoneNumberScreen)
        }
    }

    fun updateEditableCity(newCity: String) {
        _editableCity.value = newCity
    }

    fun updateEditableBirthday(newBirthday: String) {
        _editableBirthday.value = newBirthday
    }

    fun updateEditableStatus(newStatus: String) {
        _editableStatus.value = newStatus
    }

    fun navigateBack() {
        viewModelScope.launch {
            _effect.emit(ProfileEffect.NavigateBack)
        }
    }

    fun encodeImageToBase64(imageUri: Uri, context: Context): String? {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun isDateValid(dateString: String): Boolean {
        if (dateString.length != 10) return false

        val regex = """^\d{2}\.\d{2}\.\d{4}$""".toRegex()

        if (!regex.matches(dateString)) return false

        return try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(dateString) != null
        } catch (e: ParseException) {
            false
        }
    }
}

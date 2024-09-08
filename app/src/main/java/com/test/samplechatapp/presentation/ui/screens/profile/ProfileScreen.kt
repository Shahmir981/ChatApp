package com.test.samplechatapp.presentation.ui.screens.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.test.samplechatapp.R
import com.test.samplechatapp.domain.model.AvatarModel
import com.test.samplechatapp.domain.model.UpdateProfileModel
import com.test.samplechatapp.domain.model.ProfileModel
import com.test.samplechatapp.presentation.MainViewModel
import com.test.samplechatapp.presentation.ui.components.utils.ChatButton
import com.test.samplechatapp.presentation.ui.components.utils.ChatsAppBar
import com.test.samplechatapp.presentation.ui.components.utils.CoilImage
import com.test.samplechatapp.presentation.ui.components.utils.ErrorScreen
import com.test.samplechatapp.presentation.ui.components.utils.LoadingScreen
import com.test.samplechatapp.presentation.ui.components.navigation.ScreenType
import com.test.samplechatapp.presentation.ui.theme.Black70
import com.test.samplechatapp.presentation.ui.theme.Gray
import com.test.samplechatapp.presentation.ui.theme.Gray20
import com.test.samplechatapp.presentation.ui.theme.Withe50

@Composable
fun ProfileScreen(
    mainViewModel: MainViewModel,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val context = LocalContext.current

    val editableStatus by viewModel.editableStatus.collectAsState()
    val editableName by viewModel.editableName.collectAsState()
    val editableCity by viewModel.editableCity.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()
    val state by viewModel.profileState.collectAsState()
    val editableBirthday by viewModel.editableBirthday.collectAsState()
    val effectFlow = viewModel.effect

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToPhoneNumberScreen -> mainViewModel.updateStartDestination(
                    ScreenType.LoginGraph.route
                )

                is ProfileEffect.ShowErrorMessage -> Toast.makeText(
                    context,
                    effect.message,
                    Toast.LENGTH_SHORT
                ).show()

                is ProfileEffect.ProfileUpdated -> Toast.makeText(
                    context,
                    "Profile updated",
                    Toast.LENGTH_SHORT
                ).show()

                is ProfileEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            ChatsAppBar(
                title = stringResource(R.string.profile),
                navigationIcon = Icons.Default.ArrowBack,
                actionIcon = Icons.Default.Edit,
                onNavigationClick = { viewModel.navigateBack() },
                onActionClick = { viewModel.handleIntent(ProfileIntent.StartEditing) }
            )
        },
        content = { innerPadding ->
            when (state) {
                is ProfileState.Loading -> LoadingScreen(innerPadding)
                is ProfileState.Success -> UserProfileContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    viewModel = viewModel,
                    userProfile = (state as ProfileState.Success).userProfile,
                    isEditing = isEditing,
                    avatarUri = avatarUri,
                    editableName = editableName,
                    editableCity = editableCity,
                    editableBirthday = editableBirthday,
                    editableStatus = editableStatus,
                    onUpdateProfile = { updatedProfile -> viewModel.handleIntent(ProfileIntent.UpdateProfile(updatedProfile)) },
                    onLogout = { viewModel.handleIntent(ProfileIntent.Logout) },
                    onAvatarSelected = { uri -> viewModel.handleIntent(ProfileIntent.SetAvatarUri(uri)) }
                )
                is ProfileState.Error -> ErrorScreen((state as ProfileState.Error).message, innerPadding)
            }
        }
    )
}


@Composable
fun UserProfileContent(
    modifier: Modifier,
    viewModel: ProfileViewModel,
    userProfile: ProfileModel,
    isEditing: Boolean,
    avatarUri: Uri?,
    editableName: String,
    editableCity: String,
    editableBirthday: String,
    editableStatus: String,
    onUpdateProfile: (UpdateProfileModel) -> Unit,
    onLogout: () -> Unit,
    onAvatarSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> onAvatarSelected(uri) }
        }
    }

    val isBirthdayValid = viewModel.isDateValid(editableBirthday)

    Column(
        modifier = modifier
            .padding(25.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarSection(
            avatarUri = avatarUri,
            avatarUrl = userProfile.avatarUrl,
            isEditing = isEditing,
            onClick = {
                imagePickerLauncher.launch(Intent(Intent.ACTION_PICK).apply { type = "image/*" })
            }
        )
        EditableTextField(
            label = stringResource(R.string.username),
            value = editableName,
            onValueChange = { },
            isEditing = isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        EditableTextField(
            label = stringResource(R.string.phone),
            value = userProfile.phone,
            onValueChange = {},
            isEditing = false,
            modifier = Modifier.fillMaxWidth()
        )
        EditableTextField(
            label = stringResource(R.string.birth_date),
            value = editableBirthday,
            onValueChange = { newBirthday -> viewModel.updateEditableBirthday(newBirthday) },
            isEditing = isEditing,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            errorMessage = if (isEditing && !isBirthdayValid) "Format is dd.mm.yyyy" else null,
            modifier = Modifier.weight(1f)
        )
        EditableTextField(
            label = stringResource(R.string.city),
            value = editableCity,
            onValueChange = { newCity -> viewModel.updateEditableCity(newCity) },
            isEditing = isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        EditableTextField(
            label = stringResource(R.string.about),
            value = editableStatus,
            onValueChange = { newAbout -> viewModel.updateEditableStatus(newAbout) },
            isEditing = isEditing,
            modifier = Modifier.fillMaxWidth()
        )


        if (isEditing) {
            SaveButton(
                onClick = {
                    if (isBirthdayValid) {
                        val avatarModel = avatarUri?.let { uri ->
                            val base64Image = viewModel.encodeImageToBase64(uri, context)
                            AvatarModel(filename = "avatar.png", base64 = base64Image)
                        }
                        onUpdateProfile(
                            UpdateProfileModel(
                                name = editableName,
                                username = userProfile.username,
                                birthday = editableBirthday,
                                city = editableCity,
                                status = editableStatus,
                                avatar = avatarModel
                            )
                        )
                    }
                },
                enabled = isBirthdayValid
            )
        } else {
            ChatButton(onClick = onLogout, text = stringResource(R.string.logout))
        }
    }
}

@Composable
fun EditableTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    isRequired: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
    ) {
        TextField(
            value = value,
            onValueChange = {
                val formattedValue = visualTransformation.filter(AnnotatedString(it)).text.text
                onValueChange(formattedValue)
            },
            label = {
                Row {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (isRequired) {
                        Text(
                            text = " *",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (isEditing) Withe50 else Gray20,
                unfocusedContainerColor = if (isEditing) Withe50 else Gray20,
                disabledContainerColor = Gray20,
                focusedIndicatorColor = Black70,
                unfocusedIndicatorColor = Black70,
                disabledTextColor = Color.Black,
                cursorColor = Black70,
                disabledIndicatorColor = Color.Transparent,
                focusedSupportingTextColor = Black70,
                disabledSupportingTextColor = Withe50,
                unfocusedLabelColor = Black70,
                focusedLabelColor = Black70,
                disabledLabelColor = Black70
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            enabled = isEditing,
        )
        Spacer(modifier = Modifier.height(8.dp))
        errorMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AvatarSection(
    modifier: Modifier = Modifier,
    avatarUri: Uri?,
    avatarUrl: String?,
    isEditing: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .padding(16.dp)
            .clickable { if (isEditing) onClick() },
        contentAlignment = Alignment.Center
    ) {
        CoilImage(
            imageUrl = avatarUri?.toString() ?: avatarUrl,
            defaultImageResId = R.drawable.profile_placeholder,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.medium)
        )
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Gray)
    ) {
        Text(stringResource(R.string.save), color = Color.White)
    }
}


package com.test.samplechatapp.presentation.ui.screens.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.test.samplechatapp.R
import com.test.samplechatapp.presentation.MainViewModel
import com.test.samplechatapp.presentation.ui.components.navigation.ScreenType
import com.test.samplechatapp.presentation.ui.components.utils.ChatButton
import com.test.samplechatapp.presentation.ui.components.utils.ChatsAppBar
import com.test.samplechatapp.presentation.ui.components.utils.ErrorScreen
import com.test.samplechatapp.presentation.ui.components.utils.LoadingScreen
import com.test.samplechatapp.presentation.ui.theme.Black70
import com.test.samplechatapp.presentation.ui.theme.LightGray
import com.test.samplechatapp.presentation.ui.theme.Withe50

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    mainViewModel: MainViewModel,
    paddingValues: PaddingValues,
    phoneNumber: String
) {
    val state by viewModel.state.collectAsState()
    val effectFlow = viewModel.effect
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is RegistrationEffect.NavigateToChatList -> {
                    mainViewModel.updateStartDestination(ScreenType.ChatsGraph.route)
                }
                is RegistrationEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChatsAppBar(title = stringResource(R.string.registration))

        when (state) {
            is RegistrationState.Initial -> {
                RegistrationContent(
                    name = viewModel.name,
                    onNameChange = viewModel::onNameChange,
                    username = viewModel.username,
                    onUsernameChange = viewModel::onUsernameChange,
                    phoneNumber = phoneNumber,
                    onRegisterClick = {
                        viewModel.handleIntent(RegistrationIntent.RegisterUser(phoneNumber, viewModel.name, viewModel.username))
                    }
                )
            }

            is RegistrationState.Loading -> {
                LoadingScreen(innerPadding = paddingValues)
            }

            is RegistrationState.Error -> {
                ErrorScreen(message = (state as RegistrationState.Error).message, innerPadding = paddingValues)
            }

            is RegistrationState.Success -> {}
        }
    }
}

@Composable
fun RegistrationContent(
    name: String,
    onNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    phoneNumber: String,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            RegistrationTextField(
                value = phoneNumber,
                label = stringResource(R.string.phone_number),
                enabled = false,
                onValueChange = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            RegistrationTextField(
                value = name,
                label = stringResource(R.string.name),
                onValueChange = onNameChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            RegistrationTextField(
                value = username,
                label = stringResource(R.string.username),
                keyboardType = KeyboardType.Ascii,
                onValueChange = onUsernameChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChatButton(
                text = stringResource(R.string.register),
                onClick = onRegisterClick
            )
        }
    }
}

@Composable
fun RegistrationTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Withe50,
            unfocusedContainerColor = Withe50,
            disabledContainerColor = LightGray,
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
            .clip(MaterialTheme.shapes.medium)
    )
}


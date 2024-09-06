package com.test.samplechatapp.presentation.ui.screens.verificationCode

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.test.samplechatapp.R
import com.test.samplechatapp.presentation.MainViewModel
import com.test.samplechatapp.presentation.ui.components.utils.ChatButton
import com.test.samplechatapp.presentation.ui.components.utils.ChatsAppBar
import com.test.samplechatapp.presentation.ui.components.utils.ErrorScreen
import com.test.samplechatapp.presentation.ui.components.utils.LoadingScreen
import com.test.samplechatapp.presentation.ui.components.navigation.ScreenType
import com.test.samplechatapp.presentation.ui.theme.Black70
import com.test.samplechatapp.presentation.ui.theme.LightGray
import com.test.samplechatapp.presentation.ui.theme.Withe50


@Composable
fun SmsCodeScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    viewModel: SmsVerificationCodeViewModel = hiltViewModel(),
    navController: NavHostController,
    paddingValues: PaddingValues,
    phoneNumber: String
) {
    val state by viewModel.state.collectAsState()
    val otpValues by viewModel.otpValues.collectAsState()
    val effectFlow = viewModel.effect
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is VerificationCodeEffect.NavigateToChatList -> {
                    navController.navigate(ScreenType.ChatListScreen.route)
                }
                is VerificationCodeEffect.NavigateToRegistration -> {
                    navController.navigate(ScreenType.RegistrationScreen.createRoute(effect.phoneNumber)) {
                        popUpTo(ScreenType.SmsCodeScreen.route) { inclusive = true }
                    }
                }
                is VerificationCodeEffect.ShowErrorToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ChatsAppBar(
        title = stringResource(R.string.verification_code_screen),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is VerificationCodeState.Initial -> {
                OtpInputSection(
                    otpValues = otpValues,
                    onOtpValueChange = { index, newValue -> viewModel.onOtpValueChange(index, newValue) },
                    onVerifyCodeClick = { viewModel.verifyCode(phoneNumber) }
                )
            }

            is VerificationCodeState.Loading -> {
                LoadingScreen(innerPadding = paddingValues)
            }

            is VerificationCodeState.Error -> {
                ErrorScreen(message = (state as VerificationCodeState.Error).message, innerPadding = paddingValues)
            }

            else -> Unit
        }
    }
}

@Composable
fun OtpInputSection(
    otpValues: List<String>,
    onOtpValueChange: (index: Int, newValue: String) -> Unit,
    onVerifyCodeClick: () -> Unit
) {
    val focusRequesters = remember { List(otpValues.size) { FocusRequester() } }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            otpValues.forEachIndexed { index, value ->
                TextField(
                    value = value,
                    onValueChange = { newValue ->
                        onOtpValueChange(index, newValue)
                        if (newValue.isNotEmpty() && index < otpValues.size - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (newValue.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(42.dp)
                        .height(60.dp)
                        .focusRequester(focusRequesters[index]),
                    singleLine = true,
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
                        disabledLabelColor = Black70,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChatButton(text = stringResource(R.string.verify_code), onClick = onVerifyCodeClick)
    }
}

//fun NavHostController.navigateSingleTopToAndRetainState(route: String) = this.navigate(route) {
//    popUpTo(
//
//    ) {
//        saveState = false
//    }
//    launchSingleTop = true
//    restoreState = true
//}


package com.test.samplechatapp.presentation.ui.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.test.samplechatapp.R
import com.test.samplechatapp.domain.model.CountryModel
import com.test.samplechatapp.presentation.ui.components.utils.ChatsAppBar
import com.test.samplechatapp.presentation.ui.components.utils.ErrorScreen
import com.test.samplechatapp.presentation.ui.components.utils.LoadingScreen
import com.test.samplechatapp.presentation.ui.components.navigation.ScreenType
import com.test.samplechatapp.presentation.ui.theme.Black70
import com.test.samplechatapp.presentation.ui.theme.Gray
import com.test.samplechatapp.presentation.ui.theme.LightGray
import com.test.samplechatapp.presentation.ui.theme.Withe50


@Composable
fun PhoneNumberScreen(
    viewModel: PhoneNumberViewModel = hiltViewModel(),
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val state by viewModel.state.collectAsState()
    val countries by viewModel.countries.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ChatsAppBar(
            title = stringResource(R.string.phone_number),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (state) {
                    is PhoneNumberState.Initial -> {
                        PhoneNumberInput(
                            countries = countries,
                            selectedCountry = selectedCountry,
                            phoneNumber = phoneNumber,
                            onCountrySelected = { selectedCountryCode ->
                                viewModel.handleIntent(
                                    PhoneNumberIntent.SelectCountry(
                                        selectedCountryCode
                                    )
                                )
                            },
                            onPhoneNumberChanged = { number ->
                                viewModel.handleIntent(PhoneNumberIntent.UpdatePhoneNumber(number))
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        selectedCountry?.let { country ->
                            SendCodeButton(
                                phoneNumber = phoneNumber,
                                onClick = {
                                    val fullPhoneNumber = "${country.codeNumber}$phoneNumber"
                                    viewModel.handleIntent(
                                        PhoneNumberIntent.SendPhoneNumber(
                                            fullPhoneNumber
                                        )
                                    )
                                }
                            )
                        }
                    }

                    is PhoneNumberState.Loading -> {
                        LoadingScreen(innerPadding = paddingValues)
                    }

                    is PhoneNumberState.Success -> {
                        navController.navigate(ScreenType.SmsCodeScreen.createRoute(phoneNumber))
                    }

                    is PhoneNumberState.Error -> {
                        ErrorScreen(
                            message = (state as PhoneNumberState.Error).message,
                            innerPadding = paddingValues
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneNumberInput(
    countries: List<CountryModel>,
    selectedCountry: CountryModel?,
    phoneNumber: String,
    onCountrySelected: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            selectedCountry?.let {
                FlagSelector(
                    selectedRegion = it.code,
                    onClick = { showDialog = true }
                )

                Spacer(modifier = Modifier.width(8.dp))

                CountryCodeField(mobileCode = it.codeNumber)

                Spacer(modifier = Modifier.width(8.dp))
            }

            PhoneNumberField(
                phoneNumber = phoneNumber,
                onPhoneNumberChanged = onPhoneNumberChanged
            )
        }

        if (showDialog) {
            CountryPickerDialog(
                countries = countries,
                onCountrySelected = { region, _ ->
                    onCountrySelected(region)
                    showDialog = false
                },
                onDismissRequest = { showDialog = false }
            )
        }
    }
}

@Composable
fun FlagSelector(selectedRegion: String, onClick: () -> Unit) {
    val flagResId = getFlagResource(selectedRegion)
    Image(
        painter = painterResource(id = flagResId),
        contentDescription = null,
        modifier = Modifier
            .size(24.dp)
            .clickable { onClick() }
    )
}

@Composable
fun CountryCodeField(mobileCode: String) {
    TextField(
        value = mobileCode,
        onValueChange = {},
        label = { Text("Code") },
        modifier = Modifier.width(80.dp),
        enabled = false,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = LightGray,
            disabledIndicatorColor = Color.Transparent,
            disabledSupportingTextColor = Withe50,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
}

@Composable
fun PhoneNumberField(
    phoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit
) {
    TextField(
        value = phoneNumber,
        onValueChange = { newNumber ->
            val digitsOnly = newNumber.replace("\\D".toRegex(), "")
            if (digitsOnly.length <= 10) {
                onPhoneNumberChanged(digitsOnly)
            }
        },
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
        label = { Text(stringResource(R.string.phone_number)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun SendCodeButton(
    phoneNumber: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        enabled = phoneNumber.length == 10,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Gray)
    ) {
        Text(stringResource(R.string.next))
    }
}


@Composable
fun CountryPickerDialog(
    countries: List<CountryModel>,
    onCountrySelected: (String, String) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = stringResource(R.string.choose_country)) },
        text = {
            LazyColumn {
                items(countries) { country ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCountrySelected(country.code, country.codeNumber)
                                onDismissRequest()
                            }
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = country.flagResId),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = country.name, color = Color.Black)
                    }
                }
            }
        },
        confirmButton = {
           // ignore
        }
    )
}

fun getFlagResource(countryCode: String): Int {
    return countries[countryCode] ?: R.drawable.flag_of_armenia
}

val countries = mapOf(
    "GA" to R.drawable.flag_of_georgia,
    "AM" to R.drawable.flag_of_armenia,
    "RU" to R.drawable.flag_of_russia
)
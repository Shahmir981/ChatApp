package com.test.samplechatapp.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.test.samplechatapp.presentation.MainViewModel
import com.test.samplechatapp.presentation.ui.components.navigation.MainNavHost
import com.test.samplechatapp.presentation.ui.components.scafford.Root


@Composable
fun App(viewModel: MainViewModel = hiltViewModel()) {

    val startDestination = viewModel.startDestination.collectAsState().value

    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Root(navController = navController) { paddingValues ->
            MainNavHost(
                navController = navController,
                paddingValues = paddingValues,
                startDestination = startDestination
            )
        }
    }
}

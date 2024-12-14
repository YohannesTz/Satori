package com.github.yohannestz.satori.ui.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.yohannestz.satori.ui.theme.SatoriTheme

//TODO refactor this to be more flexible
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    showNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showNavigateBack) {
                BackIconButton(onClick = navigateBack)
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultTopAppBarPreview() {
    SatoriTheme {
        DefaultTopAppBar(
            title = "Satori",
            navigateBack = {},
            showNavigateBack = false
        )
    }
}
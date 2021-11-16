package com.aniapi.dreamsubanime.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.aniapi.dreamsubanime.R

@Composable
fun AppWordMark() {
    Icon(
        painter = painterResource(R.drawable.ic_app_wordmark),
        contentDescription = stringResource(R.string.app_name),
        tint = MaterialTheme.colors.primary,
    )
}

@Composable
fun AppLogo() {
    Icon(
        painter = painterResource(R.drawable.ic_app_logo),
        contentDescription = null, // decorative
        tint = MaterialTheme.colors.primary
    )
}
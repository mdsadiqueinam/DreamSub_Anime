package com.aniapi.dreamsubanime.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.aniapi.dreamsubanime.R

@Composable
fun AnimeScreenTopBar(
    title: String,
    onBack: () -> Unit,
) {
    InsetAwareTopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        title = {
            Text(
                text = title,
                letterSpacing = (1.5).sp,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    )
}

@Preview
@Composable
fun AnimeScreenTopBarPreview() {
    AnimeScreenTopBar(
        title = "Demon Slayer",
        onBack = {}
    )
}
package com.aniapi.dreamsubanime.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aniapi.dreamsubanime.R


val FontFamily.Companion.MontSerrate: FontFamily
	get() = FontFamily(
		Font(R.font.montserrat_regular),
		Font(R.font.montserrat_light, FontWeight.Light),
		Font(R.font.montserrat_semibold, FontWeight.SemiBold)
	)

val AppTypography = Typography(
	defaultFontFamily = FontFamily.MontSerrate
)

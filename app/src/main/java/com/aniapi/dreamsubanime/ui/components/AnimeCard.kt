package com.aniapi.dreamsubanime.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.aniapi.dreamsubanime.R
import com.dreamSubAnime.api.models.entities.Anime

@ExperimentalCoilApi
@Composable
fun AnimeCard(anime: Anime, onClick: () -> Unit) {
    val image = anime.bannerImage ?: anime.coverImage
    ImageCard(
        painter = rememberImagePainter(
            data = image,
            onExecute = { _, _ -> true },
            builder = {
                crossfade(true)
                placeholder(R.drawable.ic_broken_image)
            }
        ),
        title = anime.titles?.english ?: anime.titles?.japanese ?: "No title",
        contentDescription = "",
        episodes = anime.episodesCount ?: 0,
        modifier = Modifier.fillMaxWidth().clickable(enabled = true, onClick = onClick)
    )
}

@ExperimentalCoilApi
@Preview
@Composable
fun PreviewImageCard() {
    ImageCard(
        painter = rememberImagePainter(
            data = "https://www.cnet.com/a/img/C45n2FvoNOUVO1rE6kg5kNEt0Dg=/940x0/2021/05/03/bd37964d-fe5c-4574-9df3-1986e0bf5050/demon-slayer-mugen-train-1239731.jpg",
            onExecute = { _, _ -> true },
            builder = {
                crossfade(true)
                placeholder(R.drawable.ic_broken_image)
            }
        ),
        title = "Demon Slayer",
        contentDescription = "",
        episodes = 16,
    )
}

@Composable
fun ImageCard(
    painter: Painter,
    title: String,
    contentDescription: String,
    episodes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = 5.dp
    ) {
        Box(
            modifier = Modifier.height(200.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 50f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Text(
                        text = title,
                        style = TextStyle(color = Color.White, fontSize = 18.sp)
                    )
                    Text(
                        text = stringResource(R.string.episodes_count, episodes),
                        style = TextStyle(color = Color.White, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}

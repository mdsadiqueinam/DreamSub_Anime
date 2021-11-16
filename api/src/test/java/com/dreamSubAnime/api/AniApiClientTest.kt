package com.dreamSubAnime.api

import com.dreamSubAnime.api.models.entities.Format
import com.dreamSubAnime.api.models.entities.SeasonPeriod
import com.dreamSubAnime.api.models.entities.Status
import com.dreamSubAnime.api.models.entities.Type
import com.dreamSubAnime.api.services.AniApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class AniApiClientTest {

    private val client = AniApiClient.getClient()

    @Test
    fun `GET anime`() = runBlocking {
        val response = client.getAnime()
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET anime by format`() = runBlocking {
        val response = client.getAnimeByFormats(listOf(Format.MUSIC, Format.MOVIE))
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET anime by season`() = runBlocking {
        val response = client.getAnimeBySeason(SeasonPeriod.WINTER)
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET anime by status`() = runBlocking {
        val response = client.getAnimeByStatus(Status.FINISHED)
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET anime by id`() = runBlocking {
        val response = client.getAnimeById((1..1000L).random())
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET random anime`() = runBlocking {
        val response = client.getRandomAnime((1..50).random())
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET episode by Id`() = runBlocking {
        val response = client.getEpisodeById((1..1000).random())
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET episodes`() = runBlocking {
        val response = client.getEpisodes(
            animeId = (1..1000).random(),
            episodeNumber = (1..10).random(),
            source = "dreamsub",
            locale = "it"
        )
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET episodes by number`() = runBlocking {
        val response = client.getEpisodes(episodeNumber = (1..10).random())
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET episodes by anime Id`() = runBlocking {
        val response = client.getEpisodes(animeId = (1..1000).random())
        assertNotNull(response.body()?.message)
    }

    @Test
    fun `GET resources`() = runBlocking {
        val response = client.getResource(Type.LOCALIZATIONS)
        assertNotNull(response.body()?.message)
    }
}
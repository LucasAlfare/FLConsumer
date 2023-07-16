package com.lucasalfare.flconsumer.core

import com.lucasalfare.fllistener.EventManageable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * The root API prefix to be called when requesting data of.
 */
const val API_URL_PREFIX = "https://api.github.com/users/"

/**
 * The main HTTP connector, provided by the Ktor dependency.
 */
private val myClient = HttpClient(CIO)

/**
 * Manages the input and output of events that comes and needs to go to the UI.
 */
class ApiManager : EventManageable() {

  private var tmpUserReposUrl = ""

  init {
    initiated = true
  }

  override fun onInitiated() {

  }

  override fun onNotInitiated() {

  }

  override fun onEvent(event: Any, data: Any?, origin: Any?) {
    if (event == "api-request") {
      val targetUser = data as String
      handleUserInfoRequest(targetUser)
    }

    if (event == "client-dispose") {
      myClient.close()
    }
  }

  private fun handleUserInfoRequest(targetUser: String) {
    performRequest(
      url = "$API_URL_PREFIX$targetUser",
      onPreRequest = { notifyListeners("api-pre-request") }
    ) { response ->
      //TODO: validate response before parse

      val jsonObject = jsonTextAsJsonObject(responseToText(response))
      getModelFor(jsonObject, "user").forEach {
        when (it.jsonPropertyName) {
          JsonPropertyName.AvatarUrl -> State.Companion.Header.currentAvatarUrl = it.value as String
          JsonPropertyName.Login -> State.Companion.Header.currentLogin = it.value as String
          JsonPropertyName.Name -> State.Companion.Header.currentName = it.value as String
          JsonPropertyName.Bio -> State.Companion.Header.currentBio = it.value as String
          JsonPropertyName.Followers -> State.Companion.Header.currentFollowers = it.value as Int
          JsonPropertyName.PublicRepos -> State.Companion.Header.currentPublicRepos = it.value as Int

          JsonPropertyName.ReposUrl -> handleReposRequest(it.value as String)
          else -> {}
        }
      }
    }
  }

  private fun handleReposRequest(reposUrl: String) {
    performRequest(reposUrl) { response ->
      val jsonArray = jsonTextAsJsonArray(responseToText(response))
      State.Companion.Repos.currentRepos.clear()

      getModelsFor(jsonArray, "repo").forEach { repoModel ->
        val nextRepoData = State.Companion.Repos.Companion.RepoData()
        repoModel.forEach {
          when (it.jsonPropertyName) {
            JsonPropertyName.RepoName -> nextRepoData.name = it.value as String
            JsonPropertyName.RepoDescription -> nextRepoData.description = it.value as String
            JsonPropertyName.RepoStargazersCount -> nextRepoData.stargazersCount = it.value as Int
            JsonPropertyName.RepoForksCount -> nextRepoData.forksCount = it.value as Int
            else -> {}
          }
        }

        State.Companion.Repos.currentRepos += nextRepoData
      }

      notifyListeners("api-fetched")
    }
  }
}

/**
 * Performs a call to a particular API url, using the main HTTP client.
 * Also takes a function callback to be executed when the response was
 * succsessful received.
 */
fun performRequest(
  url: String = "https://api.github.com/users/LucasAlfare",
  onPreRequest: () -> Unit = {},
  onResponseReceived: suspend (HttpResponse) -> Unit = {}
) {
  onPreRequest()

  CoroutineScope(Job()).launch {
    val response = myClient.get(url)
    onResponseReceived(response)
    this.cancel("The URL request was performed.")
  }
}

package com.lucasalfare.flconsumer.core

import com.google.gson.JsonParser
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
      performRequest("$API_URL_PREFIX$targetUser") { response ->
        response.bodyAsText().let {
          val root = JsonParser.parseString(it).asJsonObject
          val userInfoModel = getUserInfoModelFor(root)

          // just updates observable state static fields
          State.Companion.Header.currentAvatarUrl =
            userInfoModel.jsonProperties[JsonPropertyName.AvatarUrl]!!.value as String

          State.Companion.Header.currentLogin =
            userInfoModel.jsonProperties[JsonPropertyName.Login]!!.value as String

          State.Companion.Header.currentName =
            userInfoModel.jsonProperties[JsonPropertyName.Name]!!.value as String

          State.Companion.Header.currentBio =
            userInfoModel.jsonProperties[JsonPropertyName.Bio]!!.value as String

          State.Companion.Header.currentPublicRepos =
            userInfoModel.jsonProperties[JsonPropertyName.PublicRepos]!!.value as Int

          State.Companion.Header.currentFollowers =
            userInfoModel.jsonProperties[JsonPropertyName.Followers]!!.value as Int

          notifyListeners("api-fetched")
        }
      }
    }

    if (event == "client-dispose") {
      myClient.close()
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
  onResponseReceived: suspend (HttpResponse) -> Unit = {}
) {
  CoroutineScope(Job()).launch {
    val response = myClient.get(url)
    onResponseReceived(response)
    this.cancel("The URL request was performed.")
  }
}

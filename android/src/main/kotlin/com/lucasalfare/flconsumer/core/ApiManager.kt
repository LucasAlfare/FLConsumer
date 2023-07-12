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
import java.lang.Exception


private val myClient = HttpClient(CIO)

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
      val targetUrl = data as String
      performRequest(targetUrl) {
        val txt = it.bodyAsText()
        val root = JsonParser.parseString(txt).asJsonObject

        try {
          State.currentAvatarUrl.value = root.get("avatar_url").asString
          State.currentUserNickName.value = root.get("login").asString
          State.currentUserRealName.value = root.get("name").asString
          State.currentUserBio.value = root.get("bio").asString
          State.currentNumberOfRepositories.value = root.get("public_repos").asInt
          State.currentNumberOfFollowers.value = root.get("followers").asInt

          notifyListeners("api-fetched")
        } catch (e: Exception) {
          notifyListeners("api-fetch-error")
        }
      }
    }

    if (event == "client-dispose") {
      myClient.close()
    }
  }
}

fun performRequest(
  url: String = "https://api.github.com/users/LucasAlfare",
  onResponseReceived: suspend (HttpResponse) -> Unit = {}
) {
  CoroutineScope(Job()).launch  {
    val response = myClient.get(url)
    onResponseReceived(response)
    this.cancel("The URL request was performed.")
  }
}
package com.lucasalfare.flconsumer.core

import com.google.gson.JsonElement
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


const val API_URL_PREFIX = "https://api.github.com/users/"
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
      val targetUser = data as String
      performRequest("$API_URL_PREFIX$targetUser") { response ->
        response.bodyAsText().let { txt ->
          val root = JsonParser.parseString(txt).asJsonObject

          if (root.has("avatar_url")) {
            root.get("avatar_url").let {
              State.currentAvatarUrl.value =
                if (!it.isJsonNull) it.asString else ""
            }
          }

          if (root.has("login")) {
            root.get("login").let {
              State.currentUserNickName.value =
                if (!it.isJsonNull) it.asString else ""
            }
          }

          if (root.has("name")) {
            root.get("name").let {
              State.currentUserRealName.value =
                if (!it.isJsonNull) it.asString else ""
            }
          }

          if (root.has("bio")) {
            root.get("bio").let {
              State.currentUserBio.value =
                if (!it.isJsonNull) it.asString else ""
            }
          }

          if (root.has("public_repos")) {
            root.get("public_repos").let {
              State.currentNumberOfRepositories.value =
                if (!it.isJsonNull) it.asInt else 0
            }
          }

          if (root.has("followers")) {
            root.get("followers").let {
              State.currentNumberOfFollowers.value =
                if (!it.isJsonNull) it.asInt else 0
            }
          }

          notifyListeners("api-fetched")
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
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
        response.bodyAsText().let {
          val root = JsonParser.parseString(it).asJsonObject
          val userInfoModel = getUserInfoModelFor(root)

          State.Companion.Header.currentAvatarUrl = userInfoModel.avatarUrl
          State.Companion.Header.currentLogin = userInfoModel.login
          State.Companion.Header.currentName = userInfoModel.name
          State.Companion.Header.currentBio = userInfoModel.bio
          State.Companion.Header.currentPublicRepos = userInfoModel.publicRepos
          State.Companion.Header.currentFollowers = userInfoModel.followers

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
package com.lucasalfare.flconsumer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.lucasalfare.flconsumer.ui.composables.Header


/**
 * The only pruporse of App is read and show up state fields values and collect inputs (and notify those inputs to core/backend, as well)
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App() {
  var targetNickName by remember { mutableStateOf("LucasAlfare") }
  var apiResultCode by remember { mutableStateOf(0) }
  val keyboardController = LocalSoftwareKeyboardController.current

  DisposableEffect(true) {
    val callback = uiManager.addCallback { event, _ ->
      if (event == "api-fetched") {
        apiResultCode = 1
      } else if (event == "api-fetch-error") {
        apiResultCode = 2
      }
    }

    onDispose { uiManager.removeCallback(callback) }
  }

  Column(modifier = Modifier.padding(12.dp).fillMaxSize()) {
    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
      TextField(
        value = targetNickName,
        onValueChange = {
          targetNickName = it
        },
        modifier = Modifier.padding(4.dp)
      )

      Button(
        onClick = {
          if (targetNickName.isNotEmpty()) {
            keyboardController!!.hide()
            apiResultCode = 0
            uiManager.notifyListeners(
              event = "api-request",
              data = "https://api.github.com/users/${targetNickName}"
            )
          }
        },
        modifier = Modifier.padding(4.dp)
      ) {
        Text("Search user...")
      }
    }

    /*
    0=idle_state
    1=successfully_fetched_state
    2=error_state
     */
    when (apiResultCode) {
      1 -> {
        Header()
      }

      0 -> {
        Text("waiting for API fetch the search....")
      }

      else -> {
        Text("Error. The requested user seems doesn't exists in GitHub...")
      }
    }
  }
}
package com.lucasalfare.flconsumer

import androidx.compose.foundation.layout.*
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
import com.lucasalfare.flconsumer.ui.composables.Repos


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
      when (event) {
        "api-pre-request" -> {
          apiResultCode = 1
        }
        "api-fetched" -> {
          apiResultCode = 2
        }
        "api-fetch-error" -> {
          apiResultCode = 3
        }
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
        enabled = apiResultCode != 1,
        onClick = {
          if (targetNickName.isNotEmpty()) {
            keyboardController!!.hide()
            apiResultCode = 0
            uiManager.notifyListeners(
              event = "api-request",
              data = targetNickName
            )
          }
        },
        modifier = Modifier.padding(4.dp)
      ) {
        Text("Search user")
      }
    }

    /*
    0=idle_state
    1=perfoming_request
    2=successfully_fetched_state
    3=error_state
     */
    when (apiResultCode) {
      0 -> Text("Search someone!")

      1 -> {
        Text("waiting for API fetch the search....")
      }

      2 -> {
        Column {
          Header()
          Spacer(modifier = Modifier.height(50.dp))
          Repos()
        }
      }

      3 -> {
        Text("Error trying to find the specified user.")
      }
    }
  }
}
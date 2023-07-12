package com.lucasalfare.flconsumer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lucasalfare.flconsumer.core.State

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

@Composable
fun Header() {
  Column {
    Row(modifier = Modifier.fillMaxWidth()) {
      Box(modifier = Modifier.size(120.dp).background(Color.Gray)) {
        AsyncImage(
          model = State.currentAvatarUrl.value,
          contentDescription = null
        )
      }

      Column {
        Text(
          text = State.currentUserNickName.value,
          fontSize = 35.sp,
          modifier = Modifier.padding(4.dp)
        )

        Text(
          text = State.currentUserRealName.value,
          fontSize = 16.sp,
          modifier = Modifier.padding(4.dp)
        )

        Text(
          text = State.currentUserBio.value,
          fontSize = 12.sp,
          color = Color.Gray,
          modifier = Modifier.padding(4.dp)
        )
      }
    }

    Row(modifier = Modifier.padding(4.dp)) {
      Text(text = buildAnnotatedString {
        withStyle(SpanStyle()) {
          append("Repositories: ")
        }

        withStyle(
          SpanStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        ) {
          append("${State.currentNumberOfRepositories.value}")
        }

      })

      Spacer(modifier = Modifier.width(16.dp))

      Text(text = buildAnnotatedString {
        withStyle(SpanStyle()) {
          append("Followers: ")
        }

        withStyle(
          SpanStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        ) {
          append("${State.currentNumberOfFollowers.value}")
        }

      })
    }
  }
}
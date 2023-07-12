package com.lucasalfare.flconsumer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lucasalfare.flconsumer.core.State

//TODO: move state data to params?
@Composable
fun Header() {
  Column {
    Row(modifier = Modifier.fillMaxWidth()) {
      Box(modifier = Modifier.size(120.dp).background(Color.Gray)) {
        AsyncImage(
          model = State.Companion.Header.currentAvatarUrl,
          contentDescription = null
        )
      }

      Column {
        Text(
          text = State.Companion.Header.currentLogin,
          fontSize = 35.sp,
          modifier = Modifier.padding(4.dp)
        )

        Text(
          text = State.Companion.Header.currentName,
          fontSize = 16.sp,
          modifier = Modifier.padding(4.dp)
        )

        Text(
          text = State.Companion.Header.currentBio,
          fontSize = 12.sp,
          color = Color.Gray,
          modifier = Modifier.padding(4.dp)
        )
      }
    }

    Row(modifier = Modifier.padding(4.dp)) {
      Text(
        text = buildAnnotatedString {
          withStyle(SpanStyle()) {
            append("Repositories: ")
          }

          withStyle(
            SpanStyle(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          ) {
            append("${State.Companion.Header.currentPublicRepos}")
          }
        }
      )

      Spacer(modifier = Modifier.width(16.dp))

      Text(
        text = buildAnnotatedString {
          withStyle(SpanStyle()) {
            append("Followers: ")
          }

          withStyle(
            SpanStyle(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          ) {
            append("${State.Companion.Header.currentFollowers}")
          }
        }
      )
    }
  }
}
package com.lucasalfare.flconsumer.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasalfare.flconsumer.core.State

@Composable
fun Repos() {
  Column {
    Text(buildAnnotatedString {
      withStyle(SpanStyle(fontSize = 16.sp)) { append("Repositories:") }
    })

    LazyColumn {
      State.Companion.Repos.currentRepos.forEach {
        item {
          RepoDataView(it)
        }
      }
    }
  }
}

@Composable
private fun RepoDataView(repoData: State.Companion.Repos.Companion.RepoData) {
  var expanded by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .defaultMinSize(minHeight = 40.dp)
      .clickable {
        expanded = !expanded
      }
      .animateContentSize()
  ) {
    Column(modifier = Modifier.align(Alignment.CenterStart)) {
      Text(
        text = buildAnnotatedString {
          withStyle(SpanStyle(fontSize = 18.sp, fontFamily = FontFamily.Monospace)) { append(repoData.name) }
        }
      )

      if (expanded) {
        Text(
          text = buildAnnotatedString {
            withStyle(SpanStyle(fontSize = 12.sp, color = Color.Gray)) { append(repoData.description) }
          }
        )

        Row {
          Text(
            text = buildAnnotatedString {
              withStyle(
                SpanStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.DarkGray
                )
              ) { append("⭐") }

              withStyle(
                SpanStyle(
                  fontSize = 12.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold,
                  color = Color.DarkGray
                )
              ) { append(repoData.stargazersCount.toString()) }
            }
          )

          Spacer(modifier = Modifier.width(28.dp))

          Text(
            text = buildAnnotatedString {
              withStyle(
                SpanStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.DarkGray
                )
              ) { append("⑂") }

              withStyle(
                SpanStyle(
                  fontSize = 12.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold,
                  color = Color.DarkGray
                )
              ) { append(repoData.forksCount.toString()) }
            }
          )
        }
      }

      Divider()
    }
  }
}
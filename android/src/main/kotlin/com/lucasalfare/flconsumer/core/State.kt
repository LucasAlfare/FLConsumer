package com.lucasalfare.flconsumer.core

import androidx.compose.runtime.mutableStateOf

class State {
  companion object {
    var currentAvatarUrl = mutableStateOf("")
    var currentUserNickName = mutableStateOf(":USER_NICKNAME:")
    var currentUserRealName = mutableStateOf(":REAL_NAME:")
    var currentUserBio = mutableStateOf(":BIO:")
    var currentNumberOfRepositories = mutableStateOf(0)
    var currentNumberOfFollowers = mutableStateOf(0)
  }
}
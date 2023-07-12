package com.lucasalfare.flconsumer.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * This structure holds all the data that UI is whatching on.
 *
 * Changing any value of these fields will make UI recomponse,
 * in other words, the UI will be redrawn with the newest values
 * from here, at least the composables that are whatching then.
 */
// TODO: find a way to refactor this tree as an Object
class State {
  companion object {

    /**
     * Used to hold values of the user info/header.
     */
    class Header {
      companion object {
        var currentAvatarUrl by mutableStateOf("")
        var currentLogin by mutableStateOf("")
        var currentName by mutableStateOf("")
        var currentBio by mutableStateOf("")
        var currentPublicRepos by mutableStateOf(0)
        var currentFollowers by mutableStateOf(0)
      }
    }
  }
}
package com.lucasalfare.flconsumer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import com.lucasalfare.flconsumer.core.ApiManager
import com.lucasalfare.flconsumer.core.State
import com.lucasalfare.fllistener.CallbacksManager
import com.lucasalfare.fllistener.setupManagers


/**
 * Global field to provide to the UI communication with another
 * managers through the application flow.
 *
 * This must be stored in a field instead an annonimous object
 * in order to expose itself to other parts of the code of
 * this UI access it as well.
 */
val uiManager = CallbacksManager()


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      // before showing the app composables, just
      // sets ups the internal managers, thourgh
      // this side effect
      LaunchedEffect(true) {
        setupManagers(
          uiManager,
          ApiManager()
        )
      }

      App()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    uiManager.notifyListeners("client-dispose")
  }

  override fun onStop() {
    super.onStop()
    uiManager.notifyListeners("client-dispose")
  }
}
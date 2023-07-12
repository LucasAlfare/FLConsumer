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

val uiManager = CallbacksManager()

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
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

  override fun onPause() {
    super.onPause()
    uiManager.notifyListeners("client-dispose")
  }
}
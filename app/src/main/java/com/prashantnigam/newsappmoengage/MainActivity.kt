package com.prashantnigam.newsappmoengage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.prashantnigam.newsappmoengage.ui.screens.HomeScreen
import com.prashantnigam.newsappmoengage.ui.screens.HomeScreenViewModel
import com.prashantnigam.newsappmoengage.ui.theme.NewsAppMoengageTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel = HomeScreenViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        askNotificationPermission()
        retrieveFCMToken()

        // notification comes when app is killed
        val count: Int? = intent?.extras?.getString("count")?.toInt()
        Log.d("push notification from", "count : $count")
        count?.let {
            PushNotificationManager.setDataReceived(count = count)
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NewsAppMoengageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d("push notification ", " on new intent extras? : ${intent?.extras}")

        // notification coming when app in inactive/background, data included in intent extra
        val count: Int? = intent?.extras?.getString("count")?.toInt()
        count?.let {
            Log.d("push notification ", " on new intent count : $count")
            PushNotificationManager.setDataReceived(count = count)
            lifecycleScope.launch {
                return@launch
            }
            return
        }
    }

    private fun retrieveFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("push notification test", "failed with error: ${task.exception}")
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("push notification token", "token received: $token")
            lifecycleScope.launch {
                PushNotificationManager.registerTokenOnServer(token)
            }
        })
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        Log.d("Is Permission Granted", "$isGranted")
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.POST_NOTIFICATIONS)) {
                return
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

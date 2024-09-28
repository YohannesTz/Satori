package com.github.yohannestz.satori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.github.yohannestz.satori.ui.theme.SatoriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SatoriTheme {
                Scaffold { padding ->
                    Text("dfdsfasdf asdf ", modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
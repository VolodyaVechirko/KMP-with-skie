package org.example.project

import Greeting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello world!") }
        var toggle by remember { mutableStateOf(false) }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                toggle = !toggle
                greetingText = if (toggle) {
                    "Compose: ${Greeting().greet()}"
                } else {
                    "Hello world!"
                }
            }) {
                Text("Click me!")
            }

            Text(text = greetingText, modifier = Modifier.padding(all = 16.dp))
        }
    }
}
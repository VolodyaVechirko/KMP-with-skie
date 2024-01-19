package org.example.project

import Greeting
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vv.kmm.AddressModel
import com.vv.kmm.SharedRepository
import com.vv.kmm.UserModel
import com.vv.kmm.format
import kotlinx.coroutines.launch

@Composable
fun App(modifier: Modifier) {
    val scope = rememberCoroutineScope()
    val greeting = Greeting().greet()

    var user by remember { mutableStateOf<UserModel?>(null) }
    var address by remember { mutableStateOf<AddressModel?>(null) }

    var timerTick by remember { mutableIntStateOf(0) }
    var chatList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        val sharedRepository = SharedRepository("www.android.com")
        scope.launch {
            user = sharedRepository.getUser(userId = 23)
        }

        scope.launch {
            address = sharedRepository.getAddress(short = true)
        }

        scope.launch {
            sharedRepository.messagesFlow().collect { message ->
                chatList = chatList.toMutableList()
                    .apply { add(message) }
            }
        }

        scope.launch {
            sharedRepository.sharedFlow.collect { seconds ->
                timerTick = seconds
            }
        }
    }

    MaterialTheme {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_compose),
                contentDescription = null,
                modifier = Modifier.height(280.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Compose: $greeting")
            Spacer(modifier = Modifier.height(8.dp))

            // Wait for suspend function
            Text("User: ${user?.toPrint() ?: "loading..."}")

            // Wait for suspend function
            Text("Address: ${address?.format() ?: "loading..."}")

            // Print timer
            Text("Time: $timerTick seconds")

            Spacer(modifier = Modifier.height(16.dp))
            Column {
                chatList.forEach {
                    Text(text = "--: $it")
                }
            }
        }
    }
}
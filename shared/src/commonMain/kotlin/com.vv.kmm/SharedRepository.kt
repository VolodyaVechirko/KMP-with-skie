package com.vv.kmm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

class SharedRepository(val url: String) {
    // Emulates some network request
    suspend fun getUser(userId: Int): UserModel {
        return withContext(Dispatchers.IO) {
            delay(500)
            UserModel(name = "David", age = 29)
        }
    }

    // Emulates some network request
    suspend fun getAddress(short: Boolean): AddressModel {
        return withContext(Dispatchers.IO) {
            delay(500)
            AddressModel(
                city = "London",
                street = "Baker st.",
                flat = 34,
                coordinates = floatArrayOf(23.98f, 45.56f)
            )
        }
    }

    // Emulates flow which accumulates messages of some chat
    fun messagesFlow(): Flow<String> = flow {
        delay(500)
        emit("Hi, John")
        delay(1000)
        emit("Hello, how are you?")
        delay(500)
        emit("Just fine, let's meet tomorrow?")
        delay(1000)
        emit("Ok. London, Baker st. at 16:00.")
        delay(500)
        emit("Maybe 17:00?")
        delay(1000)
        emit("Ok")
    }.flowOn(Dispatchers.IO)


    // Emulates sharedFlow which emits some data regardless there is subscribers
    val sharedFlow: Flow<Int> = flow {
        repeat(1000) {
            emit(it)
            delay(500)
        }
    }.shareIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly)
}
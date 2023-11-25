package com.vv.kmm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SharedRepository(val url: String) {
    suspend fun getUser(): UserModel {
        return withContext(Dispatchers.IO) {
            delay(500)
            UserModel(name = "David", age = 29)
        }
    }

    suspend fun getAddress(): AddressModel {
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
}

data class UserModel(
    val name: String,
    val age: Int,
) : Printable {
    override fun toPrint(): String {
        return "$name, age $age"
    }
}

data class AddressModel(
    val city: String,
    val street: String,
    val flat: Int,
    val coordinates: FloatArray
) : Printable {
    override fun toPrint(): String = this.toString()

    fun format(): String {
        return "$street $flat, $city"
    }
}

data class WebAddress(
    val url: String,
    val port: Int,
) : Printable {
    override fun toPrint(): String = this.toString()
}

interface Printable {
    fun toPrint(): String
}

sealed class Status {
    data object Loading : Status()
    data class Error(val message: String) : Status()
    data class Success(val result: Float) : Status()
}
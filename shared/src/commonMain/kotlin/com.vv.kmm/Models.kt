package com.vv.kmm

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
}

// Extension to try in Swift code
fun AddressModel.format(): String {
    return "$street $flat, $city"
}

// Interface to try in Swift code
interface Printable {
    fun toPrint(): String
}

sealed class Status {
    data object Loading : Status()
    data class Error(val message: String) : Status()
    data class Success(val result: Float) : Status()
}
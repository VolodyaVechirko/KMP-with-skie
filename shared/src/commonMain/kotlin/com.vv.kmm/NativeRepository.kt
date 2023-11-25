package com.vv.kmm

class SharedRepository(val url: String) {
    fun getUser(): UserModel {
        return UserModel(name = "David", age = 29)
    }

    fun getAddress(): AddressModel {
        return AddressModel(
            city = "London",
            street = "Baker st.",
            flat = 34,
            coordinates = floatArrayOf(23.98f, 45.56f)
        )
    }

    fun getWebAddress(): WebAddress {
        return WebAddress(url = url, port = 344)
    }
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
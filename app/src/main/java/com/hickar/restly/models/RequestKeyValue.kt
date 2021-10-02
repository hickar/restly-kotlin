package com.hickar.restly.models

data class RequestKeyValue(
    var key: String = "",
    var value: String = "",
    var enabled: Boolean = true
) {
    override fun equals(other: Any?): Boolean {
        return other is RequestKeyValue &&
                other.key == key &&
                other.value == value
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + enabled.hashCode()
        return result
    }
}
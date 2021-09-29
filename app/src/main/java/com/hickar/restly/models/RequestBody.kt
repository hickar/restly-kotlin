package com.hickar.restly.models

data class RequestBody(
    var enabled: Boolean = false,
    var type: String = "",
    var rawText: String = "",
    var formData: List<FormData> = listOf(),
    var multipartData: List<FormData> = listOf()
)
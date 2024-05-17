package com.test.landmarkremark.data.base

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("result")
    val result: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: T? = null
)

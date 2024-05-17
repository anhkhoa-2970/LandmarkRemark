package com.test.landmarkremark.data.base

sealed class NetworkResult<out T> {
    class Loading<out T> : NetworkResult<T>()
    data class Success<out T>(val message: String?, val data: T?) : NetworkResult<T>()
    data class Failure<out T>(val message: String?, val code: Int) : NetworkResult<T>()
}

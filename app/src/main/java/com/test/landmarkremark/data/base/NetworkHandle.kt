package com.test.landmarkremark.data.base

import org.json.JSONObject
import retrofit2.Response
import com.test.landmarkremark.R
import java.lang.Exception
import com.test.landmarkremark.utils.Constants
import com.test.landmarkremark.utils.Utils.getString
import com.test.landmarkremark.utils.isTimeoutError

suspend fun <T> getNetworkResult(call: suspend () -> Response<ApiResponse<T>>): NetworkResult<T> {
    return try {
        val response = call()
        return if (response.isSuccessful) {
            if (response.body()?.result == Constants.HTTP_RESULT) {
                val body = response.body()
                checkNetworkResult(body = body)
            } else {
                val message = response.body()?.message.toString()
                val code = response.code()
                getNetworkResultFailure(message, code)
            }
        } else {
            getNetworkResultFailure(response)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        if (e.isTimeoutError()) {
            NetworkResult.Failure(
                message = getString(R.string.time_out),
                code = Constants.CODE_ERROR_INTERNAL_SERVER
            )
        } else {
            NetworkResult.Failure(
                message = e.message.toString(),
                code = Constants.CODE_UNKNOWN
            )
        }
    }
}


private fun <T> checkNetworkResult( body: ApiResponse<T>?): NetworkResult<T> {
    if (body is ApiResponse<T>) {
        return NetworkResult.Success(body.message, body.data)
    }
    return getNetworkResultFailure()
}

private fun <T> getNetworkResultFailure(response: Response<ApiResponse<T>>): NetworkResult<T> {
    val errorString = response.errorBody()?.string() ?: "{}"
    val jsonObject = JSONObject(errorString)
    if (jsonObject.optString(Constants.MESSAGE).isNotEmpty()) {
        jsonObject.let {
            return (NetworkResult.Failure(
                message = it.optString(Constants.MESSAGE),
                code = response.code()
            ))
        }
    } else {
        val errorJson = JSONObject(errorString).getJSONObject(Constants.MESSAGE)
        val message = errorJson.optString(Constants.MESSAGE)
        return NetworkResult.Failure(message = message, code = response.code())
    }
}

private fun <T> getNetworkResultFailure(
    msg: String = "some error",
    code: Int = Constants.CODE_UNKNOWN
): NetworkResult<T> {
    return NetworkResult.Failure(
        message = msg,
        code = code
    )
}

fun <T> NetworkResult<T>.handleNetworkResult(
    success: (NetworkResult.Success<T>) -> Unit = {},
    fail: (NetworkResult.Failure<T>) -> Unit = {},
    loading: (Boolean) -> Unit = {}
) {
    when (this) {
        is NetworkResult.Loading -> loading(true)
        is NetworkResult.Success -> {
            success(this)
            loading(false)
        }

        is NetworkResult.Failure -> {
            fail(this)
            loading(false)
        }
    }
}